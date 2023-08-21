# Docker-compose로 springboot + mysql container 올리기

### Docker-compose란?

여러 개의 도커 컨테이너를 정의하고 실행하는 도구 → multi-containers

- 기능
    - 여러 컨테이너 정의 : 동일한 애플리케이션에 필요한 여러 컨테이너를 정의하고 구성
        
        ex) 웹 서버, 데이터베이스, 큐잉 시스템 등이 각각 별도의 컨테이너로 정의
        
    - 환경 구성 관리 : 각 컨테이너의 환경 변수, 포트 매핑, 볼륨 마운트 등의 구성을 관리하고 설정
    - 네트워크 관리 : 서비스 간 통신을 위한 네트워크를 자동 생성하고 관리
        
        각 서비스는 해당 네트워크에 연결되며, 이름으로 서로 참조
        
    - 실행 정지 : **`docker-compose up`** 컨테이너를 실행하고, **`docker-compose down`** 컨테이너를 중지하고 제거
    - 스케일링 : 동일한 서비스의 여러 복제본을 생성하여 애플리케이션의 성능을 향상 가능

### Docker-compose 환경 세팅

---

- 프로젝트 root에 `docker-compose.yml`, `Dockerfile` 생성

![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/7f282fff-5a71-4fd2-98a5-57a11ff4a440)


- Dockerfile 코드
    
    ```docker
    FROM openjdk:17
    ARG JAR_FILE=build/libs/*.jar
    COPY ${JAR_FILE} test12-0.0.1-SNAPSHOT.jar 
    ENTRYPOINT ["java","-jar","/test12-0.0.1-SNAPSHOT.jar"]
    ```
    
    `FROM` : java 버전 → 17
    
    - `[gradle.build](http://gradle.build)` 의 java 버전 확인
        
        ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/553040cd-b370-4ee7-8590-4caa2ccb911d)

        
    - `COPY`  : /build/libs 안에 있는 jar 파일

- docker-compose.yml  코드
    - `database`→ mysql image, container 생성
        - ports: `3309:3306`  DB 접근 시 3309포트 접속
    - `application` → springboot container 생성
        - ports: `3050:8080`  local 접근 시 3050포트 접속
        - `depends_on` : DB 접근
        - environment
            
            : `SPRING_DATASOURCE_URL` 의 url은 local → database(DB 이름)으로 접속해야함
            
            ex) `mysql://database:3306/asac_spring_test1` 
            
    - `networks` → mysql, springboot 소통 진행 통로 생성
    
    ```yaml
    version: '3'
    services:
      database:
        image: mysql
        container_name: mysql-root3
        restart: always
        ports:
          - "3309:3306"
        environment:
          - MYSQL_ROOT_PASSWORD=1234
          - MYSQL_DATABASE=asac_spring_test1
          - MYSQL_CHARSET=utf8mb4
          - MYSQL_COLLATION=utf8mb4_unicode_ci
        volumes:
          - /etc/mysql/my.cnf
        command:
          - "--character-set-server=utf8mb4"
          - "--collation-server=utf8mb4_unicode_ci"
        networks:
          - test_network2
    
      application:
        build: .
        container_name: spring-boot-crud6
        ports:
          - "3050:8080"
        depends_on:
          - database
        restart: always
        environment:
          - SPRING_DATASOURCE_URL=jdbc:mysql://database:3306/asac_spring_test1?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&serverTimezone=Asia/Seoul
          - SPRING_DATASOURCE_USERNAME=root
          - SPRING_DATASOURCE_PASSWORD=1234
        networks:
          - test_network2
    
    networks: 
      test_network2:
    ```
    

### Docker 환경 실행

---

- 프로젝트 root path로 이동하여 `docker-compose up` 실행
    
    ```docker
    C:\Users\82109\git\springboot_with_docker\test12>docker-compose up
    ```
    
    - image 및 container 생성 완료
        
        ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/c93b1dab-dd14-4da3-a35e-86dee9e24d1b)

        
    - springboot, mysql 실행 진행됨
        
        ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/6010ebeb-5985-4032-b7dd-235ae3cb9d46)

        
    
    - docker desktop에서 확인 가능
        
        ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/e5b7d4e4-366e-41a4-af2c-d180ba0ce1b3)

        
        - [localhost:3040/home](http://localhost:3040/home) 접속 시 → CRUD 가능
            
            ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/09427591-1d2f-444f-81fe-9171e608c705)

            
        - **mysql-root4 접속 시 로그와 terminal 접속 가능**

            
            터미널로 db 조작 가능
            
            [PoiemaWeb](https://poiemaweb.com/docker-mysql)
            
            ```docker
            mysql -u root -p //db 접속 
            
            use asac_spring_test1; 
            insert into user values ("123@gmail.com", "A", "123"), ("456@gmail.com", "B", "456");
            
            ```
            
    
    - docker-compose 실행 코드
        
        ```docker
        #도커 올리기
        docker-compose up
        
        #도커 내리기
        docker-compose down
        
        #현재 컨테이너 확인, 아래 이미지 첨부
        docker ps
        
        #이미지 확인, 아래 이미지 첨부
        docker images
        ```
        
        - 도커 container 실행 예시
        
        ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/29fdd671-5dab-47d2-9783-5a13b481e36e)

        
        - 도커 image 실행 예시
        
        ![image](https://github.com/dahyunko/springboot_with_docker/assets/101400650/08c50513-65bc-4758-8a3a-48b7072dd778)
