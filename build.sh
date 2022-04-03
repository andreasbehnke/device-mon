#!/bin/bash
(
  cd backend
  export DOCKER_BUILDKIT=1
  docker build -t devicemon_backend .
  docker tag devicemon_backend:latest ghcr.io/andreasbehnke/devicemon_backend:latest
  docker push ghcr.io/andreasbehnke/devicemon_backend:latest
) &
(
  cd webclient
  export DOCKER_BUILDKIT=1
  docker build -t devicemon_webclient .
  docker tag devicemon_webclient:latest ghcr.io/andreasbehnke/devicemon_webclient:latest
  docker push ghcr.io/andreasbehnke/devicemon_webclient:latest
) &
wait
