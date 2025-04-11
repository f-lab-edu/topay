-- 1. 사용자 테이블 (users)
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CHECK (role IN ('USER', 'SELLER', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 상품 테이블 (products)
CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    image_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_seller FOREIGN KEY (seller_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 주문 테이블 (orders)
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 주문 항목 테이블 (order_items)
CREATE TABLE order_items (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE RESTRICT,
    UNIQUE KEY uk_order_product (order_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 타임딜 테이블 (time_deals)
CREATE TABLE time_deals (
     timedeal_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     product_id BIGINT NOT NULL,
     start_time DATETIME NOT NULL,
     end_time DATETIME NOT NULL,
     title VARCHAR(100),
     CONSTRAINT fk_time_deals_product FOREIGN KEY (product_id)
         REFERENCES products(product_id)
         ON DELETE CASCADE,
     UNIQUE KEY uk_time_deals_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 쿠폰 정책 테이블 (coupon_policies)
CREATE TABLE coupon_policies (
    coupon_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timedeal_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL,
    discount_rate DECIMAL(5,2) NOT NULL,
    max_issuance INT DEFAULT NULL,
    CONSTRAINT fk_coupon_policies_timedeal FOREIGN KEY (timedeal_id)
        REFERENCES time_deals(timedeal_id)
        ON DELETE CASCADE,
    UNIQUE KEY uk_coupon_policy (timedeal_id, type),
    CHECK (type IN ('IMMEDIATE', 'DOWNLOAD'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 쿠폰 발급 이력 테이블 (coupon_issues)
CREATE TABLE coupon_issues (
    issue_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    issued_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    used_at DATETIME DEFAULT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_coupon_issues_policy FOREIGN KEY (coupon_id)
        REFERENCES coupon_policies(coupon_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_coupon_issues_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,
    UNIQUE KEY uk_coupon_issue (coupon_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. 알림 로그 테이블 (notification_logs)
CREATE TABLE notification_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_notification_logs_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 9. 결제 수단 테이블 (payment_methods)
CREATE TABLE payment_methods (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    details VARCHAR(100),
    billing_info VARCHAR(100),
    CONSTRAINT fk_payment_methods_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
