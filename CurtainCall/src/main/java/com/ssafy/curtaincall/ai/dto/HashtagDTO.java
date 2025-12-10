package com.ssafy.curtaincall.ai.dto;

import java.time.LocalDate;

import com.ssafy.curtaincall.board.dto.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HashtagDTO {
	
	// ai 기능 : 해시태그 별 개수 전달 객체
	private String hashtag;
	private int count;
}
