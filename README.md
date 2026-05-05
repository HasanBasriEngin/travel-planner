# SENG 324 Travel Planner System

## Project Description
Travel Planner System is a Java Swing desktop application developed for a SENG 324 university term project. It demonstrates core object-oriented design principles and five classic design patterns through a travel planning scenario involving Turkish cities, live weather simulation, sorting, filtering, charts, and activity planning.

## Technologies Used
- Java 17
- Maven
- Swing
- FlatLaf Light
- Jackson
- JFreeChart
- PlantUML

## Required Java Version
- Java 17

## Run With Maven
From the project root:

```bash
mvn clean compile exec:java
```

## Windows
- Double-click `TravelPlanner.bat`.
- It will build the project automatically if `target/TravelPlanner.jar` does not exist.
- It will then run `target/TravelPlanner.jar`.
- Java 17 and Maven are required.

## UI Theme
- The desktop UI uses `FlatLaf Light` for a cleaner modern Swing appearance.
- If FlatLaf cannot be initialized on a machine, the application falls back to the system Swing look and feel.

## Package The Fat Jar
From the project root:

```bash
mvn clean package
```

This produces the runnable fat jar at:

```bash
target/TravelPlanner.jar
```

## Run The Packaged Jar
From the project root:

```bash
java -jar target/TravelPlanner.jar
```

## GUI Usage Instructions
1. Launch the application.
2. Use the `Sort Cities` combo box to sort the full city list by name, population, or area.
3. Use the `Filter by Weather` combo box to view cities with the selected weather state in the filtered list.
4. Watch the temperature bar chart and weather distribution pie chart update automatically every 3 seconds.
5. Select a city from the all-cities list to activate the activity planner.
6. Check or uncheck activity options:
   - Museum Visit
   - Shopping Mall Visit
   - Park Visit
   - City Center Visit
7. Review the generated plan description, total cost, and total required hours.

## Design Patterns

### Singleton
`CityRepository` is implemented as a Singleton so the application shares one repository instance. The JSON city data is loaded once and reused by the GUI, weather provider, and chart panels.

### Strategy
Sorting behavior is separated into interchangeable strategy classes:
- `NameSortStrategy`
- `PopulationSortStrategy`
- `AreaSortStrategy`

`MainFrame` selects the active sorting strategy based on the combo box selection.

### Iterator
Weather filtering is implemented with custom iterators:
- `SunnyCityIterator`
- `CloudyCityIterator`
- `RainyCityIterator`
- `SnowyCityIterator`

`MainFrame` uses `WeatherFilteredCollection` to obtain the correct iterator for the selected weather type.

### Observer
`WeatherReportProvider` acts as the subject and updates weather data every 3 seconds on a background thread. It notifies observers including:
- `MainFrame`
- `TemperatureChartPanel`
- `WeatherPieChartPanel`

This keeps the lists and charts synchronized with live weather changes.

### Decorator
Activity planning is implemented with decorators so planning behavior is added without changing the `City` class. A base plan starts from `BaseCityPlan`, then optional activities wrap it:
- `MuseumVisitDecorator`
- `ShoppingMallVisitDecorator`
- `ParkVisitDecorator`
- `CityCenterVisitDecorator`

## Project Structure
```text
travel-planner/
+-- pom.xml
+-- README.md
+-- CONTRIBUTION_REPORT.md
+-- TravelPlanner.bat
`-- src/
    `-- main/
        +-- java/
        |   `-- travelplanner/
        |       +-- Main.java
        |       +-- controller/
        |       +-- decorator/
        |       +-- iterator/
        |       +-- model/
        |       +-- observer/
        |       +-- repository/
        |       +-- strategy/
        |       `-- view/
        `-- resources/
            +-- cities.json
            `-- uml/
                `-- travel-planner-class-diagram.puml
```

## Assumptions
- The project is intended as an educational desktop application for demonstrating design patterns.
- The sample dataset contains Turkish cities and simulated weather data.
- Weather updates are randomized every 3 seconds for demonstration purposes.
- Activity planning is cost/time-based only and does not persist data.

## Known Limitation
- In environments where `Java` or `Maven` are not available on `PATH`, a local toolchain setup may still be required to build or verify the project.
