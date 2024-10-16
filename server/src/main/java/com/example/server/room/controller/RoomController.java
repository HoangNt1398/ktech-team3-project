package com.example.server.room.controller;

import com.example.server.auth.jwt.custom.CheckUserPermission;
import com.example.server.auth.utils.ErrorResponse;
import com.example.server.common.response.MultiResponseDto;
import com.example.server.member.entity.Member;
import com.example.server.member.service.MemberService;
import com.example.server.room.dto.RoomDto;
import com.example.server.room.entity.Room;
import com.example.server.room.mapper.RoomMapper;
import com.example.server.room.service.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final MemberService memberService;
    private final RoomMapper mapper;

    @Value("${default.thumbnail.image}")
    private String thumbnail;

    @PostMapping("/{member-id}/add")
    @CheckUserPermission
    public ResponseEntity postRoom(@PathVariable("member-id") long memberId,
                                   @Valid @RequestBody RoomDto.Post requestBody) {

        requestBody.setAdminMemberId(memberId);
        if(requestBody.getImageUrl() == null) requestBody.setImageUrl(thumbnail);
        Room room = mapper.postDtoToRoom(requestBody);
        room = roomService.createRoom(room, requestBody);

        return new ResponseEntity<>(mapper.roomToPostResponseDto(room), HttpStatus.CREATED);
    }


    //방제 중복체크
    @PostMapping("/check")
    public ResponseEntity checkTitle(@Valid @RequestBody RoomDto.CheckTitle requestBody) {
        ResponseEntity checkTitle = roomService.verifyExistsCheck(requestBody.getTitle());

        if(checkTitle != null) return checkTitle;
        return new ResponseEntity(requestBody.getTitle(), HttpStatus.OK);
    }


    @PatchMapping("/{room-id}/edit")
    @CheckUserPermission
    public ResponseEntity patchRoom(@Valid @RequestBody RoomDto.Patch requestBody,
                                    @PathVariable("room-id") @Positive long roomId) {
        requestBody.setRoomId(roomId);
        Member member = memberService.findMember(requestBody.getAdminMemberId()); //10월 09일 태경 추가
        Room room = mapper.patchDtoToRoom(requestBody);
        room = roomService.updateRoom(room, requestBody.getAdminMemberId());
        return new ResponseEntity<>(mapper.roomToPatchResponseDto(room,member),HttpStatus.OK);
    }
    //log.info("비밀번호 확인 1 {}",requestBody.getPassword());
    //log.info("비밀번호 확인 2 {}", room.getPassword());
    //log.info("비밀번호 확인 3 {}", room.getPassword());




    @PatchMapping("/{room-id}/switch")
    public ResponseEntity patchAdmin(@Valid @RequestBody RoomDto.PatchAdmin requestBody,
                                     @PathVariable("room-id") @Positive long roomId) {
        requestBody.setRoomId(roomId);
        Room room = mapper.patchAdminDtoToRoom(requestBody);
        room = roomService.switchAdmin(room, requestBody.getNewAdminId());
        return new ResponseEntity<>(mapper.roomToPatchAdminResponseDto(room),HttpStatus.OK);
    }


    @GetMapping("/new")
    public ResponseEntity getNewRooms(@RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                      @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<Room> roomPage = roomService.findNewRooms(page-1, size);
        List<Room> roomList = roomPage.getContent();
        List<RoomDto.GetRoomResponseDtos> responseDtosList = mapper.roomToNewRoomResponseDtos(roomList);
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, roomPage), HttpStatus.OK);
    }


    @GetMapping("/0/recommend") //url 수정
    public ResponseEntity getRecommendRooms(@RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                            @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<Room> nonMemberPage = roomService.findRecommendRoomsNonMember(page - 1, size);
        List<Room> nonMemberList = nonMemberPage.getContent();
        List<RoomDto.GetRoomResponseDtos> responseDtoList = mapper.memberToNonMemberRecommendResponseDtos(nonMemberList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtoList, nonMemberPage), HttpStatus.OK);
    }


    @GetMapping("/{member-id}/recommend") //url 수정
    public ResponseEntity getRecommendRooms(@PathVariable("member-id") long memberId,
                                            @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                            @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Member member = memberService.findMember(memberId);
        //log.info("# 회원 {}",member);
        Page<Room> recommendPage = roomService.findRecommendRooms(page-1, size);

        //null 일 경우 빈응답 페이지 반환
        if (recommendPage == null || recommendPage.isEmpty())
            return new ResponseEntity<>(
                    new MultiResponseDto<>(new ArrayList<>(), Page.empty()), HttpStatus.OK);

        List<Room> recommendList = recommendPage.getContent();
        List<RoomDto.GetRoomResponseDtos> responseDtoList = mapper.memberToRecommendResponseDtos(recommendList, member);
        //log.info("# 응답리스트 {}",responseDtoList.isEmpty());

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtoList,recommendPage), HttpStatus.OK);
    }




    @DeleteMapping("/{room-id}")
    //@CheckUserPermission requestBody가 아님
    public ResponseEntity deleteRoom(@PathVariable("room-id") @Positive long roomId,
                                     @RequestParam("member") @Positive long memberId,
                                     Authentication authentication){

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        boolean isAdmin = (boolean) principal.get("isAdmin");
        Room room = roomService.findRoom(roomId);
        long adminId = room.getAdminMemberId();

        if (!isAdmin) {
            if (jwtMemberId != memberId || jwtMemberId != adminId || memberId != adminId) {
                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
        }
        roomService.deleteRoom(roomId); //완전삭제

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
