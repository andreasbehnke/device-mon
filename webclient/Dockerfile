# **** build stage ****
FROM node:16-alpine as build
WORKDIR /workspace/app
COPY package.json .
RUN npm install
COPY src src
COPY public public
COPY tsconfig.json tsconfig.json
RUN npm run build
