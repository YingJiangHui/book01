-- 将system_settings表中的name字段设置为唯一索引
ALTER TABLE system_settings ADD CONSTRAINT system_settings_name_unique UNIQUE (name);