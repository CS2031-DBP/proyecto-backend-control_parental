package org.control_parental.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.control_parental.hijo.dto.NewHijoDto;
import org.control_parental.padre.dto.NewPadreDto;
import org.control_parental.profesor.dto.NewProfesorDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static Iterable<CSVRecord> record(InputStream is) throws IOException {

        BOMInputStream bomIn = BOMInputStream.builder().setInputStream(is).get();
        Reader reader = new InputStreamReader(bomIn, "UTF-8");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
        CSVParser csvParser = new CSVParser(reader, csvFormat);

        return csvParser.getRecords();
    }

    public static List<NewHijoDto> csvToHijos(InputStream is) throws IOException {

        Iterable<CSVRecord> csvRecords = record(is);
        List<NewHijoDto> hijos = new ArrayList<>();


        for (CSVRecord csvRecord : csvRecords) {
            NewHijoDto hijo = NewHijoDto.parse(csvRecord);
            hijos.add(hijo);
        }

        return hijos;
    }
    public static List<NewProfesorDto> csvToProfesor(InputStream is) throws IOException {
        Iterable<CSVRecord> csvRecords = record(is);
        List<NewProfesorDto> profesores = new ArrayList<>();

        for (CSVRecord record : csvRecords) {
            NewProfesorDto profesor =  NewProfesorDto.parse(record);
            profesores.add(profesor);
        }
        return profesores;

    }

    public static List<NewPadreDto> csvToPadres(InputStream is)  throws IOException {
        Iterable<CSVRecord> csvRecords = record(is);
        List<NewPadreDto> padres = new ArrayList<>();

        for (CSVRecord record : csvRecords) {
            NewPadreDto padre = NewPadreDto.parse(record);
            padres.add(padre);
        }
        return padres;
    }
}
