package by.egorivanov.cloudstorage.repository;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.minio.manager.CreateDirectoryManager;
import by.egorivanov.cloudstorage.minio.manager.DeleteManager;
import by.egorivanov.cloudstorage.minio.manager.DownloadManager;
import by.egorivanov.cloudstorage.minio.manager.GetResourceManager;
import by.egorivanov.cloudstorage.minio.manager.MoveManager;
import by.egorivanov.cloudstorage.minio.manager.SearchManager;
import by.egorivanov.cloudstorage.minio.manager.UploadManager;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;

import io.minio.MinioClient;

import io.minio.Result;

import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
@ConditionalOnBean(MinioClient.class)
@Slf4j
public class MinioRepository implements StorageRepository {

    private static final String MY_BUCKET_NAME = "user-files";
    private static final String DIRECTORY_TYPE = "DIRECTORY";
    private static final String FILE_TYPE = "FILE";

    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;

    private final DeleteManager deleteManager;
    private final UploadManager uploadManager;
    private final CreateDirectoryManager createDirectoryManager;
    private final DownloadManager downloadManager;
    private final MoveManager moveManager;
    private final GetResourceManager getResourceManager;
    private final SearchManager searchManager;

    @Override
    public List<ResourceResponse> getAllInDirectory(String path, int userId) {
        String fullPath = pathUtils.buildFullPath(userId, path);
        Iterable<Result<Item>> items = minioUtils.listObjects(MY_BUCKET_NAME, fullPath, false);

        List<ResourceResponse> responses = new ArrayList<>();
        for (Result<Item> result : items) {
            try {
                Item item = result.get();
                if (item.isDir())
                    responses.add(MinioUtils.buildResourceResponse(fullPath,
                            pathUtils.extractDisplayName(item.objectName(), fullPath) + "/",
                            null,
                            pathUtils.getResourceType(item.objectName())
                    ));
                else if (!item.objectName().endsWith("/")) {
                    responses.add(
                            MinioUtils.buildResourceResponse(fullPath,
                                    pathUtils.extractDisplayName(item.objectName(), fullPath),
                                    item.size(),
                                    pathUtils.getResourceType(item.objectName())
                            )
                    );
                }
            } catch (Exception e) {
                log.error("Failed to get item from bucket", e);
                throw new StorageException("Error processing item ", e);
            }

        }
        log.info("Get all resource success - have {} objects", responses.size());
        return responses;
    }

    @Override
    public List<ResourceResponse> uploadFiles(String path, int userId, List<MultipartFile> files) {
        return uploadManager.uploadResources(path, userId, files, MY_BUCKET_NAME);
    }

    @Override
    public ResourceResponse createDirectory(String path, int userId) {
        return createDirectoryManager.createDirectory(path, userId, MY_BUCKET_NAME, DIRECTORY_TYPE);
    }

    @Override
    public void delete(String path, int userId) {
        if (path.endsWith("/"))
            deleteManager.deleteDirectory(path, MY_BUCKET_NAME);
        else {
            try {
                deleteManager.deleteObject(path, MY_BUCKET_NAME);
            } catch (Exception e) {
                log.error("Failed to delete object");
                throw new StorageException("Failed to delete object", e);
            }
        }
    }

    @Override
    public InputStream downloadObject(String path, int userId) {
        if (pathUtils.isDirectory(path)) {
            return downloadManager.downloadDirectory(path, userId, MY_BUCKET_NAME);
        }
        return downloadManager.downloadFile(path, MY_BUCKET_NAME);
    }

    @Override
    public ResourceResponse move(String from, String to, int userId) {
        if (moveManager.isRenameAction(from, to)) {
            return moveManager.renameResource(from, to, MY_BUCKET_NAME, DIRECTORY_TYPE, FILE_TYPE);
        }
        return moveManager.moveResource(from, to, MY_BUCKET_NAME, DIRECTORY_TYPE, FILE_TYPE,userId);
    }

    @Override
    public ResourceResponse get(String path, int userId) {
        return getResourceManager.get(path, userId, MY_BUCKET_NAME);
    }

    @Override
    public List<ResourceResponse> search(String query, int userId) {
        return searchManager.search(query,userId,MY_BUCKET_NAME);
    }

}
