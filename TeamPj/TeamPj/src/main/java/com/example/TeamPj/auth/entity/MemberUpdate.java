package com.example.TeamPj.auth.entity;

import com.example.TeamPj.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdate extends Member {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member target;

    private Boolean approved;
}
