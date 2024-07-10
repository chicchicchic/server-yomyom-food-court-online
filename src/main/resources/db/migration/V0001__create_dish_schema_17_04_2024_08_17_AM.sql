CREATE TABLE tbl_dish (
    dish_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    original_price DECIMAL(10, 2),
    discount DOUBLE,
    preparation_time INT,
    meal_set VARCHAR(255),
    image LONGBLOB,
    category_enum VARCHAR(50)
);