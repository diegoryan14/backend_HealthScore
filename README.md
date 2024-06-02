# HealthScore
## Antes de inicializar o projeto você deve instalar:
  Java, Git e Node.js
  1 - git: https://git-scm.com/download/win
  2 - java jdk (Windows x64 installer): https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
  3 - node js (version 18): https://nodejs.org/en/download
  4 - startar o mysql

## Depois o JHipster:
  npm install -g generator-jhipster

## Para rodar o projeto:
  - npm install
  - ./gradlew -x webapp

## Observação
  O banco deve estar com a senha 'root', caso queira trocar no projeto deve ir:
  '/src/main/resources/config/application-dev.yml' e '/src/main/resources/config/application-prod.yml'
  e alterar o password, que fica entre as linhas 37 e 39
  
