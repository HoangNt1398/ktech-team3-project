package com.example.TeamPj.tags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository  extends JpaRepository<Tags, Integer> {
    Optional<Object> findById(Long id);

    void deleteById(Long id);
}
