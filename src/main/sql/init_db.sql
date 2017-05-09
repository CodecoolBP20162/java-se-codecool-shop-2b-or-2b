DROP TABLE IF EXISTS products, product_categories, suppliers, ordered_items, orders, customers;



CREATE TABLE product_categories
(
  id varchar(36) PRIMARY KEY,
  name varchar(50),
  department varchar(50),
  description varchar(1000)
);


CREATE TABLE suppliers
(
  id varchar(36) PRIMARY KEY,
  name varchar(50),
  description varchar(1000)
);

CREATE TABLE products
(
  id varchar(36) PRIMARY KEY,
  name varchar(50),
  description varchar(1000),
  default_price integer,
  currency varchar(20),
  product_category varchar REFERENCES product_categories (id),
  supplier varchar REFERENCES suppliers (id)
);

CREATE TABLE ordered_items
(
  id varchar(36) PRIMARY KEY,
  product varchar REFERENCES products (id),
  quantity integer,
  price integer
);

CREATE TABLE customers
(
  id varchar(36) PRIMARY KEY,
  name varchar(100),
  email varchar(100),
  phone_number integer,
  billing_address varchar(150),
  shipping_address varchar(150)
);

CREATE TABLE orders
(
  id VARCHAR(36) PRIMARY KEY,
  customer VARCHAR REFERENCES customers (id),
  ordered_items VARCHAR REFERENCES ordered_items (id)
);