/*将reservations的 fulfilled字段删除*/
ALTER TABLE reservations DROP COLUMN fulfilled;
/*给reservations增加 状态字段 */
ALTER TABLE reservations ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'PENDING';