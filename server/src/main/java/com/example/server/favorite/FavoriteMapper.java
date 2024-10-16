package com.example.server.favorite;

import com.example.server.common.base.BaseDto;
import com.example.server.common.base.BaseMapper;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FavoriteMapper extends BaseMapper {

    default List<BaseDto.FillRoomResponseDtos> favoriteToResponseDto(List<Favorite> favoriteList, long memberId) {
        return favoriteList.stream()
                .filter(favorite -> favorite.getMember().getMemberId() == memberId)
                .map(favorite -> this.fillRoomDatas_Member(favorite.getRoom(), favorite.getMember()))
                .collect(Collectors.toList());
    }
}
