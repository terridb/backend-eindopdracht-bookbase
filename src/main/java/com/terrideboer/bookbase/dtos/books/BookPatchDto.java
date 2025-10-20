package com.terrideboer.bookbase.dtos.books;

import com.terrideboer.bookbase.models.enums.Genre;

public class BookPatchDto {
    public String title;
    public String isbn;
    public String imageUrl;
    public Genre genre;
}
