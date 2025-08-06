# Show Booking LLD Implementation

## Problem Statement

Design a movie show booking system that supports multiple theatres, movies, shows, seat types, and flexible pricing strategies. The system should allow users to book/cancel seats, make/decline payments, and be easily extensible for new requirements.

## Requirements

- Support for multiple theatres, movies, and shows.
- Different seat types (Normal, Premium).
- Flexible price strategies for seat pricing.
- User roles (Admin, Normal user) with restricted actions.
- Booking, payment, and cancellation workflows.
- Thread-safe operations for booking and payment.

## How to Run

Use `ShowBookingDemo.java` to see example usage.

## Design Patterns Used

- **Singleton:** `ShowBookingService` ensures only one instance exists.
- **Strategy:** Pricing uses the `PriceStrategy` interface for flexible seat pricing.
- **Factory:** `BookingFactory` for booking creation.

## Class Diagram

```mermaid
classDiagram
    class ShowBookingService {
        - List~Movie~ movies
        - List~Theatre~ theatres
        - PriceStrategy priceStrategy
        + addMovie()
        + addTheatre()
        + addShow()
        + bookShow()
        + makePayment()
        + cancelBooking()
        + declinePayment()
    }
    class Movie {
        - String name
        - String description
        - double basePrice
    }
    class Theatre {
        - String name
        - List~Show~ shows
    }
    class Show {
        - Movie movie
        - List~Seat~ seats
        - Theatre theatre
    }
    class Seat {
        - int row
        - int col
        - SeatStatus status
        - SeatType type
    }
    class Booking {
        - Show show
        - List~Seat~ seats
        - User user
        - BookingStatus status
        - Payment payment
    }
    class Payment {
        - PaymentStatus status
    }
    class User {
        - String name
        - String email
        - UserType type
    }
    ShowBookingService --> Movie
    ShowBookingService --> Theatre
    ShowBookingService --> Show
    ShowBookingService --> Booking
    Show --> Seat
    Booking --> Payment
    Booking --> User
```
