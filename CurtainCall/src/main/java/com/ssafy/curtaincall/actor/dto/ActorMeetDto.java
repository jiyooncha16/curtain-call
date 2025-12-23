package com.ssafy.curtaincall.actor.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActorMeetDto {
	
	// Actor
	
		private int actorId;
		private String name;
		private String birth;
		private String agency;
		private String sns;
		private String image;
		private String work1; 
		private String work2; 
		private String work3; 
		private String work4; 
		private String work5; 
		private int count;

}
