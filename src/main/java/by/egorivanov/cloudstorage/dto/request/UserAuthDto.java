package by.egorivanov.cloudstorage.dto.request;

import jakarta.validation.constraints.Size;

public record UserAuthDto(@Size(min = 5, max = 20, message = "Username must be between 5 and 15 characters long.")
                          String username,
                          @Size(min = 5, max = 20, message = "Password must be between 7 and 20 characters long.")
                          String password) {
}
