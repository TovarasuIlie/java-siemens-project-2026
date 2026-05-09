# 🚂 Train Ticketing Application

A RESTful Spring Boot application for managing train bookings, routes, and schedules. Supports customer ticket booking and administrator management operations, with email notifications via Mailtrap.

---

## 📋 Table of Contents

- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
  - [Authentication](#authentication)
  - [Bookings](#bookings)
  - [Route Search](#route-search)
  - [Admin — Trains](#admin--trains)
  - [Admin — Routes](#admin--routes)
- [Email Notifications](#email-notifications)
- [Error Handling](#error-handling)

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Email | Spring Mail + Mailtrap |
| Build | Maven |

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+

---

## 📡 API Reference

All protected endpoints require the header:
```
Authorization: Bearer <jwt_token>
```

---

### Authentication

#### Register
```
POST /api/auth/register
```
**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response `201`:**
```json
{
  "status": "201 CREATED",
  "message": "User registered successfully."
}
```

---

#### Login
```
POST /api/auth/login
```
**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response `200`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Bookings

#### Book a single ticket
```
POST /api/bookings
Authorization: Bearer <token>
```
**Request:**
```json
{
  "trainId": 1,
  "startStationId": 1,
  "endStationId": 3,
  "seatsBooked": 2,
  "travelDate": "2025-06-15"
}
```
**Response `201`:**
```json
[
    {
        "status": "201 CREATED",
        "message": "Booking confirmed! A confirmation email has been sent."
    }
]
```
**Response `409` (overbooking):**
```json
{
    "status": "400 BAD_REQUEST",
    "message": "Not enough seats available. Requested: 2000, Available: 430"
}
```

---

#### Book multiple tickets

**Request:**
```json
[
  {
    "trainId": 1,
    "startStationId": 1,
    "endStationId": 3,
    "seatsBooked": 1,
    "travelDate": "2025-06-15"
  },
  {
    "trainId": 2,
    "startStationId": 4,
    "endStationId": 5,
    "seatsBooked": 2,
    "travelDate": "2025-06-20"
  }
]
```
**Response `201`:** array of booking confirmations (same format as single booking).
```json
[
    {
        "status": "201 CREATED",
        "message": "Booking confirmed! A confirmation email has been sent."
    },
    {
        "status": "201 CREATED",
        "message": "Booking confirmed! A confirmation email has been sent."
    }
]
```

---

#### View my bookings
```
GET /api/bookings/my
Authorization: Bearer <token>
```
**Response `200`:**
```json
[
  {
        "bookingDate": "2026-05-09T19:13:55.267263Z",
        "bookingId": 2,
        "endStation": "Verești",
        "seatsBooked": 2,
        "startStation": "Târgu Frumos",
        "trainNumber": "1833",
        "travelDate": "2026-06-15"
    },
    {
        "bookingDate": "2026-05-09T19:14:54.363417Z",
        "bookingId": 3,
        "endStation": "Verești",
        "seatsBooked": 2,
        "startStation": "Târgu Frumos",
        "trainNumber": "1833",
        "travelDate": "2026-06-15"
    },
    {
        "bookingDate": "2026-05-09T19:18:01.265879Z",
        "bookingId": 4,
        "endStation": "Verești",
        "seatsBooked": 2,
        "startStation": "Târgu Frumos",
        "trainNumber": "1833",
        "travelDate": "2026-06-15"
    }
]
```

---

### Route Search

#### Find connections between two stations
```
GET /api/routes/search?from=Cluj Napoca&to=Ilva Mică
```
**Response `200` (direct connection):**
```json
[
      {
        "trainNumber": "1831",
        "trainType": "IR",
        "departureTime": "14:08:00",
        "arrivalTime": "16:33:00",
        "delayMinutes": 0
    },
    {
        "trainNumber": "4707",
        "trainType": "R",
        "departureTime": "15:04:00",
        "arrivalTime": "19:03:00",
        "delayMinutes": 0
    },
    {
        "trainNumber": "1838",
        "trainType": "IR",
        "departureTime": "21:18:00",
        "arrivalTime": "23:50:00",
        "delayMinutes": 0
    }
]

```
GET /api/routes/search-with-changes?from=Cluj Napoca&to=Ilva Mică
```

```
**Response `200` (with changeover):**
```json
[
    {
        "segments": [
            {
                "trainNumber": "1832",
                "fromStation": "Cluj Napoca",
                "toStation": "Ilva Mică",
                "departureTime": "09:40:00",
                "arrivalTime": "12:11:00",
                "fromStopOrder": 1,
                "toStopOrder": 7
            }
        ]
    },
    {
        "segments": [
            {
                "trainNumber": "1831",
                "fromStation": "Cluj Napoca",
                "toStation": "Ilva Mică",
                "departureTime": "14:08:00",
                "arrivalTime": "16:33:00",
                "fromStopOrder": 18,
                "toStopOrder": 24
            }
        ]
    },
    {
        "segments": [
            {
                "trainNumber": "1831",
                "fromStation": "Cluj Napoca",
                "toStation": "Dej Călători",
                "departureTime": "14:08:00",
                "arrivalTime": "15:06:00",
                "fromStopOrder": 18,
                "toStopOrder": 20
            },
            {
                "trainNumber": "4707",
                "fromStation": "Dej Călători",
                "toStation": "Ilva Mică",
                "departureTime": "16:35:00",
                "arrivalTime": "19:03:00",
                "fromStopOrder": 16,
                "toStopOrder": 35
            }
        ]
    },
    {
        "segments": [
            {
                "trainNumber": "4707",
                "fromStation": "Cluj Napoca",
                "toStation": "Ilva Mică",
                "departureTime": "15:04:00",
                "arrivalTime": "19:03:00",
                "fromStopOrder": 1,
                "toStopOrder": 35
            }
        ]
    }
]
```
**Response `404` (no connection):**
```json
{
    "status": "404 NOT_FOUND",
    "message": "No trains found between Cluj Napoca and Bucuresti"
}
```

---

### Admin — Trains

> All admin endpoints require a JWT token from an `ADMIN` account.

#### Add a train
```
POST /api/admin/trains
Authorization: Bearer <admin_token>
```
**Request:**
```json
{
  "trainNumber": "ICE-500",
  "trainType": "ICE",
  "routeId": 1,
  "totalCapacity": 300
}
```
**Response `201`:**
```json
{
  "id": 5,
  "trainNumber": "ICE-500",
  "trainType": "ICE",
  "routeName": "Cluj - București",
  "totalCapacity": 300,
  "delayMinutes": 0
}
```

---

#### Update a train
```
PUT /api/admin/trains/{id}
Authorization: Bearer <admin_token>
```
**Request:** same format as Add a train.

---

#### Delete a train
```
DELETE /api/admin/trains/{id}
Authorization: Bearer <admin_token>
```
**Response `204`:** no content.

---

#### View bookings for a train
```
GET /api/admin/trains/{id}/bookings
Authorization: Bearer <admin_token>
```
**Response `200`:**
```json
[
  {
        "bookingId": 1,
        "customerEmail": "niculaii58@gmail.com",
        "endStation": "Verești",
        "seatsBooked": 2,
        "startStation": "Târgu Frumos",
        "trainNumber": "1833",
        "travelDate": "2026-06-15"
    },
    {
        "bookingId": 2,
        "customerEmail": "niculaii58@gmail.com",
        "endStation": "Verești",
        "seatsBooked": 2,
        "startStation": "Târgu Frumos",
        "trainNumber": "1833",
        "travelDate": "2026-06-15"
    },
    {
        "bookingId": 3,
        "customerEmail": "niculaii58@gmail.com",
        "endStation": "Verești",
        "seatsBooked": 2,
        "startStation": "Târgu Frumos",
        "trainNumber": "1833",
        "travelDate": "2026-06-15"
    }
]
```

---

#### Report a delay
```
POST /api/admin/trains/{id}/delay?delayMinutes=30
Authorization: Bearer <admin_token>
```
**Response `200`:**
```json
{
  "status": 200,
  "message": "Delay reported and customers notified."
}
```
> All customers with upcoming bookings on this train are automatically notified via email.

---

### Admin — Routes

#### Add a route
```
POST /api/admin/routes
Authorization: Bearer <admin_token>
```
**Request:**
```json
{
  "routeName": "Cluj - București",
  "stations": [
    { "stationId": 1, "stopOrder": 1, "departureTime": "08:00" },
    { "stationId": 2, "stopOrder": 2, "arrivalTime": "10:30", "departureTime": "10:35" },
    { "stationId": 3, "stopOrder": 3, "arrivalTime": "13:00" }
  ]
}
```
**Response `201`:**
```json
{
  "id": 1,
  "routeName": "Cluj - București",
  "stations": ["Cluj-Napoca", "Brașov", "București Nord"]
}
```

---

#### Update a route
```
PUT /api/admin/routes/{id}
Authorization: Bearer <admin_token>
```
**Request:** same format as Add a route.

---

#### Delete a route
```
DELETE /api/admin/routes/{id}
Authorization: Bearer <admin_token>
```
**Response `204`:** no content.

---

## 📧 Email Notifications

Emails are sent asynchronously (non-blocking) via Mailtrap.

### Booking Confirmation
Sent automatically after a successful booking.

```
Subject: Booking Confirmation - IR-1833

Dear user@example.com,

Your booking has been confirmed!

Train:          IR-1833
From:           Cluj-Napoca
To:             București Nord
Travel Date:    2025-06-15
Seats Booked:   2
Booking Date:   2025-05-09T10:30:00Z

Thank you for choosing our service!
```

### Delay Notification
Sent automatically to all affected passengers when an admin reports a delay.

```
Subject: Delay Notice - IR-1833

Dear user@example.com,

We regret to inform you that train IR-1833 is delayed by 30 minutes.

We apologize for the inconvenience.
```

---

## ⚠️ Error Handling

All errors follow a consistent format:

```json
{
  "status": "404 NOT_FOUND",
  "message": "Train not found with id: 99"
}
```

| HTTP Status | Situation |
|---|---|
| `400 Bad Request` | Invalid input / station not on route |
| `401 Unauthorized` | Missing or invalid JWT token |
| `403 Forbidden` | Customer accessing admin endpoint |
| `404 Not Found` | Train / station / route not found |
| `409 Conflict` | Overbooking attempted |