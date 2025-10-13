package com.terrideboer.bookbase;

import com.terrideboer.bookbase.dtos.AuthorDto;
import com.terrideboer.bookbase.mappers.AuthorMapper;
import com.terrideboer.bookbase.models.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorMapperTest {

    @Test
    public void toDtoDisplayNameShouldHandleSingleMiddleName() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Sarah");
        author.setMiddleNames("Janet");
        author.setLastName("Maas");

        AuthorDto dto = AuthorMapper.toDto(author);

        assertEquals("Sarah J. Maas", dto.displayName);
    }

    @Test
    public void toDtoDisplayNameShouldHandleTwoMiddleNames() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Neil");
        author.setMiddleNames("Richard MacKinnon");
        author.setLastName("Gaiman");

        AuthorDto dto = AuthorMapper.toDto(author);

        assertEquals("Neil R.M. Gaiman", dto.displayName);
    }

    @Test
    public void toDtoShouldHandleNoMiddleNames() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Ali");
        author.setLastName("Hazelwood");

        AuthorDto dto = AuthorMapper.toDto(author);

        assertEquals("Ali Hazelwood", dto.displayName);
    }
}
