package com.library.service;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private AuthorService authorService;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleAuthor = new Author();
        sampleAuthor.setAuthorId(1L);
        sampleAuthor.setName("John Doe");
        sampleAuthor.setBiography("Famous author");
    }

    @Test
    void createAuthor_Success() {
        when(validator.validate(sampleAuthor)).thenReturn(Collections.emptySet());

        Author created = authorService.createAuthor(sampleAuthor);

        assertNotNull(created.getCreatedDate());
        verify(authorRepository, times(1)).save(sampleAuthor);
    }

    @Test
    void createAuthor_ValidationFails() {
        ConstraintViolation<Author> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(() -> "name");
        when(violation.getMessage()).thenReturn("must not be blank");
        Set violations = Set.of(violation);

        when(validator.validate(sampleAuthor)).thenReturn(violations);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authorService.createAuthor(sampleAuthor));
        assertTrue(ex.getMessage().contains("name: must not be blank"));
        verify(authorRepository, never()).save(any());
    }

    @Test
    void getAuthorById_Found() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        Author found = authorService.getAuthorById(1L);

        assertEquals("John Doe", found.getName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void getAuthorById_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authorService.getAuthorById(1L));
        assertEquals("Author not found: 1", ex.getMessage());
    }

    @Test
    void updateAuthor_Success() {
        when(validator.validate(sampleAuthor)).thenReturn(Collections.emptySet());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        Author updated = new Author();
        updated.setName("Jane Doe");
        updated.setBiography("Updated bio");

        Author result = authorService.updateAuthor(1L, updated);

        assertEquals("Jane Doe", result.getName());
        assertEquals("Updated bio", result.getBiography());
        assertNotNull(result.getUpdatedDate());
        verify(authorRepository, times(1)).update(sampleAuthor);
    }

    @Test
    void updateAuthor_NotFound() {
        when(validator.validate(sampleAuthor)).thenReturn(Collections.emptySet());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authorService.updateAuthor(1L, sampleAuthor));
        assertEquals("Author not found: 1", ex.getMessage());
    }

    @Test
    void deleteAuthor_Success() {
        when(authorRepository.hasActiveBooks(1L)).thenReturn(false);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).delete(1L);
    }

    @Test
    void deleteAuthor_WithActiveBooks() {
        when(authorRepository.hasActiveBooks(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authorService.deleteAuthor(1L));
        assertEquals("Author has active books", ex.getMessage());
        verify(authorRepository, never()).delete(anyLong());
    }

    @Test
    void searchAuthors_ReturnsList() {
        List<Author> authors = List.of(sampleAuthor);
        when(authorRepository.searchAuthors("John")).thenReturn(authors);

        List<Author> result = authorService.searchAuthors("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
}