
# 테이블 전체 조회
SELECT * FROM musical;
SELECT * FROM theater;
SELECT * FROM hashtag;
SELECT * FROM tag_connection;
SELECT * FROM video;
SELECT * FROM actor;
SELECT * FROM cast;
SELECT * FROM users;
SELECT * FROM review;
SELECT * FROM board;
SELECT * FROM reply;
SELECT * FROM like_board;
SELECT * FROM like_review;
SELECT * FROM like_musical;
SELECT * FROM like_actor;

# review test
update review
set content = '다시 보니 별로인 듯?',
	rate = 1
where review_id = 3;

delete from review where review_id = 18;
commit;
 
SELECT ROUND(AVG(rate), 1) FROM review
WHERE musical_id = 3;

# 검색 및 페이징 테스트
SELECT m.*, COUNT(l.musical_id) AS like_count
FROM musical AS m
JOIN tag_connection AS c ON m.musical_id = c.musical_id
JOIN hashtag AS t ON c.tag_id = t.tag_id
LEFT JOIN like_musical AS l ON m.musical_id = l.musical_id
WHERE t.tag IN ('로맨스', '액션')
GROUP BY m.musical_id
ORDER BY like_count DESC;

commit;

# 리뷰 추가 더미데이터

INSERT INTO review (`musical_id`, `content`, `rate`, `user_id`) VALUES
(8, '더미데이터0', 5, 1),
(9, '더미데이터0', 5, 1),
(10, '더미데이터0', 5, 1);


# ai를 위한 유저가 관람한 뮤지컬 목록 뽑기
select h.tag, count(*)
from users as u
left join review as r
on u.user_id = r.user_id
left join musical as m
on r.musical_id = m.musical_id
left join tag_connection as t
on t.musical_id = m.musical_id
left join hashtag as h
on h.tag_id = t.tag_id
where u.user_id = 1 and r.rate >= 3
group by h.tag;

#
SELECT m.*
		FROM musical AS m
		JOIN tag_connection AS c ON m.musical_id = c.musical_id
		JOIN hashtag AS t ON c.tag_id = t.tag_id
		LEFT JOIN like_musical AS l ON m.musical_id = l.musical_id
        GROUP BY m.musical_id
        ORDER BY COUNT(l.musical_id) DESC
            	LIMIT 10 OFFSET 10;