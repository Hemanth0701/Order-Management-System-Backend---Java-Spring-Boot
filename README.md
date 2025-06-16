📌 Key Features & Technologies:
  ⚙️ Tech Stack :
      Java 17+
      Spring Boot
      Spring Security with JWT
      Spring Data JPA
      RESTful APIs
      MapStruct for DTO ↔ Entity mapping
      Exception Handling: Global handler for custom exceptions
      Maven for build management
      Role-based Access Control (RBAC) using custom Role enum
      Entity Design: Polymorphic UserDetails (User, Admin)
      DTO Architecture: Entry, Exit, Update DTOs for clean separation
      Scheduling: Price updates with PriceSchedulerService
      Secure Authentication: JWT token filter, login/logout routes
      
   🧠 Domain Design :
      Entities: User, Admin, Product, Cart, Order, CartItem, OrderItem, Price
      Enum-based business logic: Status, Roles, OrderStatus, Action Types
      Repositories: Clean, segregated JPA Repositories
      Controllers: Auth, Product, Cart, Order, User controllers
      Service Layer: Interfaces and Implementations separated (e.g., UserService + UserServiceImpl)
      Security Layer:
          JwtAuthFilter, JwtUtil, CustomUserDetailsService
          Secure role validation and stateless session handling

  Detailed Description :
  - Developed a secure and scalable Order Management System backend using Java 17 and Spring Boot with JWT-based authentication and RBAC.
  - Implemented clean layered architecture with separation of DTOs (entry, exit, update), entities, services, repositories, and exception handling.
  - Designed and integrated REST APIs for cart, product, user, and order management, supporting full CRUD operations with role-based authorization.
  - Utilized Spring Security and JWT to secure APIs, building a stateless authentication mechanism with custom filters and token utilities.
  - Created custom global exception handling with structured error responses, enhancing debuggability and maintainability.
  - Built scheduled services using Spring's `@Scheduled` to update product pricing dynamically.
  - Employed MapStruct for efficient, boilerplate-free mapping between DTOs and domain entities.
  - Followed best practices with Maven, modular packaging, and meaningful naming conventions for extensibility and testability.
