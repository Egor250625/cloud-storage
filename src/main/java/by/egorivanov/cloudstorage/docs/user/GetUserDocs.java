package by.egorivanov.cloudstorage.docs.user;

import by.egorivanov.cloudstorage.dto.response.ErrorMessageResponse;
import by.egorivanov.cloudstorage.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Get user details by ID",
        requestBody = @RequestBody(
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CustomUserDetails.class)
                )),
        responses = {
                @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "401", description = "User not authenticated",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorMessageResponse.class,
                                        description = "User not authenticated"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorMessageResponse.class,
                                        description = "Internal server error")))}
)
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface GetUserDocs {
}
