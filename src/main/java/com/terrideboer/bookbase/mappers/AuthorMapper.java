package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.AuthorDto;
import com.terrideboer.bookbase.dtos.AuthorInputDto;
import com.terrideboer.bookbase.models.Author;

public class AuthorMapper {
    public static Author toEntity(AuthorInputDto authorInputDto, Author author) {
        if (author == null) {
            author = new Author();
        }

        author.setFirstName(authorInputDto.firstName);
        author.setMiddleNames(authorInputDto.middleNames);
        author.setLastName(authorInputDto.lastName);
        author.setDateOfBirth(authorInputDto.dateOfBirth);

        return author;
    }

    public static AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();

        authorDto.id = author.getId();
        authorDto.firstName = author.getFirstName();
        authorDto.middleNames = author.getMiddleNames();
        authorDto.lastName = author.getLastName();
        authorDto.dateOfBirth = author.getDateOfBirth();

        String displayName = author.getFirstName();

        if (author.getMiddleNames() != null) {
            String[] namesArray = author.getMiddleNames().trim().split(" ");
            StringBuilder initials = new StringBuilder();

            for (String name : namesArray) {
                initials.append(name.charAt(0)).append(".");
            }

            displayName += " " + initials;
        }

        displayName += " " + author.getLastName();
        authorDto.displayName = displayName;

        return authorDto;
    }
}
