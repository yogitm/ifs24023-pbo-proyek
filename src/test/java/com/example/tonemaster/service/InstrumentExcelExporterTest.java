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
public class InstrumentExcelExporterTest {

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletOutputStream outputStream;

    @Test
    public void testExport() throws IOException {
        List<Instrument> list = new ArrayList<>();
        Instrument i1 = new Instrument();
        
        // PENTING: ID adalah Long. Ini akan memicu 'instanceof Long'
        i1.setId(99L); 
        i1.setName("Gitar"); 
        i1.setBrand("Yamaha"); 
        i1.setCategory("Alat Petik"); 
        i1.setPrice(1000.0); // Double
        i1.setStock(5);      // Integer
        
        list.add(i1);

        Mockito.when(response.getOutputStream()).thenReturn(outputStream);

        InstrumentExcelExporter exporter = new InstrumentExcelExporter(list);
        exporter.export(response);

        Mockito.verify(response).getOutputStream();
    }
}