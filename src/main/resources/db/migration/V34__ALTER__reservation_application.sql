-- 将 reservation_application 表的 status 类型改为字符类型
ALTER TABLE reservation_application
    ALTER COLUMN status SET DATA TYPE VARCHAR(255);