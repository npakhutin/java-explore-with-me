package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.dto.AddUserRqDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addNewUser(AddUserRqDto userDto) {
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToDto(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByIds(List<Long> idList, Integer start, Integer size) {
        PageRequest pageable = PageRequest.of(start, size, Sort.by(User.Fields.id).ascending());
        Page<User> foundUsers;
        if (idList.isEmpty()) {
            foundUsers = userRepository.findAll(pageable);
        } else {
            foundUsers = userRepository.findAllByIdIn(idList, pageable);
        }
        return foundUsers.stream().map(UserMapper::mapToDto).collect(Collectors.toList());
    }
}
