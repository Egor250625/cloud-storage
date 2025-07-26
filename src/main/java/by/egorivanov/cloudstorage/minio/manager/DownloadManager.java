package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadManager {
    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;

    public InputStream downloadDirectory(String path, int userId,String bucket) {
        try {
            Path tempZip = Files.createTempFile("user_" + userId + "_", ".zip");

            try (FileOutputStream fileOutputStream = new FileOutputStream(tempZip.toFile());
                 ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

                boolean hasFiles = false;

                for (Result<Item> result : minioUtils.listObjects(bucket, path, true)) {
                    Item item = result.get();
                    String objectName = item.objectName();
                    if (item.isDir()) {
                        log.info("Directory found with path = {}", objectName);
                        continue;
                    }
                    try (InputStream input =minioUtils.getObject(bucket, objectName)) {
                        zipOutputStream.putNextEntry(new ZipEntry(pathUtils.extractDisplayName(objectName, path)));
                        input.transferTo(zipOutputStream);
                        zipOutputStream.closeEntry();
                        hasFiles = true;
                    }
                }
                if (!hasFiles) {
                    log.error("The folder is empty: {}", path);
                    throw new StorageException("The folder is empty: " + path);
                }
            }
            return new FileInputStream(tempZip.toFile());
        } catch (Exception e) {
            log.error("Failed to get folder contents for path: {}", path, e);
            throw new StorageException("Failed to get folder contents", e);
        }
    }

    public InputStream downloadFile(String path,String bucket) {
        return minioUtils.getObject(bucket, path);
    }
}
