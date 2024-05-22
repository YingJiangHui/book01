/* reservations 表添加returned_at和 borrowed_at字段 */
ALTER TABLE reservations ADD COLUMN returned_at TIMESTAMP NULL;
ALTER TABLE reservations ADD COLUMN borrowed_at TIMESTAMP NULL;
/* 删除reserved_at字段 */
ALTER TABLE reservations DROP COLUMN reserved_at;
