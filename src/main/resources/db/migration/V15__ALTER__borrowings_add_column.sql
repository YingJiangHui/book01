/*为borrowings 添加预期归还时间*/
ALTER TABLE borrowings ADD COLUMN expected_return_at TIMESTAMP;