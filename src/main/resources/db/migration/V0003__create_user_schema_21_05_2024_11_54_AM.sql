CREATE TABLE tbl_user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATETIME,
    role_enum VARCHAR(50),
    created_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN
);

-- Add unique constraints or indexes if necessary
CREATE UNIQUE INDEX idx_user_name ON tbl_user(user_name);
CREATE UNIQUE INDEX idx_user_email ON tbl_user(email);
CREATE UNIQUE INDEX idx_user_phone ON tbl_user(phone);