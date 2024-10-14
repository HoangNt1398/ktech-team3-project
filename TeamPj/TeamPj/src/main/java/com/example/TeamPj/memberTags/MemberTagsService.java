package com.example.TeamPj.memberTags;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberTagsService {
    @Autowired
    private final MemberTagsRepository memberTagsRepository;

    public List<MemberTags> getMemberTags() {
        return (List<MemberTags>) memberTagsRepository.findAll();
    }
    public MemberTags getMemberTag(Long id) {
        return  memberTagsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("MemberTags not found"));
    }

    public MemberTagsRepository addMemberTags(MemberTags memberTags) {
        return (MemberTagsRepository) memberTagsRepository.save(memberTags);
    }
    public MemberTags deleteMemberTags(MemberTags memberTags) {
        memberTagsRepository.delete(memberTags);
        return memberTags;
    }
}
