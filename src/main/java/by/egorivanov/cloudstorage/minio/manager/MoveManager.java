package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.exception.MoveResourceException;
import by.egorivanov.cloudstorage.exception.RenameResourceException;
import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MoveManager {
    private final MinioClient minioClient;
    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;
    private final DeleteManager deleteManager;


    public ResourceResponse renameResource(String from, String to,String bucket,String directoryType,String fileType) {
        if (pathUtils.isDirectory(to)) {
            return renameDirectory(from, to,bucket,directoryType);
        }
        return renameFile(from, to,bucket,fileType);
    }

    public ResourceResponse renameDirectory(String from, String to, String bucket,String directoryType) {
        try {
            for (Result<Item> item : minioUtils.listObjects(bucket, from, true)) {
                Item i = item.get();
                String oldKey = i.objectName();
                if (oldKey.startsWith(from)) {
                    String newKey = to + oldKey.substring(from.length());
                    minioUtils.copyObject(newKey, oldKey,bucket);
                }
            }
        } catch (Exception e) {
            log.error("Error while trying to rename directory with path = {}", from, e);
            throw new RenameResourceException("Error while trying to rename directory", e);
        }
       deleteManager.deleteDirectory(from, bucket);

        return MinioUtils.buildResourceResponse(
               pathUtils.getParentPath(to),
                pathUtils.extractNameFromPath(to),
                null,
                directoryType
        );
    }

    private ResourceResponse renameFile(String from, String to,String bucket,String fileType) {
        try {
            minioUtils.copyObject(to, from,bucket);

            deleteManager.deleteObject(from, bucket);
        } catch (Exception e) {
            log.error("Error while trying to rename file from '{}' to '{}'", from, to, e);
            throw new RenameResourceException("Error while trying to rename file", e);
        }
        return MinioUtils.buildResourceResponse(
                pathUtils.getParentPath(to),
                pathUtils.extractNameFromPath(to),
                null,
                fileType
        );
    }

    public boolean isRenameAction(String from, String to) {
        return pathUtils.getParentPath(from).equals(pathUtils.getParentPath(to));
    }

    public ResourceResponse moveResource(String from, String to,String bucket,String directoryType,String fileType,int userId) {
        try {
            String targetToPath = pathUtils.getUserDirectory(userId);

            // Убедимся, что to имеет полный путь с user-<id>-files/
            String destinationBasePath = to.contains(targetToPath) ? to : targetToPath + to;

            if (pathUtils.isDirectory(from)) {
                for (Result<Item> item : minioUtils.listObjects(bucket, from, true)) {
                    Item i = item.get();
                    String oldKey = i.objectName();

                    // Получаем относительный путь: отрезаем начало 'from'
                    String relativePath = oldKey.substring(from.length());

                    // Формируем новый путь назначения
                    String newKey = destinationBasePath + relativePath;

                    minioUtils.copyObject(newKey, oldKey, bucket);
                }

                // Удаляем исходную директорию после копирования
                deleteManager.deleteDirectory(from, bucket);

                return MinioUtils.buildResourceResponse(
                        pathUtils.getParentPath(to),
                        pathUtils.extractNameFromPath(to),
                        null,
                        directoryType
                );
            } else {
                String newKey = to.contains(targetToPath) ? to : targetToPath + to;

                minioUtils.copyObject(newKey, from,bucket);
                deleteManager.deleteObject(from, bucket);

                StatObjectResponse stat = minioUtils.buildStatObject(newKey, bucket);
                long size = stat.size();

                return MinioUtils.buildResourceResponse(
                        pathUtils.getParentPath(to),
                        pathUtils.extractNameFromPath(to),
                        size,
                        fileType
                );
            }
        } catch (Exception e) {
            log.error("Error while trying to move resource from  path {},to path {}", from, to, e);
            throw new MoveResourceException("Error during moving resource", e);
        }
    }

}
