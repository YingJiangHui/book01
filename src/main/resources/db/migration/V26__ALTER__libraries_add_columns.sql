-- 为libraries 表添加一些设置字段包括：最大借阅天数，默认借阅天数，闭馆字段
ALTER TABLE libraries ADD COLUMN max_borrow_days INT DEFAULT 45;
ALTER TABLE libraries ADD COLUMN default_borrow_days INT DEFAULT 90;
ALTER TABLE libraries ADD COLUMN closed BOOLEAN DEFAULT FALSE;