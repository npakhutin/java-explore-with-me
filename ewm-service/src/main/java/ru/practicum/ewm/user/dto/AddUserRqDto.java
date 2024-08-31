package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRqDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 250, message = "Имя пользователя должно укладываться в диапазон 2-250 символов")
    private String name;
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неправильный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @Size(min = 6, max = 254, message = "Почтовый адрес должен укладываться в диапазон 6-254 символов")
    private String email;
}
