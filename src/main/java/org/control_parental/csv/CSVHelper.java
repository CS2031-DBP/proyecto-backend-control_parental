package org.control_parental.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.control_parental.hijo.dto.NewHijoDto;
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
    public static List<NewHijoDto> csvToHijos(InputStream is) throws IOException {
//        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
//        CSVParser csvParser = new CSVParser(fileReader, csvFormat);
//        System.out.println("CSV Headers: " + csvParser.getHeaderNames());
//        List<NewHijoDto> hijos = new ArrayList<>();
//
//        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
//
//        for(CSVRecord csvRecord:csvRecords) {
//            NewHijoDto hijo = new NewHijoDto(
//                    csvRecord.get("Nombre"),
//                    csvRecord.get("Apellido")
//            );
//            System.out.println(hijo.getNombre());
//            System.out.println(hijo.getApellido());
//            hijos.add(hijo);
//        }
//
//
//        return hijos;

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        List<NewHijoDto> hijos = new ArrayList<>();

        String line = fileReader.readLine(); // Reading header, Ignoring

        while ((line = fileReader.readLine()) != null && !line.isEmpty()) {
            String[] fields = line.split(",");
            String nombre = fields[0];
            String apellido = fields[1];
            String email = fields[2];
            NewHijoDto newHijoDto = new NewHijoDto(nombre, apellido, email);
            hijos.add(newHijoDto);
        }
        fileReader.close();
        return hijos;
    };

};