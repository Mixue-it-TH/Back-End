package com.example.kanbanbackend.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import io.viascom.nanoid.NanoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;



    public Map uploadFile(MultipartFile file, String resourceType) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        }

        String publicId = "uploads/" + NanoId.generate(20) + fileExtension;
        return cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", resourceType,
                        "public_id", publicId
                )
        );
    }
    public List<Map> uploadFiles(List<MultipartFile> files) throws IOException {
        List<Map> uploadResults = new ArrayList<>();

        for (MultipartFile file : files) {
            String resourceType = getResourceType(file);
            Map result = uploadFile(file,resourceType);
            uploadResults.add(result);
        }

        return uploadResults;
    }

    public Map deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public Map deleteFiles(List<String> publicIds) throws Exception {
        return cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
    }
    public String createSignedUrl(String publicId) {
        return cloudinary.url()
                .resourceType("auto")
                .publicId(publicId)
                .signed(true)
                .transformation(new Transformation().flags("attachment"))
                .generate();
    }

    public String getResourceType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        switch (fileExtension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return "image";
            case "mp4":
            case "avi":
            case "mov":
                return "video";
            case "pdf":
            case "doc":
            case "docx":
            case "txt":
                return "raw";
            default:
                return "raw";
        }
    }
}
