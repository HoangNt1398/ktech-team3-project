package com.example.TeamPj.room;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    public List<Room> getAllRooms() {
        return (List<Room>) roomRepository.findAll();
    }
    public Room getRoomById(Long id) {
        return  roomRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Room not found"));
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);

    }
    public Room deleteRoom(Room room) {
        roomRepository.deleteById(room.getId());
        return  room;
    }

}
