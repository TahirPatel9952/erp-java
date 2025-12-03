-- Manufacturing ERP Seed Data
-- Version: 1.0.0
-- Description: Initial seed data for roles, permissions, and default settings

-- =====================================================
-- ROLES
-- =====================================================

INSERT INTO roles (name, description) VALUES
('ADMIN', 'System Administrator with full access'),
('MANAGER', 'Manager with access to reports and approvals'),
('SUPERVISOR', 'Supervisor with operational access'),
('OPERATOR', 'Operator with limited operational access'),
('VIEWER', 'Read-only access to data');

-- =====================================================
-- PERMISSIONS
-- =====================================================

-- User Management Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('USER_VIEW', 'USER', 'VIEW', 'View users'),
('USER_CREATE', 'USER', 'CREATE', 'Create users'),
('USER_UPDATE', 'USER', 'UPDATE', 'Update users'),
('USER_DELETE', 'USER', 'DELETE', 'Delete users');

-- Master Data Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('MASTER_VIEW', 'MASTER', 'VIEW', 'View master data'),
('MASTER_CREATE', 'MASTER', 'CREATE', 'Create master data'),
('MASTER_UPDATE', 'MASTER', 'UPDATE', 'Update master data'),
('MASTER_DELETE', 'MASTER', 'DELETE', 'Delete master data');

-- Supplier Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('SUPPLIER_VIEW', 'SUPPLIER', 'VIEW', 'View suppliers'),
('SUPPLIER_CREATE', 'SUPPLIER', 'CREATE', 'Create suppliers'),
('SUPPLIER_UPDATE', 'SUPPLIER', 'UPDATE', 'Update suppliers'),
('SUPPLIER_DELETE', 'SUPPLIER', 'DELETE', 'Delete suppliers');

-- Customer Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('CUSTOMER_VIEW', 'CUSTOMER', 'VIEW', 'View customers'),
('CUSTOMER_CREATE', 'CUSTOMER', 'CREATE', 'Create customers'),
('CUSTOMER_UPDATE', 'CUSTOMER', 'UPDATE', 'Update customers'),
('CUSTOMER_DELETE', 'CUSTOMER', 'DELETE', 'Delete customers');

-- Inventory Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('INVENTORY_VIEW', 'INVENTORY', 'VIEW', 'View inventory'),
('INVENTORY_CREATE', 'INVENTORY', 'CREATE', 'Create inventory items'),
('INVENTORY_UPDATE', 'INVENTORY', 'UPDATE', 'Update inventory items'),
('INVENTORY_DELETE', 'INVENTORY', 'DELETE', 'Delete inventory items'),
('INVENTORY_ADJUST', 'INVENTORY', 'ADJUST', 'Adjust stock levels');

-- Production Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('PRODUCTION_VIEW', 'PRODUCTION', 'VIEW', 'View production orders'),
('PRODUCTION_CREATE', 'PRODUCTION', 'CREATE', 'Create production orders'),
('PRODUCTION_UPDATE', 'PRODUCTION', 'UPDATE', 'Update production orders'),
('PRODUCTION_DELETE', 'PRODUCTION', 'DELETE', 'Delete production orders'),
('PRODUCTION_APPROVE', 'PRODUCTION', 'APPROVE', 'Approve production orders');

-- Purchase Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('PURCHASE_VIEW', 'PURCHASE', 'VIEW', 'View purchase orders'),
('PURCHASE_CREATE', 'PURCHASE', 'CREATE', 'Create purchase orders'),
('PURCHASE_UPDATE', 'PURCHASE', 'UPDATE', 'Update purchase orders'),
('PURCHASE_DELETE', 'PURCHASE', 'DELETE', 'Delete purchase orders'),
('PURCHASE_APPROVE', 'PURCHASE', 'APPROVE', 'Approve purchase orders');

-- GRN Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('GRN_VIEW', 'GRN', 'VIEW', 'View goods receipt notes'),
('GRN_CREATE', 'GRN', 'CREATE', 'Create goods receipt notes'),
('GRN_UPDATE', 'GRN', 'UPDATE', 'Update goods receipt notes'),
('GRN_APPROVE', 'GRN', 'APPROVE', 'Approve goods receipt notes');

-- Sales Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('SALES_VIEW', 'SALES', 'VIEW', 'View sales orders'),
('SALES_CREATE', 'SALES', 'CREATE', 'Create sales orders'),
('SALES_UPDATE', 'SALES', 'UPDATE', 'Update sales orders'),
('SALES_DELETE', 'SALES', 'DELETE', 'Delete sales orders'),
('SALES_APPROVE', 'SALES', 'APPROVE', 'Approve sales orders');

