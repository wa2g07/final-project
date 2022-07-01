FROM postgres:9.6

FROM postgres
ENV POSTGRES_PASSWORD password
ENV POSTGRES_DB postgres

COPY init.sql /docker-entrypoint-initdb.d/
