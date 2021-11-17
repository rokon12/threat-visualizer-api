## The Threat Visualizer API
This is a simple backend application that fetches abuse IP threat score and finds out their geographical information and format to be displayed on a map through rest endpoints.

It publishes a restful API, which can be used for displaying information in, such as on a map, and then further sort & paged etc.
It also provides rich endpoints to search for information.

The application works in three phases. First, it fetches blacklisted IPs with associated abuse scores from a third-party service called: abuseipdb.com.
The website has constraints on how many requests one particular user can make at a time. So we figured to use a corn job to run it periodically so that it doesn't hit too many requests.

Once a request is processed through a scheduled job, it then finds the geolocation based on the IP address using a local geolocation database. The database is available on maxmind.com. Then the information is stored in a local SQL database.

Finally, the information is then exposed through a rest endpoint so that the application user consumes it. The APIs are protected.  So the authentication is required.

### Test Coverage Report
</br>![Coverage](.github/badges/jacoco.svg)

## Technology 
- Java 17
- Spring Boot 2.5.6
- Spring Data
- Spring Security
- JWT 
- Spring Cache 
- Lombok 
- Junit 5
- Mockito 
- Swagger Docs
- Gradle
- GitHub Action (CI/CD)
- SonarQube (Static Analysis)

## Getting Started
### Prerequisite
- Java 17
This project is written on java 17.  Download the latest version of java from: https://adoptium.net/

### Clone the project
````
git clone https://github.com/rokon12/threat-visualizer-api.git
````
### Tools Setup
The project can easily be setup on intelliJ IDEA. `File > Open ` and then find out the build.gradle file from the project directory and then select `Open as Project`
That's it. 

### How to run
To run the project, you just have find out the main class which is `ThreatVisualizedApplication` from the IDE and run the main method using the IDE. 
On the other hand, we can run it using `Gradle` as well.  From the command line go to the project root directory and run the following command- 
```
./gradlwe clean bootRun
```


## 
