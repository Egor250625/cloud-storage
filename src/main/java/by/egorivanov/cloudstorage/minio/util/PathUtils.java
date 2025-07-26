package by.egorivanov.cloudstorage.minio.util;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PathUtils {
    private static final String DIRECTORY_TYPE = "DIRECTORY";
    private static final String FILE_TYPE = "FILE";

    private final MinioClient minioClient;

    public String buildFullPath(int userId, String path) {
        return "user-" + userId + "-files/" + (path.isEmpty() ? "" : path);
    }

    public String extractDisplayName(String objectName, String prefix) {
        if (objectName == null || !objectName.startsWith(prefix) || objectName.length() <= prefix.length()) {
            return "";
        }

        String relative = objectName.substring(prefix.length());

        if (isDirectory(relative)) {
            return relative.substring(0, relative.length() - 1);
        }
        String[] parts = relative.split("/");
        String filename = parts[parts.length - 1];
        int dotIndex = filename.lastIndexOf(".");
        //return dotIndex == -1 ? filename : filename.substring(0, dotIndex);
        return relative;
    }

    public boolean isDirectory(String path) {
        return path.endsWith("/") || path.isEmpty();
    }

    public String getResourceType(String objectName) {
        if (isDirectory(objectName)) {
            return DIRECTORY_TYPE;
        }
        return FILE_TYPE;
    }

    public String extractNameFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash == -1) {
            return path;
        }
        return path.substring(lastSlash + 1);
    }

    public String getParentPath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        String trimmedPath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        int lastSlash = trimmedPath.lastIndexOf('/');

        if (lastSlash == -1) {
            return "";
        }
        return trimmedPath.substring(0, lastSlash + 1);
    }

    public String getUserDirectory(int userId) {
        return String.format("user-%d-files/", userId);
    }
}
