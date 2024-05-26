# Semtech-test
The repository contains implementation of semtech coding test.

## Tests

`mvn clean test` → is running only unit tests / integration test

`mvn clean install` → is building the project only unit tests

## Build Docker image

`mvn package` → Prepare the package using maven

`docker image build -t semtech-coding-test:latest .` → User Docker to build the image

## Run Docker image

`docker run -v absolute_path_to_your_local_file:/file.csv -t semtech-coding-test:latest file.csv`


Example:
`docker run -v C:\Users\fakch\Downloads\semtech-test\population_2019.csv:/file.csv -t semtech-coding-test:latest file.csv`

## Apply code style to your intellj setting project

The code style file `semtech-test-style.xml` is located at the root of the project.

