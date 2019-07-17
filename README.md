# Reserve Campspot
A RESTful API built with [SparkJava](http://sparkjava.com/) that returns available "CampSpots" for a searched date window.

## Requirements
* OpenJDK 12.0.2
* Maven 3.6.1

[Install OpenJDK](https://jdk.java.net/12/)\
[Install Maven](https://maven.apache.org/install.html)

## Application Configuration
The application sets up a "faked response" using two .json files:
* search-response.json
* gaprule-config.json

Normally, these responses would come from another API endpoint on your server.
For simplicity sake, these files are parsed and converted into objects.

**Note:** each campspot must assigned one (and only one) "gapRule".
It can be easily abstracted to handle multiple if necessary.

## Setup
**(IDE)**:
If you are using an IDE like IntelliJ/Eclipse,
set your project's SDK to OpenJDK's application directory.
These IDE's have built-in Maven commands which makes it easy.

**(Terminal)**:
Make sure to set your system's **JAVA_HOME** and **MAVEN_HOME**
environment variables to OpenJDK's and Maven's directory (respectively).
Also, make sure add OpenJDK's and Maven's **bin** directories to your system's **Path** variable.

Once your system variables are configured,
open a new terminal and change directory to the application folder.\
Then run the following Maven commands:
```bash
mvn clean
mvn validate
mvn compile
mvn test
mvn package
```

## Instantiate the API
Run the jar file in the target directory.
```
cd target
jar -jar reserve-campspot-1.0.jar
```

## Using the API
The API service should be live now.
Visit the below end point to get available campsites!\
**Note**: Date's must be formatted as *yyyy-MM-dd* in the URL.
```html
http://localhost:4567/search/available/from/<yyyy-MM-dd>/to/<yyyy-MM-dd>
```
**Example**
```html
http://localhost:4567/search/available/from/2018-06-04/to/2018-06-06
```
response format
```json
{"campsites":[
      {"name":"Comfy Cabin","id":2},
      {"name":"Rustic Cabin","id":3},
      {"name":"Cabin in the Woods","id":5}]
}
```