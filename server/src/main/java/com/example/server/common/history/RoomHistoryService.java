package com.example.server.common.history;

import com.example.server.common.search.SearchService;
import com.example.server.common.sort.SortMethod;
import com.example.server.member.entity.Member;
import com.example.server.member.entity.MemberRoom;
import com.example.server.member.service.MemberService;
import com.example.server.room.entity.Room;
import com.example.server.room.service.RoomService;
import com.example.server.tag.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomHistoryService {

    private final RoomHistoryRepository roomHistoryRepository;
    private final MemberService memberService;
    private final RoomService roomService;
    private final SearchService searchService;

    public RoomHistoryDto checkVisitHistory(long memberId, String roomTitle) {

        Member member = memberService.findMember(memberId);
        Room room = roomService.findRoom(roomTitle);

        RoomHistory existingHistory = roomHistoryRepository.findByMember_MemberIdAndRoom_Title(memberId,roomTitle);
        if(existingHistory != null) roomHistoryRepository.delete(existingHistory);

        RoomHistory roomHistory = new RoomHistory();
        roomHistory.setMember(member);
        roomHistory.setRoom(room);
        roomHistory.setVisitTime(LocalDateTime.now());

        RoomHistoryDto roomHistoryDto = createRoomHistoryDto(roomHistory);
        roomHistoryRepository.save(roomHistory);

        return roomHistoryDto;
    }



    public Page<RoomHistoryDto> getVisitHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        String sort = "newRoom";


        List<RoomHistory> roomHistoryList = roomHistoryRepository.findAll();
        List<RoomHistory> sortList = settingSort(roomHistoryList,sort);
        List<RoomHistoryDto> roomHistoryDtoList = sortList.stream()
                .map(this::createRoomHistoryDto)
                .collect(Collectors.toList());

        return new PageImpl<>(roomHistoryDtoList, pageable, roomHistoryList.size());
    }

    public List<RoomHistory> settingSort(List<RoomHistory> searchRooms, String sort) {
        if (sort != null && sort.equals("newRoom")) {
            return searchRooms.stream().sorted(SortMethod.sortByNewHistory())
                    .collect(Collectors.toList());
        }
        return searchRooms;
    }




    private RoomHistoryDto createRoomHistoryDto(RoomHistory roomHistory) {
        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setRoom(roomHistory.getRoom());

        List<TagDto.TagResponseDto> tagResponseDtos = roomHistory.getRoom().getRoomTagList().stream()
                .map(tag -> new TagDto.TagResponseDto(tag.getTag().getTagId(), tag.getTag().getName()))
                .collect(Collectors.toList());

        return RoomHistoryDto.builder()
                .roomId(roomHistory.getRoom().getRoomId())
                .title(roomHistory.getRoom().getTitle())
                .info(roomHistory.getRoom().getInfo())
                .imageUrl(roomHistory.getRoom().getImageUrl())
                .memberMaxCount(roomHistory.getRoom().getMemberMaxCount())
                .memberMaxCount(roomHistory.getRoom().getMemberCurrentCount())
                .isPrivate(roomHistory.getRoom().isPrivate())
                .favoriteStatus(memberRoom.getFavorite())
                .favoriteCount(roomHistory.getRoom().getFavoriteCount())
                .createdAt(LocalDateTime.now())
                .tags(tagResponseDtos).build();

    }
}