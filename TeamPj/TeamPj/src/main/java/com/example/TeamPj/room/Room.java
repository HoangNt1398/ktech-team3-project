package com.example.TeamPj.room;

import ch.qos.logback.core.status.Status;
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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberAdminId;
    private String memberAdminNickname;
    private int memberMaxCount;
    private int memberCurrentCount;
    private int favoriteCount;

    private String title;
    private String info;
    private String password;
    private String imageUrl;

    private boolean isPrivates;
    private String notice;

//    @Enumerated(EnumType.STRING)
//    private Status status = Status.OPEN;
//
//    public enum Status{OPEN,CLOSED}


    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tags tags;
    private LocalDateTime createAt;
    private LocalDateTime lastModifiedAt;
}
