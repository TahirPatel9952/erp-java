# Manufacturing ERP System - Application User Guide

## Table of Contents
1. [Getting Started](#1-getting-started)
2. [Authentication](#2-authentication)
3. [User Management](#3-user-management)
4. [Master Data Management](#4-master-data-management)
5. [Inventory Management](#5-inventory-management)
6. [Purchase Management](#6-purchase-management)
7. [Sales Management](#7-sales-management)
8. [Production Management](#8-production-management)
9. [Reports & Analytics](#9-reports--analytics)
10. [API Reference](#10-api-reference)

---

## 1. Getting Started

### 1.1 System Requirements
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### 1.2 Running the Application

```bash
# Navigate to backend directory
cd manufacturing-erp-backend

# Run with Maven
mvn spring-boot:run

# Or run the JAR
java -jar target/manufacturing-erp-1.0.0-SNAPSHOT.jar
```

### 1.3 Base URL
```
http://localhost:8080/api
```

### 1.4 API Documentation (Swagger)
```
http://localhost:8080/api/swagger-ui.html
```

---

## 2. Authentication

### 2.1 Register a New User

**Endpoint:** `POST /v1/auth/register`

**Request:**
```json
{
  "username": "john.doe",
  "email": "john.doe@company.com",
  "password": "SecurePass@123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+91-9876543210",
  "roleId": 4
}
```

**Role IDs:**
| ID | Role | Description |
|----|------|-------------|
| 1 | ADMIN | Full system access |
| 2 | MANAGER | Approval rights, reports |
| 3 | SUPERVISOR | Operational rights |
| 4 | OPERATOR | Limited access |
| 5 | VIEWER | Read-only access |

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "user": {
    "id": 7,
    "username": "john.doe",
    "email": "john.doe@company.com",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "role": "OPERATOR",
    "permissions": ["INVENTORY_READ", "PRODUCTION_EXECUTE"]
  }
}
```

### 2.2 Login

**Endpoint:** `POST /v1/auth/login`

**Request:**
```json
{
  "usernameOrEmail": "john.doe",
  "password": "SecurePass@123"
}
```

**Response:** Same as register response

### 2.3 Refresh Token

**Endpoint:** `POST /v1/auth/refresh`

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

### 2.4 Logout

**Endpoint:** `POST /v1/auth/logout`

**Headers:**
```
Authorization: Bearer <access_token>
```

### 2.5 Using Authentication

For all protected endpoints, include the access token in the header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 3. User Management

### 3.1 Get All Users (Admin Only)

**Endpoint:** `GET /v1/users`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Page number |
| size | int | 20 | Items per page |
| sort | string | id,asc | Sort field and direction |
| search | string | - | Search by name/email |
| roleId | long | - | Filter by role |
| isActive | boolean | - | Filter by status |

**Example:**
```
GET /v1/users?page=0&size=10&search=john&isActive=true
```

### 3.2 Get User by ID

**Endpoint:** `GET /v1/users/{id}`

### 3.3 Update User

**Endpoint:** `PUT /v1/users/{id}`

**Request:**
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "phone": "+91-9876543211",
  "roleId": 3,
  "isActive": true
}
```

### 3.4 Delete User (Soft Delete)

**Endpoint:** `DELETE /v1/users/{id}`

### 3.5 Change Password

**Endpoint:** `PUT /v1/users/{id}/change-password`

**Request:**
```json
{
  "currentPassword": "OldPass@123",
  "newPassword": "NewPass@456"
}
```

---

## 4. Master Data Management

### 4.1 Categories

Categories organize raw materials and finished goods.

#### Get All Categories
**Endpoint:** `GET /v1/categories`

**Query Parameters:**
- `type`: RAW_MATERIAL, FINISHED_GOOD, IN_PROCESS
- `parentId`: Filter by parent category

#### Create Category
**Endpoint:** `POST /v1/categories`

```json
{
  "name": "Electronic Components",
  "code": "RM-ELEC",
  "type": "RAW_MATERIAL",
  "parentId": 1,
  "description": "Electronic parts and components"
}
```

### 4.2 Units of Measurement

#### Get All Units
**Endpoint:** `GET /v1/units`

**Query Parameters:**
- `type`: QUANTITY, WEIGHT, LENGTH, VOLUME, AREA, TIME

#### Create Unit
**Endpoint:** `POST /v1/units`

```json
{
  "name": "Kilogram",
  "symbol": "Kg",
  "type": "WEIGHT",
  "baseUnitId": 5,
  "conversionFactor": 1000
}
```

### 4.3 Warehouses

#### Get All Warehouses
**Endpoint:** `GET /v1/warehouses`

#### Create Warehouse
**Endpoint:** `POST /v1/warehouses`

```json
{
  "name": "Main Warehouse",
  "code": "WH-001",
  "address": "123 Industrial Area",
  "city": "Mumbai",
  "state": "Maharashtra",
  "country": "India",
  "pincode": "400001",
  "contactPerson": "John Doe",
  "contactPhone": "+91-22-12345678",
  "contactEmail": "warehouse@company.com"
}
```

### 4.4 Warehouse Locations

#### Create Location
**Endpoint:** `POST /v1/warehouses/{warehouseId}/locations`

```json
{
  "locationCode": "WH-001-A1-R1-S1",
  "zone": "A",
  "rack": "R1",
  "shelf": "S1",
  "bin": "B1",
  "capacity": 1000,
  "capacityUnitId": 1
}
```

---

## 5. Inventory Management

### 5.1 Raw Materials

#### Get All Raw Materials
**Endpoint:** `GET /v1/raw-materials`

**Query Parameters:**
- `categoryId`: Filter by category
- `search`: Search by name/code
- `isActive`: Filter by status
- `belowReorderLevel`: true/false

#### Create Raw Material
**Endpoint:** `POST /v1/raw-materials`

```json
{
  "name": "Stainless Steel Sheet 304",
  "code": "RM-SS-304",
  "description": "Grade 304 Stainless Steel Sheet",
  "categoryId": 2,
  "unitId": 6,
  "hsnCode": "7219",
  "reorderLevel": 500,
  "reorderQuantity": 1000,
  "minimumOrderQuantity": 100,
  "leadTimeDays": 7,
  "standardCost": 250.00,
  "taxPercent": 18.00,
  "shelfLifeDays": null,
  "storageConditions": "Keep dry",
  "isBatchTracked": false
}
```

#### Update Raw Material
**Endpoint:** `PUT /v1/raw-materials/{id}`

### 5.2 Finished Goods

#### Get All Finished Goods
**Endpoint:** `GET /v1/finished-goods`

#### Create Finished Good
**Endpoint:** `POST /v1/finished-goods`

```json
{
  "name": "Smart Controller Unit A1",
  "code": "FG-SCU-A1",
  "description": "IoT enabled smart controller",
  "categoryId": 9,
  "unitId": 1,
  "hsnCode": "8537",
  "sellingPrice": 2500.00,
  "minimumSellingPrice": 2200.00,
  "mrp": 2999.00,
  "standardCost": 1800.00,
  "reorderLevel": 100,
  "taxPercent": 18.00,
  "weight": 0.5,
  "weightUnitId": 6,
  "dimensions": "15x10x5 cm",
  "barcode": "8901234567890",
  "isBatchTracked": true
}
```

### 5.3 Stock Management

#### Get Raw Material Stock
**Endpoint:** `GET /v1/stock/raw-materials`

**Query Parameters:**
- `warehouseId`: Filter by warehouse
- `rawMaterialId`: Filter by raw material
- `belowReorderLevel`: true/false

#### Get Finished Goods Stock
**Endpoint:** `GET /v1/stock/finished-goods`

#### Stock Adjustment
**Endpoint:** `POST /v1/stock/adjustments`

```json
{
  "itemType": "RAW_MATERIAL",
  "itemId": 1,
  "warehouseId": 2,
  "locationId": 5,
  "adjustmentType": "ADD",
  "quantity": 100,
  "reason": "Physical count correction",
  "batchNo": "BATCH-2024-001",
  "unitCost": 250.00
}
```

---

## 6. Purchase Management

### 6.1 Suppliers

#### Get All Suppliers
**Endpoint:** `GET /v1/suppliers`

#### Create Supplier
**Endpoint:** `POST /v1/suppliers`

```json
{
  "name": "Steel India Pvt Ltd",
  "code": "SUP-001",
  "contactPerson": "Mahesh Agarwal",
  "phone": "+91-22-44556677",
  "email": "sales@steelindia.com",
  "address": "100 Steel Complex, MIDC",
  "city": "Pune",
  "state": "Maharashtra",
  "country": "India",
  "pincode": "411001",
  "gstNo": "27AABCS1234H1Z5",
  "panNo": "AABCS1234H",
  "bankName": "HDFC Bank",
  "bankAccountNo": "12345678901234",
  "bankIfsc": "HDFC0001234",
  "paymentTerms": 30,
  "creditLimit": 5000000.00
}
```

### 6.2 Purchase Orders

#### Get All Purchase Orders
**Endpoint:** `GET /v1/purchase-orders`

**Query Parameters:**
- `status`: DRAFT, PENDING_APPROVAL, APPROVED, SENT, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
- `supplierId`: Filter by supplier
- `fromDate`, `toDate`: Date range

#### Create Purchase Order
**Endpoint:** `POST /v1/purchase-orders`

```json
{
  "supplierId": 1,
  "expectedDeliveryDate": "2024-12-15",
  "shippingAddress": "123 Factory Road, Mumbai",
  "notes": "Urgent order",
  "items": [
    {
      "rawMaterialId": 1,
      "quantity": 500,
      "unitPrice": 248.50,
      "taxPercent": 18.00,
      "discountPercent": 2.00
    },
    {
      "rawMaterialId": 2,
      "quantity": 200,
      "unitPrice": 345.00,
      "taxPercent": 18.00
    }
  ]
}
```

#### Approve Purchase Order
**Endpoint:** `PUT /v1/purchase-orders/{id}/approve`

#### Send to Supplier
**Endpoint:** `PUT /v1/purchase-orders/{id}/send`

### 6.3 Goods Receipt Note (GRN)

#### Create GRN
**Endpoint:** `POST /v1/grn`

```json
{
  "purchaseOrderId": 1,
  "receivedDate": "2024-12-10",
  "invoiceNumber": "INV-2024-001",
  "invoiceDate": "2024-12-08",
  "warehouseId": 2,
  "notes": "Partial delivery",
  "items": [
    {
      "purchaseOrderItemId": 1,
      "receivedQuantity": 300,
      "acceptedQuantity": 295,
      "rejectedQuantity": 5,
      "rejectionReason": "Damaged",
      "locationId": 5,
      "batchNo": "SS304-2024-001"
    }
  ]
}
```

---

## 7. Sales Management

### 7.1 Customers

#### Get All Customers
**Endpoint:** `GET /v1/customers`

#### Create Customer
**Endpoint:** `POST /v1/customers`

```json
{
  "name": "TechMart Distributors",
  "code": "CUS-001",
  "customerType": "DISTRIBUTOR",
  "contactPerson": "Arun Kumar",
  "phone": "+91-22-11112222",
  "email": "orders@techmart.in",
  "billingAddress": "500 Commercial Complex",
  "billingCity": "Mumbai",
  "billingState": "Maharashtra",
  "billingCountry": "India",
  "billingPincode": "400050",
  "shippingAddress": "500 Commercial Complex",
  "shippingCity": "Mumbai",
  "shippingState": "Maharashtra",
  "shippingCountry": "India",
  "shippingPincode": "400050",
  "gstNo": "27AAACT1111H1Z2",
  "panNo": "AAACT1111H",
  "creditLimit": 10000000.00,
  "paymentTerms": 45,
  "discountPercent": 5.00
}
```

### 7.2 Sales Orders

#### Get All Sales Orders
**Endpoint:** `GET /v1/sales-orders`

**Query Parameters:**
- `status`: DRAFT, CONFIRMED, PROCESSING, READY_TO_SHIP, SHIPPED, DELIVERED, CANCELLED
- `customerId`: Filter by customer
- `fromDate`, `toDate`: Date range

#### Create Sales Order
**Endpoint:** `POST /v1/sales-orders`

```json
{
  "customerId": 1,
  "expectedDeliveryDate": "2024-12-20",
  "shippingAddress": "500 Commercial Complex, Mumbai",
  "notes": "Priority order",
  "items": [
    {
      "finishedGoodsId": 1,
      "quantity": 50,
      "unitPrice": 2500.00,
      "taxPercent": 18.00,
      "discountPercent": 5.00
    },
    {
      "finishedGoodsId": 2,
      "quantity": 25,
      "unitPrice": 4500.00,
      "taxPercent": 18.00
    }
  ]
}
```

#### Confirm Sales Order
**Endpoint:** `PUT /v1/sales-orders/{id}/confirm`

### 7.3 Delivery Challan

#### Create Delivery Challan
**Endpoint:** `POST /v1/delivery-challans`

```json
{
  "salesOrderId": 1,
  "warehouseId": 3,
  "vehicleNumber": "MH-01-AB-1234",
  "driverName": "Ramesh",
  "driverPhone": "+91-9876543210",
  "notes": "Handle with care",
  "items": [
    {
      "salesOrderItemId": 1,
      "dispatchedQuantity": 50,
      "stockId": 1
    }
  ]
}
```

### 7.4 Invoices

#### Create Invoice
**Endpoint:** `POST /v1/invoices`

```json
{
  "salesOrderId": 1,
  "deliveryChallanId": 1,
  "invoiceDate": "2024-12-15",
  "dueDate": "2025-01-30",
  "notes": "Payment due in 45 days"
}
```

### 7.5 Payments

#### Record Payment
**Endpoint:** `POST /v1/payments`

```json
{
  "invoiceId": 1,
  "paymentDate": "2024-12-20",
  "amount": 150000.00,
  "paymentMode": "BANK_TRANSFER",
  "referenceNumber": "UTR123456789",
  "notes": "Partial payment"
}
```

---

## 8. Production Management

### 8.1 Bill of Materials (BOM)

#### Get All BOMs
**Endpoint:** `GET /v1/bom`

#### Create BOM
**Endpoint:** `POST /v1/bom`

```json
{
  "finishedGoodsId": 1,
  "bomCode": "BOM-SCU-A1-V2",
  "version": "V2.0",
  "description": "Updated BOM for Smart Controller",
  "outputQuantity": 1,
  "outputUnitId": 1,
  "effectiveFrom": "2024-12-01",
  "standardTimeMinutes": 45,
  "setupTimeMinutes": 10,
  "details": [
    {
      "itemType": "RAW_MATERIAL",
      "itemId": 5,
      "sequenceNo": 1,
      "quantity": 1,
      "unitId": 1,
      "wastagePercent": 2.00,
      "isCritical": true,
      "notes": "Main microcontroller"
    },
    {
      "itemType": "RAW_MATERIAL",
      "itemId": 6,
      "sequenceNo": 2,
      "quantity": 1,
      "unitId": 1,
      "wastagePercent": 1.00,
      "isCritical": true,
      "notes": "Display module"
    }
  ]
}
```

### 8.2 Work Orders

#### Get All Work Orders
**Endpoint:** `GET /v1/work-orders`

**Query Parameters:**
- `status`: DRAFT, PLANNED, RELEASED, IN_PROGRESS, COMPLETED, CANCELLED
- `finishedGoodsId`: Filter by product
- `fromDate`, `toDate`: Date range

#### Create Work Order
**Endpoint:** `POST /v1/work-orders`

```json
{
  "bomId": 1,
  "plannedQuantity": 100,
  "plannedStartDate": "2024-12-10",
  "plannedEndDate": "2024-12-15",
  "warehouseId": 1,
  "priority": "HIGH",
  "notes": "Urgent production batch"
}
```

#### Release Work Order
**Endpoint:** `PUT /v1/work-orders/{id}/release`

#### Start Production
**Endpoint:** `PUT /v1/work-orders/{id}/start`

```json
{
  "actualStartDate": "2024-12-10T09:00:00"
}
```

#### Record Production Output
**Endpoint:** `POST /v1/work-orders/{id}/output`

```json
{
  "producedQuantity": 50,
  "goodQuantity": 48,
  "rejectedQuantity": 2,
  "batchNo": "SCU-A1-2024-002",
  "locationId": 9,
  "notes": "First batch completed"
}
```

#### Record Material Consumption
**Endpoint:** `POST /v1/work-orders/{id}/consumption`

```json
{
  "rawMaterialId": 5,
  "quantity": 52,
  "stockId": 5,
  "notes": "Consumed for first batch"
}
```

#### Complete Work Order
**Endpoint:** `PUT /v1/work-orders/{id}/complete`

```json
{
  "actualEndDate": "2024-12-14T17:00:00",
  "remarks": "Completed ahead of schedule"
}
```

---

## 9. Reports & Analytics

### 9.1 Inventory Reports

#### Stock Summary
**Endpoint:** `GET /v1/reports/stock-summary`

**Query Parameters:**
- `warehouseId`: Filter by warehouse
- `categoryId`: Filter by category
- `type`: RAW_MATERIAL or FINISHED_GOODS

#### Low Stock Report
**Endpoint:** `GET /v1/reports/low-stock`

#### Stock Valuation
**Endpoint:** `GET /v1/reports/stock-valuation`

### 9.2 Sales Reports

#### Sales Summary
**Endpoint:** `GET /v1/reports/sales-summary`

**Query Parameters:**
- `fromDate`, `toDate`: Date range
- `customerId`: Filter by customer

#### Top Selling Products
**Endpoint:** `GET /v1/reports/top-products`

### 9.3 Purchase Reports

#### Purchase Summary
**Endpoint:** `GET /v1/reports/purchase-summary`

#### Supplier Performance
**Endpoint:** `GET /v1/reports/supplier-performance`

### 9.4 Production Reports

#### Production Summary
**Endpoint:** `GET /v1/reports/production-summary`

#### Production Efficiency
**Endpoint:** `GET /v1/reports/production-efficiency`

---

## 10. API Reference

### 10.1 Common Response Formats

#### Success Response
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful"
}
```

#### Error Response
```json
{
  "success": false,
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "User not found with id: 100",
    "timestamp": "2024-12-03T10:30:00",
    "path": "/api/v1/users/100"
  }
}
```

#### Paginated Response
```json
{
  "content": [ ... ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8
  }
}
```

### 10.2 HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 422 | Validation Error |
| 500 | Internal Server Error |

### 10.3 Common Query Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| page | Page number (0-indexed) | page=0 |
| size | Items per page | size=20 |
| sort | Sort field,direction | sort=createdAt,desc |
| search | Search term | search=john |

### 10.4 Date Formats

- Date: `YYYY-MM-DD` (e.g., `2024-12-03`)
- DateTime: `YYYY-MM-DDTHH:mm:ss` (e.g., `2024-12-03T10:30:00`)

---

## Quick Reference Card

### Authentication
```
POST /v1/auth/register    - Register new user
POST /v1/auth/login       - Login
POST /v1/auth/refresh     - Refresh token
POST /v1/auth/logout      - Logout
```

### Users
```
GET    /v1/users          - List users
GET    /v1/users/{id}     - Get user
PUT    /v1/users/{id}     - Update user
DELETE /v1/users/{id}     - Delete user
```

### Inventory
```
GET/POST /v1/raw-materials      - Raw materials
GET/POST /v1/finished-goods     - Finished goods
GET/POST /v1/stock/adjustments  - Stock adjustments
```

### Purchases
```
GET/POST /v1/suppliers        - Suppliers
GET/POST /v1/purchase-orders  - Purchase orders
GET/POST /v1/grn              - Goods receipt notes
```

### Sales
```
GET/POST /v1/customers         - Customers
GET/POST /v1/sales-orders      - Sales orders
GET/POST /v1/delivery-challans - Delivery challans
GET/POST /v1/invoices          - Invoices
GET/POST /v1/payments          - Payments
```

### Production
```
GET/POST /v1/bom          - Bill of materials
GET/POST /v1/work-orders  - Work orders
```

---

## Support

For technical support or questions:
- Email: support@erp-system.com
- Documentation: http://localhost:8080/api/swagger-ui.html

---

*Last Updated: December 2024*
*Version: 1.0.0*

