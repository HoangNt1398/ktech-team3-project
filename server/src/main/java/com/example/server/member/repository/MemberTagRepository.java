package com.example.server.member.repository;

import com.example.server.member.entity.MemberTag;
import com.example.server.tag.entity.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

    @EntityGraph(attributePaths = {"tag"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<MemberTag> findByTag(Tag tag);
}
