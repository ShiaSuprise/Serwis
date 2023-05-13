CREATE DATABASE pro;
 Drop DATABASE pro;
USE pro;
 Drop table customers;
 Drop table complaints;

CREATE TABLE customers(
cu_id INT AUTO_INCREMENT PRIMARY KEY,
cu_name VARCHAR(30),
cu_tel BIGINT(9),
cu_surname VARCHAR(30),
cu_email VARCHAR(30),
cu_password Varchar(30));

CREATE TABLE complaints(
co_id INT AUTO_INCREMENT PRIMARY KEY,
co_name Varchar(40),
co_text Varchar(500),
co_status ENUM('wyslana', 'naprawa', 'nowe urzadzenie', 'odrzucona'),
co_bought DATE,
co_today DATE
);

CREATE TABLE complaintsView(
coV_id INT AUTO_INCREMENT PRIMARY KEY,
coV_text Varchar(500),
coV_name Varchar(40),
coV_today DATE
);

CREATE TABLE stats(
s_id INT AUTO_INCREMENT PRIMARY KEY,
s_all int,
s_send int,
s_remade int,
s_exchanged int,
s_rejected int
);

insert into customers (cu_name, cu_tel, cu_surname, cu_email, cu_password) values
('adrian', 123123123, 'toth', 'adr.toth@gmail.com', '12345'),
('ola', 321321321, 'bochenek', 'ola.boch@gmail.com', 'admin');
select * from customers;
drop table customers;

insert into complaints (co_name, co_text, co_status, co_bought, co_today) values
('pralka', 'stopped working as intended', 'wyslana', '2021-10-01', curdate()),
('pralka', 'broken buttons', 'wyslana', '2021-10-01', '2010-10-01');
select*from complaints;
drop table complaints;
select ifFree('2020-02-09');

DELIMITER //
create procedure complaint(in co_name varchar(40), in co_text Varchar(500), in co_bought DATE) -- skladanie nowej reklamacji
begin
declare co_status ENUM('wyslana', 'naprawa', 'nowe urzadzenie', 'odrzucona');
set co_status = 'wyslana';
-- jesli reklamacja jest skladana 2 lata po zakupie altomaczynie jest jej nadany status "odrzucona" 
if(ifFree(co_bought)) then insert into complaints (co_name, co_text, co_status, co_bought, co_today) values
(co_name, co_text, co_status, co_bought, curdate());
else insert into complaints (co_name, co_text, co_status, co_bought, co_today) values
(co_name, co_text, 'odrzucona', co_bought, curdate());
end if;
end //
delimiter ;
call complaint('telefon', 'taaaak', '2021-10-02');
select*from complaints;
drop procedure complaint;

DELIMITER //
 create function ifFree(co_bought date) -- sprawdza czy minely 2 lata od zakupu
 returns boolean
 deterministic
 begin
 declare ageS boolean;
 declare age smallint;
 SELECT TIMESTAMPDIFF(Day, co_bought, CURDATE()) into age;
 if(age <= 730) then set ageS = true;
else set ageS = false;
end if;
return ageS;
 end; //
 DELIMITER ;
 
 DELIMITER //
create procedure statusChange(in id int, in status ENUM('wyslana', 'naprawa', 'nowe urzadzenie', 'odrzucona')) -- zmiana statusu reklamacji
begin 
UPDATE complaints
SET co_status = status WHERE co_id = id;
end //
delimiter ;

call statusChange(2, 'wyslana');
select * from complaints;
drop procedure statusChange;

SET SQL_SAFE_UPDATES = 0;

DELIMITER //
create procedure complaintView(in id int) -- sprawdzenie przyczyny reklamacji
begin 
declare textX VARCHAR(500);
declare title Varchar(40);
declare complaintD DATE;
DELETE FROM complaintsView;
SELECT co_text into textX FROM complaints WHERE co_id = id;
SELECT co_name into title FROM complaints WHERE co_id = id;
SELECT co_today into complaintD FROM complaints WHERE co_id = id;
insert into complaintsView (coV_today, coV_name, coV_text) values (complaintD, title, textX); 
end //
delimiter ;

call complaintView(1);
select*from complaintsView;
drop procedure complaintView;

DELIMITER //
 create procedure stats(in today date) -- statystyki reklamacji w danym dniu
 begin
 declare wszystkie int;
 declare send int;
 declare remade int;
 declare exchanged int;
 declare rejected int;
 
 DELETE FROM stats;
 
 SELECT count(*) into wszystkie FROM complaints WHERE co_today = today;
 SELECT count(*) into send FROM complaints WHERE co_today = today and co_status = 'wyslana';
 SELECT count(*) into remade FROM complaints WHERE co_today = today and co_status = 'naprawa';
 SELECT count(*) into exchanged FROM complaints WHERE co_today = today and co_status = 'nowe urzadzenie';
 SELECT count(*) into rejected FROM complaints WHERE co_today = today and co_status = 'odrzucona';
 
 insert into stats (s_all, s_send, s_remade, s_exchanged, s_rejected) values (wszystkie, send, remade, exchanged, rejected);
 
 end; //
 DELIMITER ;
 
 select*from stats;
 call stats('2010-10-03');
 drop table stats;
 




