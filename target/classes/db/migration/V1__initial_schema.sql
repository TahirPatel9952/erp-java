-- Manufacturing ERP Database Schema
-- Version: 1.0.0
-- Description: Initial database schema with all core tables

-- =====================================================
-- USER MANAGEMENT
-- =====================================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    module VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone VARCHAR(20),
    role_id BIGINT REFERENCES roles(id),
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    last_login_at TIMESTAMP,
    password_changed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(500) NOT NULL,
    refresh_token VARCHAR(500),
    expires_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    is_valid BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- MASTER DATA
-- =====================================================

CREATE TABLE units_of_measurement (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    type VARCHAR(30) NOT NULL, -- WEIGHT, LENGTH, VOLUME, QUANTITY, etc.
    base_unit_id BIGINT REFERENCES units_of_measurement(id),
    conversion_factor DECIMAL(15,6) DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE,
    type VARCHAR(30) NOT NULL, -- RAW_MATERIAL, FINISHED_GOOD, IN_PROCESS
    parent_id BIGINT REFERENCES categories(id),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE warehouses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50) DEFAULT 'India',
    pincode VARCHAR(10),
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE warehouse_locations (
    id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id) ON DELETE CASCADE,
    zone VARCHAR(20),
    rack VARCHAR(20),
    shelf VARCHAR(20),
    bin VARCHAR(20),
    location_code VARCHAR(50) NOT NULL,
    capacity DECIMAL(15,3),
    capacity_unit_id BIGINT REFERENCES units_of_measurement(id),
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(warehouse_id, location_code)
);

-- =====================================================
-- SUPPLIERS & CUSTOMERS
-- =====================================================

CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50) DEFAULT 'India',
    pincode VARCHAR(10),
    gst_no VARCHAR(20),
    pan_no VARCHAR(15),
    bank_name VARCHAR(100),
    bank_account_no VARCHAR(30),
    bank_ifsc VARCHAR(15),
    payment_terms INTEGER DEFAULT 30, -- days
    credit_limit DECIMAL(15,2) DEFAULT 0,
    current_balance DECIMAL(15,2) DEFAULT 0,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    customer_type VARCHAR(30) DEFAULT 'REGULAR', -- REGULAR, WHOLESALE, RETAIL, DISTRIBUTOR
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    billing_address TEXT,
    billing_city VARCHAR(50),
    billing_state VARCHAR(50),
    billing_country VARCHAR(50) DEFAULT 'India',
    billing_pincode VARCHAR(10),
    shipping_address TEXT,
    shipping_city VARCHAR(50),
    shipping_state VARCHAR(50),
    shipping_country VARCHAR(50) DEFAULT 'India',
    shipping_pincode VARCHAR(10),
    gst_no VARCHAR(20),
    pan_no VARCHAR(15),
    credit_limit DECIMAL(15,2) DEFAULT 0,
    current_balance DECIMAL(15,2) DEFAULT 0,
    payment_terms INTEGER DEFAULT 30, -- days
    discount_percent DECIMAL(5,2) DEFAULT 0,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- =====================================================
-- INVENTORY - RAW MATERIALS
-- =====================================================

