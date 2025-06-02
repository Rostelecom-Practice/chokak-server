CREATE TABLE cities (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE streets (
                         id SERIAL PRIMARY KEY,
                         name TEXT NOT NULL UNIQUE
                         CONSTRAINT fk_streets_city FOREIGN KEY (city_id) REFERENCES cities(id)

);

ALTER TABLE organizations
    ADD COLUMN city_id INTEGER,
    ADD COLUMN street_id INTEGER;

INSERT INTO cities (name)
SELECT DISTINCT city FROM organizations;

INSERT INTO streets (name)
SELECT DISTINCT address FROM organizations;

UPDATE organizations o
SET city_id = c.id
    FROM cities c
WHERE o.city = c.name;

UPDATE organizations o
SET street_id = s.id
    FROM streets s
WHERE o.address = s.name;

ALTER TABLE organizations
DROP COLUMN city,
    DROP COLUMN address;

ALTER TABLE organizations
    ALTER COLUMN city_id SET NOT NULL,
ALTER COLUMN street_id SET NOT NULL;

ALTER TABLE organizations
    ADD CONSTRAINT fk_organizations_city FOREIGN KEY (city_id) REFERENCES cities(id),
    ADD CONSTRAINT fk_organizations_street FOREIGN KEY (street_id) REFERENCES streets(id);

-- Таблица зданий
CREATE TABLE buildings (
                           id SERIAL PRIMARY KEY,
                           number VARCHAR(20) NOT NULL, -- например, "12к1"
                           street_id INTEGER NOT NULL,
                           CONSTRAINT fk_buildings_street FOREIGN KEY (street_id) REFERENCES streets(id)
);

ALTER TABLE organizations
    ADD COLUMN building_id INTEGER NOT NULL,
    ADD CONSTRAINT fk_organization_building FOREIGN KEY (building_id) REFERENCES buildings(id);