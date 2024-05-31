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
           WHEN r.borrowed_at +
        interval '3 days' < now() THEN 'EXPIRED'
--             当前时间小于借阅时间时状态为不可借阅
        WHEN r.borrowed_at > now() THEN 'NOT_BORROWABLE'
--             当前时间大于等于借阅时间时状态为可借阅
        WHEN r.borrowed_at <= now() THEN 'BORROWABLE'
END
AS status
FROM reservations r


