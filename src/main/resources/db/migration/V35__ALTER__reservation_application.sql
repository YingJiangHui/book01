-- 为reservation_application表添加一个disableReserveApplication字段
ALTER TABLE reservation_application ADD COLUMN disable_reserve_application BOOLEAN DEFAULT FALSE;