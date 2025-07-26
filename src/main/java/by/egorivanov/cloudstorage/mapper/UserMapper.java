package by.egorivanov.cloudstorage.mapper;

import by.egorivanov.cloudstorage.dto.request.UserAuthDto;
import by.egorivanov.cloudstorage.dto.request.UserCreateEditDto;
import by.egorivanov.cloudstorage.dto.response.UserReadDto;
import by.egorivanov.cloudstorage.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserReadDto toDto(User user);

    //@Mapping(target = "id", ignore = true)
    User toEntity(UserCreateEditDto dto);

    @Mapping(target = "id", ignore = true)
    User toEntityAuth(UserAuthDto dto);
}
