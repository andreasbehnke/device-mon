#!/bin/bash
(
  cd backend
  DOCKER_BUILDKIT=1 docker build -t devicemon_backend .
)
(
  cd webclient
  DOCKER_BUILDKIT=1 docker build -t devicemon_webclient .
)

