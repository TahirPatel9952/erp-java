# Manufacturing ERP - Application Workflow Guide

## 🏭 System Overview

This Manufacturing ERP manages the complete **manufacturing lifecycle** from raw material procurement to finished goods delivery.

---

## 📊 Module Relationships & Data Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              MASTER DATA (Foundation)                            │
├─────────────────────────────────────────────────────────────────────────────────┤
│  Categories ──┬── Units ──┬── Warehouses ──┬── Suppliers ──┬── Customers        │
│               │           │                │               │                    │
└───────────────┼───────────┼────────────────┼───────────────┼────────────────────┘
                │           │                │               │
                ▼           ▼                ▼               ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              INVENTORY MANAGEMENT                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│  Raw Materials ◄──── Finished Goods ◄──── Stock Management ◄──── Stock Adjust  │
│       │                    │                     │                              │
└───────┼────────────────────┼─────────────────────┼──────────────────────────────┘
        │                    │                     │
        ▼                    ▼                     ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              TRANSACTIONS                                        │
├───────────────────────┬─────────────────────┬───────────────────────────────────┤
│   PURCHASE FLOW       │   PRODUCTION FLOW   │      SALES FLOW                   │
│   ─────────────       │   ───────────────   │      ──────────                   │
│   Purchase Order      │   BOM (Recipe)      │      Sales Order                  │
│        ↓              │        ↓            │           ↓                       │
│   GRN (Goods Receipt) │   Work Order        │      Delivery Challan             │
│        ↓              │        ↓            │           ↓                       │
│   Supplier Invoice    │   Production        │      Invoice                      │
│        ↓              │        ↓            │           ↓                       │
│   Payment             │   Quality Check     │      Payment Receipt              │
└───────────────────────┴─────────────────────┴───────────────────────────────────┘
```

---

## 🔧 Master Data Modules - Where & Why They're Used

### 1️⃣ Categories

Categories organize your inventory items hierarchically.

| Where Used | Purpose |
|------------|---------|
| **Raw Materials** | Classify raw materials (Metals, Plastics, Chemicals, etc.) |
| **Finished Goods** | Group products (Electronics, Furniture, Packaging, etc.) |
| **Reports** | Filter inventory/sales by category |
| **BOM** | Find materials by category when creating recipes |

**Example Categories:**

```
RAW_MATERIAL
├── Metals (Steel, Aluminum, Copper)
├── Plastics (ABS, PVC, HDPE)
├── Chemicals (Paints, Adhesives)
└── Packaging (Boxes, Tape, Labels)

FINISHED_GOODS
├── Electronics (PCB, Motors)
├── Components (Gears, Bearings)
└── Assemblies (Sub-assemblies)
```

**Category Types in System:**
- `RAW_MATERIAL` - For raw materials/inputs
- `FINISHED_GOODS` - For manufactured products
- `SEMI_FINISHED` - For work-in-progress items
- `CONSUMABLE` - For consumables (oil, grease, etc.)
- `PACKAGING` - For packaging materials

---

### 2️⃣ Units of Measurement

Units define how items are measured, purchased, stored, and sold.

| Where Used | Purpose |
|------------|---------|
| **Raw Materials** | Define purchase unit (kg, meters, liters) |
| **Finished Goods** | Define selling unit (pieces, sets, cartons) |
| **BOM** | Specify quantity per unit in recipe |
| **Purchase Orders** | Quantity in supplier's unit |
| **Sales Orders** | Quantity in customer's unit |
| **Stock** | Inventory tracking |
| **Conversions** | Auto-convert between units (1 kg = 1000 g) |

**Example Units:**

```
WEIGHT: kg (base) → g, ton, lb
LENGTH: m (base) → cm, mm, inch, feet
VOLUME: L (base) → mL, gallon
QUANTITY: pcs (base) → dozen, box, carton
```

**Unit Types in System:**
- `WEIGHT` - kg, g, ton, lb, mg
- `LENGTH` - m, cm, mm, inch, feet
- `VOLUME` - L, mL, gallon
- `QUANTITY` - pcs, dozen, box, carton, set
- `AREA` - sqm, sqft
- `TIME` - hours, minutes, days

**Unit Conversion Example:**

```
You buy Steel in TONS from supplier
You consume Steel in KG in production
System auto-converts: 1 Ton = 1000 kg

