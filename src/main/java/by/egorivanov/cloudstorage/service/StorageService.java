package by.egorivanov.cloudstorage.service;

import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    public List<ResourceResponse> uploadFiles(String path, int userId, List<MultipartFile> files) {
        return storageRepository.uploadFiles(path, userId, files);
    }

    public List<ResourceResponse> getAllDirectories(String path, int userId) {
        return storageRepository.getAllInDirectory(path, userId);
    }

    public ResourceResponse createDirectory(String path, int userId) {
        return storageRepository.createDirectory(path, userId);
    }

    public void delete(String path, int userId) {
        storageRepository.delete(path, userId);
    }

    public InputStream download(String path, int userId) {
        return storageRepository.downloadObject(path, userId);
    }

    public ResourceResponse move(String from, String to, int userId) {
        return storageRepository.move(from, to, userId);
    }

    public ResourceResponse get(String path,int userId){
        return storageRepository.get(path, userId);
    }

    public List<ResourceResponse> search(String query,int userId){
        return storageRepository.search(query,userId);
    }
}
