-- Create/Generate Sample-Data

-- Country (Level 1)
INSERT INTO regions (names, region_type, parent_id)
VALUES ('Indonesia', 'COUNTRY', NULL); -- id=1

select * from regions r ;

-- Provinces (Level 2)
INSERT INTO regions (names, region_type, parent_id)
VALUES
    ('East Java', 'PROVINCE', 1), -- id=2
    ('West Java', 'PROVINCE', 1); -- id=3

select * from regions r;

-- Cities (Level 3)
INSERT INTO regions (names, region_type, parent_id)
VALUES
    ('Surabaya', 'CITY', 2), -- id=4 (under East Java)
    ('Jakarta', 'CITY', 3), -- id=5 (under West Java)
    ('Bandung', 'CITY', 3); -- id=6 (under West Java)

-- Managers
INSERT INTO managers (names, email, region_id)
VALUES
    ('Manager A', 'manager.a@bank.id', 4), -- Oversees Surabaya (City)
    ('Manager B', 'manager.b@bank.id', 2), -- Oversees East Java (Province)
    ('Manager C', 'manager.c@bank.id', 1), -- Oversees Indonesia (Country)
    ('Manager D', 'manager.d@bank.id', 3), -- Oversees West Java (Province)
    ('Manager E', 'manager.e@bank.id', 5); -- Oversees Jakarta (City)

-- Customers
INSERT INTO customers (names, email, manager_id, region_id)
VALUES
-- Manager A's customers (Surabaya)
('Customer C1', 'c1@bank.id', 1, 4),
('Customer C2', 'c2@bank.id', 1, 4),

-- Manager B's customers (East Java Province)
('Customer C3', 'c3@bank.id', 2, 2), -- In East Java Province
('Customer C4', 'c4@bank.id', 2, 4), -- In Surabaya (child of East Java)

-- Manager C's customers (Indonesia)
('Customer C5', 'c5@bank.id', 3, 1), -- In Indonesia (Country)
('Customer C6', 'c6@bank.id', 3, 5), -- In Jakarta (under Indonesia hierarchy)

-- Manager D's customers (West Java Province)
('Customer C7', 'c7@bank.id', 4, 3), -- In West Java Province
('Customer C8', 'c8@bank.id', 4, 5), -- In Jakarta (child of West Java)

-- Manager E's customers (Jakarta)
('Customer C9', 'c9@bank.id', 5, 5),
('Customer C10', 'c10@bank.id', 5, 5);

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- ######################################################################################################################################################################
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
