# Wishlist Helper REST API

This is a REST API for managing a wishlist application. It is built using Kotlin, Spring Boot, and Gradle.

## Project Structure

- `src/main/kotlin/be/hpauwel/wishlisthelperrest/WishlistHelperRestApplication.kt`: Main application entry point.
- `src/main/kotlin/be/hpauwel/wishlisthelperrest/model/User.kt`: User entity definition.
- `src/main/kotlin/be/hpauwel/wishlisthelperrest/repository/UserRepository.kt`: User repository interface.
- `src/main/kotlin/be/hpauwel/wishlisthelperrest/service/UserService.kt`: User service for business logic.

## Prerequisites

- JDK 21 or higher
- Gradle 7.0 or higher

## Getting Started

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/wishlist-helper-rest.git
    cd wishlist-helper-rest
    ```

2. Build the project:
    ```sh
    ./gradlew build
    ```

3. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## API Endpoints

### User Endpoints

- **Find User by Email**
    - **URL:** `/users/email/{email}`
    - **Method:** `GET`
    - **Description:** Retrieve a user by their email address.

- **Find All Users**
    - **URL:** `/users`
    - **Method:** `GET`
    - **Description:** Retrieve all users.

- **Find User by ID**
    - **URL:** `/users/{id}`
    - **Method:** `GET`
    - **Description:** Retrieve a user by their ID.

- **Create User**
    - **URL:** `/users`
    - **Method:** `POST`
    - **Description:** Create a new user.
    - **Request Body:**
        ```json
        {
            "email": "user@example.com",
            "password": "password123"
        }
        ```

## Models

### User

```kotlin
@Entity(name = "owner")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,
    val email: String,
    val password: String
)