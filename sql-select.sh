#!/bin/bash

# Odczytaj dane z tabeli stock_data
docker exec -it mymysql mysql -u streamuser -pstream stock -e "
SELECT * FROM stock_data;"