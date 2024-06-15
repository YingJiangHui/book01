-- 删除enable_reserve、enable_reserve字段
ALTER TABLE libraries DROP COLUMN enable_reserve;
ALTER TABLE libraries DROP COLUMN enable_borrow;
-- 添加disable_reserve、disable_borrow字段
ALTER TABLE libraries ADD COLUMN disable_reserve BOOLEAN DEFAULT FALSE;
ALTER TABLE libraries ADD COLUMN disable_borrow BOOLEAN DEFAULT FALSE;