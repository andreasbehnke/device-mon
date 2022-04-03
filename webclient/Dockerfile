# **** build stage ****
FROM node:16-alpine as build
WORKDIR /workspace/app

# create react app
COPY package.json .
RUN npm install
COPY src src
COPY public public
COPY tsconfig.json tsconfig.json
RUN npm run build

# create ssl certificate
RUN apk update && \
    apk add --no-cache openssl && \
    openssl req -x509 -nodes -days 3650 \
      -subj  "/C=CA/ST=QC/O=Company Inc/CN=example.com" \
      -newkey rsa:2048 -keyout ssl.key \
      -out ssl.crt;

# **** runtime stage ****
FROM nginx:stable
RUN apt-get update && apt-get upgrade -y
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/app/ssl.key /etc/nginx/ssl.key
COPY --from=build /workspace/app/ssl.crt /etc/nginx/ssl.crt
COPY --from=build /workspace/app/build/ /usr/share/nginx/html
