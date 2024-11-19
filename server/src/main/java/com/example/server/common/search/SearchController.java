package com.example.server.common.search;

import com.example.server.common.response.MultiResponseDto;
import com.example.server.room.dto.RoomDto;
import com.example.server.tag.dto.TagDto;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;


    @GetMapping
    public ResponseEntity getSearchRoomTitle(@RequestParam(value = "sort") String sort, @RequestParam("keyword") String query,
                                             @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                             @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<RoomDto.SearchResponseDto> searchPage = searchService.searchRoomTitles(page-1, size, sort, query);
        List<RoomDto.SearchResponseDto> searchList = searchPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(searchList,searchPage), HttpStatus.OK);
    }


    @GetMapping("/info")
    public ResponseEntity getSearchRoomInfo(@RequestParam(value = "sort") String sort, @RequestParam("keyword") String query,
                                            @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                            @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<RoomDto.SearchResponseDto> searchPage = searchService.searchRoomInfo(page-1, size, sort, query);
        List<RoomDto.SearchResponseDto> searchList = searchPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(searchList,searchPage), HttpStatus.OK);
    }


    @GetMapping("/roomTag")
    public ResponseEntity getSearchRoomTag(@RequestParam(value = "sort") String sort, @RequestParam("keyword") String query,
                                           @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<RoomDto.SearchResponseDto> searchPage = searchService.searchRoomTags(page-1, size, sort, query);
        List<RoomDto.SearchResponseDto> searchList = searchPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(searchList,searchPage), HttpStatus.OK);
    }


    @GetMapping("/tag")
    public ResponseEntity searchTags(@RequestParam("keyword") String query) {

        List<TagDto.TagSearchResponseDto> responseDtos = searchService.searchTag(query);
        return new ResponseEntity<>(responseDtos,HttpStatus.OK);
    }
}
