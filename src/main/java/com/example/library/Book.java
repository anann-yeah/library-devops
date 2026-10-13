package com.example.library;

import jakarta.validation.constraints.*;

public class Book {
    private Long id;
    @NotBlank(message = "title required") private String title;
    @NotBlank(message = "author required") private String author;
    @Min(value = 1, message = "copies must be at least 1") private int copies;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getAuthor() { return author; }
    public void setAuthor(String a) { this.author = a; }
    public int getCopies() { return copies; }
    public void setCopies(int c) { this.copies = c; }
}