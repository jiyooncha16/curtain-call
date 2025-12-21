package com.ssafy.curtaincall.review.dto;

import java.time.LocalDate;

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
public class MyReviewDto {

	private int reviewId;
	private String content;
	private int rate;
	private LocalDate createDate;
	private String title;
	private String image;
	private String nickname;
	private int likeCount;
}
