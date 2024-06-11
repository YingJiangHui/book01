-- 为books添加是否为推荐图书和是否为banner图书字段
ALTER TABLE books ADD COLUMN is_recommend BOOLEAN DEFAULT FALSE;
ALTER TABLE books ADD COLUMN is_banner BOOLEAN DEFAULT FALSE;