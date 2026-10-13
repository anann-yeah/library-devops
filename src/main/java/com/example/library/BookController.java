package com.example.library;
// webhook test

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
public class BookController {
    private final Map<Long, Book> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book b) {
        boolean dup = store.values().stream()
            .anyMatch(x -> x.getTitle().equalsIgnoreCase(b.getTitle()));
        if (dup) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        b.setId(seq.getAndIncrement());
        store.put(b.getId(), b);
        return ResponseEntity.status(HttpStatus.CREATED).body(b);
    }

    @GetMapping
    public Collection<Book> all() { return store.values(); }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable Long id) {
        Book b = store.get(id);
        return b == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return store.remove(id) == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.noContent().build();
    }
}