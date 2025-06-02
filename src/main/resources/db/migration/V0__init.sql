CREATE TABLE reviews (
                         id UUID PRIMARY KEY,
                         author_id UUID NOT NULL,
                         source_id UUID NOT NULL,
                         organization_id UUID NOT NULL,
                         title TEXT,
                         content TEXT,
                         published_at TIMESTAMP,
                         parent_review_id UUID,
                         rating INTEGER,

                         CONSTRAINT fk_parent_review FOREIGN KEY (parent_review_id)
                             REFERENCES reviews(id) ON DELETE SET NULL
);

CREATE INDEX idx_reviews_org ON reviews(organization_id);
CREATE INDEX idx_reviews_source ON reviews(source_id);
