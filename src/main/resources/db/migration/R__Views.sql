-- 如果视图存在则先删除
DROP VIEW IF EXISTS user_view;
CREATE VIEW user_view AS
SELECT u.id as user_id,
       u.email,
       r.role_name as role_name,
       r.description as role_description,
       l.id as library_id,
       l.name as library_name,
       l.latitude as latitude,
       l.longitude as longitude,
       l.address as address
from public.users u
         left join public.user_roles ur on u.id = ur.user_id
         left join public.roles r on ur.role_id = r.id
         left join public.library_users ul on u.id = ul.user_id
         left join public.libraries l on ul.library_id = l.id;