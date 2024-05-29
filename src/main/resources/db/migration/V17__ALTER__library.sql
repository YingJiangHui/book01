/*将 library 表的经纬度字段改为支持小数*/
ALTER TABLE libraries
ALTER COLUMN latitude TYPE numeric(10, 8),
ALTER COLUMN latitude SET NOT NULL,
ALTER COLUMN longitude TYPE numeric(11, 8),
ALTER COLUMN longitude SET NOT NULL;