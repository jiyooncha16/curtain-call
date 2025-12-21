package com.ssafy.curtaincall.actor.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatedActorDto {
    private int actorId;
    private String name;
    private String image;
 
}
