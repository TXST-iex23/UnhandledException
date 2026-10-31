# Vehicle Rental System

Group project for CS 3354.R04 at Texas State University. The existing roster in this repository lists Jaime Martinez and Brandon Crenshaw; confirm the roster before submission.

## Purpose

This beginner-level Java program models a small vehicle rental location. A customer can browse the sample inventory, start an immediate rental, return a vehicle, review active rentals, and make a reservation for a future pickup date.

## Java version and entry point

The application was compiled and its validation driver was run with the VS Code Java extension's bundled JDK 25. The entry point is `Main.java` (`Main.main`). It creates the sample inventory and starts `console.ConsoleUI`.

## Setup, compile, run, and validate

Install or select a JDK that provides `javac` and `java`, then open a terminal in the project root. The following PowerShell commands compile the application and test sources into `out`:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out Main.java common\*.java common\vehicles\*.java console\*.java tests\*.java
```

Start the console application:

```powershell
java -cp out Main
```

Run the dependency-free validation driver:

```powershell
java -cp out tests.VehicleRentalValidationTest
```

The driver checks rental, return, pricing, date, input, and reservation behavior. The current committed validation driver has reported 67 passing checks.

## Implemented features

- Sample inventory with two cars and one motorcycle, each assigned an ID at startup.
- Listing vehicles available for immediate rental.
- Immediate rentals, active-rental display, pricing, and returns.
- Reservation menu to create a reservation, view pending reservations, cancel a pending reservation, and activate it for pickup.
- Reservations use the date format `MM/DD/YYYY`. Pickup dates cannot be in the past, and invalid dates are prompted again.
- Inventory checks the entire inclusive reservation period for conflicts. Adjacent bookings that do not share a day can use the same vehicle.
- A reservation can be activated only on its pickup date. Once activated, it is an active rental and can be returned through the main menu.
- Car and motorcycle daily rates are calculated by their vehicle subclasses.

## Limitations

- Inventory and reservation records exist only while the program is running; they are not saved to a file or database.
- The application starts with fixed sample vehicles and does not provide an administrator console for adding or removing vehicles.
- Payment processing, accounts, and persistent customer records are not implemented.
- This is a console demonstration, not a production rental service.

## Sample reservation interaction

Each fresh run starts with `C0001` (Toyota Camry), `C0002` (Honda Civic), and `M0001` (Honda Rebel). For this example, enter a valid future date such as `12/31/2099` and choose `C0001` from the vehicles printed for that whole period. Reservation numbering starts at `R0001` each run.

```text
Choose an option: 5
*** Reservations ***
1. Create a reservation
2. View pending reservations
3. Cancel a reservation
4. Activate a reservation for pickup
5. Return to the main menu
Choose an option: 1
Enter pickup date (MM/DD/YYYY): 12/31/2099
Enter rental duration in days: 2
Vehicles available for pickup on December 31, 2099 for 2 day(s):
C0001 - 2022 Toyota Camry ($50.00 per day)
Enter the vehicle ID to reserve: C0001
Enter renter first name: Alex
Enter renter last name: Example
Reservation R0001 created for C0001 on December 31, 2099.
```

Use reservation option 2 to view pending bookings, option 3 to cancel by reservation ID, or option 4 on the pickup date to activate the booking. After activation, return to the main menu and choose option 3 to return the vehicle by its vehicle ID. The date above is an example; the program rejects dates that are in the past when entered.

## Submission details to confirm

- The team roster above is copied from the existing project README and should be confirmed by the team.
- Add genuine demonstration screenshots in the appropriate submission location; none are included by this README update.
