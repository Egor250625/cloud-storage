package by.egorivanov.cloudstorage.rest;

import by.egorivanov.cloudstorage.docs.storage.resource.DeleteResourceDocs;
import by.egorivanov.cloudstorage.docs.storage.resource.DownloadResourceDocs;
import by.egorivanov.cloudstorage.docs.storage.resource.GetResourceDocs;
import by.egorivanov.cloudstorage.docs.storage.resource.MoveResourceDocs;
import by.egorivanov.cloudstorage.docs.storage.resource.SearchResourceDocs;
import by.egorivanov.cloudstorage.docs.storage.resource.UploadResourceDocs;
import by.egorivanov.cloudstorage.dto.response.ResourceResponse;
import by.egorivanov.cloudstorage.security.CustomUserDetails;
import by.egorivanov.cloudstorage.service.StorageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
@Validated
public class ResourceController {
    private final StorageService storageService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteResourceDocs
    public void delete(@RequestParam String path, @AuthenticationPrincipal CustomUserDetails userDetails) {
        storageService.delete(path, userDetails.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @UploadResourceDocs
    public List<ResourceResponse> uploadResource(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestParam @NotNull String path,
                                                 @RequestPart("object") @NotNull List<MultipartFile> files) {
        return storageService.uploadFiles(path, userDetails.getId(), files);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @GetResourceDocs
    public ResourceResponse get(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam @NotNull String path){
        return storageService.get(path, userDetails.getId());
    }

    @GetMapping("/download")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DownloadResourceDocs
    public ResponseEntity<byte[]> downloadResource(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestParam @NotNull String path
    ) {
        try (InputStream inputStream = storageService.download(path, userDetails.getId())) {
            byte[] bytes = inputStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(path, StandardCharsets.UTF_8));

            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading file", e);
        }
    }

    @GetMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    @MoveResourceDocs
    public ResourceResponse move(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam("from") @NotNull String from,
                                 @RequestParam("to") @NotNull String to) {
        return storageService.move(from, to, userDetails.getId());
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @SearchResourceDocs
    public List<ResourceResponse> search(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam("query") @NotNull String query
                                        ){
        return storageService.search(query,userDetails.getId());
    }
}
