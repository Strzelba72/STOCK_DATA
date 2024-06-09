#!/bin/bash

# Wstaw dane do tabeli stock_data
docker exec -it mymysql mysql -u streamuser -pstream stock -e "
INSERT INTO stock_data (stock, stock_full, min_low, max_high, avg_close, volume_sum, month)
VALUES ('AAPL', 'Apple Inc.', 150.00, 200.00, 175.00, 1000000, 1);"