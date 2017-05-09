DROP TABLE IF EXISTS products, product_categories, suppliers, ordered_items, orders, customers;



CREATE TABLE product_categories
(
  id integer PRIMARY KEY,
  name varchar(50),
  department varchar(50),
  description varchar(1000)
);


CREATE TABLE suppliers
(
  id integer PRIMARY KEY,
  name varchar(50),
  description varchar(1000)
);

CREATE TABLE products
(
  id integer PRIMARY KEY,
  name varchar(50),
  description varchar(1000),
  default_price integer,
  currency varchar(20),
  product_category integer REFERENCES product_categories (id),
  supplier integer REFERENCES suppliers (id)
);

CREATE TABLE ordered_items
(
  id integer PRIMARY KEY,
  product integer REFERENCES products (id),
  quantity integer,
  price integer
);

CREATE TABLE customers
(
  id integer PRIMARY KEY,
  name varchar(100),
  email varchar(100),
  phone_number integer,
  billing_address varchar(150),
  shipping_address varchar(150)
);

CREATE TABLE orders
(
  id integer PRIMARY KEY,
  customer integer REFERENCES customers (id),
  ordered_items integer REFERENCES ordered_items (id)
);