package com.library.service;

import com.library.entity.Book;
import com.library.entity.Borrowing;
import com.library.entity.Member;
import com.library.repository.BorrowingRepository;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowingService.class);
    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final Validator validator;

    public BorrowingService(BorrowingRepository borrowingRepository, BookRepository bookRepository,
                           MemberRepository memberRepository, Validator validator) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    public List<Borrowing> borrowBooks(Long memberId, List<Long> bookIds, LocalDate dueDate) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Borrowing books {} for member {}", bookIds, memberId);

        if (!isEligibleToBorrow(memberId, bookIds.size())) {
            LOGGER.warn("Member {} cannot borrow due to limit", memberId);
            throw new RuntimeException("Member borrowing limit exceeded");
        }
        if (dueDate.isBefore(LocalDate.now())) {
            LOGGER.warn("Invalid due date: {}", dueDate);
            throw new RuntimeException("Due date must be after borrow date");
        }

        List<Book> books = bookRepository.findByIds(bookIds);
        if (books.size() != bookIds.size()) {
            LOGGER.warn("Some books not found: {}", bookIds);
            throw new RuntimeException("One or more books not found");
        }
        if (books.stream().anyMatch(book -> !book.getAvailable())) {
            LOGGER.warn("Some books are not available: {}", bookIds);
            throw new RuntimeException("One or more books are not available");
        }

        List<Borrowing> borrowings = new ArrayList<>();
        for (Book book : books) {
            Borrowing borrowing = new Borrowing();
            borrowing.setMember(memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found: " + memberId)));
            borrowing.setBook(book);
            borrowing.setBorrowDate(LocalDate.now());
            borrowing.setDueDate(dueDate);
            borrowing.setStatus(Borrowing.BorrowingStatus.BORROWED);
            borrowings.add(borrowing);
            book.setAvailable(false);
        }

        borrowingRepository.saveAll(borrowings);
        bookRepository.updateAll(books);
        LOGGER.info("Borrowed {} books for member {}", bookIds.size(), memberId);
        MDC.clear();
        return borrowings;
    }

    public List<Borrowing> returnBooks(List<Long> borrowingIds) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Returning borrowings: {}", borrowingIds);

        List<Borrowing> borrowings = borrowingRepository.findByIds(borrowingIds);
        if (borrowings.size() != borrowingIds.size()) {
            LOGGER.warn("Some borrowings not found: {}", borrowingIds);
            throw new RuntimeException("One or more borrowings not found");
        }

        List<Book> booksToUpdate = new ArrayList<>();
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getStatus() == Borrowing.BorrowingStatus.RETURNED) {
                LOGGER.warn("Borrowing {} already returned", borrowing.getBorrowingId());
                throw new RuntimeException("Borrowing already returned");
            }
            borrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);
            borrowing.setReturnDate(LocalDate.now());
            booksToUpdate.add(borrowing.getBook());
            borrowing.getBook().setAvailable(true);
        }

        borrowingRepository.updateAll(borrowings);
        bookRepository.updateAll(booksToUpdate);
        LOGGER.info("Returned {} borrowings", borrowingIds.size());
        MDC.clear();
        return borrowings;
    }

    private boolean isEligibleToBorrow(Long memberId, int additionalBooks) {
        return memberRepository.countActiveBorrowings(memberId) + additionalBooks <= 5;
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