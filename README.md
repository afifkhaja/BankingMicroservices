12.06.2025 - Fully functional banking java microservices application created by Afif Khaja

How to run & test:

1-Start account-service (port 8081).
2-Start payment-service (port 8082).
3-Start gateway-service (port 8080).

Then use something like Postman/curl against the gateway:

Create two accounts
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "ownerName": "Alice",
  "initialBalance": 100.00
}

POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "ownerName": "Bob",
  "initialBalance": 20.00
}

Grab their id values from responses.

Create a payment
POST http://localhost:8080/api/payments
Content-Type: application/json

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 30.00
}

Check:

GET http://localhost:8080/api/payments
GET http://localhost:8080/api/accounts/1
GET http://localhost:8080/api/accounts/2

You should see balances updated and payment marked SUCCESS.
GET http://localhost:8080/api/accounts/2

You should see balances updated and payment marked SUCCESS.
