* Uruchomienie klastra
```sh
gcloud dataproc clusters create ${CLUSTER_NAME} \
--enable-component-gateway --region ${REGION} --subnet default \
--master-machine-type n1-standard-4 --master-boot-disk-size 50 \
--num-workers 2 --worker-machine-type n1-standard-2 --worker-boot-disk-size 50 \
--image-version 2.1-debian11 --optional-components FLINK,DOCKER,ZOOKEEPER \
--project ${PROJECT_ID} --max-age=3h \
--metadata "run-on-master=true" \
--initialization-actions \
gs://goog-dataproc-initialization-actions-${REGION}/kafka/kafka.sh
```
* Sklonuj repozytorum/wgraj na klaster
```sh
git clone https://github.com/Strzelba72/STOCK_DATA
```
* Wprowadź poniższe komedy, uzupełniająć dane
```sh
export BUCKET_NAME="placeholder" 
export STREAM_DIR_DATA="gs://$BUCKET_NAME/nazwa_folderu" 
export STATIC_DATA="gs://$BUCKET_NAME/nazwa_pliku.csv" 
export INPUT_DIR="stream-data" 
```
* Uruchom poniższe komendy, które stworzą Dockera z bazą danych
```sh
mkdir /tmp/datadir
docker run --name mymysql -v /tmp/datadir:/var/lib/mysql -p 6033:3306 \
 -e MYSQL_ROOT_PASSWORD=flink -d mysql:debian
```
