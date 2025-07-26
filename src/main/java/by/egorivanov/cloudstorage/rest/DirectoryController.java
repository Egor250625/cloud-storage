package by.egorivanov.cloudstorage.rest;

import by.egorivanov.cloudstorage.docs.storage.directory.CreateDirectoryDocs;
import by.egorivanov.cloudstorage.docs.storage.directory.GetDirectoriesDocs;
import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.security.CustomUserDetails;
import by.egorivanov.cloudstorage.service.StorageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
@Validated
public class DirectoryController {

    private final StorageService storageService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CreateDirectoryDocs
    public ResourceResponse createDirectory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestParam @NotNull String path) {
        return storageService.createDirectory(path, userDetails.getId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @GetDirectoriesDocs
    public List<ResourceResponse> getDirectory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestParam @NotNull String path) {
        return storageService.getAllDirectories(path, userDetails.getId());
    }
}
