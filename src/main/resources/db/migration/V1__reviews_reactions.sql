CREATE TABLE review_reactions (
                                  id UUID PRIMARY KEY,
                                  review_id UUID NOT NULL REFERENCES reviews(id) ON DELETE CASCADE,
                                  user_id UUID NOT NULL,
                                  reaction_type VARCHAR(10) NOT NULL,
                                  value CHAR(1) NOT NULL,
                                  created_at TIMESTAMP NOT NULL DEFAULT now(),
                                  UNIQUE (review_id, user_id, reaction_type, value)
);
