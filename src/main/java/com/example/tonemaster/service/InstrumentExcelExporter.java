package com.example.tonemaster.service; // Saya taruh di service biar gampang

import com.example.tonemaster.entity.Instrument;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class InstrumentExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Instrument> listInstruments;

    public InstrumentExcelExporter(List<Instrument> listInstruments) {
        this.listInstruments = listInstruments;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Inventaris Musik");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "No", style); // Ubah ID jadi No
        createCell(row, 1, "Nama Alat", style);
        createCell(row, 2, "Merk", style);
        createCell(row, 3, "Kategori", style);
        createCell(row, 4, "Harga", style);
        createCell(row, 5, "Stok", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        
        // --- PERUBAHAN DISINI ---
        // Ubah dari 'int' menjadi 'long' agar logika 'instanceof Long' terpakai
        long nomorUrut = 1L; 

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Instrument instrument : listInstruments) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            // Karena nomorUrut sekarang Long, dia akan masuk ke blok 'else if (value instanceof Long)'
            createCell(row, columnCount++, nomorUrut++, style);
            
            createCell(row, columnCount++, instrument.getName(), style);
            createCell(row, columnCount++, instrument.getBrand(), style);
            createCell(row, columnCount++, instrument.getCategory(), style);
            createCell(row, columnCount++, instrument.getPrice(), style);
            createCell(row, columnCount++, instrument.getStock(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}