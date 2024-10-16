package com.example.server.tag.controller;

import com.example.server.common.base.BaseDto;
import com.example.server.common.response.MultiResponseDto;
import com.example.server.room.entity.Room;
import com.example.server.tag.dto.TagCustomResponseDto;
import com.example.server.tag.dto.TagDto;
import com.example.server.tag.entity.Tag;
import com.example.server.tag.mapper.TagMapper;
import com.example.server.tag.service.TagService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper mapper;


    @GetMapping("/{tag-id}")
    public ResponseEntity getTag(@PathVariable("tag-id") @Positive long tagId,
                                 @RequestParam(value = "sort") String sort,
                                 @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                 @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<Room> roomTagPage = tagService.findTagSortedByFavorite(page - 1, size, tagId, sort);
        List<Room> roomTagList = roomTagPage.getContent();
        List<BaseDto.FillRoomResponseDtos> tagResponseDtoList = mapper.tagToRoomTagResponseDto(roomTagList);

        Tag tag = tagService.findVerifiedTag(tagId);
        String name = tag.getName();
        TagCustomResponseDto responseDto = new TagCustomResponseDto(name,tagResponseDtoList,roomTagPage);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }




    @GetMapping
    public ResponseEntity getTags(@RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                  @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        Page<Tag> tagPage = tagService.findTags(page - 1, size);
        List<Tag> tagList = tagPage.getContent();
        List<TagDto.TagResponseDto> tagResponseDtosList = mapper.tagToGetTagResponseDtos(tagList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(tagResponseDtosList, tagPage), HttpStatus.OK);
    }
}
