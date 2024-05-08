    -- 用户表
    CREATE TABLE users (
                           id SERIAL PRIMARY KEY,
                           username VARCHAR(50) UNIQUE NOT NULL,
                           password VARCHAR(100) NOT NULL, -- 最好使用哈希后的密码
                           email VARCHAR(100) UNIQUE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 可选的角色表
    CREATE TABLE roles (
                           id SERIAL PRIMARY KEY,
                           role_name VARCHAR(100) UNIQUE NOT NULL,
                           description TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 用户角色
    CREATE TABLE user_roles (
                                id SERIAL PRIMARY KEY,
                                user_id INT REFERENCES users(id),
                                role_id INT REFERENCES roles(id),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 图书分类表
    CREATE TABLE book_categories (
                                     id SERIAL PRIMARY KEY,
                                     category_name VARCHAR(100) UNIQUE NOT NULL,
                                     description TEXT,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 图书表
    CREATE TABLE books (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           author VARCHAR(255) NOT NULL,
                           category_id INT REFERENCES book_categories(id),
                           published_year INT,
                           isbn VARCHAR(20) UNIQUE, -- 国际标准书号
                           available BOOLEAN DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 借阅表
    CREATE TABLE borrowings (
                                id SERIAL PRIMARY KEY,
                                user_id INT REFERENCES users(id),
                                book_id INT REFERENCES books(id),
                                borrowed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                returned_at TIMESTAMP,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 预约表
    CREATE TABLE reservations (
                                  id SERIAL PRIMARY KEY,
                                  user_id INT REFERENCES users(id),
                                  book_id INT REFERENCES books(id),
                                  reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  fulfilled BOOLEAN DEFAULT FALSE,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 图书馆
    CREATE TABLE libraries (
                               id SERIAL PRIMARY KEY,
                               name VARCHAR(50) UNIQUE NOT NULL,
                               latitude INT,
                               longitude INT,
                               circumference INT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 图书馆关联图书
    CREATE TABLE library_books (
                                   id SERIAL PRIMARY KEY,
                                   library_id INT REFERENCES libraries(id),
                                   book_id INT REFERENCES books(id),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted BOOLEAN DEFAULT FALSE
    );

    -- 图书馆关联管理员
    CREATE TABLE library_users (
                                   id SERIAL PRIMARY KEY,
                                   library_id INT REFERENCES libraries(id),
                                   user_id INT REFERENCES users(id),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted BOOLEAN DEFAULT FALSE
    );

    -- CREATE OR REPLACE FUNCTION update_updated_at()
    -- RETURNS TRIGGER AS $$
    -- BEGIN
    --     NEW.updated_at = NOW();
    -- RETURN NEW;
    -- END;
    -- $$ LANGUAGE plpgsql;
    --
    -- DO $$
    -- DECLARE
    -- current_table_name TEXT;
    -- BEGIN
    -- FOR current_table_name IN SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE' LOOP
    --         EXECUTE format('CREATE TRIGGER trigger_update_updated_at
    --                         BEFORE UPDATE ON %I
    --                         FOR EACH ROW
    --                         EXECUTE FUNCTION update_updated_at()', current_table_name);
    -- END LOOP;
    -- END $$;

