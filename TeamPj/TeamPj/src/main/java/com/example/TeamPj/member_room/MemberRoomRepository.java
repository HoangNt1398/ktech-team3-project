package com.example.TeamPj.member_room;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoomRepository  extends CrudRepository<MemberRoom, Long> {
}
