DROP TABLE IF EXISTS products, product_categories, suppliers, ordered_items, orders, customers;


CREATE TABLE product_categories
(
  id          INTEGER PRIMARY KEY,
  name        VARCHAR(50),
  department  VARCHAR(50),
  description VARCHAR(1000)
);


CREATE TABLE suppliers
(
  id          INTEGER PRIMARY KEY,
  name        VARCHAR(50),
  description VARCHAR(1000)
);

CREATE TABLE products
(
  id               INTEGER PRIMARY KEY,
  name             VARCHAR(50),
  description      VARCHAR(1000),
  default_price    INTEGER,
  currency         VARCHAR(20),
  product_category INTEGER REFERENCES product_categories (id),
  supplier         INTEGER REFERENCES suppliers (id)
);

CREATE TABLE customers
(
  id               SERIAL PRIMARY KEY,
  name             VARCHAR(100),
  email            VARCHAR(100),
  phone_number     INTEGER,
  billing_address  VARCHAR(150),
  shipping_address VARCHAR(150)
);

CREATE TABLE orders
(
  id          SERIAL PRIMARY KEY,
  customer_id INTEGER REFERENCES customers (id),
  total_price INTEGER
);

CREATE TABLE ordered_items
(
  id         SERIAL PRIMARY KEY,
  order_id   INTEGER REFERENCES orders (id),
  product_id INTEGER REFERENCES products (id),
  quantity   INTEGER
);