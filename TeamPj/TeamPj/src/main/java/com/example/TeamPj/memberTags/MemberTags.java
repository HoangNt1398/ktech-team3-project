package com.example.TeamPj.memberTags;

import com.example.TeamPj.member.entity.Member;
import com.example.TeamPj.tags.Tags;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.generic.Tag;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

   @ManyToOne
    @JoinColumn(name = "tags_id")
    private Tags tags;

   private LocalDateTime createdAt;
   private LocalDateTime lastModifiedAt;




}
