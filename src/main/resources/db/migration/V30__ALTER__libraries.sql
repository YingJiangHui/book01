-- 添加 预约 借阅 功能的是否开启的字段
ALTER TABLE libraries ADD COLUMN enable_reserve BOOLEAN DEFAULT FALSE;
ALTER TABLE libraries ADD COLUMN enable_borrow BOOLEAN DEFAULT FALSE;


