package com.terrideboer.bookbase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthorControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllAuthors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].displayName").value("Sarah J. Maas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].displayName").value("Suzanne Collins"))
                .andReturn();
    }

    @Test
    void shouldGetAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("Sarah J. Maas"))
                .andReturn();
    }

    @Test
    void shouldGetAllBooksFromAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("A Court of Thorns and Roses"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("A Court of Mist and Fury"))
                .andReturn();
    }

    @Test
    void shouldPostAuthor() throws Exception {
        String requestJson = """
                   {
                        "firstName" : "Ann",
                        "middleNames" : "Matthews",
                        "lastName" :  "Martin",
                        "dateOfBirth" : "1955-08-12"
                 }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", matchesPattern("^.*/authors/\\d+")))
                .andReturn();
    }

    @Test
    void shouldUpdateAuthor() throws Exception {
        String requestJson = """
                   {
                        "firstName" : "Ali",
                        "middleNames" : "Example",
                        "lastName" :  "Hazelwood",
                        "dateOfBirth" : "1989-12-11"
                 }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/authors/3")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleNames").value("Example"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("Ali E. Hazelwood"))
                .andReturn();
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/4"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

}
