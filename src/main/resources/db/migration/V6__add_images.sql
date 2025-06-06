-- Добавляем поле image_url в таблицу organizations
ALTER TABLE organizations
    ADD COLUMN IF NOT EXISTS image_url VARCHAR(255);

-- Добавляем поле image_url в таблицу reviews
ALTER TABLE reviews
    ADD COLUMN IF NOT EXISTS image_url VARCHAR(255);