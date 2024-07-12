-- Create tbl_order table
CREATE TABLE tbl_order (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    total_payment DOUBLE,
    payment_method VARCHAR(50),
    delivery_time VARCHAR(50),
    notes TEXT,
    order_status_enum VARCHAR(50),

    created_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN
);

-- Create tbl_order_item table
CREATE TABLE tbl_order_item (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dish_id INT NOT NULL,
    quantity INT NOT NULL,
    total_price DOUBLE,
    order_item_status_enum VARCHAR(50),
    order_id BIGINT,

    created_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN,

    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES tbl_order(order_id)
        ON DELETE CASCADE
);

