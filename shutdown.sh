#!/bin/bash

# Run the docker compose
DIR_DOCKER_COMPOSE="docker"

# Navigate to the directory and run gradle build
cd "$DIR_DOCKER_COMPOSE" || exit 1

docker-compose down

# Confirm the build
if [ $? -eq 0 ]; then
  echo "Docker containers terminated"
else
  echo "Failed to terminate docker containers."
  exit 1
fi