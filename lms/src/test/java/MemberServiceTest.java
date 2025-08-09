package com.library.service;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private Validator validator;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        validator = mock(Validator.class);
        memberService = new MemberService(memberRepository, validator);

        // mặc định không có lỗi validate
        when(validator.validate(any())).thenReturn(Collections.emptySet());
    }

    @Test
    void registerMember_success() {
        Member member = new Member();
        member.setEmail("test@example.com");

        when(memberRepository.existsByEmail(member.getEmail())).thenReturn(false);

        Member saved = memberService.registerMember(member);

        assertNotNull(saved.getJoinDate());
        verify(memberRepository).save(member);
    }

    @Test
    void registerMember_duplicateEmail_throwsException() {
        Member member = new Member();
        member.setEmail("test@example.com");

        when(memberRepository.existsByEmail(member.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> memberService.registerMember(member));
        assertEquals("Email already exists", ex.getMessage());
        verify(memberRepository, never()).save(any());
    }

    @Test
    void getMemberById_found() {
        Member member = new Member();
        member.setMemberId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(1L);

        assertEquals(1L, result.getMemberId());
    }

    @Test
    void getMemberById_notFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> memberService.getMemberById(1L));
        assertTrue(ex.getMessage().contains("Member not found"));
    }

    @Test
    void updateMember_success() {
        Member existing = new Member();
        existing.setMemberId(1L);
        existing.setEmail("old@example.com");

        Member updateData = new Member();
        updateData.setName("New Name");
        updateData.setEmail("new@example.com");
        updateData.setPhone("123456");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(memberRepository.existsByEmail("new@example.com")).thenReturn(false);

        Member updated = memberService.updateMember(1L, updateData);

        assertEquals("New Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertNotNull(updated.getUpdatedDate());
        verify(memberRepository).update(existing);
    }

    @Test
    void updateMember_duplicateEmail_throwsException() {
        Member existing = new Member();
        existing.setMemberId(1L);
        existing.setEmail("old@example.com");

        Member updateData = new Member();
        updateData.setEmail("dup@example.com");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(memberRepository.existsByEmail("dup@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> memberService.updateMember(1L, updateData));
        assertEquals("Email already exists", ex.getMessage());
        verify(memberRepository, never()).update(any());
    }

    @Test
    void deleteMember_success() {
        when(memberRepository.hasActiveBorrowings(1L)).thenReturn(false);

        memberService.deleteMember(1L);

        verify(memberRepository).delete(1L);
    }

    @Test
    void deleteMember_withActiveBorrowings_throwsException() {
        when(memberRepository.hasActiveBorrowings(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> memberService.deleteMember(1L));
        assertEquals("Member has active borrowings", ex.getMessage());
        verify(memberRepository, never()).delete(any());
    }

    @Test
    void searchMembers_success() {
        Member member = new Member();
        when(memberRepository.searchMembers("keyword")).thenReturn(List.of(member));

        List<Member> result = memberService.searchMembers("keyword");

        assertEquals(1, result.size());
    }

    @Test
    void registerMember_validationFails_throwsException() {
        Member member = new Member();
        ConstraintViolation<Member> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(() -> "email");
        when(violation.getMessage()).thenReturn("must not be blank");

        when(validator.validate(member)).thenReturn(Set.of(violation));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> memberService.registerMember(member));
        assertTrue(ex.getMessage().contains("email: must not be blank"));
    }
}