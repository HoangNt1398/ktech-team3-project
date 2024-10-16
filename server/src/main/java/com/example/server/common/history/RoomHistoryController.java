package com.example.server.common.history;

import com.example.server.auth.utils.ErrorResponse;
import com.example.server.common.response.MultiResponseDto;
import com.example.server.room.mapper.RoomMapper;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RoomHistoryController {

    private final RoomHistoryService roomHistoryService;
    private final RoomMapper mapper;

    @PostMapping("{member-id}/visit")
    public ResponseEntity visitRoom(@PathVariable("member-id") long memberId,
                                    @RequestParam("title") String title) {
        RoomHistoryDto roomHistory = roomHistoryService.checkVisitHistory(memberId,title);
        return ResponseEntity.status(HttpStatus.OK).body(roomHistory);
    }


    @GetMapping("/members/{member-id}/history")
    public ResponseEntity getUserRoomHistories(@PathVariable("member-id") Long memberId,
                                               @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                               @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                               Authentication authentication) {

        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Page<RoomHistoryDto> roomHistoryPage = roomHistoryService.getVisitHistory(page-1, size);
        List<RoomHistoryDto> roomHistoryList = roomHistoryPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(roomHistoryList, roomHistoryPage), HttpStatus.OK);
    }
}
