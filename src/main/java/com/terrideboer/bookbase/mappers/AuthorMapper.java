package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.models.Author;

public class AuthorMapper {
    public static Author toEntity(AuthorInputDto authorInputDto, Author author) {
        if (author == null) {
            author = new Author();
        }

        author.setFirstName(authorInputDto.firstName);
        author.setMiddleNames(authorInputDto.middleNames);
        author.setLastName(authorInputDto.lastName);
        author.setDisplayName(authorInputDto.displayName);
        author.setDateOfBirth(authorInputDto.dateOfBirth);

        return author;
    }

    public static AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();

        authorDto.id = author.getId();
        authorDto.firstName = author.getFirstName();
        authorDto.middleNames = author.getMiddleNames();
        authorDto.lastName = author.getLastName();
        authorDto.displayName = author.getDisplayName();
        authorDto.dateOfBirth = author.getDateOfBirth();

        return authorDto;
    }
}