CREATE TABLE raw_materials (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(30) NOT NULL UNIQUE,
    description TEXT,
    category_id BIGINT REFERENCES categories(id),
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    hsn_code VARCHAR(20),
    reorder_level DECIMAL(15,3) DEFAULT 0,
    reorder_quantity DECIMAL(15,3) DEFAULT 0,
    minimum_order_quantity DECIMAL(15,3) DEFAULT 1,
    lead_time_days INTEGER DEFAULT 0,
    standard_cost DECIMAL(15,4) DEFAULT 0,
    last_purchase_price DECIMAL(15,4) DEFAULT 0,
    avg_purchase_price DECIMAL(15,4) DEFAULT 0,
    tax_percent DECIMAL(5,2) DEFAULT 18,
    shelf_life_days INTEGER,
    storage_conditions VARCHAR(255),
    is_batch_tracked BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE raw_material_stock (
    id BIGSERIAL PRIMARY KEY,
    raw_material_id BIGINT NOT NULL REFERENCES raw_materials(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    location_id BIGINT REFERENCES warehouse_locations(id),
    quantity DECIMAL(15,3) NOT NULL DEFAULT 0,
    reserved_quantity DECIMAL(15,3) DEFAULT 0,
    available_quantity DECIMAL(15,3) GENERATED ALWAYS AS (quantity - reserved_quantity) STORED,
    batch_no VARCHAR(50),
    lot_no VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    unit_cost DECIMAL(15,4) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(raw_material_id, warehouse_id, location_id, batch_no)
);

CREATE TABLE raw_material_transactions (
    id BIGSERIAL PRIMARY KEY,
    raw_material_id BIGINT NOT NULL REFERENCES raw_materials(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    transaction_type VARCHAR(30) NOT NULL, -- RECEIPT, ISSUE, ADJUSTMENT, TRANSFER_IN, TRANSFER_OUT, RETURN
    quantity DECIMAL(15,3) NOT NULL,
    unit_cost DECIMAL(15,4),
    total_cost DECIMAL(15,4),
    batch_no VARCHAR(50),
    reference_type VARCHAR(30), -- PURCHASE_ORDER, WORK_ORDER, GRN, ADJUSTMENT
    reference_id BIGINT,
    reference_no VARCHAR(50),
    notes TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INVENTORY - FINISHED GOODS
-- =====================================================

CREATE TABLE finished_goods (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(30) NOT NULL UNIQUE,
    description TEXT,
    category_id BIGINT REFERENCES categories(id),
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    hsn_code VARCHAR(20),
    selling_price DECIMAL(15,4) NOT NULL,
    minimum_selling_price DECIMAL(15,4),
    mrp DECIMAL(15,4),
    standard_cost DECIMAL(15,4) DEFAULT 0,
    reorder_level DECIMAL(15,3) DEFAULT 0,
    tax_percent DECIMAL(5,2) DEFAULT 18,
    shelf_life_days INTEGER,
    weight DECIMAL(10,4),
    weight_unit_id BIGINT REFERENCES units_of_measurement(id),
    dimensions VARCHAR(50),
    barcode VARCHAR(50) UNIQUE,
    image_url VARCHAR(255),
    is_batch_tracked BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE finished_goods_stock (
    id BIGSERIAL PRIMARY KEY,
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    location_id BIGINT REFERENCES warehouse_locations(id),
    quantity DECIMAL(15,3) NOT NULL DEFAULT 0,
    reserved_quantity DECIMAL(15,3) DEFAULT 0,
    available_quantity DECIMAL(15,3) GENERATED ALWAYS AS (quantity - reserved_quantity) STORED,
    batch_no VARCHAR(50),
    lot_no VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    unit_cost DECIMAL(15,4) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(finished_goods_id, warehouse_id, location_id, batch_no)
);

CREATE TABLE finished_goods_transactions (
    id BIGSERIAL PRIMARY KEY,
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    transaction_type VARCHAR(30) NOT NULL, -- PRODUCTION, SALE, ADJUSTMENT, TRANSFER_IN, TRANSFER_OUT, RETURN
    quantity DECIMAL(15,3) NOT NULL,
    unit_cost DECIMAL(15,4),
    total_cost DECIMAL(15,4),
    batch_no VARCHAR(50),
    reference_type VARCHAR(30), -- WORK_ORDER, SALES_ORDER, DELIVERY_CHALLAN, ADJUSTMENT
    reference_id BIGINT,
    reference_no VARCHAR(50),
    notes TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- BILL OF MATERIALS (BOM)
-- =====================================================

CREATE TABLE bom_headers (
    id BIGSERIAL PRIMARY KEY,
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    bom_code VARCHAR(30) NOT NULL UNIQUE,
    version VARCHAR(10) DEFAULT '1.0',
    description TEXT,
    output_quantity DECIMAL(15,3) DEFAULT 1,
    output_unit_id BIGINT REFERENCES units_of_measurement(id),
    effective_from DATE DEFAULT CURRENT_DATE,
    effective_to DATE,
    standard_time_minutes INTEGER DEFAULT 0,
    setup_time_minutes INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE bom_details (
    id BIGSERIAL PRIMARY KEY,
    bom_header_id BIGINT NOT NULL REFERENCES bom_headers(id) ON DELETE CASCADE,
    item_type VARCHAR(20) NOT NULL, -- RAW_MATERIAL, IN_PROCESS, SUB_ASSEMBLY
    item_id BIGINT NOT NULL,
    sequence_no INTEGER DEFAULT 0,
    quantity DECIMAL(15,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    wastage_percent DECIMAL(5,2) DEFAULT 0,
    is_critical BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PRODUCTION / WORK ORDERS
-- =====================================================

CREATE TABLE work_orders (
    id BIGSERIAL PRIMARY KEY,
    work_order_no VARCHAR(30) NOT NULL UNIQUE,
    bom_id BIGINT NOT NULL REFERENCES bom_headers(id),
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    planned_quantity DECIMAL(15,3) NOT NULL,
    completed_quantity DECIMAL(15,3) DEFAULT 0,
    rejected_quantity DECIMAL(15,3) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, PLANNED, RELEASED, IN_PROGRESS, COMPLETED, CANCELLED
    priority VARCHAR(10) DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    scheduled_start_date DATE,
    scheduled_end_date DATE,
    actual_start_date TIMESTAMP,
    actual_end_date TIMESTAMP,
    batch_no VARCHAR(50),
    notes TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE production_stages (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL REFERENCES work_orders(id) ON DELETE CASCADE,
    stage_name VARCHAR(100) NOT NULL,
    sequence_no INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, IN_PROGRESS, COMPLETED, SKIPPED
    planned_start_time TIMESTAMP,
    planned_end_time TIMESTAMP,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    assigned_to BIGINT REFERENCES users(id),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production_consumption (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL REFERENCES work_orders(id) ON DELETE CASCADE,
    item_type VARCHAR(20) NOT NULL, -- RAW_MATERIAL, IN_PROCESS
    item_id BIGINT NOT NULL,
    planned_quantity DECIMAL(15,6) NOT NULL,
    actual_quantity DECIMAL(15,6) DEFAULT 0,
    wastage_quantity DECIMAL(15,6) DEFAULT 0,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    batch_no VARCHAR(50),
    consumed_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INVENTORY - IN-PROCESS MATERIALS (WIP)
-- =====================================================

CREATE TABLE in_process_materials (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(30) NOT NULL UNIQUE,
    description TEXT,
    stage VARCHAR(50),
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    standard_cost DECIMAL(15,4) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE in_process_stock (
    id BIGSERIAL PRIMARY KEY,
    in_process_material_id BIGINT NOT NULL REFERENCES in_process_materials(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    work_order_id BIGINT REFERENCES work_orders(id),
    quantity DECIMAL(15,3) NOT NULL DEFAULT 0,
    stage VARCHAR(50),
    batch_no VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE in_process_transactions (
    id BIGSERIAL PRIMARY KEY,
    in_process_material_id BIGINT NOT NULL REFERENCES in_process_materials(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    work_order_id BIGINT REFERENCES work_orders(id),
    transaction_type VARCHAR(30) NOT NULL, -- CREATED, MOVED, CONSUMED, ADJUSTMENT
    quantity DECIMAL(15,3) NOT NULL,
    from_stage VARCHAR(50),
    to_stage VARCHAR(50),
    notes TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PURCHASE
-- =====================================================

CREATE TABLE purchase_orders (
    id BIGSERIAL PRIMARY KEY,
    po_number VARCHAR(30) NOT NULL UNIQUE,
    supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    expected_date DATE,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, PENDING_APPROVAL, APPROVED, ORDERED, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
    payment_terms VARCHAR(100),
    delivery_terms VARCHAR(100),
    subtotal DECIMAL(15,2) DEFAULT 0,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    discount_amount DECIMAL(15,2) DEFAULT 0,
    tax_amount DECIMAL(15,2) DEFAULT 0,
    shipping_charges DECIMAL(15,2) DEFAULT 0,
    grand_total DECIMAL(15,2) DEFAULT 0,
    notes TEXT,
    internal_notes TEXT,
    approved_by BIGINT REFERENCES users(id),
    approved_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE purchase_order_items (
    id BIGSERIAL PRIMARY KEY,
    po_id BIGINT NOT NULL REFERENCES purchase_orders(id) ON DELETE CASCADE,
    raw_material_id BIGINT NOT NULL REFERENCES raw_materials(id),
    quantity DECIMAL(15,3) NOT NULL,
    received_quantity DECIMAL(15,3) DEFAULT 0,
    pending_quantity DECIMAL(15,3) GENERATED ALWAYS AS (quantity - received_quantity) STORED,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    unit_price DECIMAL(15,4) NOT NULL,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    tax_percent DECIMAL(5,2) DEFAULT 18,
    tax_amount DECIMAL(15,2) DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    expected_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE goods_receipt_notes (
    id BIGSERIAL PRIMARY KEY,
    grn_number VARCHAR(30) NOT NULL UNIQUE,
    po_id BIGINT REFERENCES purchase_orders(id),
    supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    receipt_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, PENDING_QC, QC_PASSED, QC_FAILED, COMPLETED
    vehicle_no VARCHAR(20),
    driver_name VARCHAR(100),
    challan_no VARCHAR(50),
    challan_date DATE,
    notes TEXT,
    qc_notes TEXT,
    qc_by BIGINT REFERENCES users(id),
    qc_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE grn_items (
    id BIGSERIAL PRIMARY KEY,
    grn_id BIGINT NOT NULL REFERENCES goods_receipt_notes(id) ON DELETE CASCADE,
    po_item_id BIGINT REFERENCES purchase_order_items(id),
    raw_material_id BIGINT NOT NULL REFERENCES raw_materials(id),
    ordered_quantity DECIMAL(15,3),
    received_quantity DECIMAL(15,3) NOT NULL,
    accepted_quantity DECIMAL(15,3) DEFAULT 0,
    rejected_quantity DECIMAL(15,3) DEFAULT 0,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    unit_price DECIMAL(15,4),
    batch_no VARCHAR(50),
    lot_no VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    location_id BIGINT REFERENCES warehouse_locations(id),
    qc_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, PASSED, FAILED
    rejection_reason TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- SALES & DELIVERY
-- =====================================================

CREATE TABLE sales_orders (
    id BIGSERIAL PRIMARY KEY,
    so_number VARCHAR(30) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    delivery_date DATE,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, CONFIRMED, PROCESSING, PARTIALLY_DELIVERED, DELIVERED, CANCELLED
    payment_terms VARCHAR(100),
    delivery_terms VARCHAR(100),
    shipping_address TEXT,
    shipping_city VARCHAR(50),
    shipping_state VARCHAR(50),
    shipping_pincode VARCHAR(10),
    subtotal DECIMAL(15,2) DEFAULT 0,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    discount_amount DECIMAL(15,2) DEFAULT 0,
    tax_amount DECIMAL(15,2) DEFAULT 0,
    shipping_charges DECIMAL(15,2) DEFAULT 0,
    grand_total DECIMAL(15,2) DEFAULT 0,
    notes TEXT,
    internal_notes TEXT,
    confirmed_by BIGINT REFERENCES users(id),
    confirmed_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE sales_order_items (
    id BIGSERIAL PRIMARY KEY,
    so_id BIGINT NOT NULL REFERENCES sales_orders(id) ON DELETE CASCADE,
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    quantity DECIMAL(15,3) NOT NULL,
    delivered_quantity DECIMAL(15,3) DEFAULT 0,
    pending_quantity DECIMAL(15,3) GENERATED ALWAYS AS (quantity - delivered_quantity) STORED,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    unit_price DECIMAL(15,4) NOT NULL,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    tax_percent DECIMAL(5,2) DEFAULT 18,
    cgst_percent DECIMAL(5,2) DEFAULT 0,
    sgst_percent DECIMAL(5,2) DEFAULT 0,
    igst_percent DECIMAL(5,2) DEFAULT 0,
    tax_amount DECIMAL(15,2) DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE delivery_challans (
    id BIGSERIAL PRIMARY KEY,
    challan_number VARCHAR(30) NOT NULL UNIQUE,
    so_id BIGINT NOT NULL REFERENCES sales_orders(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    delivery_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, READY, DISPATCHED, IN_TRANSIT, DELIVERED, CANCELLED
    shipping_address TEXT,
    shipping_city VARCHAR(50),
    shipping_state VARCHAR(50),
    shipping_pincode VARCHAR(10),
    vehicle_no VARCHAR(20),
    driver_name VARCHAR(100),
    driver_phone VARCHAR(20),
    transporter_name VARCHAR(100),
    lr_no VARCHAR(50),
    lr_date DATE,
    eway_bill_no VARCHAR(50),
    eway_bill_date DATE,
    dispatched_at TIMESTAMP,
    delivered_at TIMESTAMP,
    received_by VARCHAR(100),
    receiver_phone VARCHAR(20),
    proof_of_delivery_url VARCHAR(255),
    signature_url VARCHAR(255),
    delivery_notes TEXT,
    internal_notes TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE delivery_challan_items (
    id BIGSERIAL PRIMARY KEY,
    challan_id BIGINT NOT NULL REFERENCES delivery_challans(id) ON DELETE CASCADE,
    so_item_id BIGINT REFERENCES sales_order_items(id),
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    quantity DECIMAL(15,3) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    batch_no VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INVOICING
-- =====================================================

CREATE TABLE invoices (
    id BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(30) NOT NULL UNIQUE,
    challan_id BIGINT REFERENCES delivery_challans(id),
    so_id BIGINT REFERENCES sales_orders(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    invoice_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, PENDING, SENT, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED
    billing_address TEXT,
    billing_city VARCHAR(50),
    billing_state VARCHAR(50),
    billing_pincode VARCHAR(10),
    shipping_address TEXT,
    shipping_city VARCHAR(50),
    shipping_state VARCHAR(50),
    shipping_pincode VARCHAR(10),
    subtotal DECIMAL(15,2) DEFAULT 0,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    discount_amount DECIMAL(15,2) DEFAULT 0,
    taxable_amount DECIMAL(15,2) DEFAULT 0,
    cgst_amount DECIMAL(15,2) DEFAULT 0,
    sgst_amount DECIMAL(15,2) DEFAULT 0,
    igst_amount DECIMAL(15,2) DEFAULT 0,
    total_tax_amount DECIMAL(15,2) DEFAULT 0,
    shipping_charges DECIMAL(15,2) DEFAULT 0,
    round_off DECIMAL(10,2) DEFAULT 0,
    grand_total DECIMAL(15,2) DEFAULT 0,
    paid_amount DECIMAL(15,2) DEFAULT 0,
    balance_amount DECIMAL(15,2) GENERATED ALWAYS AS (grand_total - paid_amount) STORED,
    payment_status VARCHAR(20) DEFAULT 'UNPAID', -- UNPAID, PARTIALLY_PAID, PAID
    terms_and_conditions TEXT,
    notes TEXT,
    internal_notes TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE invoice_items (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    finished_goods_id BIGINT NOT NULL REFERENCES finished_goods(id),
    description VARCHAR(255),
    hsn_code VARCHAR(20),
    quantity DECIMAL(15,3) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measurement(id),
    unit_price DECIMAL(15,4) NOT NULL,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    taxable_amount DECIMAL(15,2) DEFAULT 0,
    cgst_percent DECIMAL(5,2) DEFAULT 0,
    cgst_amount DECIMAL(15,2) DEFAULT 0,
    sgst_percent DECIMAL(5,2) DEFAULT 0,
    sgst_amount DECIMAL(15,2) DEFAULT 0,
    igst_percent DECIMAL(5,2) DEFAULT 0,
    igst_amount DECIMAL(15,2) DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    batch_no VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    payment_number VARCHAR(30) NOT NULL UNIQUE,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    payment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    amount DECIMAL(15,2) NOT NULL,
    payment_mode VARCHAR(30) NOT NULL, -- CASH, CHEQUE, BANK_TRANSFER, UPI, CARD, CREDIT_NOTE
    reference_no VARCHAR(100),
    bank_name VARCHAR(100),
    cheque_no VARCHAR(30),
    cheque_date DATE,
    transaction_id VARCHAR(100),
    status VARCHAR(20) DEFAULT 'COMPLETED', -- PENDING, COMPLETED, FAILED, REFUNDED
    notes TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

CREATE TABLE credit_notes (
    id BIGSERIAL PRIMARY KEY,
    credit_note_number VARCHAR(30) NOT NULL UNIQUE,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    credit_note_date DATE NOT NULL DEFAULT CURRENT_DATE,
    reason VARCHAR(50) NOT NULL, -- RETURN, DAMAGED, PRICING_ERROR, OTHER
    subtotal DECIMAL(15,2) DEFAULT 0,
    tax_amount DECIMAL(15,2) DEFAULT 0,
    grand_total DECIMAL(15,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, ISSUED, APPLIED, CANCELLED
    applied_to_invoice_id BIGINT REFERENCES invoices(id),
    notes TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- =====================================================
-- AUDIT & SYSTEM
-- =====================================================

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL, -- CREATE, UPDATE, DELETE, STATUS_CHANGE
    old_value JSONB,
    new_value JSONB,
    changed_fields TEXT[],
    user_id BIGINT,
    user_name VARCHAR(100),
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE system_settings (
    id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    setting_type VARCHAR(20) DEFAULT 'STRING', -- STRING, NUMBER, BOOLEAN, JSON
    description VARCHAR(255),
    is_editable BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sequence_generators (
    id BIGSERIAL PRIMARY KEY,
    sequence_type VARCHAR(50) NOT NULL UNIQUE,
    prefix VARCHAR(10),
    suffix VARCHAR(10),
    current_value BIGINT DEFAULT 0,
    padding_length INTEGER DEFAULT 6,
    fiscal_year VARCHAR(10),
    reset_on_fiscal_year BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30), -- INFO, WARNING, ERROR, SUCCESS
    category VARCHAR(30), -- INVENTORY, PRODUCTION, SALES, PURCHASE, SYSTEM
    reference_type VARCHAR(50),
    reference_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INDEXES
-- =====================================================

-- Users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role_id);
CREATE INDEX idx_user_sessions_user ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_token ON user_sessions(token);

-- Raw Materials
CREATE INDEX idx_raw_materials_code ON raw_materials(code);
CREATE INDEX idx_raw_materials_category ON raw_materials(category_id);
CREATE INDEX idx_raw_material_stock_material ON raw_material_stock(raw_material_id);
CREATE INDEX idx_raw_material_stock_warehouse ON raw_material_stock(warehouse_id);
CREATE INDEX idx_raw_material_transactions_material ON raw_material_transactions(raw_material_id);
CREATE INDEX idx_raw_material_transactions_date ON raw_material_transactions(transaction_date);

-- Finished Goods
CREATE INDEX idx_finished_goods_code ON finished_goods(code);
CREATE INDEX idx_finished_goods_barcode ON finished_goods(barcode);
CREATE INDEX idx_finished_goods_category ON finished_goods(category_id);
CREATE INDEX idx_finished_goods_stock_product ON finished_goods_stock(finished_goods_id);
CREATE INDEX idx_finished_goods_stock_warehouse ON finished_goods_stock(warehouse_id);
CREATE INDEX idx_finished_goods_transactions_product ON finished_goods_transactions(finished_goods_id);
CREATE INDEX idx_finished_goods_transactions_date ON finished_goods_transactions(transaction_date);

-- BOM
CREATE INDEX idx_bom_headers_product ON bom_headers(finished_goods_id);
CREATE INDEX idx_bom_details_header ON bom_details(bom_header_id);

-- Work Orders
CREATE INDEX idx_work_orders_number ON work_orders(work_order_no);
CREATE INDEX idx_work_orders_status ON work_orders(status);
CREATE INDEX idx_work_orders_bom ON work_orders(bom_id);
CREATE INDEX idx_work_orders_product ON work_orders(finished_goods_id);
CREATE INDEX idx_production_stages_work_order ON production_stages(work_order_id);
CREATE INDEX idx_production_consumption_work_order ON production_consumption(work_order_id);

-- Purchase
CREATE INDEX idx_purchase_orders_number ON purchase_orders(po_number);
CREATE INDEX idx_purchase_orders_supplier ON purchase_orders(supplier_id);
CREATE INDEX idx_purchase_orders_status ON purchase_orders(status);
CREATE INDEX idx_purchase_order_items_po ON purchase_order_items(po_id);
CREATE INDEX idx_grn_number ON goods_receipt_notes(grn_number);
CREATE INDEX idx_grn_po ON goods_receipt_notes(po_id);
CREATE INDEX idx_grn_items_grn ON grn_items(grn_id);

-- Sales
CREATE INDEX idx_sales_orders_number ON sales_orders(so_number);
CREATE INDEX idx_sales_orders_customer ON sales_orders(customer_id);
CREATE INDEX idx_sales_orders_status ON sales_orders(status);
CREATE INDEX idx_sales_order_items_so ON sales_order_items(so_id);

-- Delivery
CREATE INDEX idx_delivery_challans_number ON delivery_challans(challan_number);
CREATE INDEX idx_delivery_challans_so ON delivery_challans(so_id);
CREATE INDEX idx_delivery_challans_customer ON delivery_challans(customer_id);
CREATE INDEX idx_delivery_challans_status ON delivery_challans(status);
CREATE INDEX idx_delivery_challan_items_challan ON delivery_challan_items(challan_id);

-- Invoices
CREATE INDEX idx_invoices_number ON invoices(invoice_number);
CREATE INDEX idx_invoices_customer ON invoices(customer_id);
CREATE INDEX idx_invoices_status ON invoices(status);
CREATE INDEX idx_invoices_payment_status ON invoices(payment_status);
CREATE INDEX idx_invoice_items_invoice ON invoice_items(invoice_id);
CREATE INDEX idx_payments_invoice ON payments(invoice_id);
CREATE INDEX idx_payments_customer ON payments(customer_id);

-- Audit
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_timestamp ON audit_logs(timestamp);
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);

-- Notifications
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_unread ON notifications(user_id, is_read) WHERE is_read = FALSE;

-- Suppliers & Customers
CREATE INDEX idx_suppliers_code ON suppliers(code);
CREATE INDEX idx_customers_code ON customers(code);

