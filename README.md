# Casino Backend System

Welcome to the Casino Backend System project! This project is a proof of concept for a casino start-up, aimed at demonstrating key functionalities within a limited timeframe. The system is built using Java and Spring Boot, and interacts with a slot game provided by Dagacube. Below are the key features, requirements, and instructions to get the project up and running.

## Project Overview

### Key Features
1. **Player Balance Management**: Retrieve and update player balances.
2. **Transaction Handling**: Ensure correct behavior for multiple rapid transactions.
3. **Transaction History**: Retrieve the last ten transactions for a player.

### Technologies Used
- **Java**
- **Spring Boot**
- **H2 Database**

## Requirements

### Dagacube's Requirements
- **API Integration**: Implement an API to host the slot game.
- **Concurrency Handling**: Ensure balance updates correctly under high transaction load.

### Casino's Requirements
- **Framework**: Use Java and Spring Boot.
- **Transaction History**: Enable retrieval of the last ten wager/win transactions for customer support.

### CEO's Requirements
- **Database**: Use H2 database for storage.
- **Simplified Structure**: Implement a minimal setup with one controller and service.

## API Specifications

### Base Path: `/casino`

#### Get Balance

**Endpoint**: `GET /player/{playerId}/balance`

- **Request**:
  - `playerId` (path): Integer representing the player's ID.
  
- **Response**:
  - `playerId`: The player's ID.
  - `balance`: The current balance of the player.

- **Errors**:
  - Invalid `playerId` results in HTTP 400 (Bad Request).

#### Update Balance

**Endpoint**: `POST /player/{playerid}/balance/update`

- **Request**:
  - `playerId` (path): Integer representing the player's ID.
  - `amount` (body): Currency value representing the transaction amount (must be positive).
  - `transactionType` (body): Static value, either "WAGER" or "WIN".

- **Response**:
  - `transactionId`: The ID of the transaction.
  - `balance`: The player's current balance.

- **Errors**:
  - Invalid `playerId` results in HTTP 400 (Bad Request).
  - Negative `amount` results in HTTP 400 (Bad Request).
  - Wager greater than current balance results in HTTP 418 (I'm a teapot).

#### Last 10 Transactions

**Endpoint**: `POST /admin/player/transactions`

- **Request**:
  - `username` (body): String representing the player's username (max length 50).

- **Response**:
  - An array of the last 10 transactions containing:
    - `transactionType`: Indicates if the transaction was a wager or a win.
    - `transactionId`: The ID of the transaction.
    - `amount`: The financial value of the transaction.

- **Errors**:
  - Invalid `username` results in HTTP 400 (Bad Request).

## Getting Started

### Prerequisites
- Java 17 
- Maven

### Setup Instructions

1. **Clone the Repository**
   ```sh
   git clone https://github.com/SethuBS/Casino-Backend-System.git
   cd casino-backend-system
   mvn clean install
   mvn spring-boot:run

### Access the Application
- The application will be accessible at `http://localhost:8080/casino`.

### Testing
1. Get Player Balance
   - **Errors**: Retrieve the balance of an existing player.
   - **Method**: GET
   - URL: `http://localhost:8080/casino/player/{playerId}/balance`
   - **Path Variable**: `playerId` - The ID of the player (e.g., `1`)

### Steps:
1. Open Postman and create a new GET request.
2. Enter the `URL http://localhost:8080/casino/player/1/balance`.
3. Click `Send`.

### Expected Response:
   ```json
  {
    "playerId": 1,
    "balance": 100.00
  }
  ```

#### Error Scenarios:

- ***Invalid Player ID***: When an invalid `playerId` is provided, expect a 400 Bad Request response with an appropriate error message.
  ### Expected Response:
    ```json
    {
    "timestamp": "2024-05-23T19:17:12.29113",
    "status": 400,
    "error": "BAD_REQUEST",
    "reason": "Invalid player Id",
    "path": "/casino/player/2/balance"
    }
  ```
    
2. ### Update Player Balance
   ***Scenario 1:*** Update the balance of a player with a valid wager transaction.
- **Method:** POST
- **URL:** `http://localhost:8080/casino/player/{playerId}/balance/update`
- **Path Variable:** `playerId` - The ID of the player (e.g., `1`).
- **Request Body:**
   ```json
     {
    "amount": 10.00,
    "transactionType": "WAGER"
    }
    ```