Conversion Factor: How many BASE UNITS = 1 of THIS UNIT
- kg (base): conversion_factor = 1
- g: conversion_factor = 0.001 (0.001 kg = 1 g)
- ton: conversion_factor = 1000 (1000 kg = 1 ton)
```

---

### 3️⃣ Warehouses

Physical or logical storage locations for inventory.

| Where Used | Purpose |
|------------|---------|
| **Stock Management** | Track inventory per warehouse |
| **Purchase/GRN** | Receive goods into specific warehouse |
| **Sales/Dispatch** | Ship goods from specific warehouse |
| **Stock Transfer** | Move stock between warehouses |
| **Production** | Consume from / produce to warehouse |
| **Reports** | Warehouse-wise stock reports |

**Example Warehouse Setup:**

```
Main Warehouse (WH-001)
├── Raw Material Store
├── Finished Goods Store
└── Dispatch Area

Factory Warehouse (WH-002)
├── Production Floor
├── Quality Hold Area
└── Reject Store

Branch Warehouse (WH-003)
└── Sales Stock
```

**Warehouse Location Hierarchy:**

```
Warehouse
└── Zone (e.g., Zone-A, Zone-B)
    └── Rack (e.g., Rack-01, Rack-02)
        └── Shelf (e.g., Shelf-1, Shelf-2)
            └── Bin (e.g., Bin-001)
