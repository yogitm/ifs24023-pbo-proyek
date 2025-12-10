package com.example.tonemaster.service;

import com.example.tonemaster.entity.Instrument;
import com.example.tonemaster.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentService {

    @Autowired
    private InstrumentRepository repository;

    // Method yang sudah diupdate untuk handle pencarian
    public List<Instrument> getAllInstruments(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repository.search(keyword);
        }
        return repository.findAll();
    }

    // 2. Simpan Data
    public void saveInstrument(Instrument instrument) {
        repository.save(instrument);
    }

    // 3. Ambil Data berdasarkan ID (untuk Edit)
    public Instrument getInstrumentById(Long id) {
        Optional<Instrument> optional = repository.findById(id);
        Instrument instrument = null;
        if (optional.isPresent()) {
            instrument = optional.get();
        } else {
            throw new RuntimeException("Instrument not found for id :: " + id);
        }
        return instrument;
    }

    // 4. Hapus Data
    public void deleteInstrument(Long id) {
        this.repository.deleteById(id);
    }
    
}