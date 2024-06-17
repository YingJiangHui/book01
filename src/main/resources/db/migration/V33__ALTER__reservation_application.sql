-- 将 reservation_application 表的 borrowing_id 改为非必填
ALTER TABLE reservation_application
    ALTER COLUMN borrowing_id DROP NOT NULL;