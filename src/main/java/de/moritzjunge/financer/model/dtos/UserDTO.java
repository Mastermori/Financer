package de.moritzjunge.financer.model.dtos;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDTO {
    @NotNull
    @NotEmpty(message = "Please enter a username")
    private String name;
    @Email
    private String email;
    @NotEmpty(message = "Please enter a password")
    private String password;

    public String getName() {
        return name;
    }

    public UserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
