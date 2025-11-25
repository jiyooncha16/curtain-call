# 2025. 11. 25

# --------------------------------------------------------
# --------------------------------------------------------


# 0. 기본 DB 설정

DROP DATABASE IF EXISTS musical_db;
CREATE DATABASE musical_db;
USE musical_db;


# --------------------------------------------------------
# --------------------------------------------------------


# 1. 공연장 theater
CREATE TABLE `theater` (
	`theater`	VARCHAR(255)	PRIMARY KEY,
	`address`	VARCHAR(255)	NOT NULL,
	`parking`	BOOLEAN	NOT NULL,
	`seats`	INT	NOT NULL,
	`subway`	VARCHAR(255)	NULL
);


# --------------------------------------------------------
# --------------------------------------------------------


# 2. 뮤지컬 musical
CREATE TABLE `musical` (
	`musical_id`	INT PRIMARY KEY AUTO_INCREMENT,
	`title`	VARCHAR(255)	NOT NULL,
	`start_date`	DATE	NOT NULL,
	`end_date`	DATE	NOT NULL,
	`description`	TEXT	NOT NULL,
	`theater`	VARCHAR(255)	NOT NULL -- FK
);

ALTER TABLE musical
ADD CONSTRAINT FK_musical_theater
FOREIGN KEY (theater)
REFERENCES theater(theater);


# --------------------------------------------------------
# --------------------------------------------------------


# 3. 해시태그 hashtag
CREATE TABLE `hashtag` (
	`tag_id`	INT	PRIMARY KEY AUTO_INCREMENT,
	`tag`	VARCHAR(255)	NOT NULL
);


# --------------------------------------------------------
# --------------------------------------------------------


# 4. 태그 연결 tag_connection (hashtag - musical)

CREATE TABLE `tag_connection` (
	`musical_id`	INT	NOT NULL, -- FK
	`tag_id`	INT	NOT NULL -- FK
);
ALTER TABLE `tag_connection` ADD CONSTRAINT `PK_TAG_CONNECTION` PRIMARY KEY (
	`musical_id`,
	`tag_id`
);
ALTER TABLE `tag_connection` ADD CONSTRAINT `FK_musical_TO_tag_connection_1` FOREIGN KEY (
	`musical_id`
)
REFERENCES `musical` (
	`musical_id`
);

-- 태그가 삭제되면 태그연결 데이터도 삭제
ALTER TABLE `tag_connection` ADD CONSTRAINT `FK_hashtag_TO_tag_connection_1` FOREIGN KEY (
	`tag_id`
)
REFERENCES `hashtag` (
	`tag_id`
)
ON DELETE CASCADE;


# --------------------------------------------------------
# --------------------------------------------------------


# 5. 영상 video
CREATE TABLE `video` (
	`video_id`	INT	PRIMARY KEY AUTO_INCREMENT,
	`musical_id`	INT	NOT NULL, -- FK
	`url`	VARCHAR(255)	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`view_cnt`	INT	DEFAULT 0
);
ALTER TABLE `video` ADD CONSTRAINT `FK_musical_TO_video_1` FOREIGN KEY (
	`musical_id`
)
REFERENCES `musical` (
	`musical_id`
);


# --------------------------------------------------------
# --------------------------------------------------------


# 6. 배우 actor

CREATE TABLE `actor` (
	`actor_id`	INT	PRIMARY KEY AUTO_INCREMENT,
	`name`	VARCHAR(255)	NOT NULL,
	`description`	TEXT	NOT NULL
);


# --------------------------------------------------------
# --------------------------------------------------------


# 7. 캐스트 cast (actor - musical)

CREATE TABLE `cast` (
	`musical_id`	INT	NOT NULL, -- FK
	`actor_id`	INT	NOT NULL, -- FK
	`role_name`	VARCHAR(255) NOT NULL
);
ALTER TABLE `cast` ADD CONSTRAINT `PK_CAST` PRIMARY KEY (
	`musical_id`,
	`actor_id`
);
ALTER TABLE `cast` ADD CONSTRAINT `FK_actor_TO_cast_1` FOREIGN KEY (
	`actor_id`
)
REFERENCES `actor` (
	`actor_id`
);
ALTER TABLE `cast` ADD CONSTRAINT `FK_musical_TO_cast_1` FOREIGN KEY (
	`musical_id`
)
REFERENCES `musical` (
	`musical_id`
);


# --------------------------------------------------------
# --------------------------------------------------------

# 8. 유저 users -- user은 예약어

CREATE TABLE `users` (
	`user_id`	INT	PRIMARY KEY AUTO_INCREMENT,
	`username`	VARCHAR(255)	NOT NULL, # 아이디 !!
	`password`	VARCHAR(255)	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL,
	`email`	VARCHAR(255)	NOT NULL,
	`phone`	VARCHAR(255)	NOT NULL,
	`nickname`	VARCHAR(255)	NOT NULL
);


# --------------------------------------------------------
# --------------------------------------------------------


# 9. 리뷰 review

