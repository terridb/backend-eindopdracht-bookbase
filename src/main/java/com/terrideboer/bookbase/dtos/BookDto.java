package com.terrideboer.bookbase.dtos;

import com.terrideboer.bookbase.models.enums.Genre;

import java.util.List;
import java.util.Set;

public class BookDto {
    public Long id;
    public String title;
    public String isbn;
    public String imageUrl;
    public Genre genre;
//    todo mapper ids
    public Set<Long> authorIds;
//    todo mapper ids
    public List<Long> bookCopyIds;
}
