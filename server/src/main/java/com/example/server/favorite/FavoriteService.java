package com.example.server.favorite;

import com.example.server.member.entity.Member;
import com.example.server.member.repository.MemberRepository;
import com.example.server.member.service.MemberService;
import com.example.server.room.entity.Room;
import com.example.server.room.repository.RoomRepository;
import com.example.server.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final MemberService memberService;
    private final RoomService roomService;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final FavoriteRepository repository;

    public void changeFavoriteStatus(long roomId, long memberId, boolean favorite) {
        Room findRoom = roomService.findRoom(roomId);
        Member findMember = memberService.findMember(memberId);

        if(favorite && !findMember.isVoted()) addFavorite(findRoom, findMember);
        else undoFavorite(findRoom, findMember);
    }
    public void addFavorite(Room room, Member member) {
        //findByRoom -> findByRoomAndMember 변경 후, 덮어 씌워 지는 문제 해결
        Optional<Favorite> favoriteOptional = repository.findByRoomAndMember(room, member);

        Favorite favorite;
        if(!favoriteOptional.isPresent()) {
            favorite = new Favorite();
            favorite.setMember(member);
            favorite.setRoom(room);
            favorite.setFavorite(true);
            repository.save(favorite);
        }
        else {
            favorite = favoriteOptional.get();
            favorite.setFavorite(true);
        }

        member.setVoted(true);
        member.setFavoriteCount(member.getFavoriteCount() + 1);
        room.setFavoriteCount(room.getFavoriteCount() + 1);

        memberRepository.save(member);
        roomRepository.save(room);
    }

    public void undoFavorite(Room room, Member member) {
        member.setVoted(false);
        member.setFavoriteCount(member.getFavoriteCount() - 1);
        room.setFavoriteCount(room.getFavoriteCount() - 1);
        memberRepository.save(member);
        roomRepository.save(room);

        Optional<Favorite> favoriteRoom = repository.findByRoomAndMember(room, member);
        favoriteRoom.ifPresent(favorite -> repository.delete(favorite));
    }


    public Page<Favorite> getFavorites(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("favoriteId").descending());
        return repository.findByMemberMemberId(memberId, pageable);
    }
}