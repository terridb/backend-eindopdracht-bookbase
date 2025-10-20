package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.AuthorMapper;
import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> dtoAuthors = new ArrayList<>();

        for (Author author : authors) {
            dtoAuthors.add(AuthorMapper.toDto(author));
        }

        return dtoAuthors;
    }

    public AuthorDto getAuthorById(Long id) {
        return AuthorMapper.toDto(
                authorRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Author with id " + id + " not found")));
    }

    public AuthorDto postAuthor(AuthorInputDto authorInputDto) {
        Author author = AuthorMapper.toEntity(authorInputDto, null);
        Author savedAuthor = authorRepository.save(author);

        AuthorDto authorDto = AuthorMapper.toDto(savedAuthor);
        if (authorInputDto.displayName == null) {
            authorDto.displayName = buildDisplayName(savedAuthor);
        }

        return authorDto;
    }

    private String buildDisplayName(Author author) {
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

        return displayName;
    }

}
