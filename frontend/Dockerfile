# node 이미지를 받는다 build이미지
FROM node:latest as build-stage
# RUN, CMD, ENTRYPOINT의 명령이 실행될 디렉터리
WORKDIR /app
# package*.json이 WORKDIR에 복사된다
COPY package*.json ./
# 복사했으니 디펜던시 설치가 가능하다
RUN npm install
# 소스코드를 복사한다
COPY ./ .
RUN npm run build

# nginx 이미지를 받는다. 실행 이미지
FROM nginx as production-stage
RUN mkdir /app
# builder에서 빌드한 바이너리를 실행할 이미지로 전달해주기 위해 copy --from 옵션을 사용하여 실행 이미지로 전달한다
COPY --from=build-stage /app/dist /app
RUN hostname -i
COPY nginx.conf /etc/nginx/nginx.conf
RUN service nginx restart
