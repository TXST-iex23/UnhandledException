# Vehicle Rental System

<div align="center"><img src="./supportFiles/Assignment.png" alt="assignment" width="75%" /></div>

## Purpose

This Java console application models a small vehicle rental location. Users can browse available vehicles, rent and return vehicles, inspect active rentals, reserve vehicles for future dates, and use administrator options to add or remove vehicles and review vehicle history.

## Java version and entry point

The source uses `java.time.LocalDate` and requires Java 8 or later. `Main.java` creates the sample inventory and starts `console.ConsoleUI`.

## Setup, compile, run, and validate

From the project root, compile the application and validation driver:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out Main.java common\*.java common\vehicles\*.java console\*.java tests\*.java
```

Run the application:

```powershell
java -cp out Main
```

Run the dependency-free validation driver:

```powershell
java -cp out tests.VehicleRentalValidationTest
```

## Features

- Sample inventory of cars and a motorcycle with generated, type-prefixed vehicle IDs.
- Immediate rentals, active rental display, subclass-specific pricing, and returns.
- A reservation workflow to create, view, cancel, and activate reservations on their pickup date.
- Reservation dates use `MM/DD/YYYY`; dates in the past are rejected. Inventory checks the full inclusive reservation period for conflicts.
- Administrator actions to add cars or motorcycles, remove vehicles without active rentals or pending reservations, and view vehicle history.
- Vehicle history records inventory, reservation, pickup, rental, return, and removal events with dates.

## Limitations

- Inventory, rental, and reservation records exist only while the program is running.
- Payment processing, customer accounts, and persistent customer records are not implemented.
- This is a console demonstration, not a production rental service.

## Console workflows

Choose **5. Reservations and administrator options** from the main menu. The next menu opens either the reservation workflow or administrator options. The main menu's option 6 exits the application.

For a reservation, choose the pickup date, duration, one of the vehicles available for that entire period, and renter names. The application assigns a reservation ID. Pending reservations can be viewed or cancelled; activation is available on the pickup date. After activation, return the vehicle from the main menu using its vehicle ID.

The administrator menu can add a car or motorcycle, remove a vehicle only when it has no active rental or pending reservation, and display a vehicle's history by ID.

## Team

- Jaime Martinez
- Brandon Crenshaw
