package by.egorivanov.cloudstorage.rest;

import by.egorivanov.cloudstorage.docs.user.GetUserDocs;
import by.egorivanov.cloudstorage.dto.response.UserReadDto;
import by.egorivanov.cloudstorage.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/me")
@Tag(name = "User", description = "Endpoints for managing user")
public class UserRestController {
    @GetUserDocs
    @GetMapping
    public UserReadDto getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new UserReadDto(userDetails.getUsername());
    }
}