```

---

### 4️⃣ Suppliers

Vendors who supply raw materials and services.

| Where Used | Purpose |
|------------|---------|
| **Raw Materials** | Link preferred supplier to materials |
| **Purchase Orders** | Select supplier for ordering |
| **GRN** | Receive goods from supplier |
| **Supplier Invoice** | Record bills from supplier |
| **Payments** | Track supplier payments & balance |
| **Reports** | Supplier-wise purchase analysis |

**Supplier Information Tracked:**
- Basic Info: Name, Code, Contact Person, Phone, Email
- Address: Full address with city, state, pincode
- Tax Info: GST Number, PAN Number
- Bank Details: Bank name, Account number, IFSC
- Terms: Payment terms (days), Credit limit
- Rating: Supplier performance rating

---

### 5️⃣ Customers

Buyers of your finished goods.

| Where Used | Purpose |
|------------|---------|
| **Sales Orders** | Create orders for customers |
| **Delivery Challan** | Dispatch goods to customer |
| **Invoices** | Bill the customer |
| **Payment Receipts** | Record customer payments |
| **Credit Management** | Track credit limit & outstanding |
| **Reports** | Customer-wise sales analysis |

**Customer Types:**
- `REGULAR` - Standard customers
- `DISTRIBUTOR` - Wholesale distributors
- `CORPORATE` - B2B corporate clients
- `RETAIL` - Direct retail customers

**Customer Information Tracked:**
- Basic Info: Name, Code, Type, Contact Person
- Billing Address: Full billing address
- Shipping Address: Delivery address (can differ from billing)
- Tax Info: GST Number, PAN Number
- Credit Terms: Credit limit, Payment terms, Discount %

---

## 📦 Inventory Modules

### 6️⃣ Raw Materials

Items purchased from suppliers and used in manufacturing.

**Attributes:**
- Code, Name, Description
- Category (from Categories)
- Unit of Measurement (from Units)
- HSN Code (for GST)
- Unit Price, Tax %
- Reorder Level, Reorder Quantity
- Lead Time (days)
- Preferred Supplier

**Stock Tracking:**
- Current stock per warehouse
- Batch-wise tracking (optional)
- Low stock alerts when below reorder level

---

### 7️⃣ Finished Goods

Products manufactured and sold to customers.

**Attributes:**
- Code, Name, Description, Barcode
- Category (from Categories)
- Unit of Measurement (from Units)
- HSN Code (for GST)
- Selling Price, MRP, Minimum Selling Price
- Standard Cost (manufacturing cost)
- Tax %
- Reorder Level
- Shelf Life (days)
- Weight, Dimensions
- Batch Tracking (enabled/disabled)

---

## 🔄 Complete Business Workflows

### Workflow 1: Purchase to Payment (P2P)

```
┌─────────────────────────────────────────────────────────────────┐
│  1. IDENTIFY NEED                                               │
│     └── Low stock alert OR Manual requisition                   │
│                         ↓                                       │
│  2. CREATE PURCHASE ORDER                                       │
│     └── Select Supplier → Add Raw Materials                     │
│     └── Uses: Suppliers, Raw Materials, Units, Categories       │
│     └── Status: DRAFT → APPROVED → SENT                         │
│                         ↓                                       │
│  3. RECEIVE GOODS (GRN - Goods Receipt Note)                    │
│     └── Verify quantity/quality → Accept/Reject                 │
│     └── Uses: Warehouses (where to store)                       │
│     └── Stock increases in selected warehouse                   │
│     └── Status: PENDING → RECEIVED → INSPECTED                  │
│                         ↓                                       │
│  4. SUPPLIER INVOICE                                            │
│     └── Match with PO/GRN → Approve                             │
│     └── Status: DRAFT → APPROVED → POSTED                       │
│                         ↓                                       │
│  5. PAYMENT                                                     │
│     └── Full/Partial payment → Update supplier balance          │
│     └── Payment Methods: Cash, Bank Transfer, Cheque            │
└─────────────────────────────────────────────────────────────────┘
```

**Purchase Order Statuses:**
- `DRAFT` - Created, not yet approved
- `PENDING_APPROVAL` - Waiting for manager approval
- `APPROVED` - Approved, ready to send
- `SENT` - Sent to supplier
- `PARTIALLY_RECEIVED` - Some items received
- `RECEIVED` - All items received
- `CANCELLED` - Order cancelled

---

### Workflow 2: Production (Manufacturing)

```
┌─────────────────────────────────────────────────────────────────┐
│  1. CREATE BOM (Bill of Materials)                              │
│     └── Define recipe: What raw materials make 1 finished good  │
│     └── Uses: Raw Materials, Finished Goods, Units, Categories  │
│     └── Example: 1 Chair = 4 kg Wood + 0.5 kg Nails + 1 L Paint │
│                         ↓                                       │
│  2. CREATE WORK ORDER                                           │
│     └── How many to produce? → System calculates material need  │
│     └── Uses: BOM, Warehouses (source & target)                 │
│     └── Status: DRAFT → PLANNED → IN_PROGRESS                   │
│                         ↓                                       │
│  3. MATERIAL ISSUE                                              │
│     └── Issue raw materials from warehouse to production floor  │
│     └── Raw Material stock DECREASES                            │
│                         ↓                                       │
│  4. PRODUCTION                                                  │
│     └── Manufacture the goods                                   │
│     └── Track: Start time, End time, Operator                   │
│                         ↓                                       │
│  5. PRODUCTION RECEIPT                                          │
│     └── Finished goods received into warehouse                  │
│     └── Finished Goods stock INCREASES                          │
│                         ↓                                       │
│  6. QUALITY CHECK                                               │
│     └── Pass → Ready for sale | Fail → Reject/Rework            │
│     └── Status: COMPLETED or PARTIALLY_COMPLETED                │
└─────────────────────────────────────────────────────────────────┘
```

**BOM Example:**

| Finished Good | Raw Material | Quantity | Unit |
|---------------|--------------|----------|------|
| Wooden Chair | Teak Wood | 4 | kg |
| Wooden Chair | Nails | 0.5 | kg |
| Wooden Chair | Wood Polish | 0.5 | L |
| Wooden Chair | Fabric | 1 | m |

**Work Order Statuses:**
- `DRAFT` - Created, not started
- `PLANNED` - Scheduled for production
- `RELEASED` - Released to production floor
- `IN_PROGRESS` - Currently being manufactured
- `COMPLETED` - Production finished
- `CANCELLED` - Order cancelled

---

### Workflow 3: Order to Cash (O2C)

```
┌─────────────────────────────────────────────────────────────────┐
│  1. CREATE SALES ORDER                                          │
│     └── Select Customer → Add Finished Goods → Prices auto-fill │
│     └── Uses: Customers, Finished Goods, Units, Categories      │
│     └── Check: Credit limit, Stock availability                 │
│     └── Status: DRAFT → CONFIRMED → PROCESSING                  │
│                         ↓                                       │
│  2. DELIVERY CHALLAN                                            │
│     └── Dispatch goods from warehouse                           │
│     └── Uses: Warehouses (dispatch from)                        │
│     └── Finished Goods stock DECREASES                          │
│     └── Status: DRAFT → DISPATCHED → DELIVERED                  │
│                         ↓                                       │
│  3. CREATE INVOICE                                              │
│     └── Generate tax invoice with GST                           │
│     └── Customer outstanding INCREASES                          │
│     └── Status: DRAFT → APPROVED → SENT                         │
│                         ↓                                       │
│  4. RECEIVE PAYMENT                                             │
│     └── Full/Partial/Advance payment                            │
│     └── Customer outstanding DECREASES                          │
│     └── Payment Methods: Cash, Bank, Cheque, Online             │
└─────────────────────────────────────────────────────────────────┘
```

**Sales Order Statuses:**
- `DRAFT` - Created, not confirmed
- `CONFIRMED` - Customer confirmed
- `PROCESSING` - Being prepared
- `PARTIALLY_SHIPPED` - Some items dispatched
- `SHIPPED` - All items dispatched
- `DELIVERED` - Customer received
- `CANCELLED` - Order cancelled

---

## 📈 Reports & Analytics

| Report | Uses Data From | Purpose |
|--------|----------------|---------|
| **Stock Report** | Warehouses, Raw Materials, Finished Goods | Current inventory status |
| **Low Stock Alert** | All inventory items with reorder level | Items needing reorder |
| **Stock Valuation** | Stock, Unit prices | Inventory value |
| **Purchase Report** | Suppliers, Purchase Orders, Categories | Purchase analysis |
| **Supplier Ledger** | Suppliers, Invoices, Payments | Supplier account statement |
| **Sales Report** | Customers, Sales Orders, Products | Sales analysis |
| **Customer Ledger** | Customers, Invoices, Receipts | Customer account statement |
| **Production Report** | Work Orders, BOM, Consumption | Manufacturing efficiency |
| **GST Report** | Invoices, Tax rates | Tax filing (GSTR-1, GSTR-3B) |
| **Profit/Loss** | Purchase cost, Selling price | Profitability analysis |

---

## 🎯 Quick Reference: What Links to What

| Entity | Depends On | Used By |
|--------|------------|---------|
| **Category** | - | Raw Materials, Finished Goods |
| **Unit** | Base Unit (optional) | Raw Materials, Finished Goods, BOM |
| **Warehouse** | - | Stock, GRN, Dispatch, Work Orders |
| **Supplier** | - | Raw Materials, Purchase Orders |
| **Customer** | - | Sales Orders, Invoices |
| **Raw Material** | Category, Unit, Supplier | BOM, Purchase Orders, Stock |
| **Finished Goods** | Category, Unit | BOM, Sales Orders, Stock |
| **BOM** | Finished Goods, Raw Materials | Work Orders |
| **Purchase Order** | Supplier, Raw Materials | GRN, Invoices |
| **GRN** | Purchase Order, Warehouse | Stock, Invoices |
| **Work Order** | BOM, Warehouse | Production, Stock |
| **Sales Order** | Customer, Finished Goods | Delivery, Invoices |
| **Delivery Challan** | Sales Order, Warehouse | Stock, Invoices |
| **Invoice** | Delivery/Sales Order, Customer | Payments |

---

## 🚀 Recommended Setup Order

When setting up the system for the first time, follow this sequence:

```
Step 1: Master Data Setup
   │
   ├── 1. Categories      → Foundation for organizing items
   ├── 2. Units           → How items are measured
   ├── 3. Warehouses      → Where items are stored
   ├── 4. Suppliers       → Who you buy from
   └── 5. Customers       → Who you sell to

