package by.egorivanov.cloudstorage.docs.storage.directory;

import by.egorivanov.cloudstorage.dto.response.ErrorMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "Create directory", description = "Create empty directory in storage",
        tags = {"Storage", "Directory"})
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Directory created successfully",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid path format",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "User not authenticated"))),
        @ApiResponse(responseCode = "404", description = "Resource not found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class, description = "Resource not found")
                )),
        @ApiResponse(responseCode = "409", description = "Resource already exists",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "Resource already exists"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "Internal server error")))})
public @interface CreateDirectoryDocs {
}
