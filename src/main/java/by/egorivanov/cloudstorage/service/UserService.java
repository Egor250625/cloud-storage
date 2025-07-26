package by.egorivanov.cloudstorage.service;

import by.egorivanov.cloudstorage.dto.request.UserCreateEditDto;
import by.egorivanov.cloudstorage.dto.response.UserReadDto;
import by.egorivanov.cloudstorage.entity.User;
import by.egorivanov.cloudstorage.exception.UserAlreadyExistsException;
import by.egorivanov.cloudstorage.mapper.UserMapper;
import by.egorivanov.cloudstorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .filter(dto -> !userRepository.existsByUsername(dto.username()))
                .map(dto -> {
                    User user = userMapper.toEntity(dto);
                    user.setPassword(passwordEncoder.encode(dto.password()));
                    log.info("Password encode correct for user {}",dto.username());
                    return userRepository.save(user);
                })
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserAlreadyExistsException(
                        "Account with this email already exists.Or it is incorrect"
                ));
    }


    public UserReadDto findByUsername(String username){
          return userRepository.findByUsername(username)
                  .map(userMapper::toDto).orElseThrow(
                          ()-> new UsernameNotFoundException("This User was not found"));
    }



}
