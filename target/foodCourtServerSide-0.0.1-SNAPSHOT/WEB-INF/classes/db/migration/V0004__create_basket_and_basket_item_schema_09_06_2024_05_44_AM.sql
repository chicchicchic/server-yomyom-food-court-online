-- Create the tbl_basket table
CREATE TABLE tbl_basket (
    basket_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,

    created_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN
);

-- Create the tbl_basket_item table
CREATE TABLE tbl_basket_item (
    basket_item_id INT AUTO_INCREMENT PRIMARY KEY,
    dish_id INT NOT NULL,
    quantity INT NOT NULL,
    basket_id INT,

    created_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN,

    CONSTRAINT fk_basket
        FOREIGN KEY (basket_id)
        REFERENCES tbl_basket(basket_id)
        ON DELETE CASCADE
);
