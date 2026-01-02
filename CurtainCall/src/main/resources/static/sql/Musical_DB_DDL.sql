# 2025. 11. 25
# 2025. 12. 20
# 2025. 12. 25
# 2026. 01. 02 - postgre 버전 수정 (auto increment -> serial)

# --------------------------------------------------------
# --------------------------------------------------------


# 0. 기본 DB 설정

-- DROP DATABASE IF EXISTS musical_db;
-- CREATE DATABASE musical_db;
-- USE musical_db;


# --------------------------------------------------------
# --------------------------------------------------------

-- ========================================
-- 1. musical
-- ========================================
CREATE TABLE musical (
    musical_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    theater VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL
);

-- ========================================
-- 2. hashtag
-- ========================================
CREATE TABLE hashtag (
    tag_id SERIAL PRIMARY KEY,
    tag VARCHAR(255) NOT NULL
);

-- ========================================
-- 3. tag_connection
-- ========================================
CREATE TABLE tag_connection (
    musical_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    PRIMARY KEY (musical_id, tag_id),
    FOREIGN KEY (musical_id)
        REFERENCES musical (musical_id),
    FOREIGN KEY (tag_id)
        REFERENCES hashtag (tag_id)
        ON DELETE CASCADE
);

-- ========================================
-- 4. actor
-- ========================================
CREATE TABLE actor (
    actor_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth VARCHAR(255),
    agency VARCHAR(255),
    sns VARCHAR(255),
    image VARCHAR(255) NOT NULL,
    work1 TEXT,
    work2 TEXT,
    work3 TEXT,
    work4 TEXT,
    work5 TEXT
);

-- ========================================
-- 5. musical_cast (기존 cast)
-- ========================================
CREATE TABLE musical_cast (
    cast_id SERIAL PRIMARY KEY,
    musical_id INTEGER NOT NULL,
    actor_id INTEGER NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (musical_id)
        REFERENCES musical (musical_id),
    FOREIGN KEY (actor_id)
        REFERENCES actor (actor_id)
);

-- ========================================
-- 6. users
-- ========================================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    nickname VARCHAR(255) NOT NULL,
    user_profile_image VARCHAR(255)
);

-- ========================================
-- 7. review
-- ========================================
CREATE TABLE review (
    review_id SERIAL PRIMARY KEY,
    musical_id INTEGER NOT NULL,
    content TEXT,
    rate SMALLINT CHECK (rate BETWEEN 1 AND 5),
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (musical_id)
        REFERENCES musical (musical_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);

-- ========================================
-- 8. board
-- ========================================
CREATE TABLE board (
    board_id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    category VARCHAR(10) NOT NULL DEFAULT 'free'
        CHECK (category IN ('free', 'deal')),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);

-- ========================================
-- 9. reply
-- ========================================
CREATE TABLE reply (
    reply_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    board_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (board_id)
        REFERENCES board (board_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);

-- ========================================
-- 10. like_review
-- ========================================
CREATE TABLE like_review (
    user_id INTEGER NOT NULL,
    review_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, review_id),
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (review_id)
        REFERENCES review (review_id)
        ON DELETE CASCADE
);

-- ========================================
-- 11. like_musical
-- ========================================
CREATE TABLE like_musical (
    user_id INTEGER NOT NULL,
    musical_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, musical_id),
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (musical_id)
        REFERENCES musical (musical_id)
        ON DELETE CASCADE
);

-- ========================================
-- 12. like_actor
-- ========================================
CREATE TABLE like_actor (
    user_id INTEGER NOT NULL,
    actor_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, actor_id),
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (actor_id)
        REFERENCES actor (actor_id)
        ON DELETE CASCADE
);
