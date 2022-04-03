# **** build stage ****
FROM node:16-alpine as build
WORKDIR /workspace/app
COPY package.json .
RUN npm install
COPY src src
COPY public public
COPY tsconfig.json tsconfig.json
RUN npm run build

# **** runtime stage ****
FROM nginx:stable
RUN apt-get update && apt-get upgrade -y
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/app/build/ /usr/share/nginx/html
