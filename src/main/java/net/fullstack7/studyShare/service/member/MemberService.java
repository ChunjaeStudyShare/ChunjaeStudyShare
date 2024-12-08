package net.fullstack7.studyShare.service.member;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.dto.member.MemberResponseDTO;
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDTO findByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return MemberResponseDTO.builder()
            .userId(member.getUserId())
            .name(member.getName())
            .email(member.getEmail())
            .phone(member.getPhone())
            .build();
    }
}
