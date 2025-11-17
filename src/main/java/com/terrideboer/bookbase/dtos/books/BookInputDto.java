package com.terrideboer.bookbase.dtos.books;

import com.terrideboer.bookbase.models.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookInputDto {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters")
    public String title;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^[0-9]{10,13}$", message = "ISBN must contain 10 to 13 digits")
    public String isbn;

    @NotNull(message = "Genre is required")
    public Genre genre;
}