CREATE TABLE `review` (
	`review_id`	INT	PRIMARY KEY AUTO_INCREMENT,
	`musical_id`	INT	NOT NULL, -- FK
	`content`	TEXT	NULL,
	`rate`	TINYINT	NULL CHECK (`rate` BETWEEN 1 AND 5),
	`create_date`	DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`user_id`	INT	NOT NULL -- FK
);

ALTER TABLE `review` 
ADD CONSTRAINT `FK_musical_TO_review_1` 
FOREIGN KEY (`musical_id`)
REFERENCES `musical` (`musical_id`) 
ON DELETE CASCADE;

ALTER TABLE review
ADD CONSTRAINT FK_review_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;


# --------------------------------------------------------
# --------------------------------------------------------


# 10. 게시판 board

CREATE TABLE `board` (
    `board_id` INT PRIMARY KEY auto_increment,
    `title` VARCHAR(255),
    `content` TEXT,
    `category` ENUM('notice', 'free', 'qna', 'review', 'deal') NOT NULL DEFAULT('free'),
    `create_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` INT NOT NULL -- FK
);
ALTER TABLE board
ADD CONSTRAINT FK_board_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;


# --------------------------------------------------------
# --------------------------------------------------------


# 11. 댓글 reply

CREATE TABLE `reply` (
	`reply_id`	INT	PRIMARY KEY auto_increment,
	`content`	TEXT NOT NULL,
	`create_date`	DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`board_id`	INT	NOT NULL, -- FK
	`user_id`	INT	NOT NULL -- FK
);

ALTER TABLE reply 
ADD CONSTRAINT FK_reply_board
FOREIGN KEY (board_id) 
REFERENCES board(board_id) 
ON DELETE CASCADE;

ALTER TABLE reply
ADD CONSTRAINT FK_reply_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;


# --------------------------------------------------------
# --------------------------------------------------------


# 12. 좋아요 (게시판)
CREATE TABLE `like_board` (
	`user_id`	INT	NOT NULL, -- FK
	`board_id`	INT	NOT NULL -- FK
);

ALTER TABLE `like_board` ADD CONSTRAINT `PK_LIKE_BOARD` PRIMARY KEY (
	`user_id`,
	`board_id`
);


# --------------------------------------------------------
# --------------------------------------------------------


# 13. 좋아요 (리뷰)
CREATE TABLE `like_review` (
	`user_id`	INT	NOT NULL, -- FK
	`review_id`	INT	NOT NULL -- FK
);
ALTER TABLE `like_review` ADD CONSTRAINT `PK_LIKE_REVIEW` PRIMARY KEY (
	`user_id`,
	`review_id`
);


# --------------------------------------------------------
# --------------------------------------------------------


# 14. 좋아요 (뮤지컬)
CREATE TABLE `like_musical` (
	`user_id`	INT	NOT NULL, -- FK
	`musical_id`	INT	NOT NULL -- FK
);
ALTER TABLE `like_musical` ADD CONSTRAINT `PK_LIKE_MUSICAL` PRIMARY KEY (
	`user_id`,
	`musical_id`
);


# --------------------------------------------------------
# --------------------------------------------------------


# 15. 좋아요 (배우)
CREATE TABLE `like_actor` (
	`user_id`	INT	NOT NULL, -- FK
	`actor_id`	INT	NOT NULL -- FK
);
ALTER TABLE `like_actor` ADD CONSTRAINT `PK_LIKE_ACTOR` PRIMARY KEY (
	`user_id`,
	`actor_id`
);


# --------------------------------------------------------
# --------------------------------------------------------


# 16. 좋아요 테이블들 외래키 제약조건
ALTER TABLE `like_board` ADD CONSTRAINT `FK_users_TO_like_board_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
)ON DELETE CASCADE;

ALTER TABLE `like_board` ADD CONSTRAINT `FK_board_TO_like_board_1` FOREIGN KEY (
	`board_id`
)
REFERENCES `board` (
	`board_id`
)ON DELETE CASCADE;

ALTER TABLE `like_review` ADD CONSTRAINT `FK_users_TO_like_review_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
)ON DELETE CASCADE;

ALTER TABLE `like_review` ADD CONSTRAINT `FK_review_TO_like_review_1` FOREIGN KEY (
	`review_id`
)
REFERENCES `review` (
	`review_id`
)ON DELETE CASCADE;

ALTER TABLE `like_musical` ADD CONSTRAINT `FK_users_TO_like_musical_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
)ON DELETE CASCADE;

ALTER TABLE `like_musical` ADD CONSTRAINT `FK_musical_TO_like_musical_1` FOREIGN KEY (
	`musical_id`
)
REFERENCES `musical` (
	`musical_id`
)ON DELETE CASCADE;

ALTER TABLE `like_actor` ADD CONSTRAINT `FK_users_TO_like_actor_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
)ON DELETE CASCADE;

ALTER TABLE `like_actor` ADD CONSTRAINT `FK_actor_TO_like_actor_1` FOREIGN KEY (
	`actor_id`
)
REFERENCES `actor` (
	`actor_id`
)ON DELETE CASCADE;
