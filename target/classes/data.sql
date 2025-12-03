-- =====================================================
-- MANUFACTURING ERP - SAMPLE DATA
-- =====================================================

-- =====================================================
-- 1. PERMISSIONS
-- =====================================================
INSERT INTO permissions (id, name, module, action, description, created_at) VALUES
-- User Management
(1, 'USER_CREATE', 'USER', 'CREATE', 'Create new users', CURRENT_TIMESTAMP),
(2, 'USER_READ', 'USER', 'READ', 'View user details', CURRENT_TIMESTAMP),
(3, 'USER_UPDATE', 'USER', 'UPDATE', 'Update user information', CURRENT_TIMESTAMP),
(4, 'USER_DELETE', 'USER', 'DELETE', 'Delete users', CURRENT_TIMESTAMP),
-- Inventory Management
(5, 'INVENTORY_CREATE', 'INVENTORY', 'CREATE', 'Create inventory items', CURRENT_TIMESTAMP),
(6, 'INVENTORY_READ', 'INVENTORY', 'READ', 'View inventory', CURRENT_TIMESTAMP),
(7, 'INVENTORY_UPDATE', 'INVENTORY', 'UPDATE', 'Update inventory', CURRENT_TIMESTAMP),
(8, 'INVENTORY_DELETE', 'INVENTORY', 'DELETE', 'Delete inventory items', CURRENT_TIMESTAMP),
-- Purchase Order
(9, 'PURCHASE_CREATE', 'PURCHASE', 'CREATE', 'Create purchase orders', CURRENT_TIMESTAMP),
(10, 'PURCHASE_READ', 'PURCHASE', 'READ', 'View purchase orders', CURRENT_TIMESTAMP),
(11, 'PURCHASE_UPDATE', 'PURCHASE', 'UPDATE', 'Update purchase orders', CURRENT_TIMESTAMP),
(12, 'PURCHASE_APPROVE', 'PURCHASE', 'APPROVE', 'Approve purchase orders', CURRENT_TIMESTAMP),
-- Sales Order
(13, 'SALES_CREATE', 'SALES', 'CREATE', 'Create sales orders', CURRENT_TIMESTAMP),
(14, 'SALES_READ', 'SALES', 'READ', 'View sales orders', CURRENT_TIMESTAMP),
(15, 'SALES_UPDATE', 'SALES', 'UPDATE', 'Update sales orders', CURRENT_TIMESTAMP),
(16, 'SALES_APPROVE', 'SALES', 'APPROVE', 'Approve sales orders', CURRENT_TIMESTAMP),
-- Production
(17, 'PRODUCTION_CREATE', 'PRODUCTION', 'CREATE', 'Create work orders', CURRENT_TIMESTAMP),
(18, 'PRODUCTION_READ', 'PRODUCTION', 'READ', 'View work orders', CURRENT_TIMESTAMP),
(19, 'PRODUCTION_UPDATE', 'PRODUCTION', 'UPDATE', 'Update work orders', CURRENT_TIMESTAMP),
(20, 'PRODUCTION_EXECUTE', 'PRODUCTION', 'EXECUTE', 'Execute production', CURRENT_TIMESTAMP),
-- Reports
(21, 'REPORT_VIEW', 'REPORT', 'VIEW', 'View reports', CURRENT_TIMESTAMP),
(22, 'REPORT_EXPORT', 'REPORT', 'EXPORT', 'Export reports', CURRENT_TIMESTAMP),
-- Settings
(23, 'SETTINGS_VIEW', 'SETTINGS', 'VIEW', 'View settings', CURRENT_TIMESTAMP),
(24, 'SETTINGS_MANAGE', 'SETTINGS', 'MANAGE', 'Manage settings', CURRENT_TIMESTAMP);

