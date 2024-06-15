CREATE TYPE reservation_status AS ENUM ('PENDING', 'NOTIFIED','FULFILL', 'CANCELLED');

-- 创建预约申请表
CREATE TABLE reservation_application
(
    id           SERIAL PRIMARY KEY,
    user_id      INTEGER                              NOT NULL,
    borrowing_id INTEGER                              NOT NULL,
    book_id      INTEGER                              NOT NULL,
    status       reservation_status DEFAULT 'PENDING' NOT NULL,
    created_at   TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    is_deleted   BOOLEAN            DEFAULT FALSE,
        FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (borrowing_id) REFERENCES borrowings (id),
    FOREIGN KEY (book_id) REFERENCES books (id)
);