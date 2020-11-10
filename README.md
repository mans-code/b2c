# b2c
b2c platform

2 run config 
1- hot reload 
    - Set http://localhost:8080 to program arguments
    - auto build on save
2- debugging
    - host = localhost
    - port = 8000
    - arg = -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
    - jdk = 5-8
https://medium.com/@lhartikk/development-environment-in-spring-boot-with-docker-734ad6c50b34