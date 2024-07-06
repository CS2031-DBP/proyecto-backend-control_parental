package org.control_parental.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@NoArgsConstructor
public class S3 {

    @Autowired
    private AmazonS3 s3Client;
    public File convertMultiPartFileToFile(final MultipartFile multipartFile) throws IOException {
        final File file = new File(multipartFile.getOriginalFilename());

        final FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());

        return file;
    }

    public String uploadFileToS3Bucket(final String bucketName, final File file) {
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getName(), file);
        s3Client.putObject(putObjectRequest);

        return file.getName();
    }

}
