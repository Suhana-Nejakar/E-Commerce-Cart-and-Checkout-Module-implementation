# E-Commerce Cart & Checkout Module

Production-ready Spring Boot Cart and Checkout module. This version does not use Lombok; all constructors, getters, setters, and builder helpers are written manually.

## Features

- Add product to cart
- Update product quantity
- Remove product from cart
- View cart with total price
- Apply percentage or flat coupon
- Validate stock before checkout
- Create order from cart
- Simulate payment success/failure
- Reduce inventory only on successful payment
- Keep inventory unchanged on failed payment
- Generate order summary
- Fetch paginated order history by user ID
- Global exception handling
- Unit tests for checkout business logic

## Tech Stack

- Java 17
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- JUnit 5
- Mockito

## Setup Steps

### 1. Create MySQL database

```sql
CREATE DATABASE ecommerce_checkout_db;
```

### 2. Update DB credentials

Open:

```txt
src/main/resources/application.properties
```

Update:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Run the application

```bash
mvn clean spring-boot:run
```

### 4. Run SQL script

Run:

```txt
src/main/resources/schema.sql
```

This inserts sample user, products, and coupons.

## API Endpoints

### Add item to cart

```http
POST /api/cart/items
```

```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 2
}
```

### Update cart item

```http
PUT /api/cart/1/items/1
```

```json
{
  "quantity": 3
}
```

### Remove cart item

```http
DELETE /api/cart/1/items/1
```

### View cart

```http
GET /api/cart/1
```

### Checkout success

```http
POST /api/checkout
```

```json
{
  "userId": 1,
  "couponCode": "SAVE10",
  "paymentSuccess": true
}
```

### Checkout failed payment

```http
POST /api/checkout
```

```json
{
  "userId": 1,
  "couponCode": "SAVE10",
  "paymentSuccess": false
}
```

### Order history

```http
GET /api/orders/user/1?page=0&size=10
```

## Transaction Logic

Checkout is handled inside one transactional method:

```java
@Transactional
public CheckoutResponse checkout(CheckoutRequest request)
```

During checkout:

1. Cart is fetched.
2. Cart empty condition is checked.
3. Product rows are locked using pessimistic lock.
4. Stock is validated.
5. Coupon is applied.
6. Order and order items are created.
7. Payment is simulated.
8. If payment succeeds, inventory is reduced and cart is cleared.
9. If payment fails, inventory remains unchanged.

## Run Tests

```bash
mvn test
```

## Important Interview Explanation

In real e-commerce checkout, inventory must be safe. Two users should not buy the same last item at the same time. That is why this project uses:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

This locks the product row during checkout so another checkout request cannot update the same stock at the same time.
