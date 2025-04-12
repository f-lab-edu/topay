-- 1. 사용자 테이블 (users)
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 고유 ID',
    role VARCHAR(10) NOT NULL COMMENT '사용자 역할: USER, SELLER, ADMIN',
    name VARCHAR(100) NOT NULL COMMENT '사용자 이름',
    email VARCHAR(100) NOT NULL COMMENT '사용자 이메일 주소 (유일)',
    password_hash VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    phone VARCHAR(20) DEFAULT NULL COMMENT '연락처 번호',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '사용자 상태 (ACTIVE, INACTIVE, SUSPENDED 등)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '계정 생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 정보 업데이트 시간',

    CHECK (role IN ('USER', 'SELLER', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '사용자 정보를 저장하는 테이블';


-- 2. 상품 테이블 (products)
CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 고유 ID',
    seller_id BIGINT NOT NULL COMMENT '판매자 ID (users 테이블 참조)',
    name VARCHAR(100) NOT NULL COMMENT '상품 이름',
    description TEXT COMMENT '상품 상세 설명',
    category VARCHAR(50) DEFAULT NULL COMMENT '상품 카테고리',
    sku VARCHAR(50) DEFAULT NULL COMMENT '상품 고유 코드 (SKU)',
    price DECIMAL(10,2) NOT NULL COMMENT '상품 가격',
    stock INT NOT NULL COMMENT '재고 수량',
    image_url VARCHAR(255) DEFAULT NULL COMMENT '상품 이미지 URL',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '상품 상태 (ACTIVE, OUT_OF_STOCK, DISCONTINUED 등)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '상품 등록 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 상품 수정 시간',

    CONSTRAINT fk_products_seller FOREIGN KEY (seller_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '상품 정보를 저장하는 테이블';


-- 3. 주문 테이블 (orders)
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 고유 ID',
    user_id BIGINT NOT NULL COMMENT '주문자 ID (users 테이블 참조)',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '주문 일시',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '주문 상태 (PENDING, COMPLETED, CANCELED 등)',
    total_price DECIMAL(10,2) NOT NULL COMMENT '총 결제 금액',
    discount_total DECIMAL(10,2) DEFAULT 0 COMMENT '할인 금액 총합',
    shipping_address TEXT COMMENT '배송지 주소',
    payment_method_id BIGINT DEFAULT NULL COMMENT '결제 수단 ID (payment_methods 테이블 참조)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '주문 생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '주문 정보 수정 시간',

    CONSTRAINT fk_orders_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_orders_payment_method FOREIGN KEY (payment_method_id)
        REFERENCES payment_methods(payment_id)
        ON DELETE SET NULL,

    INDEX idx_order_status (status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '주문 정보를 저장하는 테이블';


-- 4. 주문 항목 테이블 (order_items)
CREATE TABLE order_items (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 항목 고유 ID',
    order_id BIGINT NOT NULL COMMENT '주문 ID (orders 테이블 참조)',
    product_id BIGINT NOT NULL COMMENT '상품 ID (products 테이블 참조)',
    product_name VARCHAR(100) COMMENT '주문 시점 상품 이름 (변경 이력 방지)',
    quantity INT NOT NULL COMMENT '주문 수량',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '상품 단가',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '개별 상품 할인 금액',
    status VARCHAR(20) DEFAULT 'ORDERED' COMMENT '주문 항목 상태 (ORDERED, SHIPPED, DELIVERED, RETURNED 등)',

    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE RESTRICT,
        UNIQUE KEY uk_order_product (order_id, product_id),

    INDEX idx_order_items_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '주문 내 개별 상품 항목 정보를 저장하는 테이블';


-- 5. 타임딜 테이블 (time_deals)
CREATE TABLE time_deals (
    timedeal_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '타임딜 고유 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID (products 테이블 참조)',
    title VARCHAR(100) COMMENT '타임딜 제목',
    description TEXT COMMENT '타임딜 상세 설명',
    start_time DATETIME NOT NULL COMMENT '타임딜 시작 시간',
    end_time DATETIME NOT NULL COMMENT '타임딜 종료 시간',
    status VARCHAR(20) DEFAULT 'SCHEDULED' COMMENT '타임딜 상태 (SCHEDULED, ACTIVE, EXPIRED, CANCELED 등)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '타임딜 생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '타임딜 업데이트 시간',

    CONSTRAINT fk_time_deals_product FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE CASCADE,
    UNIQUE KEY uk_time_deals_product (product_id),

    INDEX idx_time_deals_start (start_time),
    INDEX idx_time_deals_end (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '타임딜 정보를 저장하는 테이블';


-- 6. 쿠폰 정책 테이블 (coupon_policies)
CREATE TABLE coupon_policies (
    coupon_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 정책 고유 ID',
    timedeal_id BIGINT NOT NULL COMMENT '타임딜 ID (time_deals 테이블 참조)',
    type VARCHAR(10) NOT NULL COMMENT '쿠폰 유형 (IMMEDIATE: 즉시할인, DOWNLOAD: 다운로드 후 사용)',
    discount_rate DECIMAL(5,2) NOT NULL COMMENT '할인율 (예: 15.00 → 15% 할인)',
    max_issuance INT DEFAULT NULL COMMENT '쿠폰 최대 발급 수량 (NULL: 무제한)',
    min_purchase_amount DECIMAL(10,2) DEFAULT 0 COMMENT '최소 구매 금액',
    description TEXT COMMENT '쿠폰 사용 설명',
    valid_from DATETIME DEFAULT NULL COMMENT '쿠폰 사용 시작 가능 시간',
    valid_to DATETIME DEFAULT NULL COMMENT '쿠폰 만료 시간',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '쿠폰 정책 등록 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '쿠폰 정책 수정 시간',

    CONSTRAINT fk_coupon_policies_timedeal FOREIGN KEY (timedeal_id)
        REFERENCES time_deals(timedeal_id)
        ON DELETE CASCADE,
    UNIQUE KEY uk_coupon_policy (timedeal_id, type),

    CHECK (type IN ('IMMEDIATE', 'DOWNLOAD'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '쿠폰 정책 정보를 저장하는 테이블';


-- 7. 쿠폰 발급 이력 테이블 (coupon_issues)
CREATE TABLE coupon_issues (
    issue_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 발급 이력 고유 ID',
    coupon_id BIGINT NOT NULL COMMENT '쿠폰 정책 ID (coupon_policies 테이블 참조)',
    user_id BIGINT NOT NULL COMMENT '사용자 ID (users 테이블 참조)',
    coupon_code VARCHAR(50) DEFAULT NULL COMMENT '발급된 쿠폰 코드',
    issued_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '쿠폰 발급 시간',
    used_at DATETIME DEFAULT NULL COMMENT '쿠폰 사용 시간',
    issued_ip VARCHAR(45) DEFAULT NULL COMMENT '쿠폰 발급 시 IP 주소',
    status VARCHAR(20) NOT NULL COMMENT '쿠폰 상태 (ISSUED, USED, EXPIRED 등)',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 상태 업데이트 시간',

    CONSTRAINT fk_coupon_issues_policy FOREIGN KEY (coupon_id)
        REFERENCES coupon_policies(coupon_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_coupon_issues_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,
    UNIQUE KEY uk_coupon_issue (coupon_id, user_id),

    INDEX idx_coupon_issues_user (user_id),
    INDEX idx_coupon_issues_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '쿠폰 발급 이력 정보를 저장하는 테이블';


-- 8. 알림 로그 테이블 (notification_logs)
CREATE TABLE notification_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '알림 로그 고유 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID (users 테이블 참조)',
    message TEXT NOT NULL COMMENT '알림 메시지 내용',
    type VARCHAR(20) DEFAULT 'SYSTEM' COMMENT '알림 유형 (SYSTEM, PROMOTION, TRANSACTION 등)',
    channel VARCHAR(20) DEFAULT 'EMAIL' COMMENT '알림 채널 (EMAIL, SMS, PUSH 등)',
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '알림 발송 시간',
    read_at DATETIME DEFAULT NULL COMMENT '알림 확인 시간',
    status VARCHAR(20) NOT NULL COMMENT '알림 상태 (SENT, DELIVERED, READ, FAILED 등)',

    CONSTRAINT fk_notification_logs_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,
    
    INDEX idx_notification_logs_user (user_id),
    INDEX idx_notification_logs_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '사용자에게 발송된 알림 로그를 저장하는 테이블';



-- 9. 결제 수단 테이블 (payment_methods)
CREATE TABLE payment_methods (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '결제 수단 고유 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID (users 테이블 참조)',
    type VARCHAR(50) NOT NULL COMMENT '결제 수단 유형 (CREDIT_CARD, BANK_TRANSFER, PAYPAL 등)',
    provider VARCHAR(50) DEFAULT NULL COMMENT '결제 제공 업체 (예: VISA, Mastercard 등)',
    details VARCHAR(100) DEFAULT NULL COMMENT '결제 수단 상세 정보 (예: 카드번호 암호화된 값)',
    billing_info VARCHAR(100) DEFAULT NULL COMMENT '청구 관련 정보',
    card_last_four VARCHAR(4) DEFAULT NULL COMMENT '카드 마지막 4자리 번호',
    expiration_date DATE DEFAULT NULL COMMENT '카드 만료 날짜',
    token VARCHAR(255) DEFAULT NULL COMMENT '결제 토큰 (외부 연동용)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 수정 시간',

    CONSTRAINT fk_payment_methods_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    INDEX idx_payment_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT = '사용자 결제 수단 정보를 저장하는 테이블';
