package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private Validator validator;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        validator = mock(Validator.class);
        bookService = new BookService(bookRepository, validator);
    }

    @Test
    void addBook_Success() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setIsbn("12345");

        when(validator.validate(book)).thenReturn(Collections.emptySet());
        when(bookRepository.existsByIsbn("12345")).thenReturn(false);

        Book result = bookService.addBook(book);

        assertNotNull(result.getAddedDate());
        verify(bookRepository).save(book);
    }

    @Test
    void addBook_DuplicateISBN_ThrowsException() {
        Book book = new Book();
        book.setIsbn("12345");

        when(validator.validate(book)).thenReturn(Collections.emptySet());
        when(bookRepository.existsByIsbn("12345")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookService.addBook(book));
        assertEquals("Book with this ISBN already exists", ex.getMessage());
    }

    @Test
    void getBookById_Success() {
        Book book = new Book();
        book.setBookId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertEquals(1L, result.getBookId());
    }

    @Test
    void getBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookService.getBookById(1L));
        assertEquals("Book not found: 1", ex.getMessage());
    }

    @Test
    void updateBook_Success() {
        Book existing = new Book();
        existing.setBookId(1L);
        existing.setIsbn("12345");

        Book updated = new Book();
        updated.setIsbn("67890");
        updated.setTitle("Updated Title");

        when(validator.validate(updated)).thenReturn(Collections.emptySet());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.existsByIsbn("67890")).thenReturn(false);

        Book result = bookService.updateBook(1L, updated);

        assertEquals("Updated Title", result.getTitle());
        assertNotNull(result.getUpdatedDate());
        verify(bookRepository).update(existing);
    }

    @Test
    void updateBook_DuplicateISBN_ThrowsException() {
        Book existing = new Book();
        existing.setBookId(1L);
        existing.setIsbn("12345");

        Book updated = new Book();
        updated.setIsbn("67890");

        when(validator.validate(updated)).thenReturn(Collections.emptySet());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.existsByIsbn("67890")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookService.updateBook(1L, updated));
        assertEquals("Book with this ISBN already exists", ex.getMessage());
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.isBookBorrowed(1L)).thenReturn(false);

        bookService.deleteBook(1L);

        verify(bookRepository).delete(1L);
    }

    @Test
    void deleteBook_BookBorrowed_ThrowsException() {
        when(bookRepository.isBookBorrowed(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookService.deleteBook(1L));
        assertEquals("Book is currently borrowed", ex.getMessage());
    }

    @Test
    void searchBooks_ReturnsList() {
        Book book = new Book();
        book.setTitle("Java Basics");

        when(bookRepository.searchBooks("Java")).thenReturn(List.of(book));

        List<Book> results = bookService.searchBooks("Java");

        assertEquals(1, results.size());
        assertEquals("Java Basics", results.get(0).getTitle());
    }
}