-- Delivery Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('DELIVERY_VIEW', 'DELIVERY', 'VIEW', 'View delivery challans'),
('DELIVERY_CREATE', 'DELIVERY', 'CREATE', 'Create delivery challans'),
('DELIVERY_UPDATE', 'DELIVERY', 'UPDATE', 'Update delivery challans'),
('DELIVERY_DISPATCH', 'DELIVERY', 'DISPATCH', 'Dispatch deliveries');

-- Invoice Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('INVOICE_VIEW', 'INVOICE', 'VIEW', 'View invoices'),
('INVOICE_CREATE', 'INVOICE', 'CREATE', 'Create invoices'),
('INVOICE_UPDATE', 'INVOICE', 'UPDATE', 'Update invoices'),
('INVOICE_DELETE', 'INVOICE', 'DELETE', 'Delete invoices');

-- Payment Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('PAYMENT_VIEW', 'PAYMENT', 'VIEW', 'View payments'),
('PAYMENT_CREATE', 'PAYMENT', 'CREATE', 'Record payments'),
('PAYMENT_UPDATE', 'PAYMENT', 'UPDATE', 'Update payments');

-- Report Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('REPORT_VIEW', 'REPORT', 'VIEW', 'View reports'),
('REPORT_EXPORT', 'REPORT', 'EXPORT', 'Export reports');

-- Dashboard Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('DASHBOARD_VIEW', 'DASHBOARD', 'VIEW', 'View dashboard');

-- Settings Permissions
INSERT INTO permissions (name, module, action, description) VALUES
('SETTINGS_VIEW', 'SETTINGS', 'VIEW', 'View settings'),
('SETTINGS_UPDATE', 'SETTINGS', 'UPDATE', 'Update settings');

-- =====================================================
-- ROLE PERMISSIONS MAPPING
-- =====================================================

-- Admin gets all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'ADMIN'), id FROM permissions;

-- Manager permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'MANAGER'), id FROM permissions 
WHERE name IN (
    'USER_VIEW',
    'MASTER_VIEW', 'MASTER_CREATE', 'MASTER_UPDATE',
    'SUPPLIER_VIEW', 'SUPPLIER_CREATE', 'SUPPLIER_UPDATE',
    'CUSTOMER_VIEW', 'CUSTOMER_CREATE', 'CUSTOMER_UPDATE',
    'INVENTORY_VIEW', 'INVENTORY_CREATE', 'INVENTORY_UPDATE', 'INVENTORY_ADJUST',
    'PRODUCTION_VIEW', 'PRODUCTION_CREATE', 'PRODUCTION_UPDATE', 'PRODUCTION_APPROVE',
    'PURCHASE_VIEW', 'PURCHASE_CREATE', 'PURCHASE_UPDATE', 'PURCHASE_APPROVE',
    'GRN_VIEW', 'GRN_CREATE', 'GRN_UPDATE', 'GRN_APPROVE',
    'SALES_VIEW', 'SALES_CREATE', 'SALES_UPDATE', 'SALES_APPROVE',
    'DELIVERY_VIEW', 'DELIVERY_CREATE', 'DELIVERY_UPDATE', 'DELIVERY_DISPATCH',
    'INVOICE_VIEW', 'INVOICE_CREATE', 'INVOICE_UPDATE',
    'PAYMENT_VIEW', 'PAYMENT_CREATE', 'PAYMENT_UPDATE',
    'REPORT_VIEW', 'REPORT_EXPORT',
    'DASHBOARD_VIEW',
    'SETTINGS_VIEW'
);

-- Supervisor permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'SUPERVISOR'), id FROM permissions 
WHERE name IN (
    'MASTER_VIEW',
    'SUPPLIER_VIEW',
    'CUSTOMER_VIEW',
    'INVENTORY_VIEW', 'INVENTORY_UPDATE', 'INVENTORY_ADJUST',
    'PRODUCTION_VIEW', 'PRODUCTION_CREATE', 'PRODUCTION_UPDATE',
    'PURCHASE_VIEW', 'PURCHASE_CREATE',
    'GRN_VIEW', 'GRN_CREATE', 'GRN_UPDATE',
    'SALES_VIEW', 'SALES_CREATE', 'SALES_UPDATE',
    'DELIVERY_VIEW', 'DELIVERY_CREATE', 'DELIVERY_UPDATE', 'DELIVERY_DISPATCH',
    'INVOICE_VIEW', 'INVOICE_CREATE',
    'PAYMENT_VIEW', 'PAYMENT_CREATE',
    'REPORT_VIEW',
    'DASHBOARD_VIEW'
);

