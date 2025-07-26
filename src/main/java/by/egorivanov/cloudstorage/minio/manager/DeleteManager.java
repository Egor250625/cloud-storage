package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteManager {
    private final MinioUtils minioUtils;
    private final PathUtils pathUtils;

    public void deleteDirectory(String path, String bucket) {
        try {
            Iterable<Result<Item>> results = minioUtils.listObjects(bucket,path,true);
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();
                deleteObject(objectName, bucket);
            }
        } catch (Exception e) {
            throw new StorageException("No results found for directory", e);
        }
    }

    public void deleteObject(String path, String bucket) {
        try {
            minioUtils.removeObject(bucket,path);
            log.info("Object removed successfully with path = {}", path);
        } catch (Exception e) {
            log.error("Object remove failed with path = {}", path);
            throw new StorageException("Object remove failed");
        }
    }

}
