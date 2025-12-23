package com.ssafy.curtaincall.user.dto;

import java.util.List;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorMeetDto;
import com.ssafy.curtaincall.musical.dto.Musical;

import lombok.Data;

@Data
public class UserMeResponse {

    private String nickname;
    private List<Musical> recentMusicals;
    private List<ActorMeetDto> favoriteActors;
    
}
