/*为files表添加 bucket_name 和 object_name字段*/
alter table files add column bucket_name varchar(255) not null default '';
alter table files add column object_name varchar(255) not null default '';