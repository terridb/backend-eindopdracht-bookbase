package com.terrideboer.bookbase.dtos.books;

import com.terrideboer.bookbase.dtos.authors.AuthorSummaryDto;
import com.terrideboer.bookbase.models.enums.Genre;

import java.util.Set;

public class BookSummaryDto {
    public Long id;
    public String title;
    public String isbn;
    public Genre genre;
    public Set<AuthorSummaryDto> authors;
}
