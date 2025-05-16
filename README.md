# Запуск сервиса

Для запуска потребуется выполнить сборку Maven package, загрузить image для PostgreSQL - `postgres:latest` и Redis - `redis:latest`, а также создать image микросервиса

```shell
docker build -t user-balance-service:latest . 
```

После этого просто ввести команду запуска docker-compose.yaml

```shell
docker-compose up -d 
```
