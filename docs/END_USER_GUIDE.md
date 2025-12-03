# Manufacturing ERP System
# End User Guide

---

## Welcome to Manufacturing ERP

This guide will help you understand how to use the Manufacturing ERP system based on your role. The system helps manage the complete manufacturing lifecycle from raw material procurement to finished goods delivery.

---

# Table of Contents

1. [Getting Started](#1-getting-started)
2. [Understanding Your Role](#2-understanding-your-role)
3. [Admin User Guide](#3-admin-user-guide)
4. [Manager User Guide](#4-manager-user-guide)
5. [Supervisor User Guide](#5-supervisor-user-guide)
6. [Operator User Guide](#6-operator-user-guide)
7. [Viewer User Guide](#7-viewer-user-guide)
8. [Common Workflows](#8-common-workflows)
9. [Frequently Asked Questions](#9-frequently-asked-questions)

---

# 1. Getting Started

## 1.1 How to Login

1. Open your web browser
2. Go to the ERP application URL (provided by your IT team)
3. Enter your **Username** or **Email**
4. Enter your **Password**
5. Click **Login**

![Login Screen](images/login.png)

## 1.2 First Time Login

If this is your first time logging in:
1. Use the credentials provided by your Administrator
2. You will be prompted to change your password
3. Choose a strong password (minimum 8 characters, include numbers and special characters)

## 1.3 Forgot Password

1. Click "Forgot Password" on the login page
2. Enter your registered email
3. Check your email for reset instructions
4. Follow the link to create a new password

## 1.4 Dashboard Overview

After login, you'll see your personalized dashboard showing:
- **Quick Stats**: Key metrics relevant to your role
- **Recent Activities**: Your recent actions
- **Alerts & Notifications**: Items requiring attention
- **Quick Actions**: Shortcuts to common tasks

---

# 2. Understanding Your Role

## 2.1 Role Hierarchy

```
┌─────────────────────────────────────────────────────────┐
│                        ADMIN                             │
│              (Full System Access)                        │
├─────────────────────────────────────────────────────────┤
│                       MANAGER                            │
│            (Approvals + Reports + Operations)            │
├─────────────────────────────────────────────────────────┤
│                      SUPERVISOR                          │
│              (Operations + Team Management)              │
├─────────────────────────────────────────────────────────┤
│                       OPERATOR                           │
│                 (Day-to-day Operations)                  │
├─────────────────────────────────────────────────────────┤
│                        VIEWER                            │
│                   (View Only Access)                     │
└─────────────────────────────────────────────────────────┘
```

## 2.2 What Each Role Can Do

| Feature | Admin | Manager | Supervisor | Operator | Viewer |
|---------|:-----:|:-------:|:----------:|:--------:|:------:|
| **User Management** |
| Create/Edit Users | ✅ | ❌ | ❌ | ❌ | ❌ |
| View Users | ✅ | ✅ | ✅ | ❌ | ✅ |
| Assign Roles | ✅ | ❌ | ❌ | ❌ | ❌ |
| **Master Data** |
| Create Categories | ✅ | ✅ | ❌ | ❌ | ❌ |
| Manage Warehouses | ✅ | ✅ | ✅ | ❌ | ❌ |
| **Inventory** |
| View Stock | ✅ | ✅ | ✅ | ✅ | ✅ |
| Add/Edit Materials | ✅ | ✅ | ✅ | ❌ | ❌ |
| Stock Adjustments | ✅ | ✅ | ✅ | ❌ | ❌ |
| **Purchases** |
| Create PO | ✅ | ✅ | ✅ | ❌ | ❌ |
| Approve PO | ✅ | ✅ | ❌ | ❌ | ❌ |
| Receive Goods (GRN) | ✅ | ✅ | ✅ | ❌ | ❌ |
| **Sales** |
| Create Sales Order | ✅ | ✅ | ✅ | ❌ | ❌ |
| Approve Sales Order | ✅ | ✅ | ❌ | ❌ | ❌ |
| Create Invoice | ✅ | ✅ | ✅ | ❌ | ❌ |
| Record Payment | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Production** |
| Create Work Order | ✅ | ✅ | ✅ | ❌ | ❌ |
| Execute Production | ✅ | ✅ | ✅ | ✅ | ❌ |
| Record Output | ✅ | ✅ | ✅ | ✅ | ❌ |
| **Reports** |
| View Reports | ✅ | ✅ | ✅ | ❌ | ✅ |
| Export Reports | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Settings** |
| System Settings | ✅ | ❌ | ❌ | ❌ | ❌ |
| Manage Permissions | ✅ | ❌ | ❌ | ❌ | ❌ |

---

# 3. Admin User Guide

As an **Administrator**, you have complete control over the system. Your responsibilities include:

## 3.1 User Management

### Creating a New User

1. Go to **Settings** → **User Management**
2. Click **+ Add New User**
3. Fill in the user details:
   - Username (unique)
   - Email address
   - First Name, Last Name
   - Phone number
   - Select Role (Admin/Manager/Supervisor/Operator/Viewer)
4. Click **Save**
5. The system will send login credentials to the user's email

### Editing User Details

1. Go to **Settings** → **User Management**
2. Find the user in the list
3. Click the **Edit** icon (✏️)
4. Update the required fields
5. Click **Save Changes**

### Deactivating a User

1. Go to **Settings** → **User Management**
2. Find the user
3. Toggle the **Active** switch to OFF
4. Confirm the action

> ⚠️ **Note**: Deactivated users cannot login but their data is preserved.

### Resetting User Password

1. Go to **Settings** → **User Management**
2. Find the user
3. Click **More Options** (⋮) → **Reset Password**
4. New password will be sent to user's email

## 3.2 System Configuration

### Managing Categories

1. Go to **Settings** → **Categories**
2. View existing categories in tree structure
3. To add new category:
   - Click **+ Add Category**
   - Enter name and code
   - Select type (Raw Material / Finished Good / In-Process)
   - Select parent category (if subcategory)
   - Click **Save**

### Managing Units of Measurement

1. Go to **Settings** → **Units**
2. Add standard units:
   - Name (e.g., Kilogram)
   - Symbol (e.g., Kg)
   - Type (Weight/Quantity/Length/Volume)
   - Conversion factor (for derived units)

### Warehouse Setup

1. Go to **Settings** → **Warehouses**
2. Click **+ Add Warehouse**
3. Enter warehouse details:
   - Name and Code
   - Address
   - Contact person
4. After saving, add **Locations** within the warehouse:
   - Zone, Rack, Shelf, Bin
   - Capacity

## 3.3 Daily Admin Tasks

| Time | Task | Where |
|------|------|-------|
| Morning | Check system health | Dashboard → System Status |
| Morning | Review pending approvals | Dashboard → Pending Items |
| As needed | User access requests | Settings → User Management |
| Weekly | Review audit logs | Reports → Audit Trail |
| Monthly | Backup verification | Settings → Backups |

---

# 4. Manager User Guide

As a **Manager**, you are responsible for approvals, oversight, and reporting.

## 4.1 Approval Workflows

### Approving Purchase Orders

1. Go to **Purchases** → **Pending Approvals**
2. Click on a Purchase Order to review
3. Check the details:
   - Supplier information
   - Items and quantities
   - Prices and totals
   - Delivery date
4. Choose action:
   - **Approve**: Order proceeds to supplier
   - **Reject**: Order returned to creator with comments
   - **Request Changes**: Send back for modifications

### Approving Sales Orders

1. Go to **Sales** → **Pending Approvals**
2. Review the sales order:
   - Customer credit status
   - Stock availability
   - Pricing and discounts
   - Delivery feasibility
3. Approve or reject with comments

## 4.2 Supplier Management

### Adding a New Supplier

1. Go to **Purchases** → **Suppliers**
2. Click **+ Add Supplier**
3. Enter supplier information:
   ```
   Basic Info:
   - Company Name
   - Contact Person
   - Phone, Email
   
   Address:
   - Street Address
   - City, State, PIN
   
   Tax & Banking:
   - GST Number
   - PAN Number
   - Bank Details
   
   Terms:
   - Payment Terms (days)
   - Credit Limit
   ```
4. Click **Save**

### Evaluating Supplier Performance

1. Go to **Reports** → **Supplier Performance**
2. Select date range
3. Review metrics:
   - On-time delivery rate
   - Quality acceptance rate
   - Price competitiveness
   - Response time

## 4.3 Customer Management

### Adding a New Customer

1. Go to **Sales** → **Customers**
2. Click **+ Add Customer**
3. Fill customer details:
   ```
   Basic Info:
   - Company Name
   - Customer Type (Retail/Distributor/Corporate)
   - Contact Person, Phone, Email
   
   Billing Address:
   - Street, City, State, PIN
   
   Shipping Address:
   - Same as billing or different
   
   Tax Info:
   - GST Number
   - PAN Number
   
   Credit Terms:
   - Credit Limit
   - Payment Terms
   - Default Discount %
   ```

### Reviewing Customer Credit

1. Go to **Sales** → **Customers**
2. Click on customer name
3. View **Credit Summary**:
   - Credit Limit
   - Current Outstanding
   - Available Credit
   - Payment History

## 4.4 Reports & Analytics

### Generating Sales Report

1. Go to **Reports** → **Sales Reports**
2. Select report type:
   - Sales Summary
   - Sales by Customer
   - Sales by Product
   - Sales Trend
3. Set date range
4. Click **Generate**
5. Export to Excel/PDF if needed

### Inventory Valuation Report

1. Go to **Reports** → **Inventory Reports**
2. Select **Stock Valuation**
3. Choose warehouse (or All)
4. View:
   - Total stock value
   - Breakdown by category
   - Slow-moving items
   - Below reorder level items

## 4.5 Daily Manager Tasks

| Time | Task | Priority |
|------|------|----------|
| 9:00 AM | Review overnight orders | High |
| 9:30 AM | Check pending approvals | High |
| 10:00 AM | Review stock alerts | Medium |
| 11:00 AM | Customer follow-ups | Medium |
| 2:00 PM | Production status review | Medium |
| 4:00 PM | Approve/review POs | High |
| 5:00 PM | End of day reports | Low |

---

# 5. Supervisor User Guide

As a **Supervisor**, you manage day-to-day operations and your team.

## 5.1 Inventory Management

### Checking Stock Levels

1. Go to **Inventory** → **Stock Overview**
2. Use filters:
   - Warehouse
   - Category
   - Material type
3. Color indicators:
   - 🟢 **Green**: Adequate stock
   - 🟡 **Yellow**: Below reorder level
   - 🔴 **Red**: Critical/Out of stock

### Creating Stock Adjustment

When physical count differs from system:

1. Go to **Inventory** → **Stock Adjustments**
2. Click **+ New Adjustment**
3. Select:
   - Item (Raw Material or Finished Good)
   - Warehouse and Location
   - Adjustment Type:
     - **Add**: Stock found more than system
     - **Remove**: Stock found less than system
4. Enter quantity and reason
5. Submit for record

### Receiving Raw Materials (GRN)

1. Go to **Purchases** → **Goods Receipt**
2. Click **+ New GRN**
3. Select Purchase Order
4. For each item received:
   - Enter received quantity
   - Enter accepted quantity
   - Enter rejected quantity (if any)
   - Specify rejection reason
   - Assign storage location
   - Enter batch number (if applicable)
5. Click **Complete Receipt**

## 5.2 Purchase Operations

### Creating Purchase Order

1. Go to **Purchases** → **Purchase Orders**
2. Click **+ New PO**
3. Select Supplier
4. Add items:
   - Search and select raw material
   - Enter quantity
   - Verify/modify unit price
   - System calculates taxes
5. Set expected delivery date
6. Add notes if needed
7. Click **Save as Draft** or **Submit for Approval**

### Tracking Purchase Orders

1. Go to **Purchases** → **Purchase Orders**
2. View order statuses:
   - **Draft**: Not yet submitted
   - **Pending Approval**: Waiting for manager
   - **Approved**: Ready to send
   - **Sent**: Sent to supplier
   - **Partially Received**: Some items received
   - **Received**: All items received
   - **Cancelled**: Order cancelled

## 5.3 Sales Operations

### Creating Sales Order

1. Go to **Sales** → **Sales Orders**
2. Click **+ New Order**
3. Select Customer
4. Check customer credit status
5. Add items:
   - Search finished goods
   - Enter quantity
   - Apply discounts if applicable
6. Verify stock availability
7. Set delivery date
8. Submit for approval

### Creating Delivery Challan

After sales order is confirmed:

1. Go to **Sales** → **Delivery Challans**
2. Click **+ New Challan**
3. Select Sales Order
4. Enter dispatch details:
   - Vehicle number
   - Driver name and phone
5. Select items to dispatch
6. Specify stock location for each item
7. Print challan for delivery

### Creating Invoice

1. Go to **Sales** → **Invoices**
2. Click **+ New Invoice**
3. Select Sales Order and Delivery Challan
4. Verify amounts
5. Set due date
6. Generate invoice number
7. Print/Email to customer

## 5.4 Production Supervision

### Monitoring Work Orders

1. Go to **Production** → **Work Orders**
2. View active orders:
   - Planned vs Actual progress
   - Material consumption
   - Output achieved
3. Click on order for details

### Recording Production Output

1. Open active Work Order
2. Go to **Output** tab
3. Click **+ Record Output**
4. Enter:
   - Produced quantity
   - Good quantity
   - Rejected quantity
   - Batch number
   - Storage location
5. Save entry

## 5.5 Daily Supervisor Tasks

| Time | Task | Action |
|------|------|--------|
| 8:00 AM | Review production schedule | Check Work Orders |
| 8:30 AM | Check material availability | Verify stock for production |
| 9:00 AM | Assign tasks to operators | Brief team |
| 12:00 PM | Mid-day production check | Update progress |
| 3:00 PM | Check incoming materials | Process GRNs |
| 4:00 PM | Review dispatch schedule | Prepare challans |
| 5:30 PM | End of day reporting | Update all records |

---

# 6. Operator User Guide

As an **Operator**, you perform the hands-on production work.

## 6.1 Your Dashboard

When you login, you'll see:
- **Today's Tasks**: Work orders assigned to you
- **Current Production**: Order you're working on
- **Quick Entry**: Shortcuts to record output

## 6.2 Viewing Your Work Orders

1. Go to **Production** → **My Tasks**
2. See list of assigned work orders
3. Each order shows:
   - Product to manufacture
   - Target quantity
   - Due date
   - Current status

## 6.3 Starting Production

1. Select a Work Order from your tasks
2. Click **Start Production**
3. System records start time
4. View required materials list
5. Collect materials from designated location
6. Begin production process

## 6.4 Recording Output

As you complete production batches:

1. On the Work Order screen, click **Record Output**
2. Enter:
   ```
   Produced Quantity: [How many you made]
   Good Quantity: [How many passed quality]
   Rejected: [How many failed quality]
   ```
3. Select rejection reasons if applicable
4. Enter batch number (if required)
5. Click **Save**

## 6.5 Recording Material Usage

If you use materials:

1. On Work Order screen, go to **Consumption** tab
2. Click **+ Record Consumption**
3. Select material
4. Enter quantity used
5. Save entry

## 6.6 Completing Your Task

When production is complete:

1. Ensure all output is recorded
2. Ensure all consumption is recorded
3. Click **Mark Complete**
4. Add any notes about the production
5. Task moves off your list

## 6.7 Quick Tips for Operators

| Do's ✅ | Don'ts ❌ |
|---------|----------|
| Record output immediately | Wait until end of day |
| Note any quality issues | Ignore defects |
| Report material shortages | Use materials without recording |
| Update progress regularly | Leave work orders hanging |
| Ask supervisor if unsure | Guess and proceed |

---

# 7. Viewer User Guide

As a **Viewer**, you have read-only access to monitor operations.

## 7.1 What You Can See

- Dashboard with key metrics
- Inventory levels
- Order statuses (Purchase & Sales)
- Production progress
- Reports

## 7.2 Dashboard Overview

Your dashboard shows:
- **Stock Summary**: Current inventory levels
- **Order Status**: Open orders count
- **Production Status**: Active work orders
- **Recent Activities**: Latest transactions

## 7.3 Viewing Inventory

1. Go to **Inventory** menu
2. Browse:
   - Raw Materials list
   - Finished Goods list
   - Stock levels by warehouse

## 7.4 Viewing Orders

1. **Purchases** → View all purchase orders
2. **Sales** → View all sales orders
3. Use filters to find specific orders
4. Click any order to see details

## 7.5 Viewing Reports

1. Go to **Reports** menu
2. Select report category
3. Set parameters (date range, filters)
4. View on screen
5. Note: You cannot export reports

## 7.6 Limitations

As a Viewer, you **cannot**:
- Create or edit any records
- Approve anything
- Delete data
- Export reports
- Change settings

If you need to perform actions, contact your Administrator for role upgrade.

---

# 8. Common Workflows

## 8.1 Complete Purchase Cycle

```
┌─────────────────┐
│ 1. Check Stock  │ (Supervisor sees low stock alert)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 2. Create PO    │ (Supervisor creates Purchase Order)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 3. Approve PO   │ (Manager reviews and approves)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 4. Send to      │ (System sends PO to supplier)
│    Supplier     │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 5. Receive      │ (Supervisor creates GRN when
│    Goods        │  materials arrive)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 6. Stock        │ (System updates inventory)
│    Updated      │
└─────────────────┘
```

## 8.2 Complete Sales Cycle

```
┌─────────────────┐
│ 1. Customer     │ (Customer places order)
│    Order        │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 2. Create SO    │ (Supervisor creates Sales Order)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 3. Approve SO   │ (Manager verifies credit & approves)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 4. Check Stock  │ (Verify finished goods available)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 5. Create       │ (Prepare for dispatch)
│    Challan      │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 6. Dispatch     │ (Ship to customer)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 7. Create       │ (Generate invoice)
│    Invoice      │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 8. Receive      │ (Record customer payment)
│    Payment      │
└─────────────────┘
```

## 8.3 Production Cycle

```
┌─────────────────┐
│ 1. Sales Order  │ (Demand created from sales)
│    Received     │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 2. Check Raw    │ (Verify materials available)
│    Materials    │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 3. Create Work  │ (Supervisor creates production order)
│    Order        │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 4. Release to   │ (Work order assigned to floor)
│    Production   │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 5. Consume      │ (Operator takes materials)
│    Materials    │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 6. Production   │ (Manufacturing process)
│    Process      │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 7. Record       │ (Operator records finished goods)
│    Output       │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 8. Quality      │ (QC inspection)
│    Check        │
└────────┬────────┘
         ▼
┌─────────────────┐
│ 9. Store        │ (Move to finished goods warehouse)
│    Finished     │
│    Goods        │
└─────────────────┘
```

## 8.4 End of Day Checklist

### For Supervisors

- [ ] All GRNs processed
- [ ] All delivery challans created
- [ ] Production output recorded
- [ ] Low stock items flagged
- [ ] Tomorrow's work orders reviewed
- [ ] Pending issues escalated

### For Operators

- [ ] All output recorded
- [ ] All consumption recorded
- [ ] Work area cleaned
- [ ] Tools returned
- [ ] Issues reported

---

# 9. Frequently Asked Questions

## General Questions

**Q: I forgot my password. What do I do?**
> Click "Forgot Password" on login screen, or contact your Admin.

**Q: Why can't I see certain menu items?**
> Your role determines what you can access. Contact Admin if you need additional access.

**Q: How do I change my password?**
> Go to Profile (top right) → Change Password

**Q: Why is my account locked?**
> After 5 wrong password attempts, account locks for 30 minutes. Contact Admin for immediate unlock.

## Inventory Questions

**Q: Stock shows available but I can't find it?**
> Check the warehouse location in system. Physical stock may be misplaced. Report to supervisor.

**Q: How do I correct wrong stock?**
> Use Stock Adjustment feature. Select reason code and explain in notes.

**Q: What does "Reserved" stock mean?**
> Stock allocated to confirmed sales orders but not yet shipped.

## Purchase Questions

**Q: My PO is stuck in "Pending Approval"?**
> The assigned approver hasn't reviewed it. Contact your manager.

**Q: Received goods don't match PO quantity?**
> Create GRN with actual received quantity. System tracks partial receipts.

**Q: How do I return goods to supplier?**
> Create a Return request from the GRN. Mark items for return with reason.

## Sales Questions

**Q: Customer wants to modify confirmed order?**
> You may need to cancel and recreate. Contact manager for order modifications.

**Q: Why can't I create SO for this customer?**
> Check if customer is active and has available credit.

**Q: How to handle partial delivery?**
> Create multiple delivery challans against same SO for each shipment.

## Production Questions

**Q: Raw material short for production?**
> Report to supervisor immediately. Don't proceed with insufficient materials.

**Q: Produced quantity less than planned?**
> Record actual output. Note the reason in remarks.

**Q: Quality rejection is high?**
> Record all rejections accurately. Supervisor will investigate root cause.

---

# Quick Reference Cards

## Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl + N` | New (context-based) |
| `Ctrl + S` | Save |
| `Ctrl + P` | Print |
| `Ctrl + F` | Search |
| `Esc` | Cancel / Close |
| `F5` | Refresh |

## Status Color Codes

| Color | Meaning |
|-------|---------|
| 🟢 Green | Active / Completed / OK |
| 🟡 Yellow | Warning / Pending / In Progress |
| 🔴 Red | Critical / Overdue / Error |
| 🔵 Blue | Information / New |
| ⚪ Gray | Inactive / Cancelled |

## Contact Support

For technical issues:
- **IT Helpdesk**: helpdesk@company.com
- **Phone**: 1800-XXX-XXXX
- **Hours**: Monday-Saturday, 9 AM - 6 PM

For training requests:
- Contact your Department Manager

---

*Document Version: 1.0*
*Last Updated: December 2024*
*For Manufacturing ERP System v1.0.0*