-- Operator permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'OPERATOR'), id FROM permissions 
WHERE name IN (
    'MASTER_VIEW',
    'SUPPLIER_VIEW',
    'CUSTOMER_VIEW',
    'INVENTORY_VIEW', 'INVENTORY_ADJUST',
    'PRODUCTION_VIEW', 'PRODUCTION_UPDATE',
    'PURCHASE_VIEW',
    'GRN_VIEW', 'GRN_CREATE',
    'SALES_VIEW',
    'DELIVERY_VIEW', 'DELIVERY_UPDATE', 'DELIVERY_DISPATCH',
    'INVOICE_VIEW',
    'PAYMENT_VIEW',
    'DASHBOARD_VIEW'
);

-- Viewer permissions (read-only)
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'VIEWER'), id FROM permissions 
WHERE action = 'VIEW';

-- =====================================================
-- DEFAULT ADMIN USER
-- Password: Admin@123 (BCrypt encoded)
-- =====================================================

INSERT INTO users (username, email, password_hash, first_name, last_name, role_id, is_active)
VALUES (
    'admin',
    'admin@manufacturing-erp.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqhqgGnWl7M3QQP3qXzP3xm3q3dXy',
    'System',
    'Administrator',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    true
);

-- =====================================================
-- UNITS OF MEASUREMENT
-- =====================================================

INSERT INTO units_of_measurement (name, symbol, type, conversion_factor) VALUES
-- Quantity Units
('Piece', 'Pcs', 'QUANTITY', 1),
('Dozen', 'Dzn', 'QUANTITY', 12),
('Gross', 'Grs', 'QUANTITY', 144),
('Hundred', 'Hnd', 'QUANTITY', 100),
('Thousand', 'Tsd', 'QUANTITY', 1000),

-- Weight Units
('Kilogram', 'Kg', 'WEIGHT', 1),
('Gram', 'g', 'WEIGHT', 0.001),
('Milligram', 'mg', 'WEIGHT', 0.000001),
('Metric Ton', 'MT', 'WEIGHT', 1000),
('Quintal', 'Qtl', 'WEIGHT', 100),
('Pound', 'Lb', 'WEIGHT', 0.453592),

-- Length Units
('Meter', 'm', 'LENGTH', 1),
('Centimeter', 'cm', 'LENGTH', 0.01),
('Millimeter', 'mm', 'LENGTH', 0.001),
('Inch', 'in', 'LENGTH', 0.0254),
('Foot', 'ft', 'LENGTH', 0.3048),
('Yard', 'yd', 'LENGTH', 0.9144),

-- Volume Units
('Liter', 'L', 'VOLUME', 1),
('Milliliter', 'mL', 'VOLUME', 0.001),
('Cubic Meter', 'm³', 'VOLUME', 1000),
('Gallon', 'Gal', 'VOLUME', 3.78541),

-- Area Units
('Square Meter', 'm²', 'AREA', 1),
('Square Foot', 'sq ft', 'AREA', 0.092903),

-- Time Units
('Hour', 'Hr', 'TIME', 1),
('Minute', 'Min', 'TIME', 0.016667),
('Day', 'Day', 'TIME', 24);

-- =====================================================
-- DEFAULT CATEGORIES
-- =====================================================

INSERT INTO categories (name, code, type, description) VALUES
-- Raw Material Categories
('Electronics Components', 'RM-ELEC', 'RAW_MATERIAL', 'Electronic components and parts'),
('Metals', 'RM-METL', 'RAW_MATERIAL', 'Metal raw materials'),
('Plastics', 'RM-PLST', 'RAW_MATERIAL', 'Plastic raw materials'),
('Chemicals', 'RM-CHEM', 'RAW_MATERIAL', 'Chemical compounds'),
('Packaging Materials', 'RM-PACK', 'RAW_MATERIAL', 'Packaging and wrapping materials'),
('Consumables', 'RM-CONS', 'RAW_MATERIAL', 'Consumable items'),

-- Finished Goods Categories
('Finished Products', 'FG-PROD', 'FINISHED_GOOD', 'Main finished products'),
('Spare Parts', 'FG-SPAR', 'FINISHED_GOOD', 'Spare parts and accessories'),
('Sub-Assemblies', 'FG-SUBA', 'FINISHED_GOOD', 'Sub-assembly units'),

