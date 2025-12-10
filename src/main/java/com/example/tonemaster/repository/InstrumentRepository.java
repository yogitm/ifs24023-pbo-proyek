package com.example.tonemaster.repository; 

import com.example.tonemaster.entity.Instrument; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    // Kita tidak perlu nulis query SQL manual.
    // Spring Boot ajaib, dia sudah menyediakan fungsi:
    // .save(), .findAll(), .findById(), .deleteById() secara otomatis!
    // Mencari data dimana Nama ATAU Brand ATAU Kategori mengandung keyword
    // ILIKE (Postgres) atau LIKE (MySQL) -> Di JPA kita pakai LIKE biasa + lower case
    @Query("SELECT i FROM Instrument i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Instrument> search(@Param("keyword") String keyword);
}
