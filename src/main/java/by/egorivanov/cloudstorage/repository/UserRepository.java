package by.egorivanov.cloudstorage.repository;


import by.egorivanov.cloudstorage.entity.User;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(
            @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters long.")
            String username);

    Optional<User> findByUsername(@Size(min = 5,max = 15,message ="Username must be between 5 and 15 characters long." ) String username);
}