-- In-Process Categories
('Work In Progress', 'IP-WIP', 'IN_PROCESS', 'Work in progress items'),
('Semi-Finished', 'IP-SEMI', 'IN_PROCESS', 'Semi-finished items');

-- =====================================================
-- DEFAULT WAREHOUSE
-- =====================================================

INSERT INTO warehouses (name, code, address, city, state, country, is_active) VALUES
('Main Warehouse', 'WH-MAIN', '123 Industrial Area', 'Mumbai', 'Maharashtra', 'India', true),
('Raw Materials Store', 'WH-RM', '123 Industrial Area, Building B', 'Mumbai', 'Maharashtra', 'India', true),
('Finished Goods Store', 'WH-FG', '123 Industrial Area, Building C', 'Mumbai', 'Maharashtra', 'India', true);

-- Default locations for Main Warehouse
INSERT INTO warehouse_locations (warehouse_id, zone, rack, shelf, bin, location_code) VALUES
((SELECT id FROM warehouses WHERE code = 'WH-MAIN'), 'A', '01', '01', '01', 'A-01-01-01'),
((SELECT id FROM warehouses WHERE code = 'WH-MAIN'), 'A', '01', '01', '02', 'A-01-01-02'),
((SELECT id FROM warehouses WHERE code = 'WH-MAIN'), 'A', '01', '02', '01', 'A-01-02-01'),
((SELECT id FROM warehouses WHERE code = 'WH-MAIN'), 'A', '02', '01', '01', 'A-02-01-01'),
((SELECT id FROM warehouses WHERE code = 'WH-MAIN'), 'B', '01', '01', '01', 'B-01-01-01'),
((SELECT id FROM warehouses WHERE code = 'WH-MAIN'), 'B', '01', '01', '02', 'B-01-01-02');

-- =====================================================
-- SEQUENCE GENERATORS
-- =====================================================

INSERT INTO sequence_generators (sequence_type, prefix, current_value, padding_length) VALUES
('PURCHASE_ORDER', 'PO', 0, 6),
('SALES_ORDER', 'SO', 0, 6),
('WORK_ORDER', 'WO', 0, 6),
('DELIVERY_CHALLAN', 'DC', 0, 6),
('INVOICE', 'INV', 0, 6),
('GRN', 'GRN', 0, 6),
('PAYMENT', 'PAY', 0, 6),
('CREDIT_NOTE', 'CN', 0, 6),
('RAW_MATERIAL', 'RM', 0, 5),
('FINISHED_GOOD', 'FG', 0, 5),
('CUSTOMER', 'CUST', 0, 5),
('SUPPLIER', 'SUPP', 0, 5);

-- =====================================================
-- SYSTEM SETTINGS
-- =====================================================

INSERT INTO system_settings (setting_key, setting_value, setting_type, description) VALUES
('COMPANY_NAME', 'Manufacturing ERP Company', 'STRING', 'Company name'),
('COMPANY_ADDRESS', '123 Industrial Area, Mumbai, Maharashtra', 'STRING', 'Company address'),
('COMPANY_PHONE', '+91-22-12345678', 'STRING', 'Company phone'),
('COMPANY_EMAIL', 'info@manufacturing-erp.com', 'STRING', 'Company email'),
('COMPANY_GST_NO', '27XXXXX1234X1ZX', 'STRING', 'Company GST number'),
('COMPANY_PAN_NO', 'XXXXX1234X', 'STRING', 'Company PAN number'),
('DEFAULT_CURRENCY', 'INR', 'STRING', 'Default currency'),
('DEFAULT_TAX_PERCENT', '18', 'NUMBER', 'Default tax percentage'),
('FISCAL_YEAR_START_MONTH', '4', 'NUMBER', 'Fiscal year start month (April)'),
('INVENTORY_VALUATION_METHOD', 'WEIGHTED_AVERAGE', 'STRING', 'Inventory valuation method'),
('LOW_STOCK_ALERT_ENABLED', 'true', 'BOOLEAN', 'Enable low stock alerts'),
('EMAIL_NOTIFICATIONS_ENABLED', 'true', 'BOOLEAN', 'Enable email notifications'),
('DATE_FORMAT', 'DD-MM-YYYY', 'STRING', 'Date format'),
('TIME_FORMAT', 'HH:mm:ss', 'STRING', 'Time format'),
('TIMEZONE', 'Asia/Kolkata', 'STRING', 'System timezone');

