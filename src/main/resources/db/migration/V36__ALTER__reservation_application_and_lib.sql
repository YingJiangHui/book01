ALTER TABLE reservation_application DROP COLUMN disable_reserve_application;
ALTER TABLE libraries ADD COLUMN disable_reserve_application BOOLEAN DEFAULT FALSE;
