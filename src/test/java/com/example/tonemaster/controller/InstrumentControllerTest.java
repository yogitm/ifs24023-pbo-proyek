package com.example.tonemaster.controller;

import com.example.tonemaster.entity.Instrument;
import com.example.tonemaster.service.InstrumentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstrumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstrumentService service;

    // --- TEST STANDAR (SAMA SEPERTI SEBELUMNYA) ---

    @Test
    public void testViewHomePage() throws Exception {
        // Ingat, kita sudah update controller pake search keyword, jadi mock harus fleksibel
        Mockito.when(service.getAllInstruments(Mockito.any())).thenReturn(Arrays.asList(new Instrument()));
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testShowNewInstrumentForm() throws Exception {
        mockMvc.perform(get("/showNewInstrumentForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_instrument"));
    }

    @Test
    public void testShowFormForUpdate() throws Exception {
        Instrument i = new Instrument();
        i.setId(1L);
        Mockito.when(service.getInstrumentById(1L)).thenReturn(i);
        mockMvc.perform(get("/showFormForUpdate/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_instrument"));
    }

    @Test
    public void testDeleteInstrument() throws Exception {
        mockMvc.perform(get("/deleteInstrument/1"))
                .andExpect(status().is3xxRedirection());
        Mockito.verify(service).deleteInstrument(1L);
    }

    // --- TEST UPLOAD GAMBAR ---

    @Test
    public void testSaveInstrument_WithImage_FolderMissing() throws Exception {
        Path uploadDir = Paths.get(System.getProperty("user.dir") + "/uploads");
        FileSystemUtils.deleteRecursively(uploadDir);
        MockMultipartFile file = new MockMultipartFile("image", "new.jpg", "image/jpeg", "content".getBytes());
        mockMvc.perform(multipart("/saveInstrument").file(file).param("name", "Gitar"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testSaveInstrument_WithImage_FolderExists() throws Exception {
        Path uploadDir = Paths.get(System.getProperty("user.dir") + "/uploads");
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
        MockMultipartFile file = new MockMultipartFile("image", "exist.jpg", "image/jpeg", "content".getBytes());
        mockMvc.perform(multipart("/saveInstrument").file(file).param("name", "Gitar"))
                .andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void testSaveInstrument_NoImage_EditMode() throws Exception {
        Instrument existing = new Instrument();
        existing.setId(1L);
        existing.setImageFileName("old.jpg");
        Mockito.when(service.getInstrumentById(1L)).thenReturn(existing);
        MockMultipartFile emptyFile = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);
        mockMvc.perform(multipart("/saveInstrument").file(emptyFile).param("id", "1").param("name", "Gitar Edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testSaveInstrument_NoImage_CreateMode() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);
        mockMvc.perform(multipart("/saveInstrument").file(emptyFile).param("name", "New Item No Image"))
                .andExpect(status().is3xxRedirection());
    }

    // --- TEST DISPLAY IMAGE ---

    @Test
    public void testDisplayImage_Jpg() throws Exception {
        String fileName = "test.jpg";
        createDummyFile(fileName);
        mockMvc.perform(get("/display").param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/uploads/" + fileName));
    }

    @Test
    public void testDisplayImage_Png() throws Exception {
        String fileName = "test.png";
        createDummyFile(fileName);
        mockMvc.perform(get("/display").param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/uploads/" + fileName));
    }

    @Test
    public void testDisplayImage_UppercasePng() throws Exception {
        String fileName = "TEST.PNG";
        createDummyFile(fileName);
        mockMvc.perform(get("/display").param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/uploads/" + fileName));
    }

    @Test
    public void testDisplayImage_NotFound() throws Exception {
        mockMvc.perform(get("/display").param("fileName", "ghost.jpg")).andExpect(status().isOk());
    }

    @Test
    public void testSaveInstrument_EditMode_ComplexBranches() throws Exception {
        Instrument existing = new Instrument();
        existing.setId(1L);
        existing.setImageFileName("old.jpg");
        Mockito.when(service.getInstrumentById(1L)).thenReturn(existing);
        MockMultipartFile emptyFile = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);
        mockMvc.perform(multipart("/saveInstrument").file(emptyFile).param("id", "1").param("name", "Null Test"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(multipart("/saveInstrument").file(emptyFile).param("id", "1").param("imageFileName", "").param("name", "Empty Test"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(multipart("/saveInstrument").file(emptyFile).param("id", "1").param("imageFileName", "old.jpg").param("name", "Keep Old Test"))
                .andExpect(status().is3xxRedirection());
    }

    // --- TEST BARU: EXPORT EXCEL & PDF (BIAR HIJAU SEMUA) ---

    @Test
    public void testExportToExcel() throws Exception {
        Mockito.when(service.getAllInstruments(Mockito.any())).thenReturn(Arrays.asList(new Instrument()));
        
        mockMvc.perform(get("/export/excel"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/octet-stream"));
    }

    @Test
    public void testExportToPDF() throws Exception {
        Mockito.when(service.getAllInstruments(Mockito.any())).thenReturn(Arrays.asList(new Instrument()));
        
        mockMvc.perform(get("/export/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"));
    }

    // Helper
    private void createDummyFile(String fileName) throws Exception {
        Path uploadDir = Paths.get(System.getProperty("user.dir") + "/uploads");
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
        Files.write(uploadDir.resolve(fileName), "dummy".getBytes());
    }
}