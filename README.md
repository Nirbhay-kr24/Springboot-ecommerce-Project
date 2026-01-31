# Spring Cart - E-Commerce REST API

A comprehensive, production-ready Spring Boot e-commerce backend API with JWT authentication, secure payment processing, and advanced features for managing products, orders, users, and analytics.

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/SpringCart/ecommerce/
│   │   ├── SpringCartApplication.java      - Main application class
│   │   ├── config/                         - Configuration classes
│   │   │   ├── JwtAuthFilter.java
│   │   │   ├── SecurityBeansConfig.java
│   │   │   ├── RestTemplateConfig.java
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/                     - REST API endpoints
│   │   │   ├── AuthController.java
│   │   │   ├── ProductController.java
│   │   │   ├── CartController.java
│   │   │   ├── OrderController.java
│   │   │   ├── UserController.java
│   │   │   ├── CategoryController.java
│   │   │   ├── ImageController.java
│   │   │   ├── AnalyticsController.java
│   │   │   └── CartItemController.java
│   │   ├── dto/                            - Data Transfer Objects
│   │   ├── entity/                         - JPA entities
│   │   ├── repository/                     - Data access layer
│   │   ├── service/                        - Business logic
│   │   │   ├── auth/
│   │   │   ├── product/
│   │   │   ├── cart/
│   │   │   ├── order/
│   │   │   ├── user/
│   │   │   ├── email/                      - Email service (SMTP)
│   │   │   ├── category/
│   │   │   ├── image/
│   │   │   ├── analytics/
│   │   │   └── currency/
│   │   ├── exceptions/                     - Custom exceptions
│   │   ├── util/                           - Utility classes
│   │   └── enums/                          - Enumerations
│   └── resources/
│       └── application.properties           - Configuration file
└── test/                                   - Unit tests
```

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.3.7
- **Language**: Java 17
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA / Hibernate
- **Email**: SMTP (Gmail/Custom SMTP)
- **Validation**: Jakarta Validation
- **Object Mapping**: ModelMapper
- **API Documentation**: SpringDoc OpenAPI
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- Git

## 🔧 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/springboot-ecommerce-project.git
cd Springboot-ecommerce-Project-main
```

### 2. Configure Database
Create a MySQL database:
```sql
CREATE DATABASE spring_cart_db;
```

### 3. Set Environment Variables
Create a `.env` file or set system environment variables:

```bash
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/spring_cart_db
DB_USERNAME=root
DB_PASSWORD=your_password

# SMTP Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Security
JWT_SECRET=your-secure-jwt-secret-key-min-256-bits
JWT_EXPIRATION=86400000
ADMIN_SECRET=your-admin-secret

# Server
SERVER_PORT=8080
```

### 4. For Gmail SMTP
1. Enable 2-Factor Authentication in your Google Account
2. Generate an [App Password](https://myaccount.google.com/apppasswords)
3. Use the generated 16-character password as `MAIL_PASSWORD`

### 5. Build the Project
```bash
mvn clean install
```

### 6. Run the Application
```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`

## 🔌 API Endpoints

### Authentication
```
POST   /api/auth/register          - Register new user
POST   /api/auth/login             - Login user
POST   /api/auth/refresh-token     - Refresh JWT token
```

### Products
```
GET    /api/products               - Get all products
GET    /api/products/{id}          - Get product by ID
POST   /api/products               - Create product (Admin)
PUT    /api/products/{id}          - Update product (Admin)
DELETE /api/products/{id}          - Delete product (Admin)
GET    /api/products/search        - Search products
```

### Categories
```
GET    /api/categories             - Get all categories
GET    /api/categories/{id}        - Get category by ID
POST   /api/categories             - Create category (Admin)
PUT    /api/categories/{id}        - Update category (Admin)
DELETE /api/categories/{id}        - Delete category (Admin)
```

### Cart
```
GET    /api/cart                   - Get user's cart
POST   /api/cart/add               - Add item to cart
PUT    /api/cart/update            - Update cart item
DELETE /api/cart/remove/{itemId}   - Remove item from cart
DELETE /api/cart/clear             - Clear cart
```

### Orders
```
POST   /api/orders                 - Create order
GET    /api/orders                 - Get user's orders
GET    /api/orders/{id}            - Get order by ID
PUT    /api/orders/{id}/status     - Update order status (Admin)
GET    /api/orders/history         - Get order history
```

### Users
```
GET    /api/users/profile          - Get user profile
PUT    /api/users/profile          - Update user profile
GET    /api/users/{id}             - Get user by ID (Admin)
DELETE /api/users/{id}             - Delete user (Admin)
```

### Images
```
POST   /api/images/upload          - Upload image
GET    /api/images/{id}            - Get image
DELETE /api/images/{id}            - Delete image
```

### Analytics
```
GET    /api/analytics/summary      - Get sales summary
GET    /api/analytics/orders       - Get order analytics
GET    /api/analytics/products     - Get product analytics
GET    /api/analytics/revenue      - Get revenue reports
```

## 📖 API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

Access OpenAPI JSON at: `http://localhost:8080/v3/api-docs`

## 🏗️ Project Structure

## 🔒 Security Considerations

1. **JWT Tokens**: Tokens expire after 24 hours by default
2. **Password Hashing**: Passwords are hashed using BCrypt
3. **HTTPS**: Use HTTPS in production
4. **Environment Variables**: Never commit sensitive data
5. **CORS**: Configure CORS based on your frontend domain
6. **Rate Limiting**: Built-in rate limiting on sensitive endpoints
7. **SQL Injection**: Protected via parameterized queries
8. **Email Security**: SMTP credentials secured via environment variables

## 📧 Email Service Configuration

### Gmail Setup
1. Create a Google App Password
2. Set `MAIL_HOST=smtp.gmail.com`
3. Set `MAIL_PORT=587`
4. Set `MAIL_USERNAME=your-email@gmail.com`
5. Set `MAIL_PASSWORD=your-app-password`

### Custom SMTP Server
```properties
MAIL_HOST=your-smtp-server.com
MAIL_PORT=587
MAIL_USERNAME=your-username
MAIL_PASSWORD=your-password
```

## 🧪 Testing

Run tests using:
```bash
mvn test
```

Or with coverage:
```bash
mvn test jacoco:report
```

## 📊 Database Schema

The application automatically creates database tables using Hibernate JPA with `ddl-auto=update`. Key entities include:

- **User** - User accounts and profiles
- **Product** - Product catalog
- **Category** - Product categories
- **Cart** - Shopping carts
- **CartItem** - Items in cart
- **Order** - Customer orders
- **OrderItem** - Items in orders
- **Image** - Product images

## 🚀 Deployment

### Docker
```bash
docker build -t spring-cart .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://mysql:3306/spring_cart_db \
  -e MAIL_HOST=smtp.gmail.com \
  spring-cart
```

### Cloud Platforms
- **AWS**: Deploy to EC2 or Elastic Beanstalk
- **Heroku**: Compatible with buildpacks
- **Google Cloud**: Deploy to App Engine or Cloud Run
- **Azure**: Deploy to App Service

## 🐛 Known Issues

- Rate limiting requires Redis for distributed systems
- Image upload requires sufficient disk space
- Email delivery depends on SMTP provider reliability

## 🔄 Roadmap

- [ ] Payment gateway integration (Stripe, PayPal)
- [ ] Order tracking with notifications
- [ ] Wishlist feature
- [ ] Product reviews and ratings
- [ ] Inventory management system
- [ ] Admin dashboard
- [ ] Mobile app API enhancements
- [ ] Multi-language support

## 👨‍💻 Author

**Nirbhay Kumar**

---
