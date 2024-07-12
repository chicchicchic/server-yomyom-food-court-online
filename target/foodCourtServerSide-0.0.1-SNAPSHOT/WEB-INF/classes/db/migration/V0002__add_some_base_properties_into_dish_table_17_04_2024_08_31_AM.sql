ALTER TABLE tbl_dish
ADD created_at DATETIME,
ADD created_by VARCHAR(255),
ADD updated_by VARCHAR(255),
ADD is_deleted BOOLEAN;
