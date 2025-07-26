package by.egorivanov.cloudstorage.minio.manager;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.exception.SearchException;
import by.egorivanov.cloudstorage.minio.util.MinioUtils;
import by.egorivanov.cloudstorage.minio.util.PathUtils;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchManager {

    private final PathUtils pathUtils;
    private final MinioUtils minioUtils;

    public List<ResourceResponse> search(String query, int userId,String bucket) {
        String path = pathUtils.getUserDirectory(userId);
        List<ResourceResponse> results = new ArrayList<>();
        try {

            for (Result<Item> item : minioUtils.listObjects(bucket, path, true)) {
                Item i = item.get();
                String fullPath = i.objectName();
                String name = pathUtils.extractNameFromPath(fullPath);

                if(name.contains(query)){
                    results.add(MinioUtils.buildResourceResponse(
                            pathUtils.getParentPath(fullPath),
                            name,
                            i.size(),
                            pathUtils.getResourceType(fullPath)
                    ));
                }
            }

        }catch (Exception e){
            log.error("No objects found with current query = {}",path,e);
            throw new SearchException("No objects found with  current query.",e);
        }
        return results;
    }

}
