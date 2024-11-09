package com.example.kanbanbackend.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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



    public Map uploadFile(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }

    public List<Map> uploadFiles(List<MultipartFile> files) throws IOException {
        List<Map> uploadResults = new ArrayList<>();

        for (MultipartFile file : files) {
            Map result = uploadFile(file);
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
}
