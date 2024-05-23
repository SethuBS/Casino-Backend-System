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
    "balance": 1000.00
}
```

#### Error Scenarios:

- ***Invalid Player ID***: When an invalid `playerId` is provided, expect a 400 Bad Request response with an appropriate error message.
  ### Expected Response:
    ```json
    {
    "timestamp": "2024-05-24T00:35:53.3791257",
    "status": 400,
    "error": "BAD_REQUEST",
    "reason": "The player ID you provided is not valid. Please enter a valid player ID.",
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
    "amount": "555.95",
    "transactionType": "WAGER"
    }
```
 ```json
     {
    "amount": "259.80",
    "transactionType": "Win"
    }
```

### Steps:

1. Open Postman and create a new GET request.
2. Enter the URL `http://localhost:8080/casino/player/1/balance.`
3. Click Send.
### Expected Response:
```json
{
    "playerId": 1,
    "balance": 100.00
}
```
### Error Scenarios:

- ***Invalid Player ID:*** When an invalid `playerId` is provided, expect a 400 Bad Request response with an appropriate error message.
  ### Expected Response:
  ```json
    {
    "timestamp": "2024-05-23T19:36:43.4585764",
    "status": 400,
    "error": "BAD_REQUEST",
    "reason": "Invalid player Id",
    "path": "/casino/player/2/balance"
    }
  ```
  
**2. Update Player Balance**
***Scenario 1:*** Update the balance of a player with a valid wager transaction.

-**Method:** POST
-**URL:** `http://localhost:8080/casino/player/{playerId}/balance/update`
-**Path Variable:** `playerId` - The ID of the player (e.g., `1`).
-**Request Body:**
```json
{
    "amount": 10.00,
    "transactionType": "WAGER"
}

```
### Steps:

1. Open Postman and create a new POST request.
2. Enter the URL `http://localhost:8080/casino/player/1/balance/update`.
3. Set the request body to:
```json
   {
    "amount": 10.00,
    "transactionType": "WAGER"
   }
```

4. Click `Send`.
**Expected Response:**
```json
{
    "transactionId": 123456,
    "balance": 90.00
}
```

***Scenario 2***: Update the balance of a player with a valid win transaction.

-**Request Body:**
```json
{
    "amount": 50.00,
    "transactionType": "WIN"
}
```


### Steps:

1. Follow the same steps as Scenario 1, but change the request body to:
```json
{
    "amount": 50.00,
    "transactionType": "WIN"
}
```
2. Click `Send`.

### Expected Response:
```json
{
    "transactionId": 123457,
    "balance": 140.00
}
```


### Error Scenarios:

- ***Invalid Player ID:*** Expect a 400 Bad Request response with an appropriate error message.
### Expected Response:
```json
{
    "timestamp": "2024-05-23T19:51:20.1362507",
    "status": 400,
    "error": "BAD_REQUEST",
    "reason": "Invalid player Id",
    "path": "/casino/player/2/balance/update"
}
```
- ***Negative Amount:*** Expect a 400 Bad Request response if the amount is negative.
### Expected Response:
```json
{
    "timestamp": "2024-05-23T19:54:03.3576626",
    "status": 400,
    "error": "BAD_REQUEST",
    "reason": "Amount must be positive",
    "path": "/casino/player/1/balance/update"
}
```
- ***Wager Greater than Balance:*** Expect a 418 I'm a teapot response if the wager amount exceeds the current balance.
### Expected Response:
```json
{
    "timestamp": "2024-05-23T19:54:42.8266459",
    "status": 418,
    "error": "I_AM_A_TEAPOT",
    "reason": "Wager greater than current balance",
    "path": "/casino/player/1/balance/update"
}
```

**3. Get Last 10 Transactions**

**Scenario:** Retrieve the last ten transactions for a player by username.

- **Method:** POST
- **URL:** `http://localhost:8080/casino/admin/player/transactions`
- **Request Body:**
```json
  {
    "username": "player1"
  }
```
### Steps:

1. Open Postman and create a new POST request.
2. Enter the URL `http://localhost:8080/casino/admin/player/transactions`.
3. Set the request body to:
```json
  {
    "username": "player1"
  }
```
Click `Send`.

### Expected Response:
```json
  {
    [
    {
        "transactionType": "WIN",
        "transactionId": 7,
        "amount": 30.71
    },
    {
        "transactionType": "WAGER",
        "transactionId": 9,
        "amount": 3.93
    },
    {
        "transactionType": "WAGER",
        "transactionId": 2,
        "amount": 95.85
    },
    {
        "transactionType": "WAGER",
        "transactionId": 1,
        "amount": 13.72
    },
    {
        "transactionType": "WIN",
        "transactionId": 8,
        "amount": 68.15
    },
    {
        "transactionType": "WAGER",
        "transactionId": 4,
        "amount": 74.67
    },
    {
        "transactionType": "WAGER",
        "transactionId": 11,
        "amount": 45.29
    },
    {
        "transactionType": "WIN",
        "transactionId": 3,
        "amount": 32.31
    },
    {
        "transactionType": "WAGER",
        "transactionId": 5,
        "amount": 18.43
    },
    {
        "transactionType": "WIN",
        "transactionId": 10,
        "amount": 84.48
    }
]
  }
```

### Error Scenarios:
- **Invalid Username:** Expect a 400 Bad Request response with an appropriate error message.
  ### Expected Response:

```json
  {
    "timestamp": "2024-05-23T20:20:12.2185456",
    "status": 400,
    "error": "BAD_REQUEST",
    "reason": "Invalid username",
    "path": "/casino/admin/player/transactions"
  }
```

# Future Improvements
- Expand the database integration for production use.
- Implement additional security measures.
- Enhance error handling and validation.

# Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.

# License
This project is licensed under the MIT License. See the [MIT License](LICENSE) file for details.




