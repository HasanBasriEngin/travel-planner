# 🧭 SENG 324 Travel Planner System
> **Java Swing Travel Planning & Weather Monitoring Application**

<p align="left">
  <img src="https://img.shields.io/badge/Status-Active-success?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/Desktop-Swing-blue?style=for-the-badge" />
</p>

---

## 🛠 Technology Stack

| **Category** | **Technologies Used** |
| :--- | :--- |
| **Language** | ![Java](https://img.shields.io/badge/java%2017-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) |
| **Build Tool** | ![Maven](https://img.shields.io/badge/apache%20maven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white) |
| **Desktop UI** | ![Swing](https://img.shields.io/badge/java%20swing-0F766E?style=for-the-badge) ![FlatLaf](https://img.shields.io/badge/flatlaf-light-38BDF8?style=for-the-badge) |
| **Data & Charts** | ![Jackson](https://img.shields.io/badge/jackson-json-2563EB?style=for-the-badge) ![JFreeChart](https://img.shields.io/badge/jfreechart-charts-F59E0B?style=for-the-badge) |
| **Documentation** | ![PlantUML](https://img.shields.io/badge/plantuml-uml-6B7280?style=for-the-badge) |

---

## 🎯 Project Purpose

Travel Planner System is a Java desktop application developed for the **SENG 324 / Software Design Patterns** term project. The application helps users explore Turkish cities, monitor simulated weather updates, compare temperatures, filter destinations by weather, and build a simple activity plan with budget and duration information.

The project focuses on clean object-oriented design and demonstrates five required design patterns through a practical travel planning scenario.

Assignment brief: [SENG-324-TermProject_TravelPlanner_Individual.pdf](SENG-324-TermProject_TravelPlanner_Individual.pdf)

---

## 📌 What The Assignment Requires

According to the term project PDF, the application should:

- read a JSON file containing `City` objects,
- model each city with `name`, `population`, `area`, `currentTemperature`, and `currentWeatherState`,
- support weather states: `SUNNY`, `CLOUDY`, `RAINY`, and `SNOWY`,
- display all cities in a GUI list,
- sort cities by name, population, or area using the **Strategy** pattern,
- show a second weather-filtered city list using the **Iterator** pattern,
- show a bar chart for city temperatures,
- show a pie chart for weather distribution percentages/counts,
- update weather information randomly every 3 seconds on a separate thread,
- notify the GUI and charts using the **Observer** pattern,
- add an activity planner without modifying the `City` class directly,
- implement at least four activity decorators using the **Decorator** pattern,
- include a Singleton city repository that reads the JSON data once,
- provide source code, packaged runnable jar, UML diagram, contribution report, and README/user guide.

---

## 🧩 Design Patterns Used

| **Pattern** | **Implementation** | **Purpose** |
| :--- | :--- | :--- |
| **Singleton** | `CityRepository` | Loads `cities.json` once and provides shared city data. |
| **Strategy** | `NameSortStrategy`, `PopulationSortStrategy`, `AreaSortStrategy` | Switches sorting behavior from the GUI combo box. |
| **Iterator** | `SunnyCityIterator`, `CloudyCityIterator`, `RainyCityIterator`, `SnowyCityIterator` | Iterates only cities matching the selected weather filter. |
| **Observer** | `WeatherReportProvider`, `WeatherObserver` | Updates lists and charts whenever weather data changes. |
| **Decorator** | `MuseumVisitDecorator`, `ShoppingMallVisitDecorator`, `ParkVisitDecorator`, `CityCenterVisitDecorator` | Adds optional activities, cost, and duration to a selected city plan. |

---

## 🏁 Quick Start Guide

Follow these steps to run the project locally.

### 1. Clone The Project

```bash
git clone https://github.com/HasanBasriEngin/travel-planner.git
cd travel-planner
```

### 2. Check Requirements

You need:

```text
Java 17
Maven
```

### 3. Run With Maven

```bash
mvn clean compile exec:java
```

### 4. Run On Windows

You can also double-click:

```text
TravelPlanner.bat
```

The batch file checks for Java, builds the project if `target/TravelPlanner.jar` is missing, and then runs the application.

---

## 📦 Package The Runnable Jar

Create the fat jar:

```bash
mvn clean package
```

Run the packaged app:

```bash
java -jar target/TravelPlanner.jar
```

---

## ✨ Main Features

- 🌦 Live simulated weather updates every 3 seconds.
- 🏙 30 Turkish cities loaded from `cities.json`.
- 🔎 Weather filtering by `SUNNY`, `CLOUDY`, `RAINY`, or `SNOWY`.
- ↕️ Sorting by city name, population, or area.
- 📊 Temperature bar chart with responsive city label rotation.
- 🥧 Weather distribution pie chart.
- 🧾 Activity planner with total cost and total required hours.
- 🧱 Modernized Swing interface using FlatLaf.

---

## 📂 Project Structure

```text
travel-planner/
+-- pom.xml
+-- README.md
+-- CONTRIBUTION_REPORT.md
+-- TravelPlanner.bat
+-- SENG-324-TermProject_TravelPlanner_Individual.pdf
`-- src/
    `-- main/
        +-- java/
        |   `-- travelplanner/
        |       +-- Main.java
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
                +-- travel-planner-class-diagram.puml
                `-- travel-planner-class-diagram.png
```

---

## 🖥 GUI Usage

1. Launch the application.
2. Use **Sort Cities** to sort the full city list.
3. Use **Filter Weather** to show only cities with the selected weather state.
4. Watch the bar chart and pie chart update as simulated weather changes.
5. Select a city from **All Cities**.
6. Choose optional activities:
   - Museum Visit
   - Shopping Mall Visit
   - Park Visit
   - City Center Visit
7. Review the generated plan description, total cost, and total required hours.

---

## 📚 Notes

- The weather data is simulated for demonstration purposes.
- The GUI mock-up in the assignment PDF is illustrative; the implementation keeps the required panels and functionality while using a cleaner custom Swing layout.
- The project is intended as an educational design-pattern implementation, not a production travel booking system.
