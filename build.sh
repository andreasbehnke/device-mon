#!/bin/bash
docker run -v "$PWD/webclient":/usr/src/app -w /usr/src/app node:17 /bin/bash -c "npm install && npm run build"

