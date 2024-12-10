package net.fullstack7.studyShare.service.member;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.dto.member.MemberResponseDTO;
import net.fullstack7.studyShare.dto.member.MemberDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import net.fullstack7.studyShare.util.JpaPageUtil;
import net.fullstack7.studyShare.dto.admin.PageResponseDTO;
import net.fullstack7.studyShare.dto.admin.PageRequestDTO;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDTO getMemberById(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return convertToDTO(member);
    }

    public MemberResponseDTO findByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return MemberResponseDTO.builder()
                .userId(member.getUserId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();
    }

    public int getTotalMemberCount() {
        return (int) memberRepository.count();
    }

    public PageResponseDTO<MemberDTO> getMembersByPaging(PageRequestDTO requestDTO) {
        PageRequest pageRequest = JpaPageUtil.getPageRequest(requestDTO);

        Page<Member> memberPage;
        
        if (requestDTO.hasSearch()) {
            switch (requestDTO.getSearchField()) {
                case "userId":
                    memberPage = memberRepository.findByUserIdContaining(
                        requestDTO.getSearchKeyword(), pageRequest);
                    break;
                case "name":
                    memberPage = memberRepository.findByNameContaining(
                        requestDTO.getSearchKeyword(), pageRequest);
                    break;
                case "email":
                    memberPage = memberRepository.findByEmailContaining(
                        requestDTO.getSearchKeyword(), pageRequest);
                    break;
                case "status":
                    memberPage = memberRepository.findByStatus(
                        Integer.parseInt(requestDTO.getSearchKeyword()), pageRequest);
                    break;
                case "all":
                    memberPage = memberRepository.findByUserIdContainingOrNameContainingOrEmailContaining(
                        requestDTO.getSearchKeyword(), 
                        requestDTO.getSearchKeyword(), 
                        requestDTO.getSearchKeyword(), 
                        pageRequest);
                    break;
                default:
                    memberPage = memberRepository.findAll(pageRequest);
            }
        } else {
            memberPage = memberRepository.findAll(pageRequest);
        }

        List<MemberDTO> memberDTOs = memberPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<MemberDTO>builder()
                .content(memberDTOs)
                .currentPage(requestDTO.getPage())
                .size(requestDTO.getSize())
                .totalElements(memberPage.getTotalElements())
                .sortField(requestDTO.getSortField())
                .sortDirection(requestDTO.getSortDirection())
                .searchField(requestDTO.getSearchField())
                .searchKeyword(requestDTO.getSearchKeyword())
                .build();
    }

    public void updateMember(MemberDTO memberDTO) {
        Member member = memberRepository.findByUserId(memberDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        member = convertToEntity(memberDTO);
        memberRepository.save(member);
    }

    private MemberDTO convertToDTO(Member member) {
        return MemberDTO.builder()
                .userId(member.getUserId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .status(member.getStatus())
                .lastLogin(member.getLastLogin())
                .build();
    }

    private Member convertToEntity(MemberDTO memberDTO) {
        return Member.builder()
                .userId(memberDTO.getUserId())
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .status(memberDTO.getStatus())
                .build();
    }
}