-- =====================================================
-- 2. ROLES
-- =====================================================
INSERT INTO roles (id, name, description, created_at, updated_at) VALUES
(1, 'ADMIN', 'System Administrator with full access', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'MANAGER', 'Manager with approval rights', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'SUPERVISOR', 'Supervisor with operational rights', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'OPERATOR', 'Operator with limited access', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'VIEWER', 'Read-only access', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 3. ROLE-PERMISSION MAPPINGS
-- =====================================================
-- Admin gets all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT 1, id FROM permissions;

-- Manager permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 2), (2, 3), (2, 6), (2, 7), (2, 9), (2, 10), (2, 11), (2, 12),
(2, 13), (2, 14), (2, 15), (2, 16), (2, 17), (2, 18), (2, 19), (2, 20),
(2, 21), (2, 22), (2, 23);

-- Supervisor permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(3, 2), (3, 6), (3, 7), (3, 9), (3, 10), (3, 11),
(3, 13), (3, 14), (3, 15), (3, 17), (3, 18), (3, 19), (3, 20), (3, 21);

-- Operator permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(4, 6), (4, 10), (4, 14), (4, 18), (4, 20);

-- Viewer permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(5, 2), (5, 6), (5, 10), (5, 14), (5, 18), (5, 21);

