CREATE TABLE customer (
  customer_id    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  customer_name  VARCHAR2(100) NOT NULL,
  email          VARCHAR2(254) NOT NULL UNIQUE,
  mobile_number  VARCHAR2(10) NOT NULL UNIQUE,
  pan_number     VARCHAR2(10) NOT NULL UNIQUE,
  created_at     TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  updated_at     TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT chk_customer_mobile CHECK (REGEXP_LIKE(mobile_number, '^[0-9]{10}$')),
  CONSTRAINT chk_customer_pan CHECK (REGEXP_LIKE(pan_number, '^[A-Z]{5}[0-9]{4}[A-Z]$'))
);

CREATE TABLE merchant (
  merchant_id    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  merchant_name  VARCHAR2(150) NOT NULL,
  category       VARCHAR2(80),
  location       VARCHAR2(150),
  merchant_status VARCHAR2(10) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT chk_merchant_status CHECK (merchant_status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE credit_card (
  card_id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  customer_id          NUMBER NOT NULL,
  card_number          VARCHAR2(19) NOT NULL UNIQUE,
  card_type            VARCHAR2(10) NOT NULL,
  credit_limit         NUMBER(12,2) NOT NULL,
  available_credit     NUMBER(12,2) NOT NULL,
  outstanding_amount   NUMBER(12,2) NOT NULL,
  expiry_date          DATE NOT NULL,
  card_status          VARCHAR2(10) DEFAULT 'ACTIVE' NOT NULL,
  issued_at            TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT fk_card_customer FOREIGN KEY (customer_id)
    REFERENCES customer(customer_id),
  CONSTRAINT chk_card_type CHECK (card_type IN ('SILVER', 'GOLD', 'PLATINUM')),
  CONSTRAINT chk_card_status CHECK (card_status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),
  CONSTRAINT chk_card_limit CHECK (credit_limit > 0),
  CONSTRAINT chk_card_balances CHECK (
    available_credit >= 0
    AND outstanding_amount >= 0
    AND available_credit + outstanding_amount = credit_limit
  )
);

CREATE TABLE card_transaction (
  transaction_id       NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  card_id              NUMBER NOT NULL,
  merchant_id          NUMBER,
  transaction_type     VARCHAR2(10) NOT NULL,
  amount               NUMBER(12,2) NOT NULL,
  transaction_date     TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  transaction_status   VARCHAR2(10) NOT NULL,
  failure_reason       VARCHAR2(250),
  CONSTRAINT fk_txn_card FOREIGN KEY (card_id)
    REFERENCES credit_card(card_id),
  CONSTRAINT fk_txn_merchant FOREIGN KEY (merchant_id)
    REFERENCES merchant(merchant_id),
  CONSTRAINT chk_txn_type CHECK (transaction_type IN ('PURCHASE', 'PAYMENT')),
  CONSTRAINT chk_txn_status CHECK (transaction_status IN ('SUCCESS', 'FAILED')),
  CONSTRAINT chk_txn_amount CHECK (amount > 0),
  CONSTRAINT chk_txn_merchant CHECK (
    (transaction_type = 'PURCHASE' AND merchant_id IS NOT NULL)
    OR (transaction_type = 'PAYMENT' AND merchant_id IS NULL)
  )
);

CREATE INDEX ix_card_customer ON credit_card(customer_id);
CREATE INDEX ix_txn_card_date ON card_transaction(card_id, transaction_date);
CREATE INDEX ix_txn_merchant_date ON card_transaction(merchant_id, transaction_date);
CREATE INDEX ix_txn_type_status_date
ON card_transaction(transaction_type, transaction_status, transaction_date);


