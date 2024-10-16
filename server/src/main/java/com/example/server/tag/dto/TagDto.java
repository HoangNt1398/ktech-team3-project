package com.example.server.tag.dto;

import com.example.server.member.entity.MemberRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class TagDto {

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Post {
        private long tagId;
        private long memberId;
        private String name;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetRoomTagResponseDto {
        private long roomId;
        private String title;
        private String info;
        @JsonProperty("image_url")
        private String imageUrl;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private int favoriteCount;
        @JsonProperty("favorite_status")
        private MemberRoom.Favorite favoriteStatus;
        private List<TagResponseDto> tags;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TagResponseDto {
        @Positive
        private long tagId;
        @Positive
        private long roomId;
        @NotBlank
        private String name;

        public TagResponseDto(long tagId, String name) {
            this.tagId = tagId;
            this.name = name;
        }
    }



    @Getter
    @Setter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TagSearchResponseDto{
        private String name;
    }
}