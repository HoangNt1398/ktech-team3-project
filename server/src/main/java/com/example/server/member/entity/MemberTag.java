package com.example.server.member.entity;

import com.example.server.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberTag { //시간 불필요 체크하기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberTagId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tag;
}