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

import org.checkerframework.checker.units.qual.m;
import org.springframework.data.domain.Page;
import net.fullstack7.studyShare.util.JpaPageUtil;
import net.fullstack7.studyShare.dto.admin.PageResponseDTO;
import net.fullstack7.studyShare.dto.admin.PageRequestDTO;
import org.springframework.transaction.annotation.Transactional;
import net.fullstack7.studyShare.repository.ShareRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.repository.FileRepository;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.repository.FriendRepository;
import net.fullstack7.studyShare.repository.ChatMemberRepository;
import net.fullstack7.studyShare.repository.ActiveTokensRepository;
import net.fullstack7.studyShare.repository.EmailCodeRepository;
import net.fullstack7.studyShare.repository.ThumbsUpRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ShareRepository shareRepository;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final FriendRepository friendRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ActiveTokensRepository activeTokensRepository;
    private final EmailCodeRepository emailCodeRepository;
    private final ThumbsUpRepository thumbsUpRepository;

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

    @Transactional
    public void deleteMember(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 1. 공유 관련 삭제
        shareRepository.deleteByUserId(member);
        log.info("공유 관련 삭제 완료");
        // 2. 게시글 관련 삭제
        List<Post> posts = postRepository.findByMember(member);
        if (!posts.isEmpty()) {
            shareRepository.deleteByPostIn(posts);
            fileRepository.deleteByPostIn(posts);
            postRepository.deleteAll(posts);
        }
        log.info("게시글 관련 삭제 완료");
        // 3. 이메일 코드 삭제
        emailCodeRepository.deleteByUser(member);
        log.info("이메일 코드 삭제 완료");
        // 4. 좋아요 삭제
        thumbsUpRepository.deleteByUser(member);
        log.info("좋아요 삭제 완료");
        // 5. 친구 관계 삭제
        friendRepository.deleteByUserId(member);
        log.info("친구 관계 삭제 완료");
        // 6. 채팅 관련 삭제
        chatMemberRepository.deleteByMember(member);
        log.info("채팅 관련 삭제 완료");
        // 7. 토큰 삭제
        activeTokensRepository.deleteByMember(member);
        log.info("토큰 삭제 완료");
        // 8. 회원 삭제
        memberRepository.deleteById(member.getUserId());
        log.info("회원 삭제 완료");
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
