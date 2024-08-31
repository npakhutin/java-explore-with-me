package ru.practicum.ewm.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.dto.AddUserRqDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapToUser(AddUserRqDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail());
    }

    public static UserDto mapToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto mapToShortDto(User initiator) {
        return new UserShortDto(initiator.getId(), initiator.getName());
    }
}
