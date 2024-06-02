/*为用户users表添加违约次数字段和是否加入黑名单字段*/
ALTER TABLE users ADD COLUMN default_times INT DEFAULT 0;
ALTER TABLE users ADD COLUMN is_blacklist BOOLEAN DEFAULT FALSE;