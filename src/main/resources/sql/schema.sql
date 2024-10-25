--DROP TABLE IF EXISTS stock;
CREATE TABLE IF NOT EXISTS stock (
  product_id int primary key,
  quantity int NOT NULL,
  unit_price NUMERIC(20,2) NOT NULL,
  last_transaction_id int NULL,
  last_transaction_date TIMESTAMP WITH TIME ZONE NULL
);
--
--DROP TABLE IF EXISTS stock_transaction;
CREATE TABLE IF NOT EXISTS stock_transaction (
  id bigint AUTO_INCREMENT primary key,
  product_id int NOT NULL,
  description varchar(50) null,
  quantity int NOT NULL,
  unit_price NUMERIC(20,2) NOT NULL,
  transaction_type char(1) NOT NULL,
  transaction_date TIMESTAMP WITH TIME ZONE NOT NULL
);