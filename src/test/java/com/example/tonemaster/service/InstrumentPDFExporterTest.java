package com.example.tonemaster.service;

import com.example.tonemaster.entity.Instrument;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InstrumentPDFExporterTest {

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletOutputStream outputStream;

    @Test
    public void testExport() throws IOException {
        // 1. Siapkan Data Dummy
        List<Instrument> list = new ArrayList<>();
        Instrument i1 = new Instrument();
        i1.setId(1L); i1.setName("Drum"); i1.setBrand("Roland"); 
        i1.setCategory("Pukul"); i1.setPrice(5000.0); i1.setStock(2);
        list.add(i1);

        // 2. Mocking Response
        Mockito.when(response.getOutputStream()).thenReturn(outputStream);

        // 3. Jalankan Export
        InstrumentPDFExporter exporter = new InstrumentPDFExporter(list);
        exporter.export(response);

        // 4. Verifikasi
        // Pastikan eksportir memanggil getOutputStream untuk menulis PDF
        Mockito.verify(response).getOutputStream();
    }
}