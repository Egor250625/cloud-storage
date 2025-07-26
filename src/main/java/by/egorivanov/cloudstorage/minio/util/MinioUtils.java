package by.egorivanov.cloudstorage.minio.util;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.exception.UploadException;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.SnowballObject;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.UploadSnowballObjectsArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioUtils {
    private final String DIRECTORY_TYPE = "DIRECTORY";
    private final String FILE_TYPE = "FILE";

    private final MinioClient minioClient;

    public Iterable<Result<Item>> listObjects(String bucket, String prefix, Boolean recursive) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build());
    }

    public static ResourceResponse buildResourceResponse(String path, String name, Long size, String type) {
        return new ResourceResponse(path, name, size, type);
    }

    public void removeObject(String bucket, String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .build());
            log.info("Object removed successfully with path = {}", path);
        } catch (Exception e) {
            log.error("Object remove failed with path = {}", path);
            throw new StorageException("Object remove failed");
        }
    }

    public void uploadSnowballObject(String bucket, List<SnowballObject> objects) {
        try {
            minioClient.uploadSnowballObjects(UploadSnowballObjectsArgs.builder()
                    .bucket(bucket)
                    .objects(objects).build());
        } catch (Exception e) {
            log.error("Failed to load snowball object", e);
            throw new UploadException("Failed to load snowball object", e);
        }
    }

    public boolean isObjectExist(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new StorageException("Failed to check object existence", e);
        } catch (Exception e) {
            throw new StorageException("Unexpected error while checking object", e);
        }
    }

    public InputStream getObject(String bucket, String object) {
        try {
            InputStream input = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build());
            log.info("Object get successfully with Object Name  = {}", object);
            return input;
        } catch (Exception e) {
            log.error("Failed to get input File with Object Name = {}", object, e);
            throw new StorageException("Failed to get input File", e);
        }
    }
    public void copyObject(String newKey, String oldKey,String bucket) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(newKey)
                            .source(
                                    CopySource.builder()
                                            .bucket(bucket)
                                            .object(oldKey)
                                            .build()
                            )
                            .build()
            );
        } catch (Exception e) {
            log.error("Error in object copy result with path from = {},to = {}", newKey, oldKey, e);
            throw new StorageException("Error during object copy", e);
        }
    }

    public StatObjectResponse buildStatObject(String path, String bucket) {
        try {
            return minioClient.statObject(StatObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(path)
                    .build());
        } catch (Exception e) {
            log.error("Error while trying to get stat resource with path  = {}", path, e);
            throw new StorageException("Error while trying to get resource", e);
        }
    }

}
