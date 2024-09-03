package ru.practicum.ewm.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.dto.AddUserRqDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.ArrayList;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapToUser(AddUserRqDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail(), new ArrayList<>());
    }

    public static UserDto mapToDto(User user) {
        return new UserDto(user.getId(),
                           user.getName(),
                           user.getEmail(),
                           user.getSubscriptions()
                               .stream()
                               .map(UserMapper::mapToShortDto)
                               .collect(Collectors.toList()));
    }

    public static UserShortDto mapToShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
