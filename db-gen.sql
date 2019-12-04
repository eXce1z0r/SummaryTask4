CREATE DATABASE summary_task_4_db;

USE summary_task_4_db;

CREATE TABLE user_roles (id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
                        name_en varchar(15) UNIQUE,
						name_ru varchar(15) UNIQUE);

INSERT INTO user_roles (name_en, name_ru) VALUES ('Customer', 'Пользователь');
INSERT INTO user_roles (name_en, name_ru) VALUES ('Manager', 'Менеджер');
INSERT INTO user_roles (name_en, name_ru) VALUES ('Admin', 'Администратор');

CREATE TABLE users (id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
                   role_id integer,
                   login varchar(20) UNIQUE,
                   mail varchar(50) UNIQUE,
                   password varchar(60),
                   status boolean,
                   FOREIGN KEY (role_id) REFERENCES user_roles(id) ON DELETE CASCADE ON UPDATE CASCADE);
				   
INSERT INTO users(role_id, login, mail, password, status) VALUES (3, 'admin', 'admin@gmail.com', 'admin', true);
INSERT INTO users(role_id, login, mail, password, status) VALUES (2, 'mod', 'mod@gmail.com', 'mod', true);
INSERT INTO users(role_id, login, mail, password, status) VALUES (1, 'cust', 'cust@gmail.com', 'cust', true);

CREATE TABLE tour_types (id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
                        name_en varchar(20),
						name_ru varchar(20));
                        
INSERT INTO tour_types (name_en, name_ru) VALUES ('recreation', 'отдых');
INSERT INTO tour_types (name_en, name_ru) VALUES ('excursion', 'экскурсия');
INSERT INTO tour_types (name_en, name_ru) VALUES ('shopping', 'шоппинг');

CREATE TABLE tour_lodging_types (id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
                        name_en varchar(20),
						name_ru varchar(20));
                       
INSERT INTO tour_lodging_types (name_en, name_ru) VALUES ('hotel', 'отель');
INSERT INTO tour_lodging_types (name_en, name_ru) VALUES ('hostel', 'хостел');
INSERT INTO tour_lodging_types (name_en, name_ru) VALUES ('open air', 'под открытым небом');

CREATE TABLE tours (id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
                   name varchar(20) NOT NULL,
                   info varchar(255),
                   price decimal(10,2),
                   type_id integer NOT NULL,
                   lodging_type_id integer NOT NULL,
                   hot boolean,
                   discount_step float,
                   discount_limit float,
                   FOREIGN KEY (type_id) REFERENCES tour_types(id) ON UPDATE CASCADE,
                   FOREIGN KEY (lodging_type_id) REFERENCES tour_lodging_types(id) ON UPDATE CASCADE);

CREATE TABLE user_tour_order_statuses (id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
                           name_en varchar(20),
						   name_ru varchar(20));
                           
INSERT INTO user_tour_order_statuses (name_en, name_ru) VALUES ('registered', 'зарегистрирован');
INSERT INTO user_tour_order_statuses (name_en, name_ru) VALUES ('payed', 'оплачен');
INSERT INTO user_tour_order_statuses (name_en, name_ru) VALUES ('canceled', 'отменен');
				   
CREATE TABLE user_tour_orders(id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
							   tour_id integer,
                               user_id integer,
                               status_id integer,
							   discount float,
                               FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE ON UPDATE CASCADE,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                               FOREIGN KEY (status_id) REFERENCES user_tour_order_statuses(id) ON UPDATE CASCADE);

INSERT INTO tours (name, info, price, type_id, lodging_type_id, hot, discount_step, discount_limit) VALUES ('test1', 'test oh test', 255.25, 1, 1, true, 0, 0);
INSERT INTO tours (name, info, price, type_id, lodging_type_id, hot, discount_step, discount_limit) VALUES ('test2', 'test oh test2', 523.73, 1, 1, false, 0, 0);
INSERT INTO tours (name, info, price, type_id, lodging_type_id, hot, discount_step, discount_limit) VALUES ('test3', 'test oh test3', 730.11, 2, 2, true, 0, 0);
INSERT INTO tours (name, info, price, type_id, lodging_type_id, hot, discount_step, discount_limit) VALUES ('test4', 'test oh test4', 1333.91, 3, 3, false, 0, 0);
					
INSERT INTO user_tour_orders (tour_id, user_id, status_id, discount) VALUES (1, 3, 1, 5.25);
INSERT INTO user_tour_orders (tour_id, user_id, status_id, discount) VALUES (2, 3, 2, 10.5);
INSERT INTO user_tour_orders (tour_id, user_id, status_id, discount) VALUES (3, 3, 3, 15.21);