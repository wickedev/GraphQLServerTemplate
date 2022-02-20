-- users
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    name TEXT,
    hash_salt TEXT NOT NULL,
    roles TEXT[]
);

-- post
CREATE TABLE IF NOT EXISTS post (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    content TEXT,
    published BOOLEAN NOT NULL DEFAULT false,
    posted_at TIMESTAMP NOT NULL,
    author_id BIGINT NOT NULL
);

-- post relationship
ALTER TABLE post ADD FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE;