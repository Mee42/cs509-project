CREATE TABLE `southwests` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `DepartDateTime` datetime NOT NULL,
  `ArriveDateTime` datetime NOT NULL,
  `DepartAirport` varchar(255) NOT NULL,
  `ArriveAirport` varchar(255) NOT NULL,
  `FlightNumber` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
);

CREATE TABLE `deltas` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `DepartDateTime` datetime NOT NULL,
  `ArriveDateTime` datetime NOT NULL,
  `DepartAirport` varchar(255) NOT NULL,
  `ArriveAirport` varchar(255) NOT NULL,
  `FlightNumber` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
);

INSERT INTO `southwests` VALUES (1,'2023-01-01T20:40:00','2023-01-01T00:03:00','Atlanta (ATL)','Denver (DEN)','WN309'),
                                (2,'2023-01-01T20:40:00','2023-01-01T00:03:00','Denver (DEN)','Minneapolis (MSP)','WN309'),
                                (3,'2023-01-01T20:40:00','2023-01-01T00:03:00','Denver (DEN)','Atlanta (ATL)','WN309'),
                                (4,'2023-01-01T20:40:00','2023-01-01T00:03:00','Atlanta (ATL)','Tucson (TUS)','WN309'),
                                (5,'2023-01-01T20:40:00','2023-01-01T00:03:00','Tucson (TUS)','Denver (DEN)','WN309'),
                                (6,'2023-01-01T20:40:00','2023-01-01T00:03:00','Denver (DEN)','Tucson (TUS)','WN309'),
                                (7,'2023-01-01T20:40:00','2023-01-01T00:03:00','Minneapolis (MSP)','Tucson (TUS)','WN309');

INSERT INTO deltas VALUES (1, '2023-01-01T01:00:00','2023-01-01T00:03:00','Paris','Japan','WN309'),
                          (2, '2023-03-01T01:00:00','2023-01-01T00:03:00','Japan','Tucson (TUS)','WN309');

create table ReservedFlight (
        Id integer not null auto_increment,
        FlightId integer not null,
        FlightTable varchar(255) not null,
        primary key (Id)
    )