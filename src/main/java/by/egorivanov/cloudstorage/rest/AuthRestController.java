package by.egorivanov.cloudstorage.rest;

import by.egorivanov.cloudstorage.docs.auth.LoginUserDocs;
import by.egorivanov.cloudstorage.docs.auth.RegisterUserDocs;
import by.egorivanov.cloudstorage.dto.request.UserAuthDto;
import by.egorivanov.cloudstorage.dto.request.UserCreateEditDto;
import by.egorivanov.cloudstorage.dto.response.UserReadDto;
import by.egorivanov.cloudstorage.security.CustomUserDetails;
import by.egorivanov.cloudstorage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final UserService userService;


    @Operation(summary = "Login a user")
    @PostMapping("/sign-in")
    @LoginUserDocs
    public UserReadDto login(@Valid @RequestBody UserAuthDto dto,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                dto.username(),
                dto.password()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContextHolderStrategy.setContext(securityContext);
        securityContext.setAuthentication(authentication);

        securityContextRepository.saveContext(securityContext, request, response);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return new UserReadDto(principal.getUsername());
    }


    @Operation(summary = "Register a new user")
    @PostMapping("/sign-up")
    @RegisterUserDocs
    public UserReadDto registration(@Valid @RequestBody UserCreateEditDto dto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        UserReadDto createdUser = userService.create(dto);


        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(securityContext);

        securityContextRepository.saveContext(securityContext, request, response);

        return createdUser;
    }
}
