package com.example.server.common.image;

import com.example.server.auth.utils.ErrorResponse;
import com.example.server.member.entity.Member;
import com.example.server.member.service.MemberService;
import com.example.server.room.entity.Room;
import com.example.server.room.service.RoomService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class S3ImageController {

    private final S3ImageService s3ImageService;
    private final RoomService roomService;
    private final MemberService memberService;

    //Profile update
    @PostMapping("/profile/{member-id}")
    public ResponseEntity uploadProfile(@PathVariable("member-id") @Positive long memberId,
                                        @RequestParam("image") MultipartFile file,
                                        Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        boolean isAdmin = (boolean) principal.get("isAdmin");

        if (isAdmin == false) {
            if(jwtMemberId != (memberId)) {
                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
        }
        try {
            return s3ImageService.uploadP(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Thumbnail update
    @PostMapping("/thumbnail")
    public ResponseEntity uploadThumbnail(@RequestParam("image") MultipartFile file,
                                          Authentication authentication) {
        if (authentication == null) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        try {
            return s3ImageService.uploadT(file);
        } catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // profile delete
    @DeleteMapping("/profile/{member-id}")
    public ResponseEntity deleteProfile(@PathVariable("member-id") long memberId,
                                        @RequestParam("image") String imageUrl,
                                        Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        boolean isAdmin = (boolean) principal.get("isAdmin");
        Member member = memberService.findMember(memberId);

        if (isAdmin == false) {
            if(jwtMemberId != (memberId)) {
                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
        }
        try{
            return s3ImageService.deleteProfile(member, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // thumbnail delete
    @DeleteMapping("/thumbnail/{room-id}")
    public ResponseEntity deleteThumbnail(@PathVariable("room-id") long roomId,
                                          @RequestParam("image") String imageUrl,
                                          Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        boolean isAdmin = (boolean) principal.get("isAdmin");

        Room room = roomService.findRoom(roomId);
        long adminId = room.getAdminMemberId();

        if (isAdmin == false) {
            if(jwtMemberId != adminId) {
                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
        }
        try {
            return s3ImageService.deleteThumbnail(room, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


