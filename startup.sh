#!/bin/bash

DIR_API_SCHEMA="../backend-service-api-schema"

# Check if the directory exists
if [ -d "$DIR_API_SCHEMA" ]; then
  echo "Directory exists: $DIR_API_SCHEMA"

  # Navigate to the directory and run gradle build
  cd "$DIR_API_SCHEMA" || exit 1
  gradle build publishToMavenLocal

  # Check if gradle command was successful
  if [[ $? -eq 0 ]]; then
    echo "gradle build successful"
  else
    echo "gradle build failed"
  fi
else
  echo "Directory does not exist: $DIR_API_SCHEMA"
fi

DIR_REGISTRY_SERVICE_API_SCHEMA="../company-registry-service-api-schema"

# Check if the directory exists
if [ -d "$DIR_REGISTRY_SERVICE_API_SCHEMA" ]; then
  echo "Directory exists: $DIR_REGISTRY_SERVICE_API_SCHEMA"

  # Navigate to the directory and run gradle build
  cd "$DIR_REGISTRY_SERVICE_API_SCHEMA" || exit 1
  gradle build publishToMavenLocal

  # Check if gradle command was successful
  if [[ $? -eq 0 ]]; then
    echo "gradle build successful"
  else
    echo "gradle build failed"
  fi
else
  echo "Directory does not exist: $DIR_REGISTRY_SERVICE_API_SCHEMA"
fi

DIR_COMPANY_REGISTRY_SERVICE="../company-registry-service"

# Check if the directory exists
if [ -d "$DIR_COMPANY_REGISTRY_SERVICE" ]; then
  echo "Directory exists: $DIR_COMPANY_REGISTRY_SERVICE"

  # Navigate to the directory and run mvn build
  cd "$DIR_COMPANY_REGISTRY_SERVICE" || exit 1
  mvn clean install -DskipTests

  # Check if gradle command was successful
  if [[ $? -eq 0 ]]; then
    echo "mvn clean install successful"
  else
    echo "mvn clean install failed"
  fi
else
  echo "Directory does not exist: $DIR_COMPANY_REGISTRY_SERVICE"
fi

# Define variables
COMPANY_REGISTRY_SERVICE_IMAGE_NAME="company-registry-service"
COMPANY_REGISTRY_SERVICE_TAG="0.0.1"

# Build the Docker image
docker build -t ${COMPANY_REGISTRY_SERVICE_IMAGE_NAME}:${COMPANY_REGISTRY_SERVICE_TAG} .

# Confirm the build
if [ $? -eq 0 ]; then
  echo "Docker image '${COMPANY_REGISTRY_SERVICE_IMAGE_NAME}:${COMPANY_REGISTRY_SERVICE_TAG}' built successfully!"
else
  echo "Failed to build the Docker image."
  exit 1
fi

DIR_BACKEND_SERVICE="../backend-service"

# Check if the directory exists
if [ -d "$DIR_BACKEND_SERVICE" ]; then
  echo "Directory exists: $DIR_BACKEND_SERVICE"

  # Navigate to the directory and run mvn build
  cd "$DIR_BACKEND_SERVICE" || exit 1
  mvn clean install -DskipTests=true

  # Check if gradle command was successful
  if [[ $? -eq 0 ]]; then
    echo "mvn clean install successful"
  else
    echo "mvn clean install failed"
  fi
else
  echo "Directory does not exist: $DIR_BACKEND_SERVICE"
fi

# Define variables
BACKEND_SERVICE_IMAGE_NAME="backend-service"
BACKEND_SERVICE_TAG="0.0.1"

# Build the Docker image
docker build -t ${BACKEND_SERVICE_IMAGE_NAME}:${BACKEND_SERVICE_TAG} .

# Confirm the build
if [ $? -eq 0 ]; then
  echo "Docker image '${BACKEND_SERVICE_IMAGE_NAME}:${BACKEND_SERVICE_TAG}' built successfully!"
else
  echo "Failed to build the Docker image."
  exit 1
fi

# Run the docker compose
DIR_DOCKER_COMPOSE="docker"

# Navigate to the directory and run gradle build
cd "$DIR_DOCKER_COMPOSE" || exit 1

docker-compose up -d && docker-compose logs -f backend-service company-registry-service

# Confirm the build
if [ $? -eq 0 ]; then
  echo "Docker containers started"
else
  echo "Failed to start docker containers."
  exit 1
fi