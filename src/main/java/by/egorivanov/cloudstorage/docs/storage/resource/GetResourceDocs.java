package by.egorivanov.cloudstorage.docs.storage.resource;

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
@Operation(
        summary = "Get Resource Info",
        description = "Retrieve information about a resource by its full path. " +
                      "Path must be URL-encoded. Folder paths should end with '/'. " +
                      "This distinction allows differentiating files and folders with the same name in one directory.",
        tags = {"Storage", "Resource"}
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid or missing path",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "The 'path' parameter is either missing or malformed"))),

        @ApiResponse(responseCode = "401", description = "User not authenticated",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "User not authenticated"))),

        @ApiResponse(responseCode = "404", description = "Resource not found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "No resource found for the given path"))),

        @ApiResponse(responseCode = "500", description = "Unknown error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessageResponse.class,
                                description = "Unexpected server error occurred")))
})
public @interface GetResourceDocs {
}
