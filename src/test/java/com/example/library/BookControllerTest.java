package com.example.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new BookController()).build();
    }

    private String json(String t, String a, int c) {
        return "{\"title\":\"" + t + "\",\"author\":\"" + a + "\",\"copies\":" + c + "}";
    }

    @Test void createValidBook() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json("Dune","Herbert",3)))
           .andExpect(status().isCreated());
    }
    @Test void blankTitleRejected() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json("","Herbert",3)))
           .andExpect(status().isBadRequest());
    }
    @Test void zeroCopiesRejected() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json("A","B",0)))
           .andExpect(status().isBadRequest());
    }
    @Test void negativeCopiesRejected() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json("A","B",-5)))
           .andExpect(status().isBadRequest());
    }
    @Test void emptyBodyRejected() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content("{}"))
           .andExpect(status().isBadRequest());
    }
    @Test void unknownIdReturns404() throws Exception {
        mvc.perform(get("/books/999")).andExpect(status().isNotFound());
    }
    @Test void deleteUnknownReturns404() throws Exception {
        mvc.perform(delete("/books/999")).andExpect(status().isNotFound());
    }
    @Test void duplicateTitleConflict() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json("Same","X",1)))
           .andExpect(status().isCreated());
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json("same","Y",2)))
           .andExpect(status().isConflict());
    }
}