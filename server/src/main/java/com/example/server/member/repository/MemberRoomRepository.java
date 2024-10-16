package com.example.server.member.repository;

import com.example.server.member.entity.MemberRoom;
import com.example.server.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
    MemberRoom findByRoom(Room findRoom);
}