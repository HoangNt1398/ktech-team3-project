package com.example.server.room.repository;

import com.example.server.room.entity.RoomTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTagRepository extends JpaRepository<RoomTag, Long> {
}
