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

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class BookCopyControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllBookCopies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book-copies"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].trackingNumber").value("BB-1-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].trackingNumber").value("BB-1-2"))
                .andReturn();
    }

    @Test
    void shouldGetBookCopyById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book-copies/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trackingNumber").value("BB-1-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.book.id").value(1))
                .andReturn();
    }

    @Test
    void shouldUpdateBookCopy() throws Exception {
        String requestJson = """
                   {
                        "trackingNumber" : "BB-4-10"
                 }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/book-copies/6")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trackingNumber").value("BB-4-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.book.id").value(4))
                .andReturn();
    }

    @Test
    void shouldDeleteBookCopy() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/book-copies/4"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

}
