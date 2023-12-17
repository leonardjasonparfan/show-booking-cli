package com.ljparfan.showbooking.command;

import com.ljparfan.showbooking.model.UserType;
import com.ljparfan.showbooking.service.AuthService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class AuthCommands extends AppCommands {
    private final AuthService authService;
    @ShellMethod(value = "Login as 'user' or 'admin'.", group = "Auth Commands", key = "Auth")
    public String auth(@Option(longNames = "Login as 'user' or 'admin'.")  @Pattern(regexp = "^(admin|user)$", message = "Role must be either 'admin' or 'user'") String userType) {
        authService.setCurrentUserType(UserType.valueOf(userType.toUpperCase()));
        return "Login as %s successful.".formatted(userType);
    }
}