-- =====================================================
-- 4. USERS (Password: password123 - BCrypt encoded)
-- =====================================================
INSERT INTO users (id, username, email, password_hash, first_name, last_name, phone, role_id, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'admin', 'admin@erp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'System', 'Administrator', '+91-9876543210', 1, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'manager', 'manager@erp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Rajesh', 'Kumar', '+91-9876543211', 2, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'supervisor', 'supervisor@erp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Priya', 'Sharma', '+91-9876543212', 3, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'operator1', 'operator1@erp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Amit', 'Patel', '+91-9876543213', 4, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'operator2', 'operator2@erp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Sunita', 'Verma', '+91-9876543214', 4, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'viewer', 'viewer@erp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Guest', 'User', '+91-9876543215', 5, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 5. UNITS OF MEASUREMENT
-- =====================================================
INSERT INTO units_of_measurement (id, name, symbol, type, base_unit_id, conversion_factor, is_active, is_deleted, created_at, updated_at) VALUES
-- Quantity units
(1, 'Piece', 'Pc', 'QUANTITY', NULL, 1.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Dozen', 'Dz', 'QUANTITY', 1, 12.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Box', 'Box', 'QUANTITY', 1, 24.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Carton', 'Ctn', 'QUANTITY', 1, 100.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Weight units
(5, 'Gram', 'g', 'WEIGHT', NULL, 1.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Kilogram', 'Kg', 'WEIGHT', 5, 1000.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Quintal', 'Qtl', 'WEIGHT', 5, 100000.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Metric Ton', 'MT', 'WEIGHT', 5, 1000000.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Length units
(9, 'Millimeter', 'mm', 'LENGTH', NULL, 1.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Centimeter', 'cm', 'LENGTH', 9, 10.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Meter', 'm', 'LENGTH', 9, 1000.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Inch', 'in', 'LENGTH', 9, 25.400000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'Feet', 'ft', 'LENGTH', 9, 304.800000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Volume units
(14, 'Milliliter', 'ml', 'VOLUME', NULL, 1.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Liter', 'L', 'VOLUME', 14, 1000.000000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 6. CATEGORIES
-- =====================================================
INSERT INTO categories (id, name, code, type, parent_id, description, is_active, is_deleted, created_at, updated_at) VALUES
-- Raw Material Categories
(1, 'Raw Materials', 'RM', 'RAW_MATERIAL', NULL, 'All raw materials', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Metals', 'RM-MET', 'RAW_MATERIAL', 1, 'Metal raw materials', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Plastics', 'RM-PLS', 'RAW_MATERIAL', 1, 'Plastic raw materials', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Electronics', 'RM-ELE', 'RAW_MATERIAL', 1, 'Electronic components', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Packaging', 'RM-PKG', 'RAW_MATERIAL', 1, 'Packaging materials', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Chemicals', 'RM-CHM', 'RAW_MATERIAL', 1, 'Chemical raw materials', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Finished Goods Categories
(7, 'Finished Goods', 'FG', 'FINISHED_GOOD', NULL, 'All finished products', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Assembled Products', 'FG-ASM', 'FINISHED_GOOD', 7, 'Assembled final products', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Consumer Electronics', 'FG-CEL', 'FINISHED_GOOD', 7, 'Consumer electronic products', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Industrial Parts', 'FG-IND', 'FINISHED_GOOD', 7, 'Industrial components', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- In-Process Categories
(11, 'Work In Progress', 'WIP', 'IN_PROCESS', NULL, 'Items under production', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Sub-Assemblies', 'WIP-SUB', 'IN_PROCESS', 11, 'Sub-assembled components', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 7. WAREHOUSES
-- =====================================================
INSERT INTO warehouses (id, name, code, address, city, state, country, pincode, contact_person, contact_phone, contact_email, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'Main Warehouse', 'WH-MAIN', '123 Industrial Area, Phase 1', 'Mumbai', 'Maharashtra', 'India', '400001', 'Ramesh Gupta', '+91-22-12345678', 'warehouse.main@erp.com', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Raw Material Store', 'WH-RM', '456 Industrial Area, Phase 2', 'Mumbai', 'Maharashtra', 'India', '400002', 'Suresh Kumar', '+91-22-87654321', 'warehouse.rm@erp.com', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Finished Goods Store', 'WH-FG', '789 Industrial Area, Phase 3', 'Mumbai', 'Maharashtra', 'India', '400003', 'Anita Singh', '+91-22-11223344', 'warehouse.fg@erp.com', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Delhi Warehouse', 'WH-DEL', '100 Okhla Industrial Area', 'Delhi', 'Delhi', 'India', '110020', 'Vikram Malhotra', '+91-11-55667788', 'warehouse.delhi@erp.com', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 8. WAREHOUSE LOCATIONS
-- =====================================================
INSERT INTO warehouse_locations (id, warehouse_id, location_code, zone, rack, shelf, bin, capacity, is_active, is_deleted, created_at, updated_at) VALUES
-- Main Warehouse locations
(1, 1, 'WH-MAIN-A1', 'A', 'R1', 'S1', 'B1', 1000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 'WH-MAIN-A2', 'A', 'R2', 'S1', 'B1', 1000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 'WH-MAIN-B1', 'B', 'R1', 'S1', 'B1', 1000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1, 'WH-MAIN-B2', 'B', 'R2', 'S1', 'B1', 1000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Raw Material Store locations
(5, 2, 'WH-RM-MET', 'M', 'R1', 'S1', 'B1', 5000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 2, 'WH-RM-PLS', 'P', 'R1', 'S1', 'B1', 3000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 2, 'WH-RM-ELE', 'E', 'R1', 'S1', 'B1', 2000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Finished Goods Store locations
(8, 3, 'WH-FG-SHP', 'S', 'R1', 'S1', 'B1', 2000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, 'WH-FG-RDY', 'R', 'R1', 'S1', 'B1', 3000.000, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 9. SUPPLIERS
-- =====================================================
INSERT INTO suppliers (id, name, code, contact_person, phone, email, address, city, state, country, pincode, gst_no, pan_no, bank_name, bank_account_no, bank_ifsc, payment_terms, credit_limit, current_balance, rating, notes, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'Steel India Pvt Ltd', 'SUP-001', 'Mahesh Agarwal', '+91-22-44556677', 'sales@steelindia.com', '100 Steel Complex, MIDC', 'Pune', 'Maharashtra', 'India', '411001', '27AABCS1234H1Z5', 'AABCS1234H', 'HDFC Bank', '12345678901234', 'HDFC0001234', 30, 5000000.00, 0.00, 5, 'Premium steel supplier', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Plastic World Corp', 'SUP-002', 'Neha Sharma', '+91-11-88990011', 'orders@plasticworld.com', '50 Plastic Market', 'Delhi', 'Delhi', 'India', '110001', '07AABCP5678K1Z8', 'AABCP5678K', 'ICICI Bank', '98765432109876', 'ICIC0005678', 45, 2000000.00, 0.00, 4, 'Reliable plastic supplier', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Electronics Hub', 'SUP-003', 'Ravi Verma', '+91-80-22334455', 'procurement@electronicshub.in', '200 Electronic City', 'Bangalore', 'Karnataka', 'India', '560100', '29AABCE9012L1Z3', 'AABCE9012L', 'Axis Bank', '55667788990011', 'UTIB0009012', 30, 3000000.00, 0.00, 5, 'Electronic component specialist', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'PackWell Industries', 'SUP-004', 'Sanjay Gupta', '+91-44-66778899', 'supply@packwell.com', '75 Packaging Zone', 'Chennai', 'Tamil Nadu', 'India', '600001', '33AABCP3456M1Z1', 'AABCP3456M', 'SBI', '11223344556677', 'SBIN0003456', 15, 1000000.00, 0.00, 4, 'Packaging solution provider', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'ChemTech Solutions', 'SUP-005', 'Pooja Reddy', '+91-40-99887766', 'sales@chemtech.com', '300 Chemical Zone', 'Hyderabad', 'Telangana', 'India', '500001', '36AABCC7890N1Z6', 'AABCC7890N', 'Kotak Bank', '77889900112233', 'KKBK0007890', 30, 1500000.00, 0.00, 4, 'Chemical supplier', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 10. CUSTOMERS
-- =====================================================
INSERT INTO customers (id, name, code, customer_type, contact_person, phone, email, billing_address, billing_city, billing_state, billing_country, billing_pincode, shipping_address, shipping_city, shipping_state, shipping_country, shipping_pincode, gst_no, pan_no, credit_limit, current_balance, payment_terms, discount_percent, notes, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'TechMart Distributors', 'CUS-001', 'DISTRIBUTOR', 'Arun Kumar', '+91-22-11112222', 'orders@techmart.in', '500 Commercial Complex', 'Mumbai', 'Maharashtra', 'India', '400050', '500 Commercial Complex', 'Mumbai', 'Maharashtra', 'India', '400050', '27AAACT1111H1Z2', 'AAACT1111H', 10000000.00, 0.00, 45, 5.00, 'Key distributor for Western region', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Global Electronics Ltd', 'CUS-002', 'CORPORATE', 'Deepak Mehta', '+91-11-33334444', 'purchase@globalelectronics.com', '100 Corporate Park', 'Delhi', 'Delhi', 'India', '110001', '150 Warehouse Complex', 'Noida', 'Uttar Pradesh', 'India', '201301', '07AABCG2222K1Z5', 'AABCG2222K', 5000000.00, 0.00, 30, 3.00, 'Corporate account - priority handling', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'SmartHome Solutions', 'CUS-003', 'REGULAR', 'Priya Iyer', '+91-80-55556666', 'procurement@smarthome.in', '200 Tech Park', 'Bangalore', 'Karnataka', 'India', '560001', '200 Tech Park', 'Bangalore', 'Karnataka', 'India', '560001', '29AABCS3333L1Z8', 'AABCS3333L', 2000000.00, 0.00, 30, 0.00, 'Growing account', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Industrial Parts Co', 'CUS-004', 'CORPORATE', 'Vijay Singh', '+91-44-77778888', 'orders@industrialparts.com', '300 Industrial Estate', 'Chennai', 'Tamil Nadu', 'India', '600001', '300 Industrial Estate', 'Chennai', 'Tamil Nadu', 'India', '600001', '33AABCI4444M1Z1', 'AABCI4444M', 3000000.00, 0.00, 30, 2.50, 'Long-term industrial customer', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'RetailMax Stores', 'CUS-005', 'RETAIL', 'Anita Desai', '+91-40-99990000', 'buying@retailmax.in', '50 Retail Mall', 'Hyderabad', 'Telangana', 'India', '500001', '50 Retail Mall', 'Hyderabad', 'Telangana', 'India', '500001', '36AABCR5555N1Z4', 'AABCR5555N', 1000000.00, 0.00, 15, 0.00, 'Retail chain customer', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 11. RAW MATERIALS
-- =====================================================
INSERT INTO raw_materials (id, name, code, description, category_id, unit_id, hsn_code, reorder_level, reorder_quantity, minimum_order_quantity, lead_time_days, standard_cost, last_purchase_price, avg_purchase_price, tax_percent, shelf_life_days, storage_conditions, is_batch_tracked, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'Stainless Steel Sheet 304', 'RM-SS-304', 'Grade 304 Stainless Steel Sheet 1mm thickness', 2, 6, '7219', 500.000, 1000.000, 100.000, 7, 250.0000, 248.5000, 249.2500, 18.00, NULL, 'Keep dry, avoid corrosive environment', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Aluminum Sheet 6061', 'RM-AL-6061', 'Grade 6061 Aluminum Sheet 2mm thickness', 2, 6, '7606', 300.000, 500.000, 50.000, 5, 350.0000, 345.0000, 347.5000, 18.00, NULL, 'Keep dry', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'ABS Plastic Granules', 'RM-ABS-001', 'High impact ABS plastic granules - Natural', 3, 6, '3903', 200.000, 400.000, 25.000, 10, 180.0000, 175.0000, 177.5000, 18.00, 365, 'Store in cool dry place', true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Polycarbonate Resin', 'RM-PC-001', 'Optical grade polycarbonate resin', 3, 6, '3907', 150.000, 300.000, 25.000, 14, 280.0000, 275.0000, 277.5000, 18.00, 365, 'Store away from moisture', true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Microcontroller ATmega328', 'RM-MCU-328', 'ATmega328P Microcontroller IC', 4, 1, '8542', 1000.000, 2000.000, 100.000, 21, 85.0000, 82.0000, 83.5000, 18.00, NULL, 'ESD protected storage', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'LED Display Module', 'RM-LED-01', '16x2 LCD Display Module Blue Backlight', 4, 1, '8531', 500.000, 1000.000, 50.000, 14, 120.0000, 115.0000, 117.5000, 18.00, NULL, 'Handle with care', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Capacitor 100uF', 'RM-CAP-100', 'Electrolytic Capacitor 100uF 25V', 4, 1, '8532', 5000.000, 10000.000, 500.000, 7, 2.5000, 2.4000, 2.4500, 18.00, NULL, 'Store in dry conditions', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Resistor Kit 1K', 'RM-RES-1K', '1K Ohm Carbon Film Resistor 1/4W', 4, 1, '8533', 10000.000, 20000.000, 1000.000, 7, 0.5000, 0.4500, 0.4750, 18.00, NULL, 'Keep dry', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Corrugated Box Medium', 'RM-BOX-M', '3-Ply Corrugated Box 30x20x15 cm', 5, 1, '4819', 1000.000, 2000.000, 100.000, 3, 25.0000, 24.0000, 24.5000, 18.00, NULL, 'Store flat, keep dry', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Bubble Wrap Roll', 'RM-BWR-01', 'Bubble Wrap 12mm bubble, 1m width', 5, 11, '3920', 200.000, 500.000, 50.000, 3, 45.0000, 42.0000, 43.5000, 18.00, NULL, 'Store in dry area', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 12. FINISHED GOODS
-- =====================================================
INSERT INTO finished_goods (id, name, code, description, category_id, unit_id, hsn_code, selling_price, minimum_selling_price, mrp, standard_cost, reorder_level, tax_percent, shelf_life_days, weight, weight_unit_id, dimensions, barcode, image_url, is_batch_tracked, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'Smart Controller Unit A1', 'FG-SCU-A1', 'IoT enabled smart controller for home automation', 9, 1, '8537', 2500.0000, 2200.0000, 2999.0000, 1800.0000, 100.000, 18.00, NULL, 0.5000, 6, '15x10x5 cm', '8901234567890', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Digital Display Panel P1', 'FG-DDP-P1', 'Industrial grade digital display panel', 9, 1, '8531', 4500.0000, 4000.0000, 5499.0000, 3200.0000, 50.000, 18.00, NULL, 1.2000, 6, '25x20x3 cm', '8901234567891', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Motor Control Board M1', 'FG-MCB-M1', 'PWM motor control board for industrial use', 10, 1, '8537', 1800.0000, 1600.0000, 2199.0000, 1200.0000, 75.000, 18.00, NULL, 0.3000, 6, '12x8x2 cm', '8901234567892', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Sensor Module Kit SK1', 'FG-SMK-S1', 'Multi-sensor module kit for IoT applications', 9, 1, '8542', 3200.0000, 2800.0000, 3999.0000, 2400.0000, 80.000, 18.00, NULL, 0.2000, 6, '10x10x3 cm', '8901234567893', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Power Supply Unit PS500', 'FG-PSU-500', '500W Industrial Power Supply Unit', 10, 1, '8504', 5500.0000, 5000.0000, 6499.0000, 4000.0000, 40.000, 18.00, NULL, 2.5000, 6, '20x15x8 cm', '8901234567894', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Assembled Circuit Board ACB1', 'FG-ACB-01', 'Pre-assembled circuit board with components', 8, 1, '8534', 850.0000, 750.0000, 999.0000, 550.0000, 200.000, 18.00, NULL, 0.1000, 6, '8x6x1 cm', '8901234567895', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'LED Driver Module LD50', 'FG-LDM-50', '50W LED Driver Module', 10, 1, '8504', 650.0000, 580.0000, 799.0000, 420.0000, 150.000, 18.00, NULL, 0.4000, 6, '10x5x3 cm', '8901234567896', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Temperature Controller TC100', 'FG-TC-100', 'Digital Temperature Controller with display', 9, 1, '9032', 1200.0000, 1050.0000, 1499.0000, 780.0000, 100.000, 18.00, NULL, 0.3000, 6, '12x8x5 cm', '8901234567897', NULL, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 13. RAW MATERIAL STOCK
-- =====================================================
INSERT INTO raw_material_stock (id, raw_material_id, warehouse_id, location_id, batch_no, quantity, reserved_quantity, unit_cost, expiry_date, manufacturing_date, created_at, updated_at) VALUES
(1, 1, 2, 5, NULL, 2500.000, 0.000, 248.5000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 5, NULL, 800.000, 0.000, 345.0000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 2, 6, 'ABS-2024-001', 600.000, 0.000, 175.0000, '2025-11-01', '2024-11-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 2, 6, 'PC-2024-001', 450.000, 0.000, 275.0000, '2025-11-15', '2024-11-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 2, 7, NULL, 5000.000, 0.000, 82.0000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 6, 2, 7, NULL, 2000.000, 0.000, 115.0000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 7, 2, 7, NULL, 25000.000, 0.000, 2.4000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 8, 2, 7, NULL, 50000.000, 0.000, 0.4500, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 9, 1, 1, NULL, 5000.000, 0.000, 24.0000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 10, 1, 1, NULL, 1000.000, 0.000, 42.0000, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 14. FINISHED GOODS STOCK
-- =====================================================
INSERT INTO finished_goods_stock (id, finished_goods_id, warehouse_id, location_id, batch_no, quantity, reserved_quantity, manufacturing_date, expiry_date, unit_cost, created_at, updated_at) VALUES
(1, 1, 3, 9, 'SCU-A1-2024-001', 250.000, 20.000, '2024-11-01', NULL, 1800.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 3, 9, 'DDP-P1-2024-001', 100.000, 10.000, '2024-11-05', NULL, 3200.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 3, 9, 'MCB-M1-2024-001', 180.000, 15.000, '2024-11-10', NULL, 1200.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 3, 9, 'SMK-S1-2024-001', 150.000, 0.000, '2024-11-12', NULL, 2400.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 3, 9, 'PSU-500-2024-001', 75.000, 5.000, '2024-11-08', NULL, 4000.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 6, 3, 8, 'ACB-01-2024-001', 500.000, 50.000, '2024-11-15', NULL, 550.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 7, 3, 8, 'LDM-50-2024-001', 320.000, 20.000, '2024-11-14', NULL, 420.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 8, 3, 8, 'TC-100-2024-001', 200.000, 0.000, '2024-11-16', NULL, 780.0000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 15. BILL OF MATERIALS (BOM) HEADERS
-- =====================================================
INSERT INTO bom_headers (id, finished_goods_id, bom_code, version, description, output_quantity, effective_from, effective_to, standard_time_minutes, setup_time_minutes, is_active, is_deleted, created_at, updated_at) VALUES
(1, 1, 'BOM-SCU-A1-V1', 'V1.0', 'Standard BOM for Smart Controller Unit A1', 1.000, '2024-01-01', NULL, 45, 10, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'BOM-DDP-P1-V1', 'V1.0', 'Standard BOM for Digital Display Panel P1', 1.000, '2024-01-01', NULL, 60, 15, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 'BOM-MCB-M1-V1', 'V1.0', 'Standard BOM for Motor Control Board M1', 1.000, '2024-01-01', NULL, 30, 5, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 6, 'BOM-ACB-01-V1', 'V1.0', 'Standard BOM for Assembled Circuit Board', 1.000, '2024-01-01', NULL, 20, 5, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 16. BILL OF MATERIALS (BOM) DETAILS
-- =====================================================
INSERT INTO bom_details (id, bom_header_id, item_type, item_id, sequence_no, quantity, unit_id, wastage_percent, is_critical, notes, created_at, updated_at) VALUES
-- BOM for Smart Controller Unit A1
(1, 1, 'RAW_MATERIAL', 5, 1, 1.000000, 1, 2.00, true, 'Main microcontroller', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 'RAW_MATERIAL', 6, 2, 1.000000, 1, 1.00, true, 'Display module', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 'RAW_MATERIAL', 7, 3, 5.000000, 1, 5.00, false, 'Capacitors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1, 'RAW_MATERIAL', 8, 4, 20.000000, 1, 5.00, false, 'Resistors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 1, 'RAW_MATERIAL', 3, 5, 0.150000, 6, 3.00, false, 'ABS plastic for casing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 1, 'RAW_MATERIAL', 9, 6, 1.000000, 1, 0.00, false, 'Packaging box', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- BOM for Digital Display Panel P1
(7, 2, 'RAW_MATERIAL', 6, 1, 2.000000, 1, 1.00, true, 'Display modules', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 2, 'RAW_MATERIAL', 5, 2, 1.000000, 1, 2.00, true, 'Controller IC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 2, 'RAW_MATERIAL', 7, 3, 8.000000, 1, 5.00, false, 'Capacitors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 2, 'RAW_MATERIAL', 2, 4, 0.500000, 6, 5.00, false, 'Aluminum frame', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 2, 'RAW_MATERIAL', 9, 5, 1.000000, 1, 0.00, false, 'Packaging box', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- BOM for Motor Control Board M1
(12, 3, 'RAW_MATERIAL', 5, 1, 1.000000, 1, 2.00, true, 'Microcontroller', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 3, 'RAW_MATERIAL', 7, 2, 10.000000, 1, 5.00, false, 'Capacitors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 3, 'RAW_MATERIAL', 8, 3, 30.000000, 1, 5.00, false, 'Resistors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- BOM for Assembled Circuit Board
(15, 4, 'RAW_MATERIAL', 7, 1, 3.000000, 1, 5.00, false, 'Capacitors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 4, 'RAW_MATERIAL', 8, 2, 15.000000, 1, 5.00, false, 'Resistors', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- Update sequences for PostgreSQL
-- =====================================================
SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('units_of_measurement_id_seq', (SELECT MAX(id) FROM units_of_measurement));
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));
SELECT setval('warehouses_id_seq', (SELECT MAX(id) FROM warehouses));
SELECT setval('warehouse_locations_id_seq', (SELECT MAX(id) FROM warehouse_locations));
SELECT setval('suppliers_id_seq', (SELECT MAX(id) FROM suppliers));
SELECT setval('customers_id_seq', (SELECT MAX(id) FROM customers));
SELECT setval('raw_materials_id_seq', (SELECT MAX(id) FROM raw_materials));
SELECT setval('finished_goods_id_seq', (SELECT MAX(id) FROM finished_goods));
SELECT setval('raw_material_stock_id_seq', (SELECT MAX(id) FROM raw_material_stock));
SELECT setval('finished_goods_stock_id_seq', (SELECT MAX(id) FROM finished_goods_stock));
SELECT setval('bom_headers_id_seq', (SELECT MAX(id) FROM bom_headers));
SELECT setval('bom_details_id_seq', (SELECT MAX(id) FROM bom_details));
