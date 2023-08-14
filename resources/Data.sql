/* Setting up PROD DB */
create database prod;
use prod;

create table parking(
PARKING_NUMBER int PRIMARY KEY,
AVAILABLE bool NOT NULL,
TYPE varchar(10) NOT NULL
);

create table ticket(
 ID int PRIMARY KEY AUTO_INCREMENT,
 PARKING_NUMBER int NOT NULL,
 VEHICLE_REG_NUMBER varchar(10) NOT NULL,
 PRICE double,
 IN_TIME DATETIME NOT NULL,
 OUT_TIME DATETIME,
 FOREIGN KEY (PARKING_NUMBER)
 REFERENCES parking(PARKING_NUMBER));

insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(1,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(2,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(3,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(4,true,'BIKE');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(5,true,'BIKE');
commit;

/* Setting up TEST DB */
create database test;
use test;

create table parking(
PARKING_NUMBER int PRIMARY KEY,
AVAILABLE bool NOT NULL,
TYPE varchar(10) NOT NULL
);

create table ticket(
 ID int PRIMARY KEY AUTO_INCREMENT,
 PARKING_NUMBER int NOT NULL,
 VEHICLE_REG_NUMBER varchar(10) NOT NULL,
 PRICE double,
 IN_TIME DATETIME NOT NULL,
 OUT_TIME DATETIME,
 FOREIGN KEY (PARKING_NUMBER)
 REFERENCES parking(PARKING_NUMBER));

insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(1,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(2,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(3,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(4,true,'BIKE');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(5,true,'BIKE');
commit;




oici une liste de commandes MySQL utiles pour gérer les bases de données:

Connexion à MySQL

Vous pouvez vous connecter à MySQL en utilisant la commande suivante:

sh
Copy code
mysql -u root -p
Remplacez "root" par votre nom d'utilisateur si nécessaire. Vous serez invité à entrer votre mot de passe.

Affichage de toutes les bases de données

Une fois connecté, vous pouvez voir toutes les bases de données avec la commande suivante:

sql
Copy code
SHOW DATABASES;
Sélection d'une base de données

Pour utiliser une base de données spécifique, utilisez la commande suivante (remplacez "your_database" par le nom de votre base de données):

sql
Copy code
USE your_database;
Affichage de toutes les tables d'une base de données

Une fois que vous avez sélectionné une base de données, vous pouvez afficher toutes ses tables avec la commande suivante:

sql
Copy code
SHOW TABLES;
Affichage de la structure d'une table

Pour voir la structure d'une table spécifique (remplacez "your_table" par le nom de votre table):

sql
Copy code
DESCRIBE your_table;
Affichage des données d'une table

Pour voir toutes les données d'une table (remplacez "your_table" par le nom de votre table):

sql
Copy code
SELECT * FROM your_table;
Création d'une nouvelle base de données

Pour créer une nouvelle base de données (remplacez "new_database" par le nom de votre nouvelle base de données):

sql
Copy code
CREATE DATABASE new_database;
Création d'une nouvelle table

Pour créer une nouvelle table (remplacez "new_table" par le nom de votre nouvelle table, et spécifiez les colonnes et les types de données appropriés):

sql
Copy code
CREATE TABLE new_table (column1 type, column2 type, ...);
Insertion de données dans une table

Pour insérer des données dans une table (remplacez "your_table", "column1", "column2", etc., par vos valeurs spécifiques):

sql
Copy code
INSERT INTO your_table (column1, column2, ...) VALUES (value1, value2, ...);
Mise à jour des données dans une table

Pour mettre à jour les données dans une table (remplacez "your_table", "column", "value", etc., par vos valeurs spécifiques):

sql
Copy code
UPDATE your_table SET column = value WHERE condition;
Suppression de données d'une table

Pour supprimer des données d'une table (remplacez "your_table" et "condition" par vos valeurs spécifiques):

sql
Copy code
DELETE FROM your_table WHERE condition;
N'oubliez pas que chaque commande SQL doit se terminer par un point-virgule (;) pour indiquer la fin de la commande.

Ces commandes devraient vous aider à commencer à travailler avec MySQL. Cependant, MySQL est un outil puissant et il y a beaucoup plus à apprendre. Vous voudrez peut-être chercher des tutoriels ou des cours plus approfondis pour apprendre à l'utiliser de manière plus efficace.