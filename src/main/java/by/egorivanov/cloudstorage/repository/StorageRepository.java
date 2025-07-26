package by.egorivanov.cloudstorage.repository;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface StorageRepository {

    List<ResourceResponse> getAllInDirectory(String path, int userId);

    List<ResourceResponse> uploadFiles(String path, int userId, List<MultipartFile> files);

    ResourceResponse createDirectory(String path, int userId);

    void delete(String path,int userId);

    InputStream downloadObject(String path,int userId);

    ResourceResponse move(String from,String to,int userId);

    ResourceResponse get(String path,int userId);

    List<ResourceResponse> search(String query, int userId);
}
