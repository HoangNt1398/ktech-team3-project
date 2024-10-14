package com.example.TeamPj.room_tag;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTagsRepository extends CrudRepository<RoomTags, Integer> {
    static Optional<Object> findById(Long id) {
        return null;
    }
    RoomTags deleteById(Long id);
}
