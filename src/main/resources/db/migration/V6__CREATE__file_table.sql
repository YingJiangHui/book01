/*创建包含url字段的files表，和books_files 关联表*/
CREATE TABLE files
(
    id  SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL
);
CREATE TABLE books_files
(
    id      SERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL,
    file_id BIGINT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (file_id) REFERENCES files (id)
);