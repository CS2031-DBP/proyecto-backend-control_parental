package org.control_parental.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.control_parental.hijo.domain.HijoDTO;
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
    public static List<HijoDTO> csvToHijos(InputStream is) throws IOException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        CSVParser csvParser = new CSVParser(fileReader, csvFormat);
        System.out.println("CSV Headers: " + csvParser.getHeaderNames());
            List<HijoDTO> hijos = new ArrayList<HijoDTO>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for(CSVRecord csvRecord:csvRecords) {
                HijoDTO hijo = new HijoDTO(
                        csvRecord.get("Nombre"),
                        csvRecord.get("Apellido")
                );
                hijos.add(hijo);
            }

            return hijos;


    };

};
