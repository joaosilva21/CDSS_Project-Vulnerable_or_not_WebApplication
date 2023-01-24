# CDSS_Project-Vulnerable_or_not_WebApplication
- [x] Finished

## Index
- [Description](#description)
- [Technologies used](#technologies-used)
- [To run this project](#to-run-this-project)
- [Notes important to read](#notes-important-to-read)
- [Authors](#authors)

## Description
This project was developed for Design and Development of Secure Software subject @University of Coimbra, Master of Informatics Security <br>
Consists in develop a webapplication and some functionalities, each functionality has 2 versions (vulnerable and correct implemented). After the webapplication is tested with some tools in order to find the expected vulnerabilities

#### Main Languages:
![](https://img.shields.io/badge/Java-333333?style=flat&logo=java&logoColor=FFFFFF) 
![](https://img.shields.io/badge/HTML-333333?style=flat&logo=html5&logoColor=E67925)

## Technologies used:
1. Java (1st way to run)
    - [Version ??](https://www.oracle.com/java/technologies/downloads/) 
2. Spring-boot 
3. [Maven](https://maven.apache.org/download.cgi) (1st way to run)
4. Thymeleaf
5. [PgAdmin](https://www.pgadmin.org/download/) (1st way to run)
6. Docker (2nd way to run)

## To run this project:
You have two ways to run this project:
1. Download the folder "#spring-boot_folder" and unzip the "Project.zip" after have Docker installed:
   * Enter in folder docker and run command to generate for the first time a Docker container
   ```shellscript
    [your-disk]:[name-path]\docker> docker-compose up
    ```
   * From this moment now you have container created running, next time you want to run the container you can simply start it in Docker
   ![image](https://i.imgur.com/TZLhj6I.png)

2. Download the folder "#spring-boot_folder" and unzip the "Project.zip" after have Java and Maven installed:
   * Create a database with name "ScoreDei"<br>
   ![image](https://i.imgur.com/NAsDm2g.png)
   ![image](https://i.imgur.com/HlNLZM8.png)
   * Configure your connection to the database changing some fields in application.properties file
   ![image](https://i.imgur.com/niv9ysU.png)
   
   NOTE: this file is in following directory: [your directory]/src/main/resources
   * Finally just run it
    ```shellscript
    [your-disk]:[name-path]> mvnw spring-boot:run
    ```
    Access the website:
    ![image](https://i.imgur.com/eYKv7tA.png)

## Notes important to read
- For more information about the project, end-points and how exploits work read the Report
- The database will be empty when started, to add books use the SQL script inside the "docker" folder to add some books directly on Docker terminal

## Authors:
- [Inês Marçal](https://github.com/inesmarcal)
