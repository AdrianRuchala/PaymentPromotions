# Adrian_Ruchała_Java_Krakow

A Java application that calculates how payments should be allocated to different payment methods for a set of orders, taking into account promotions and payment method limits.

## Technologies Used:

- Java

## Setup

1. Clone the repository:

```bash
git clone https://github.com/AdrianRuchala/PaymentPromotions.git
```

2. Go to the project folder:

```bash
cd PaymentPromotions
```

## Run application

In the project folder run:

```bash
./gradlew build 
```
Then run:

```bash
java -jar build/libs/payment-promotions-1.0-SNAPSHOT.jar orders.json paymentmethods.json   
```

## Run tests

To run application test run:

```bash
./gradlew test
```

## Input Data Format:

### orders.json

```bash
[
  { "id": "ORDER1", "value": 100.00, "promotions": ["mZysk"] },
  { "id": "ORDER2", "value": 50.00, "promotions": ["PUNKTY"] }
]
```

### paymentmethods.json

```bash
[
  { "id": "PUNKTY", "discount": 10, "limit": 150.00 },
  { "id": "mZysk", "discount": 20, "limit": 100.00 }
]
```

## Output

Payment allocation results for each order.

```bash
Wyniki przypisania platnosci:
PUNKTY: 100.00
BosBankrut: 190.00
mZysk: 165.00
```