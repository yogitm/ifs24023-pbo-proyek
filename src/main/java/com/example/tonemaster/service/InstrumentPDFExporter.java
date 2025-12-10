package com.example.tonemaster.service;

import com.example.tonemaster.entity.Instrument;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class InstrumentPDFExporter {
    private List<Instrument> listInstruments;

    public InstrumentPDFExporter(List<Instrument> listInstruments) {
        this.listInstruments = listInstruments;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("No", font)); // Ubah ID jadi No
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nama Alat", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Merk", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Kategori", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Harga", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Stok", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        int nomorUrut = 1; // Mulai dari 1

        for (Instrument instrument : listInstruments) {
            // Tampilkan nomor urut, lalu tambah 1 (++)
            table.addCell(String.valueOf(nomorUrut++)); 
            
            table.addCell(instrument.getName());
            table.addCell(instrument.getBrand());
            table.addCell(instrument.getCategory());
            table.addCell("Rp " + instrument.getPrice());
            table.addCell(String.valueOf(instrument.getStock()));
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Laporan Inventaris ToneMaster", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 3.5f, 2.0f, 2.0f, 2.0f, 1.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }
}