Step 2: Inventory Setup
   │
   ├── 6. Raw Materials   → What you purchase
   ├── 7. Finished Goods  → What you manufacture & sell
   └── 8. Opening Stock   → Initial stock balances

Step 3: Production Setup
   │
   └── 9. BOM (Recipes)   → Manufacturing recipes

Step 4: User Setup
   │
   └── 10. Users & Roles  → Who can do what
```

---

## 👥 User Roles & Permissions

| Role | Access Level | Key Permissions |
|------|--------------|-----------------|
| **ADMIN** | Full Access | Everything + User Management |
| **MANAGER** | Department Wide | Approve orders, View reports, Manage masters |
| **SUPERVISOR** | Operational | Create orders, Manage production, Update stock |
| **OPERATOR** | Task Based | Update production, Record transactions |
| **VIEWER** | Read Only | View data, Generate reports |

---

## 📱 Mobile App Features

The mobile app supports field operations:

| Feature | Use Case |
|---------|----------|
| **Barcode Scanning** | Quick item lookup, Stock counting |
| **GRN Entry** | Receive goods at warehouse |
| **Stock Check** | Check availability on-the-go |
| **Delivery Updates** | Update dispatch status |
| **Production Updates** | Record production progress |
| **Offline Mode** | Work without internet, sync later |

---

## 🔒 Data Integrity Rules

| Rule | Description |
|------|-------------|
| **Cannot delete used master** | Can't delete a category if items exist |
| **Stock cannot go negative** | System prevents overselling |
| **Credit limit check** | Warns when customer exceeds limit |
| **Audit trail** | All changes are logged with user & timestamp |
| **Soft delete** | Records are deactivated, not deleted |

---

## 📞 Support & Help

- **API Documentation**: `/api/swagger-ui.html`
- **Health Check**: `/api/actuator/health`
- **Application Logs**: `logs/manufacturing-erp.log`

---

*This document provides a comprehensive overview of the Manufacturing ERP workflow. For API-specific documentation, refer to `APPLICATION_USER_GUIDE.md`. For end-user instructions, refer to `END_USER_GUIDE.md`.*

