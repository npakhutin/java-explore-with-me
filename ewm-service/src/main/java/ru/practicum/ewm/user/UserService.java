package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.AddUserRqDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addNewUser(AddUserRqDto userDto);

    void deleteById(Long id);

    List<UserDto> findByIds(List<Long> idList, Integer start, Integer size);

}
