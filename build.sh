#!/bin/bash
(
  cd backend
  DOCKER_BUILDKIT=1 docker build -t devicemon_backend .
)
# docker run -v "$PWD/webclient":/usr/src/app -w /usr/src/app node:17 /bin/bash -c "npm install && npm run build"

