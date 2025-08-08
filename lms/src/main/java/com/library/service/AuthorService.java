package com.library.service;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;

public class AuthorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository;
    private final Validator validator;

    public AuthorService(AuthorRepository authorRepository, Validator validator) {
        this.authorRepository = authorRepository;
        this.validator = validator;
    }

    public Author createAuthor(Author author) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Creating author: {}", author.getName());

        validate(author);
        author.setCreatedDate(LocalDate.now());
        authorRepository.save(author);
        LOGGER.info("Author created: {}", author.getAuthorId());
        MDC.clear();
        return author;
    }

    public Author getAuthorById(Long id) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Fetching author: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found: " + id));
        MDC.clear();
        return author;
    }

    public Author updateAuthor(Long id, Author author) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Updating author: {}", id);

        validate(author);
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found: " + id));
        existing.setName(author.getName());
        existing.setBiography(author.getBiography());
        existing.setNationality(author.getNationality());
        existing.setBirthYear(author.getBirthYear());
        existing.setUpdatedDate(LocalDate.now());
        authorRepository.update(existing);
        LOGGER.info("Author updated: {}", id);
        MDC.clear();
        return existing;
    }

    public void deleteAuthor(Long id) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Deleting author: {}", id);

        if (authorRepository.hasBooks(id)) {
            LOGGER.warn("Cannot delete author {} due to associated books", id);
            throw new RuntimeException("Author has associated books");
        }
        authorRepository.delete(id);
        LOGGER.info("Author deleted: {}", id);
        MDC.clear();
    }

    public List<Author> searchAuthors(String keyword) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Searching authors with keyword: {}", keyword);

        List<Author> authors = authorRepository.searchAuthors(keyword);
        LOGGER.info("Found {} authors", authors.size());
        MDC.clear();
        return authors;
    }

    private void validate(Object object) {
        var violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((a, b) -> a + "; " + b).orElse("Validation failed");
            LOGGER.warn(message);
            throw new RuntimeException(message);
        }
    }
}