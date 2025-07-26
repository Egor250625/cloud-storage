package by.egorivanov.cloudstorage.docs.auth;

import by.egorivanov.cloudstorage.dto.request.UserAuthDto;
import by.egorivanov.cloudstorage.dto.response.ErrorMessageResponse;
import by.egorivanov.cloudstorage.dto.response.UserReadDto;
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

@Operation(
        summary = "Register a new user",
        requestBody = @RequestBody(
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserAuthDto.class)
                )
        ),
        responses = {
                @ApiResponse(responseCode = "201", description = "User successfully registered",
                        content = @Content(schema = @Schema(implementation = UserReadDto.class))),
                @ApiResponse(responseCode = "400", description = "Validation error",
                        content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                @ApiResponse(responseCode = "403", description = "Username is already taken",
                        content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                        content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
        }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterUserDocs {
}
