package com.example.tonemaster.controller;

import com.example.tonemaster.entity.Instrument;
import com.example.tonemaster.service.InstrumentExcelExporter;
import com.example.tonemaster.service.InstrumentPDFExporter;
import com.example.tonemaster.service.InstrumentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.DateFormat;         // <--- PENTING
import java.text.SimpleDateFormat;   // <--- PENTING
import java.util.Date;               // <--- PENTING
import java.util.List;               // <--- Pastikan ini juga ada
import com.example.tonemaster.service.InstrumentExcelExporter; // <--- Import Class Excel
import com.example.tonemaster.service.InstrumentPDFExporter;   // <--- Import Class PDF

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class InstrumentController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @Autowired
    private InstrumentService service;

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Instrument> list = service.getAllInstruments(keyword);
        
        model.addAttribute("listInstruments", list);
        model.addAttribute("keyword", keyword); // Kirim balik keyword biar tetep muncul di kotak input
        
        return "index";
    }

    @GetMapping("/showNewInstrumentForm")
    public String showNewInstrumentForm(Model model) {
        Instrument instrument = new Instrument();
        model.addAttribute("instrument", instrument);
        return "new_instrument";
    }

    @PostMapping("/saveInstrument")
    public String saveInstrument(@ModelAttribute("instrument") Instrument instrument,
                                 @RequestParam("image") MultipartFile file) throws IOException {
        
        if (!file.isEmpty()) {
            String cleanFileName = file.getOriginalFilename().replaceAll("\\s+", "_");
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, cleanFileName);
            
            if (!Files.exists(Paths.get(UPLOAD_DIRECTORY))) {
                Files.createDirectories(Paths.get(UPLOAD_DIRECTORY));
            }
            
            Files.write(fileNameAndPath, file.getBytes());
            instrument.setImageFileName(cleanFileName);
        } else {
            if (instrument.getId() != null) {
                Instrument existing = service.getInstrumentById(instrument.getId());
                // LOGIKA INI SUDAH DITES LENGKAP DI TEST CASE #12
                if (instrument.getImageFileName() == null || instrument.getImageFileName().isEmpty()) {
                     instrument.setImageFileName(existing.getImageFileName());
                }
            }
        }

        service.saveInstrument(instrument);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Instrument instrument = service.getInstrumentById(id);
        model.addAttribute("instrument", instrument);
        return "update_instrument";
    }

    @GetMapping("/deleteInstrument/{id}")
    public String deleteInstrument(@PathVariable(value = "id") Long id) {
        this.service.deleteInstrument(id);
        return "redirect:/";
    }

    @GetMapping("/display")
    @ResponseBody
    public void displayImage(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        Path imagePath = Paths.get(UPLOAD_DIRECTORY, fileName);
        
        if (Files.exists(imagePath)) {
            String contentType = "image/jpeg";
            
            // --- PERUBAHAN PENTING DISINI ---
            // Pakai toLowerCase() agar logikanya lurus dan langsung 100% coverage
            if (fileName.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            }
            
            response.setContentType(contentType);
            Files.copy(imagePath, response.getOutputStream());
        }
    }
    // 7. EXPORT TO EXCEL
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ToneMaster_Laporan_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Instrument> listInstruments = service.getAllInstruments(null); // Ambil semua data
        
        InstrumentExcelExporter excelExporter = new InstrumentExcelExporter(listInstruments);
        excelExporter.export(response);
    }

    // 8. EXPORT TO PDF
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ToneMaster_Laporan_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Instrument> listInstruments = service.getAllInstruments(null);

        InstrumentPDFExporter exporter = new InstrumentPDFExporter(listInstruments);
        exporter.export(response);
    }
    
}