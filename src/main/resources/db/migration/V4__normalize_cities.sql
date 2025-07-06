CREATE TABLE cities (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE streets (
                         id SERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         city_id INTEGER NOT NULL,
                         CONSTRAINT fk_streets_city FOREIGN KEY (city_id) REFERENCES cities(id),
                         CONSTRAINT unique_street_per_city UNIQUE (name, city_id)
);

ALTER TABLE organizations
    ADD COLUMN city_id INTEGER,
    ADD COLUMN street_id INTEGER;

INSERT INTO cities (name)
SELECT DISTINCT city FROM organizations;

INSERT INTO streets (name, city_id)
SELECT DISTINCT o.address, c.id
FROM organizations o
         JOIN cities c ON o.city = c.name;

UPDATE organizations o
SET city_id = c.id
    FROM cities c
WHERE o.city = c.name;

UPDATE organizations o
SET street_id = s.id
    FROM streets s
JOIN cities c ON s.city_id = c.id
WHERE o.address = s.name AND o.city = c.name;

ALTER TABLE organizations
DROP COLUMN city,
    DROP COLUMN address;

ALTER TABLE organizations
    ALTER COLUMN city_id SET NOT NULL,
ALTER COLUMN street_id SET NOT NULL;

ALTER TABLE organizations
    ADD CONSTRAINT fk_organizations_city FOREIGN KEY (city_id) REFERENCES cities(id),
    ADD CONSTRAINT fk_organizations_street FOREIGN KEY (street_id) REFERENCES streets(id);

CREATE TABLE buildings (
                           id SERIAL PRIMARY KEY,
                           number VARCHAR(20) NOT NULL,
                           street_id INTEGER NOT NULL,
                           CONSTRAINT fk_buildings_street FOREIGN KEY (street_id) REFERENCES streets(id),
                           CONSTRAINT unique_building_per_street UNIQUE (number, street_id)
);

ALTER TABLE organizations
    ADD COLUMN building_id INTEGER,
    ADD CONSTRAINT fk_organization_building FOREIGN KEY (building_id) REFERENCES buildings(id);
