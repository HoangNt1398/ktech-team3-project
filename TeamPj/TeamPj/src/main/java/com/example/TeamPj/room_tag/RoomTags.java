package com.example.TeamPj.room_tag;

import com.example.TeamPj.room.Room;
import com.example.TeamPj.tags.Tags;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tags_id")
    private Tags tags;


    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
