/*为reservations添加borrowing_id指向borrowings表id 列*/
ALTER TABLE reservations ADD COLUMN borrowing_id INT REFERENCES borrowings(id);
