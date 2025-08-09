package com.library.service;

import com.library.entity.Book;
import com.library.entity.Borrowing;
import com.library.entity.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRepository;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingServiceTest {

    private BorrowingRepository borrowingRepository;
    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private Validator validator;
    private BorrowingService borrowingService;

    @BeforeEach
    void setUp() {
        borrowingRepository = mock(BorrowingRepository.class);
        bookRepository = mock(BookRepository.class);
        memberRepository = mock(MemberRepository.class);
        validator = mock(Validator.class);
        borrowingService = new BorrowingService(borrowingRepository, bookRepository, memberRepository, validator);
    }

    @Test
    void borrowBooks_success() {
        Long memberId = 1L;
        List<Long> bookIds = List.of(10L, 20L);

        when(memberRepository.countActiveBorrowings(memberId)).thenReturn(2);
        Book book1 = new Book(); book1.setBookId(10L); book1.setAvailable(true);
        Book book2 = new Book(); book2.setBookId(20L); book2.setAvailable(true);
        when(bookRepository.findByIds(bookIds)).thenReturn(List.of(book1, book2));
        Member member = new Member();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        List<Borrowing> result = borrowingService.borrowBooks(memberId, bookIds, LocalDate.now().plusDays(7));

        assertEquals(2, result.size());
        assertFalse(book1.getAvailable());
        verify(borrowingRepository).saveAll(anyList());
        verify(bookRepository).updateAll(anyList());
    }

    @Test
    void borrowBooks_exceedLimit_shouldThrow() {
        when(memberRepository.countActiveBorrowings(1L)).thenReturn(5);
        assertThrows(RuntimeException.class, () ->
            borrowingService.borrowBooks(1L, List.of(1L), LocalDate.now().plusDays(1))
        );
    }

    @Test
    void borrowBooks_dueDateBeforeToday_shouldThrow() {
        when(memberRepository.countActiveBorrowings(1L)).thenReturn(0);
        assertThrows(RuntimeException.class, () ->
            borrowingService.borrowBooks(1L, List.of(1L), LocalDate.now().minusDays(1))
        );
    }

    @Test
    void borrowBooks_bookNotFound_shouldThrow() {
        when(memberRepository.countActiveBorrowings(1L)).thenReturn(0);
        when(bookRepository.findByIds(List.of(1L))).thenReturn(List.of());
        assertThrows(RuntimeException.class, () ->
            borrowingService.borrowBooks(1L, List.of(1L), LocalDate.now().plusDays(1))
        );
    }

    @Test
    void borrowBooks_bookNotAvailable_shouldThrow() {
        when(memberRepository.countActiveBorrowings(1L)).thenReturn(0);
        Book book = new Book(); book.setAvailable(false);
        when(bookRepository.findByIds(List.of(1L))).thenReturn(List.of(book));
        assertThrows(RuntimeException.class, () ->
            borrowingService.borrowBooks(1L, List.of(1L), LocalDate.now().plusDays(1))
        );
    }

    @Test
    void returnBooks_success() {
        Book book = new Book(); book.setAvailable(false);
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowingId(100L);
        borrowing.setStatus(Borrowing.BorrowingStatus.BORROWED);
        borrowing.setBook(book);

        when(borrowingRepository.findByIds(List.of(100L))).thenReturn(List.of(borrowing));

        List<Borrowing> result = borrowingService.returnBooks(List.of(100L));

        assertEquals(1, result.size());
        assertTrue(book.getAvailable());
        assertEquals(Borrowing.BorrowingStatus.RETURNED, borrowing.getStatus());
        verify(borrowingRepository).updateAll(anyList());
        verify(bookRepository).updateAll(anyList());
    }

    @Test
    void returnBooks_notFound_shouldThrow() {
        when(borrowingRepository.findByIds(List.of(1L))).thenReturn(List.of());
        assertThrows(RuntimeException.class, () ->
            borrowingService.returnBooks(List.of(1L))
        );
    }

    @Test
    void returnBooks_alreadyReturned_shouldThrow() {
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowingId(1L);
        borrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);

        when(borrowingRepository.findByIds(List.of(1L))).thenReturn(List.of(borrowing));
        assertThrows(RuntimeException.class, () ->
            borrowingService.returnBooks(List.of(1L))
        );
    }
}