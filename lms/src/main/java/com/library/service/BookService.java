package com.library.service;

import com.library.entity.Book;
import com.library.entity.Borrowing;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;

public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final Validator validator;

    public BookService(BookRepository bookRepository, Validator validator) {
        this.bookRepository = bookRepository;
        this.validator = validator;
    }

    public Book createBook(Book book) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Creating book: {}", book.getTitle());

        validate(book);
        book.setAvailable(true);
        book.setCreatedDate(LocalDate.now());
        bookRepository.save(book);
        LOGGER.info("Book created: {}", book.getBookId());
        MDC.clear();
        return book;
    }

    public Book getBookById(Long id) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Fetching book: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        MDC.clear();
        return book;
    }

    public Book updateBook(Long id, Book book) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Updating book: {}", id);

        validate(book);
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        existing.setTitle(book.getTitle());
        existing.setIsbn(book.getIsbn());
        existing.setCategory(book.getCategory());
        existing.setUpdatedDate(LocalDate.now());
        bookRepository.update(existing);
        LOGGER.info("Book updated: {}", id);
        MDC.clear();
        return existing;
    }

    public void deleteBook(Long id) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Deleting book: {}", id);

        if (bookRepository.hasActiveBorrowings(id)) {
            LOGGER.warn("Cannot delete book {} due to active borrowings", id);
            throw new RuntimeException("Book is currently borrowed");
        }
        bookRepository.delete(id);
        LOGGER.info("Book deleted: {}", id);
        MDC.clear();
    }

    public List<Book> searchBooks(String keyword) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Searching books with keyword: {}", keyword);

        List<Book> books = bookRepository.searchBooks(keyword);
        LOGGER.info("Found {} books", books.size());
        MDC.clear();
        return books;
    }

    public List<Book> findTopBorrowed(int limit) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Fetching top {} borrowed books", limit);

        List<Book> books = bookRepository.findMostPopularBooks(limit);
        LOGGER.info("Found {} top borrowed books", books.size());
        MDC.clear();
        return books;
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