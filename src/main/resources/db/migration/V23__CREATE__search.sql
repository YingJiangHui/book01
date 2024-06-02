-- 创建一个search表用户记录用户在平台发起的搜索关键字
CREATE TABLE search (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    keyword VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_user_id_keyword ON search (user_id, keyword);
