package com.example.tonemaster;

import com.example.tonemaster.entity.Instrument;
import com.example.tonemaster.repository.InstrumentRepository;
import com.example.tonemaster.service.InstrumentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InstrumentServiceTest {

    @Mock
    private InstrumentRepository repository;

    @InjectMocks
    private InstrumentService service;

    // 1. Test Ambil Data (Tanpa Keyword)
    @Test
    public void testGetAllInstruments_NoKeyword() {
        List<Instrument> list = new ArrayList<>();
        list.add(new Instrument());
        Mockito.when(repository.findAll()).thenReturn(list);

        List<Instrument> result = service.getAllInstruments(null);
        Assertions.assertEquals(1, result.size());
    }

    // 2. Test Ambil Data (DENGAN Keyword) -> INI YANG KURANG TADI
    @Test
    public void testGetAllInstruments_WithKeyword() {
        List<Instrument> list = new ArrayList<>();
        list.add(new Instrument());
        
        // Kalau ada keyword "Gitar", panggilnya search(), bukan findAll()
        Mockito.when(repository.search("Gitar")).thenReturn(list);

        List<Instrument> result = service.getAllInstruments("Gitar");
        Assertions.assertEquals(1, result.size());
        
        // Verifikasi bahwa repository.search dipanggil
        Mockito.verify(repository, Mockito.times(1)).search("Gitar");
    }

    // ... (Method lain: save, delete, getById biarkan tetap sama seperti sebelumnya) ...
    
    @Test
    public void testSaveInstrument() {
        Instrument instrument = new Instrument();
        service.saveInstrument(instrument);
        Mockito.verify(repository, Mockito.times(1)).save(instrument);
    }

    @Test
    public void testDeleteInstrument() {
        service.deleteInstrument(1L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testGetInstrumentById_Success() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(instrument));
        Instrument result = service.getInstrumentById(1L);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetInstrumentById_NotFound() {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> service.getInstrumentById(99L));
    }

    // 3. Test Ambil Data (Keyword Kosong "") -> BIAR 100% HIJAU
    @Test
    public void testGetAllInstruments_EmptyKeyword() {
        List<Instrument> list = new ArrayList<>();
        list.add(new Instrument());
        
        // Kalau keyword kosong "", harusnya panggil findAll(), BUKAN search()
        Mockito.when(repository.findAll()).thenReturn(list);

        List<Instrument> result = service.getAllInstruments(""); // String kosong
        Assertions.assertEquals(1, result.size());
        
        // Verifikasi bahwa repository.findAll dipanggil (bukan search)
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}