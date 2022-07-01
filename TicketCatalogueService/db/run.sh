#!/usr/bin/env bash

docker run --name mongodb -p 27017:27017 -v storage:/var/lib/mongodb/data mongo:latest
