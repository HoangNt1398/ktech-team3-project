package com.example.server.favorite;

import com.example.server.member.entity.Member;
import com.example.server.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite { //리팩토링 1차 (favorite 패키지 전체)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @Column
    private boolean isFavorite;

    @ManyToOne @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne @JoinColumn(name = "ROOM_ID")
    private Room room;

    public boolean isFavorite() {
        return isFavorite;
    }
}