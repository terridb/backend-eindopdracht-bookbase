package com.terrideboer.bookbase.dtos.users;

import jakarta.validation.constraints.*;

public class UserInputDto {

    @NotBlank(message = "First name is required")
    @Size(min=2, max=128)
    public String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min=2, max=128)
    public String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be a valid phone number, e.g. +31612345678")
    public String phoneNumber;
}
