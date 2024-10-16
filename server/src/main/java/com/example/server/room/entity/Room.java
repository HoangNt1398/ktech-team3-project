package com.example.server.room.entity;

import com.example.server.common.entity.BaseEntity;
import com.example.server.common.history.RoomHistory;
import com.example.server.favorite.Favorite;
import com.example.server.member.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private Long adminMemberId;

    @Column
    private String adminNickname;

    @Column(nullable = false)
    private String title;

    @Column
    private String info;

    @Column
    private String imageUrl;

    @Column
    private String password;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column(nullable = false)
    private int memberMaxCount;

    @Column
    private int memberCurrentCount;

    @Column
    private int favoriteCount;

    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberRoomList = new ArrayList<>();


    @OneToMany(mappedBy = "room" , cascade = CascadeType.ALL) //ALL
    private List<RoomTag> roomTagList = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomHistory> roomHistoryList = new ArrayList<>(); //history

    public List<RoomTag> setRoomTagList(List<RoomTag> roomTagList) {
        this.roomTagList = roomTagList;
        return roomTagList;
    }

    //리팩토링 1차
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Favorite> favoriteRoomList;
}