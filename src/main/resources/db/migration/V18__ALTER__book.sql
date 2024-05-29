/*修改books表的published_year字段为字符串*/
ALTER TABLE books ALTER COLUMN published_year TYPE VARCHAR(255);
/*为books表添加出版社publisher字段*/
ALTER TABLE books ADD COLUMN publisher VARCHAR(255);