# CS509 S24 Team 4 Airline Reservation Full Stack Web Application

## Frontend Setup Instructions

1. Run the following commands to setup the frontend:

```cmd
cd ./frontend
npm install

```

2. Start the development webserver with the following command: `npm run dev`
3. Click the hyperlinked URL in the terminal.


## Backend Setup Instructions
Recommend using Intellij as it has everything out of the box for spring MVC framework.

1. Pull the project from github into IDE.
2. Change the MySQL database login info in the application.properties file to your local info. Locate file at backend/src/main/resources.
3. The backend is running at port 8080, to use another port, just change port number in the application.yml file. Locate file at backend/src/main/resources.
4. Must also get Java SDK version 17 or higher. Intellij will suggest and get SDK internally if use Intellij. For VSCode, get Java
5. If pom dependencies are not recognized, just right click on pom.xml file, choose maven, and click reload project.
6. Click run button on file BackendApplication to run. Locate at backend/src/main/java
   - If you have Java JRE instead of Java SDK, can use this command:
     ```cmd
     cd ./backend
     java cs509.backend.BackendApplication
     ```

## Backend implementation
### API path
1. The api endpoint is located at /api/flights
2. The api returns everything in JSON format
3. Within the api path include 3 sub paths for getting data:
   - /departAirport: return an array of String containing all depart airports
   - /arriveAirport: return an array of String containing all arrive airports
   - /submitForm/{page}: return a map containing a sub map with all flights
     + must specify the page (batch) in integer: 1 - first batch, 2 - second batch. Batch is in group of 10 for default. Meaning page 1 get the first 10 flights and page 2 get the second 10 flights if any.
     + just call the api to see the returned data.
     + responds code 200 and returned data for a successful search (could be empty result)
     + responds code 400 for any bad request with error message
     + requires a JSON body (see FlightForm class, located at backend/src/main/java/cs509/backend/Data/FlightForm for any required/optional fields):
   ```json
   {
        "departAirport": "Airport1",
        "arriveAirport": "Airport2",
        "departDate": "2024-03-10",
        "roundTrip": false,
        "connectionNum": "2",
        "departTimeStart": "10:00:00",
        "departTimeEnd": "12:00:00",
        "sort": "arrive",
        "order": "asc"
   } 
   ```
   - /reserveFlight: 
     + responds code 200 with ok message for successful reserved flight
     + responds code 400 for any bad request with the error message
     + requires a JSON body:
     ```json
     {
        "flights": [
            {
                "departAirport": "Airport1",
                "arriveAirport": "Airport2",
                "departDateTime": "2024-03-10T00:00:00",
                "arriveDateTime": "2024-03-10T23:00:00",
                "flightNumber": "ABC123"
            },
            {
                "departAirport": "Airport3",
                "arriveAirport": "Airport4",
                "departDateTime": "2024-03-11T00:00:00",
                "arriveDateTime": "2024-03-11T23:00:00",
                "flightNumber": "DEF456"
            }
        ]
     }
     ```
4. If you are using Hibernate framework then no need to do anything for backend up reservation as Hibernate will create the reservation list defined. Otherwise, in your database, create a table in MySQL called ReservedFlight with columns: Id, FlightId:
     ```sql
        CREATE TABLE ReservedFlight (
           Id integer NOT NULL auto_increment,
           FlightId integer NOT NULL,
           FlightTable varchar(255) NOT NULL,
           Primary Key (Id)
        ) engine=InnoDB
     ```

## To utilize Swagger UI, follow these steps:：
Run Frontend and Backend: Ensure both the frontend and backend of the application are running.

Access Swagger UI: Open a web browser and navigate to the following address:

http://localhost:8080/swagger-ui/index.html#

Explore API Documentation: Once the Swagger UI page loads, users will have access to the interactive documentation of the API. Here, they can explore the various endpoints, parameters, request payloads, and responses provided by the API.

## Code Coverage

The code coverage can be accessed [here](https://app.codecov.io/gh/Mee42/cs509-project/)






