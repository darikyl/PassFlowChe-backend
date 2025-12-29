package stu.cn.ua.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @Email(message = "Невірний формат email")
    @NotBlank(message = "Email не може бути порожнім")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
}
