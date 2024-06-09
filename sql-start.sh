#!/bin/bash

# Stworz folder na dane MySQL
mkdir -p /tmp/datadir

# Uruchom kontener z instalacja bazy danych MySQL
docker run --name mymysql -v /tmp/datadir:/var/lib/mysql -p 6033:3306 \
 -e MYSQL_ROOT_PASSWORD=flink -d mysql:debian

# Poczekaj na uruchomienie kontenera
sleep 20

# Utworz uzytkownika bazy danych i baze danych
docker exec -it mymysql mysql -uroot -pflink -e "
CREATE USER 'streamuser'@'%' IDENTIFIED BY 'stream';
CREATE DATABASE IF NOT EXISTS stock CHARACTER SET utf8;
GRANT ALL ON stock.* TO 'streamuser'@'%';
FLUSH PRIVILEGES;"

# Utworz tabele stock_data
docker exec -it mymysql mysql -u streamuser -pstream stock -e "
CREATE TABLE stock_data (
    stock VARCHAR(10),
    stock_full VARCHAR(50),
    min_low FLOAT(8),
    max_high FLOAT(8),
    avg_close FLOAT(8),
    volume_sum FLOAT(8),
    month INT
);"