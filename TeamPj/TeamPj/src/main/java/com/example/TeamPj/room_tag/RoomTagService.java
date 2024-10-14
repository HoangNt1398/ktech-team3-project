package com.example.TeamPj.room_tag;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTagService {
    @Autowired
    private RoomTagsRepository roomTagsRepository;

    public List<RoomTags> getAllRoomTags() {
        return (List<RoomTags>) roomTagsRepository.findAll();
    }
    public RoomTags getRoomTagById(Long id) {
        return (RoomTags) RoomTagsRepository.findById(id).orElseThrow(()
        -> new RuntimeException("Room tags is not found"));
    }

    public RoomTags addRoomTag(RoomTags roomTags) {
        return roomTagsRepository.save(roomTags);
    }
    public RoomTags deleteRoomTag(Long id) {
        return roomTagsRepository.deleteById(id);

    }
}
