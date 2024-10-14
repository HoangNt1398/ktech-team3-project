package com.example.TeamPj.member.memberService;

import com.example.TeamPj.member.entity.Member;
import com.example.TeamPj.member.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    public Member getMemberById(Long id) {
        return  memberRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Member not found"));
    }
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
