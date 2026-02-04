# Bookstore API

This is the backend REST API for the **Bookstore** application, built with **Spring Boot**.  
It provides endpoints to manage books, carts, and orders for an online bookstore.

---

## Features

- **Book Management**
  - Add, retrieve, and list books.
  - API endpoints:
    - `POST /api/books` – Add a new book
    - `GET /api/books` – Retrieve all books
    - `GET /api/books/{id}` – Retrieve a book by ID

- **Cart Management**
  - Add books to the cart, update quantities, and remove items.
  - API endpoints:
    - `POST /api/cart` – Add item to cart (requires `bookId` and `quantity`)
    - `GET /api/cart` – Get all cart items for the current session
    - `PUT /api/cart/{id}` – Update quantity of a cart item
    - `DELETE /api/cart/{id}` – Remove a cart item
    - `DELETE /api/cart` – Clear all cart items for the session

- **Order Management**
  - Place orders from cart items and track order status.
  - API endpoints:
    - `POST /api/orders` – Place an order
    - `GET /api/orders` – Get all orders
    - `GET /api/orders/{orderId}` – Get a specific order
    - `GET /api/orders/session` – Get orders for current session
    - `GET /api/orders/user/{userId}` – Get orders for a user
    - `PUT /api/orders/{orderId}/status` – Update order status
    - `PUT /api/orders/{orderId}/cancel` – Cancel an order

- **Session Handling**
  - Supports anonymous sessions with `X-Session-Id` header for cart and order operations.

- **Validation and Exception Handling**
  - Custom exceptions for not found resources.
  - Validation for required fields and quantities.

---

## Getting Started

### Prerequisites
- Java 21
- Maven 4+
- MySQL or H2 database

### Build and Run
```bash
mvn clean install
mvn spring-boot:run
```
### API Base URL

```bash
http://localhost:8080/api/
```
### Testing
Unit and integration tests are included with JUnit 5 and Mockito.

### Run tests using Maven:
```bash
mvn test
```


# Bookstore UI

This application for the **Bookstore** project built with **React**.  
Simple user interface to browse books, add new books and manage a shopping cart. The frontend communicates with a Spring Boot backend via REST APIs.

## Features

- **Add Book**  
  Users can add a new book by filling out a simple form (name, author, price). The data is sent to the backend endpoint:  
  `POST http://localhost:8080/api/books`

- **Show All Books**  
  Retreive all books from the backend and displays them in a list.

- **Add to Cart**  
  Each book has an input for quantity and an "Add" button. By clicking on "Add" sends a request to:  
  `POST http://localhost:8080/api/cart`  
  with the selected `bookId` and `quantity`.  

- **Show Cart Items**  
  Displays all items in the user's cart. Fetches data from:  
  `GET http://localhost:8080/api/cart`  
  Cart item shows the book details, quantity and subtotal.


---

## Getting Started

### Prerequisites
- Node.js >= 20
- npm >= 10
- Backend Spring Boot service running on `http://localhost:8080`

### Install Dependencies
```bash
npm install
```
### Start Development Server
```bash
npm run dev
```
### Access Application
Open your browser at `http://localhost:5173`

