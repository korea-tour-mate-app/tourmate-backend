USE tourmate;

-- member 테이블에서 모든 데이터 삭제
DELETE FROM member;

-- Member 테이블에 데이터 삽입
SELECT * FROM member;
INSERT INTO member (name, password, email, created_at, created, updated)
VALUES ('노경희', 'keunghee', '21keunghee@gmail.com', '2024-08-23', '2024-08-23 00:00:00', '2024-08-23 00:00:00'),
       ('노희경', 'heekeung', '21heekeung@gmail.com', '2024-08-24', '2024-08-24 00:00:00', '2024-08-24 00:00:00');

-- my_place 테이블에 데이터 삽입
select * from my_place;
INSERT INTO my_place (member_id, visited, created_at, created, updated)
VALUES (1, true, '2024-08-23', '2024-08-23 00:00:00', '2024-08-23 00:00:00'),
       (2, false, '2024-08-23', '2024-08-23 00:00:00', '2024-08-23 00:00:00');

SELECT * FROM theme;
-- theme 테이블에 데이터 삽입
INSERT INTO theme (place_theme, created, updated)
VALUES ('K-pop', '2024-08-23 00:00:00', '2024-08-23 00:00:00'),
       ('고궁', '2024-08-23 00:00:00', '2024-08-23 00:00:00');

select * from place;
-- place 테이블에 데이터 삽입
INSERT INTO place (theme_id, latitude, longitude, place_name, place_location, image_url1, image_url2, image_url3, start_time, close_time, deadline_time, place_cost, check_in, check_out, day_off, during_time, stay_in_cost, template_stay_type, homepage_url, avg_cost, vegan_type, food_type, rate, view_type, inout_type, cafe_type, created, updated)
VALUES
    (1, 37.7749, -122.4194, 'Golden Gate Bridge', 'San Francisco, CA', 'image1.jpg', 'image2.jpg', 'image3.jpg', '08:00', '18:00', '17:30', '$10', '14:00', '12:00', 'None', '2 hours', '$100', 1, 'https://example.com', 4.5, 1, 2, 4.8, 1, 1, 1, '2024-08-01 00:00:00', '2024-08-01 00:00:00'),
    (2, 34.0522, -118.2437, 'Griffith Park', 'Los Angeles, CA', 'image1.jpg', 'image2.jpg', 'image3.jpg', '06:00', '20:00', '19:30', 'Free', 'None', 'None', 'None', '3 hours', 'None', 0, 'https://example.com', 4.2, 2, 3, 4.5, 2, 2, 2, '2024-08-02 00:00:00', '2024-08-02 00:00:00');

select * from visited_place;
-- visited_place 테이블에 데이터 삽입
INSERT INTO visited_place (my_place_id, place_id, created, updated)
VALUES (1, 1, '2024-08-05 00:00:00', '2024-08-05 00:00:00'),
       (2, 2, '2024-08-06 00:00:00', '2024-08-06 00:00:00');

select * from place_like;
-- like 테이블에 데이터 삽입
INSERT INTO place_like (member_id, place_id, created_at, created, updated, liked)
VALUES (1, 1, '2024-08-07', '2024-08-07 00:00:00', '2024-08-07 00:00:00', true),
       (2, 2, '2024-08-08', '2024-08-08 00:00:00', '2024-08-08 00:00:00', true);

select * from review;