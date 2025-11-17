package com.terrideboer.bookbase.dtos.books;

import com.terrideboer.bookbase.dtos.authors.AuthorSummaryDto;
import com.terrideboer.bookbase.models.enums.Genre;

import java.util.Set;

public class BookDto {
    public Long id;
    public String title;
    public String isbn;
    public String imageUrl;
    public Genre genre;
    public Set<AuthorSummaryDto> authors;
}
