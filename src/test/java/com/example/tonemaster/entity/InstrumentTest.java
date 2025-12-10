package com.example.tonemaster.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InstrumentTest {

    @Test
    public void testGettersAndSetters() {
        // 1. Bikin Objek Kosong
        Instrument i = new Instrument();

        // 2. Isi Data (Setter)
        i.setId(1L);
        i.setName("Gitar");
        i.setBrand("Yamaha");
        i.setCategory("Akustik");
        i.setPrice(1000.0);
        i.setStock(10);
        i.setDescription("Bagus");
        i.setImageFileName("foto.jpg");

        // 3. Ambil Data (Getter) & Cek apakah sama
        Assertions.assertEquals(1L, i.getId());
        Assertions.assertEquals("Gitar", i.getName());
        Assertions.assertEquals("Yamaha", i.getBrand());
        Assertions.assertEquals("Akustik", i.getCategory());
        Assertions.assertEquals(1000.0, i.getPrice());
        Assertions.assertEquals(10, i.getStock());
        Assertions.assertEquals("Bagus", i.getDescription());
        Assertions.assertEquals("foto.jpg", i.getImageFileName());
        
        // Cek method toString/Equals/HashCode kalau ada (tapi manual getter setter biasanya cukup ini)
    }
}