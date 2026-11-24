# Data Center Management System

A Java-based application that simulates data center infrastructure management and system alert monitoring. It manages servers, users, monitoring groups, and alerts through a file-driven command interface, using server IP addresses to associate infrastructure with its monitoring groups.

Developed as a university project for the Object-Oriented Programming (OOP) course at Politehnica University of Bucharest.

## Features

- **Server management:** Register servers with their IP address, owner, geographic location, operational status, and optional hardware specifications.
- **User management:** Represent users, operators, and administrators through an inheritance hierarchy.
- **Monitoring groups:** Create, find, and remove groups associated with servers, and manage their members.
- **Alert management:** Generate anomaly and advisory alerts with four severity levels: `LOW`, `MEDIUM`, `HIGH`, and `CRITICAL`.
- **Automatic notifications:** Notify the associated monitoring group whenever a server receives a new alert and retain received alerts in a log.
- **Input validation:** Handle missing IP addresses, invalid user information, and missing location data using custom exceptions.
- **File-based processing:** Read commands from pipe-delimited input files and generate output files for comparison against reference results.

## Architecture

The application uses the following core entities:

| Class | Responsibility |
| --- | --- |
| `Database` | Centralized, in-memory storage of servers, monitoring groups, and alerts. |
| `Server` | Represents a monitored server and its configuration; notifies its associated monitoring group of new alerts. |
| `Location` | Stores geographic information about a server. |
| `User` | Base class containing a user's name, role, and email. |
| `Operator` | Extends `User` with a department. |
| `Admin` | Extends `Operator` with an administrative clearance level. |
| `ResourceGroup` | Manages monitoring group members and records received alerts. |
| `Alert` | Represents a system event, including its type, severity, message, and associated server IP address. |

The user hierarchy is structured as follows:

```text
User
└── Operator
    └── Admin
```

Servers, monitoring groups, and alerts are associated through their `ipAddress` attribute.

## Design Patterns

Four design patterns are used to address distinct requirements of the application.

### 1. Singleton — Database

The application requires a single centralized database containing all servers, monitoring groups, and alerts. The Singleton pattern ensures that only one instance of `Database` exists throughout execution, preventing separate components from maintaining inconsistent collections of system entities.

### 2. Builder — Server and Location

Both `Server` and `Location` contain mandatory and optional attributes. A server requires an IP address, location, and owner, while its hostname, status, and hardware specifications are optional. A location requires a country, while its city, address, and coordinates are optional.

The Builder pattern allows these objects to be constructed incrementally, supplying optional attributes only when they are available. This avoids multiple constructor overloads and makes object creation easier to read and maintain.

### 3. Factory — User Creation

The `User`, `Operator`, and `Admin` classes share common attributes but require different additional information. A factory centralizes the logic for determining which class to instantiate from the supplied attributes.

This allows the database to create users through a common interface without duplicating subtype selection and constructor logic throughout the application. It also simplifies adding new user types in the future.

### 4. Observer — Alert Notifications

Whenever a new alert is associated with a server, its monitoring group must be notified. The Observer pattern models this relationship, with `Server` acting as the subject and `ResourceGroup` as the observer.

When an alert is created, the server records it and notifies its associated monitoring group. The group stores the received alert in an internal collection, simulating an alert log. This separates alert generation from notification handling and avoids requiring monitoring groups to continuously check servers for new events.

```text
New alert
    |
    v
Associated Server (Subject)
    |
    | notification
    v
ResourceGroup (Observer)
    |
    v
Received alert log
```

## Supported Commands

| Command | Description |
| --- | --- |
| `ADD SERVER` | Register a new server. |
| `ADD GROUP` | Create a monitoring group associated with a server IP address. |
| `FIND GROUP` | Find a monitoring group by IP address. |
| `REMOVE GROUP` | Remove a monitoring group by IP address. |
| `ADD MEMBER` | Add a user to a monitoring group. |
| `FIND MEMBER` | Find a member in a monitoring group by name and role. |
| `REMOVE MEMBER` | Remove a member from a monitoring group. |
| `ADD EVENT` | Create a system alert and trigger the associated notification. |

## Input and Output

The application processes CSV-like `.in` files using `|` as the field separator. Results are written to corresponding `.out` files and compared against the provided `.ref` files by the assignment's automated tests.

For example:

```text
servers_01.in   → input commands
servers_01.out  → generated output
servers_01.ref  → expected output
```

The `Main` class supports three execution modes through the `PathType` enumeration:

| Mode | Purpose |
| --- | --- |
| `SERVERS` | Process server management commands. |
| `GROUPS` | Process monitoring group and membership commands. |
| `LISTENER` | Load servers and monitoring groups, then process system alerts. |

The entry point accepts either two arguments (path type and file path) or four arguments (path type, server file path, group file path, and alert file path).

## Exception Handling

The application uses custom exceptions to report invalid input:

| Exception | Condition |
| --- | --- |
| `MissingIpAddressException` | A required server IP address is missing. |
| `UserException` | A required user name or role is missing. |
| `LocationException` | A required country is missing. |

Validation errors are reported with the corresponding command and input line number, following the output format specified in the assignment.

## Technologies and Concepts

- Java
- Object-oriented programming: encapsulation, inheritance, and polymorphism
- Design patterns: Singleton, Builder, Factory, and Observer
- Java Collections Framework and generics
- Custom exception handling
- File I/O and automated output comparison
