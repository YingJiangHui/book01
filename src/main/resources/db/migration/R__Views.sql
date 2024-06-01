-- 创建一个reservations_view 视图，要求新增一个status字段，通过 borrowing_id 来判断状态是否完成
DROP VIEW IF EXISTS reservations_view;
CREATE VIEW reservations_view AS
SELECT r.id,
       r.user_id,
       r.book_id,
       r.borrowed_at,
       r.returned_at,
       r.created_at,
       r.updated_at,
       r.is_deleted,
       r.borrowing_id,
       CASE
           WHEN r.is_deleted THEN 'CANCELED'
           WHEN r.borrowing_id IS NOT NULL THEN 'FULFILLED'
--         r.borrowed_t 的零点加上3天小于当前时间时状态为过期
           WHEN date_trunc('day', r.borrowed_at) +
        interval '4 days' < now() THEN 'EXPIRED'
    /*WHEN r.borrowed_at +
 interval '3 days' < now() THEN 'EXPIRED'*/
--             当前时间小于借阅时间时状态为不可借阅
        WHEN r.borrowed_at > now() THEN 'NOT_BORROWABLE'
--             当前时间大于等于借阅时间时状态为可借阅
        WHEN r.borrowed_at <= now() THEN 'BORROWABLE'
END
AS status
FROM reservations r;



/*创建一个基于borrowings表的 borrowing_view视图*/
DROP VIEW IF EXISTS borrowings_view;
CREATE VIEW borrowings_view AS
SELECT b.id,
       b.user_id,
       b.book_id,
       b.borrowed_at,
       b.returned_at,
       b.expected_return_at,
       b.created_at,
       b.updated_at,
       b.is_deleted,
       CASE
--           如果归还时间大于预期归还时间，则为逾期已归还
           WHEN b.returned_at IS NOT NULL AND b.expected_return_at < b.returned_at THEN 'OVERDUE_RETURNED'
           WHEN b.returned_at IS NOT NULL THEN 'RETURNED'
--            如果当前时间大于预期归还时间，则状态为逾期未归还
           WHEN b.expected_return_at < now() THEN 'OVERDUE_NOT_RETURNED'
           WHEN b.expected_return_at >= now() THEN 'NOT_RETURNED'
           END
AS status
FROM borrowings b;