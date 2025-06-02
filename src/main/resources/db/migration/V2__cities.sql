
CREATE TABLE organizations (
                               id UUID PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               city VARCHAR(255) NOT NULL
);

ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_organization
        FOREIGN KEY (organization_id) REFERENCES organizations(id);