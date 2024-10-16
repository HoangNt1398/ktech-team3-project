package com.example.server.member.service;

import com.example.server.auth.utils.ErrorResponse;
import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import com.example.server.member.dto.MemberDto;
import com.example.server.member.dto.MemberTagDtos;
import com.example.server.member.entity.Member;
import com.example.server.member.entity.MemberRoom;
import com.example.server.member.entity.MemberTag;
import com.example.server.member.repository.MemberRepository;
import com.example.server.member.repository.MemberTagRepository;
import com.example.server.tag.entity.Tag;
import com.example.server.tag.repository.TagRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTagRepository memberTagRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, MemberTagRepository memberTagRepository, TagRepository tagRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.memberTagRepository = memberTagRepository;
        this.tagRepository = tagRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member createMember(Member member, String profile) {
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);
        member.setImageUrl(profile);
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }


    public Member updateMemberNickname(Member member, long memberId) {
        Member findMember = findVerifiedMember(memberId);
        Optional.ofNullable(member.getNickname())
                .ifPresent(nickname -> findMember.setNickname(nickname));
        return memberRepository.save(findMember);
    }


    public Member updateMemberImage(Member member, long memberId) {
        Member findMember = findVerifiedMember(memberId);
        Optional.ofNullable(member.getImageUrl())
                .ifPresent(image -> findMember.setImageUrl(image));
        return memberRepository.save(findMember);
    }


    public Member updateMemberPassword(MemberDto.PatchPassword requestBody, long memberId) {
        Member findMember = findVerifiedMember(memberId);
        String newEncryptedPassword = passwordEncoder.encode(requestBody.getNewPassword());
        findMember.setPassword(newEncryptedPassword);
        return memberRepository.save(findMember);
    }


    public Member findMember(long memberId) {
        Member member = findVerifiedMember(memberId);
        return member;
    }


    public Page<MemberRoom> findLikeRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberLikeRooms = member.getMemberRoomList()

                .stream()
                .filter(memberRoom -> memberRoom.getMember().getMemberId().equals(memberId))
                .filter(memberRoom -> memberRoom.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .collect(Collectors.toList());
        return new PageImpl<>(memberLikeRooms, pageable, memberLikeRooms.size());
    }


    public Page<MemberRoom> findCreatedRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberCreatedRooms = member.getMemberRoomList()
                .stream()
                .filter(memberRoom -> memberRoom.getAuthority().equals(MemberRoom.Authority.ADMIN))
                .collect(Collectors.toList());

        return new PageImpl<>(memberCreatedRooms, pageable, memberCreatedRooms.size());
    }



    public void removeUser(long memberId) {
        Member member = findVerifiedMember(memberId);

        LocalDateTime deletionDate = LocalDateTime.now().plusDays(30);
        member.setDeletionDate(deletionDate);
        member.setStatus(Member.MemberStatus.DELETE);

        memberRepository.save(member);
    }

    //Todo : DB 체크메서드
    public Member findVerifiedMember(long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member findMember = member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if (findMember.getStatus().equals(Member.MemberStatus.DELETE)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return findMember;
    }


    public Member findVerifiedMember(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        Member findMember = member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if (findMember.getStatus().equals(Member.MemberStatus.DELETE)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return findMember;
    }


    public void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXIST);
    }


    public ResponseEntity<ErrorResponse> verifyExistsNickname(String nickname) {
        Optional<Member> memberNickname = memberRepository.findByNickname(nickname);
        if (memberNickname.isPresent()) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "이미 사용중인 이름입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return null;
    }


    public ResponseEntity<ErrorResponse> checkPassword(MemberDto.PatchPassword requestBody, long memberId) {
        Member findMember = findVerifiedMember(memberId);
        String memberPassword = findMember.getPassword();
        String encryptedPassword = passwordEncoder.encode(requestBody.getPassword());
        if (passwordEncoder.matches(memberPassword, encryptedPassword)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 비밀번호입니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return null;
    }


}
