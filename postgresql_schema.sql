-- regions definition
CREATE TABLE regions (
                         id bigserial NOT NULL,
                         names varchar(255) NOT NULL,
                         region_type varchar(50) NOT NULL,
                         parent_id bigint NULL,
                         created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                         updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                         region_path varchar(255) NULL,
                         CONSTRAINT regions_pkey PRIMARY KEY (id),
                         CONSTRAINT regions_type_check CHECK (((region_type)::text = ANY ((ARRAY['CITY'::character varying, 'PROVINCE'::character varying, 'COUNTRY'::character varying])::text[]))),
                         CONSTRAINT regions_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES regions(id)
);
-- 1. check if the pg_trgm extension is available in PostgreSQL
SELECT * FROM pg_available_extensions WHERE name = 'pg_trgm';

-- 2. enable the pg_trgm Extension
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 3. verify the Extension
SELECT * FROM pg_extension WHERE extname = 'pg_trgm';

CREATE INDEX idx_regions_parent_id ON regions USING btree (parent_id);
CREATE INDEX idx_regions_path ON regions USING gin (region_path gin_trgm_ops);
CREATE INDEX idx_regions_type ON regions USING btree (region_type);

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

-- FUNCTION update_region_path();
CREATE OR REPLACE FUNCTION update_region_path()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$function$
DECLARE
    parent_path VARCHAR(255);
BEGIN
    -- Build new path for the current region
    IF NEW.parent_id IS NOT NULL THEN
        SELECT region_path INTO parent_path FROM regions WHERE id = NEW.parent_id;
    ELSE
        parent_path := '';
    END IF;

    NEW.region_path :=
            CASE
                WHEN parent_path = '' THEN LOWER(REPLACE(NEW.names, ' ', '_'))
                ELSE parent_path || '/' || LOWER(REPLACE(NEW.names, ' ', '_'))
                END;

    -- If this is an update, cascade to children
    IF TG_OP = 'UPDATE' AND NEW.region_path <> OLD.region_path THEN
        UPDATE regions
        SET parent_id = parent_id -- Force trigger execution on children
        WHERE parent_id = NEW.id;
    END IF;

    RETURN NEW;
END;
$function$;

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

-- Create FUNCTION validate_region_parent();
CREATE OR REPLACE FUNCTION validate_region_parent()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$function$
BEGIN
    IF NEW.region_type = 'CITY' AND NOT EXISTS (SELECT 1
                                                FROM regions
                                                WHERE id = NEW.parent_id
                                                  AND region_type = 'PROVINCE') THEN
        RAISE EXCEPTION 'City must have a province as parent.';
    ELSIF NEW.region_type = 'PROVINCE' AND NOT EXISTS (SELECT 1
                                                       FROM regions
                                                       WHERE id = NEW.parent_id
                                                         AND region_type = 'COUNTRY') THEN
        RAISE EXCEPTION 'Province must have a country as parent.';
    ELSIF NEW.region_type = 'COUNTRY' AND NEW.parent_id IS NOT NULL THEN
        RAISE EXCEPTION 'Country cannot have a parent.';
    END IF;
    RETURN NEW;
END;
$function$
;

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

-- create FUNCTION validate_customer_region();
CREATE OR REPLACE FUNCTION validate_customer_region()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$function$
DECLARE
    manager_region_id INTEGER;
BEGIN
    SELECT region_id
    INTO manager_region_id
    FROM managers
    WHERE id = NEW.manager_id;

    IF NOT EXISTS (WITH RECURSIVE region_hierarchy AS (SELECT id
                                                       FROM regions
                                                       WHERE id = manager_region_id
                                                       UNION ALL
                                                       SELECT r.id
                                                       FROM regions r
                                                                JOIN region_hierarchy rh ON r.parent_id = rh.id)
                   SELECT 1
                   FROM region_hierarchy
                   WHERE id = NEW.region_id) THEN
        RAISE EXCEPTION 'Customer region is not within the manager’s scope.';
    END IF;
    RETURN NEW;
END;
$function$
;

-- Table Triggers
create trigger trg_validate_region_parent before
    insert
    or
    update
    on
        regions for each row execute function validate_region_parent();
create trigger trg_update_region_path before
    insert
    or
    update
    on
        regions for each row execute function update_region_path();


-- managers definition
CREATE TABLE managers (
                          id bigserial NOT NULL,
                          names varchar(255) NOT NULL,
                          email varchar(255) NOT NULL,
                          region_id bigint NOT NULL,
                          created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                          updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                          CONSTRAINT managers_email_key UNIQUE (email),
                          CONSTRAINT managers_pkey PRIMARY KEY (id),
                          CONSTRAINT managers_region_id_fkey FOREIGN KEY (region_id) REFERENCES regions(id)
);
CREATE INDEX idx_managers_region_id ON managers USING btree (region_id);


-- customers definition
CREATE TABLE customers (
                           id bigserial NOT NULL,
                           names varchar(255) NOT NULL,
                           email varchar(255) NOT NULL,
                           manager_id bigint NOT NULL,
                           region_id bigint NOT NULL,
                           created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                           updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                           CONSTRAINT customers_pkey PRIMARY KEY (id),
                           CONSTRAINT customers_manager_id_fkey FOREIGN KEY (manager_id) REFERENCES managers(id),
                           CONSTRAINT customers_region_id_fkey FOREIGN KEY (region_id) REFERENCES regions(id)
);
CREATE INDEX idx_customers_manager_id ON customers USING btree (manager_id);
CREATE INDEX idx_customers_region_id ON customers USING btree (region_id);

-- Table Triggers
create trigger trg_validate_customer_region before
    insert
    or
    update
    on
        customers for each row execute function validate_customer_region();

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- ######################################################################################################################################################################
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------