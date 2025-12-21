# 리뷰 좋아요 더미데이터
INSERT INTO like_review (user_id, review_id) VALUES
(1,4),(2,4),(3,4),(4,4),(5,4),
(6,4),(7,4),(8,4),(9,4),(10,4);

INSERT INTO like_review (user_id, review_id)
SELECT DISTINCT
    FLOOR(1 + RAND() * 10) AS user_id,
    r.review_id
FROM (
    SELECT review_id
    FROM review
    WHERE review_id BETWEEN 5 AND 213
) r
JOIN (
    SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
) cnt
WHERE cnt.n <= FLOOR(1 + RAND() * 10);

SELECT review_id, COUNT(*) AS like_count
FROM like_review
GROUP BY review_id
ORDER BY review_id;

select * from users;
select * from review;