package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetResourceManager {
    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;


    public ResourceResponse get(String path, int userId, String bucket) {
        String fullPath = pathUtils.buildFullPath(userId, path);
        StatObjectResponse statObjectResponse = minioUtils.buildStatObject(fullPath, bucket);
        return MinioUtils.buildResourceResponse(path, pathUtils.extractNameFromPath(path),
                statObjectResponse.size(), pathUtils.getResourceType(path));
    }
}
