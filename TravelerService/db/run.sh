#!/usr/bin/env bash

docker run -p 5432:5432 -v storage:/var/lib/postgresql/data -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres --rm postgres
