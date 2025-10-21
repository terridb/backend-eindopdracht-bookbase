package com.terrideboer.bookbase.dtos.authors;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AuthorInputDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 150, message = "First name must be between 2 and 150 characters")
    public String firstName;

    @Size(min = 2, max = 150, message = "Middle names must be between 2 and 150 characters")
    public String middleNames;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 150, message = "Last name must be between 2 and 150 characters")
    public String lastName;

    @Size(min = 2, max = 150, message = "Display name must be between 2 and 150 characters")
    public String displayName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    public LocalDate dateOfBirth;
}
