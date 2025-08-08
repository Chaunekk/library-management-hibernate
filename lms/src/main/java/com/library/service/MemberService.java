package com.library.service;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;

public class MemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final Validator validator;

    public MemberService(MemberRepository memberRepository, Validator validator) {
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    public Member registerMember(Member member) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Registering member: {}", member.getEmail());

        validate(member);
        if (memberRepository.existsByEmail(member.getEmail())) {
            LOGGER.warn("Duplicate email: {}", member.getEmail());
            throw new RuntimeException("Email already exists");
        }
        member.setJoinDate(LocalDate.now());
        memberRepository.save(member);
        LOGGER.info("Member registered: {}", member.getMemberId());
        MDC.clear();
        return member;
    }

    public Member getMemberById(Long id) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Fetching member: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        MDC.clear();
        return member;
    }

    public Member updateMember(Long id, Member member) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Updating member: {}", id);

        validate(member);
        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        if (!existing.getEmail().equals(member.getEmail()) && memberRepository.existsByEmail(member.getEmail())) {
            LOGGER.warn("Duplicate email: {}", member.getEmail());
            throw new RuntimeException("Email already exists");
        }
        existing.setName(member.getName());
        existing.setEmail(member.getEmail());
        existing.setPhone(member.getPhone());
        existing.setUpdatedDate(LocalDate.now());
        memberRepository.update(existing);
        LOGGER.info("Member updated: {}", id);
        MDC.clear();
        return existing;
    }

    public void deleteMember(Long id) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Deleting member: {}", id);

        if (memberRepository.hasActiveBorrowings(id)) {
            LOGGER.warn("Cannot delete member {} due to active borrowings", id);
            throw new RuntimeException("Member has active borrowings");
        }
        memberRepository.delete(id);
        LOGGER.info("Member deleted: {}", id);
        MDC.clear();
    }

    public List<Member> searchMembers(String keyword) {
        String correlationId = java.util.UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId != null ? correlationId : "N/A");
        LOGGER.info("Searching members with keyword: {}", keyword);

        List<Member> members = memberRepository.searchMembers(keyword);
        LOGGER.info("Found {} members", members.size());
        MDC.clear();
        return members;
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