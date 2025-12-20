USE musical_db;
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
(2, '더미데이터0', 3, 1),
(3, '더미데이터0', 3, 1),
(4, '더미데이터0', 3, 1);


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
            	LIMIT 10 OFFSET 0;
                
#
SELECT b.*, u.nickname FROM board as b
LEFT JOIN users as u on b.user_id = u.user_id;

#
select a.* from actor as a
		left join like_actor as l on a.actor_id = l.actor_id
		group by a.actor_id
		order by count(*) desc
		limit 5;
        
#
SELECT
  n.rate,
  COUNT(r.review_id) AS cnt
FROM (
  SELECT 1 AS rate UNION ALL
  SELECT 2 UNION ALL
  SELECT 3 UNION ALL
  SELECT 4 UNION ALL
  SELECT 5
) n
LEFT JOIN review r
  ON r.rate = n.rate
 AND r.user_id =2
GROUP BY n.rate
ORDER BY n.rate;

#
SELECT
  (SELECT COUNT(*) FROM like_musical WHERE user_id = 1) AS likeMusicalCount,
  (SELECT COUNT(*) FROM like_review  WHERE user_id = 1) AS likeReviewCount,
  (SELECT COUNT(*) FROM like_actor   WHERE user_id = 1) AS likeActorCount,
  (SELECT COUNT(*) FROM review       WHERE user_id = 1) AS reviewCount;
  
#
SELECT m.title, c.role_name
FROM cast as c
LEFT JOIN actor as a ON c.actor_id = a.actor_id
LEFT JOIN musical as m ON c.musical_id = m.musical_id
WHERE c.musical_id = 1;

#
SELECT t.tag
		FROM musical AS m
		left JOIN tag_connection AS c ON m.musical_id = c.musical_id
		left JOIN hashtag AS t ON c.tag_id = t.tag_id
        where m.musical_id = 2;