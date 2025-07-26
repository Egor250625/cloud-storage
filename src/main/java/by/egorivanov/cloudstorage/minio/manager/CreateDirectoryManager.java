package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateDirectoryManager {
    private final MinioClient minioClient;
    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;


    public ResourceResponse createDirectory(String path, int userId,String bucket,String directoryType){
        String fullPath = pathUtils.buildFullPath(userId, path);
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        if (minioUtils.isObjectExist(bucket, fullPath))
            throw new StorageException("Directory already exists");

        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fullPath)
                    .stream(emptyStream, 0, -1)
                    .contentType("application/x-directory")
                    .build();

            minioClient.putObject(args);
        } catch (Exception e) {
            throw new StorageException("Unexpected error while checking object", e);
        }
        ResourceResponse response = MinioUtils.buildResourceResponse(fullPath,pathUtils.extractNameFromPath(path),
                null, directoryType);
        return response;
    }
}
