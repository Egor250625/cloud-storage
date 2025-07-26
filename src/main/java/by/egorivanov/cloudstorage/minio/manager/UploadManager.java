package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.exception.UploadException;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.SnowballObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadManager {
    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;


    public List<ResourceResponse> uploadResources(String path, int userId, List<MultipartFile> files,String bucket){
        String fullPath = pathUtils.buildFullPath(userId, path);
        List<ResourceResponse> responses = new ArrayList<>();
        List<SnowballObject> objects = new ArrayList<SnowballObject>();
        if (files.isEmpty()) {
            throw new StorageException("No files found");
        }
        if (files.size() > 10) {
            log.warn("Cannot upload more than 10 files at once in directory {}", path);
            throw new UploadException("Cannot upload more than 10 files at once");
        }
        for (MultipartFile file : files) {

            String objectName = fullPath + file.getOriginalFilename();

            if (file.getSize() > 209_715_200) {
                throw new UploadException("File too large: " + file.getOriginalFilename());
            }
            try {
                objects.add(
                        new SnowballObject(
                                objectName,
                                file.getInputStream(),
                                file.getSize(),
                                null
                        )
                );
                responses.add(MinioUtils.buildResourceResponse(fullPath,
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType()));
            } catch (Exception e) {
                log.warn("Failed to add Snowball Objects in directory {}", path);
                throw new UploadException("Failed to add Snowball Objects", e);
            }
        }
        try {
            minioUtils.uploadSnowballObject(bucket,objects);
        } catch (Exception e) {
            log.warn("Failed to upload Snowball Objects in directory {}", path);
            throw new UploadException("Failed to upload Snowball Objects", e);
        }
        return responses;
    }
}
