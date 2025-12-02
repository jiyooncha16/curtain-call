
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

# 검색 및 페이징 테스트
SELECT m.*, COUNT(l.musical_id) AS like_count
FROM musical AS m
JOIN tag_connection AS c ON m.musical_id = c.musical_id
JOIN hashtag AS t ON c.tag_id = t.tag_id
LEFT JOIN like_musical AS l ON m.musical_id = l.musical_id
WHERE t.tag IN ('로맨스', '액션')
GROUP BY m.musical_id
ORDER BY like_count DESC;
