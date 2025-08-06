package com.library.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class đại diện cho bảng Author trong database
 * Chứa thông tin về tác giả của sách
 */
@Entity
@Table(name = "authors")
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;
    
    @Column(name = "nationality", length = 100)
    private String nationality;
    
    @Column(name = "birth_year")
    private Integer birthYear;
    
    // Many-to-Many relationship với Book
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();
    
    // Constructors
    public Author() {}
    
    public Author(String name) {
        this.name = name;
    }
    
    public Author(String name, String biography, String nationality) {
        this.name = name;
        this.biography = biography;
        this.nationality = nationality;
    }
    
    public Author(String name, String biography, String nationality, Integer birthYear) {
        this.name = name;
        this.biography = biography;
        this.nationality = nationality;
        this.birthYear = birthYear;
    }
    
    // Getters and Setters
    public Long getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBiography() {
        return biography;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public Integer getBirthYear() {
        return birthYear;
    }
    
    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
    
    public Set<Book> getBooks() {
        return books;
    }
    
    public void setBooks(Set<Book> books) {
        this.books = books;
    }
    
    // Utility methods
    public void addBook(Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }
    
    public void removeBook(Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
    }
    
    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return authorId != null && authorId.equals(author.authorId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}