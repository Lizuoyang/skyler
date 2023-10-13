FROM mysql/mysql-server:8.0.31

MAINTAINER lengleng(wangiegie@gmail.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./skyler.sql /docker-entrypoint-initdb.d

COPY ./skyler_codegen.sql /docker-entrypoint-initdb.d

COPY ./skyler_config.sql /docker-entrypoint-initdb.d

COPY ./skyler_job.sql /docker-entrypoint-initdb.d
