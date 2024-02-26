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
Recommend using Intellij as it has everything out of the box for spring MVC framework, could also use the Ultimate version by applying with student email to get free product for 1 year. The Ultimate version will include a database interface to connect and make query to the database without needing external workplace and many more.

1. Pull the project from github into IDE.
2. Change the MySQL database login info in the application.properties file to your local info. Locate file at backend/src/main/resources.
3. The backend is running at port 8080, to use another port, just change port number in the application.yml file. Locate file at backend/src/main/resources.
4. Must also get Java SDK version 17 or higher. Intellij will suggest and get SDK internally if use Intellij.
5. If pom dependencies are not recognized, just right click on pom.xml file, choose maven, and click reload project.
6. Click run button on file BackendApplication to run. Locate at backend/src/main/java

## Backend implementation so far
### API path
1. The api path is located at /api/flight
2. The api return everything in JSON format
3. Within the api path include 3 sub path for getting data:
     - /departAirport: return an array of String containing all depart airports
     - /arriveAirport: return an array of String containing all arrive airports
     - /submitForm/{page}: return a map containing a sub map with all flights
         + require a JSON body of the form, not normal html form data. Basically, the backend is looking for data being sent to in JSON format, meaning frontend doesn't need to use html form to display search input information. The JSON field names (key) must be the same with the field names in FlightForm class, located at backend/src/main/java/cs509/backend/Data/FlightForm
         + must specify the page (batch) in integer: 1 - first batch, 2 - second batch. Batch is in group of 10 for default. Meaning page 1 get the first 10 flights and page 2 get the second 10 flights if any.
         + Read the FlightForm class for details of what fields are required and optional. Just call the api to see the return data.
       





