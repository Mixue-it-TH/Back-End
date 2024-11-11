package com.example.kanbanbackend.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.viascom.nanoid.NanoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AmazonS3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket_name}")
    private String bucketName;

    public URL uploadFile(MultipartFile file,String fileName) throws IOException {

        String uploadFilename = NanoId.generate(10)+fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucketName, uploadFilename, file.getInputStream(), metadata));

        return amazonS3.getUrl(bucketName, uploadFilename);
    }


    public void deleteFile(String fileUrl) {
        String fileKey = extractFileKeyFromUrl(fileUrl);
        fileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8);
        amazonS3.deleteObject(bucketName, fileKey);
    }

    private String extractFileKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String bucketUrl = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, amazonS3.getRegionName());
            if (fileUrl.startsWith(bucketUrl)) {
                return fileUrl.substring(bucketUrl.length());
            } else {
                throw new IllegalArgumentException("URL does not match the bucket name or region");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid S3 URL", e);
        }
    }


}
