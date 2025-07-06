INSERT INTO cities (name) VALUES
                              ('Казань'),
                              ('Екатеринбург');

INSERT INTO streets (name, city_id) VALUES
                                        ('Баумана', (SELECT id FROM cities WHERE name = 'Казань')),
                                        ('Кремлевская', (SELECT id FROM cities WHERE name = 'Казань'));

INSERT INTO streets (name, city_id) VALUES
                                        ('Малышева', (SELECT id FROM cities WHERE name = 'Екатеринбург')),
                                        ('Ленина', (SELECT id FROM cities WHERE name = 'Екатеринбург'));

INSERT INTO buildings (number, street_id) VALUES
                                              ('3', (SELECT id FROM streets WHERE name = 'Баумана' AND city_id = (SELECT id FROM cities WHERE name = 'Казань'))),
                                              ('18', (SELECT id FROM streets WHERE name = 'Кремлевская' AND city_id = (SELECT id FROM cities WHERE name = 'Казань')));

INSERT INTO buildings (number, street_id) VALUES
                                              ('12', (SELECT id FROM streets WHERE name = 'Малышева' AND city_id = (SELECT id FROM cities WHERE name = 'Екатеринбург'))),
                                              ('7', (SELECT id FROM streets WHERE name = 'Ленина' AND city_id = (SELECT id FROM cities WHERE name = 'Екатеринбург')));

INSERT INTO organizations (id, name, type, city_id, street_id, building_id) VALUES
    ('f23ebc99-9c0b-4ef8-bb6d-6bb9bd380a34', 'Чайхана "Сказка Востока"', 'RESTAURANTS_AND_CAFES',
     (SELECT id FROM cities WHERE name = 'Казань'),
     (SELECT id FROM streets WHERE name = 'Баумана' AND city_id = (SELECT id FROM cities WHERE name = 'Казань')),
     (SELECT id FROM buildings WHERE number = '3' AND street_id = (SELECT id FROM streets WHERE name = 'Баумана'))),

    ('f24ebc99-9c0b-4ef8-bb6d-6bb9bd380a35', 'Отель "Татарстан"', 'HOTELS_AND_HOSTELS',
     (SELECT id FROM cities WHERE name = 'Казань'),
     (SELECT id FROM streets WHERE name = 'Кремлевская' AND city_id = (SELECT id FROM cities WHERE name = 'Казань')),
     (SELECT id FROM buildings WHERE number = '18' AND street_id = (SELECT id FROM streets WHERE name = 'Кремлевская')));

INSERT INTO organizations (id, name, type, city_id, street_id, building_id) VALUES
    ('f25ebc99-9c0b-4ef8-bb6d-6bb9bd380a36', 'Кафе "Уральские пельмени"', 'RESTAURANTS_AND_CAFES',
     (SELECT id FROM cities WHERE name = 'Екатеринбург'),
     (SELECT id FROM streets WHERE name = 'Малышева' AND city_id = (SELECT id FROM cities WHERE name = 'Екатеринбург')),
     (SELECT id FROM buildings WHERE number = '12' AND street_id = (SELECT id FROM streets WHERE name = 'Малышева'))),

    ('f26ebc99-9c0b-4ef8-bb6d-6bb9bd380a37', 'Хостел "На Ленина"', 'HOTELS_AND_HOSTELS',
     (SELECT id FROM cities WHERE name = 'Екатеринбург'),
     (SELECT id FROM streets WHERE name = 'Ленина' AND city_id = (SELECT id FROM cities WHERE name = 'Екатеринбург')),
     (SELECT id FROM buildings WHERE number = '7' AND street_id = (SELECT id FROM streets WHERE name = 'Ленина')));

INSERT INTO reviews (id, author_id, source_id, organization_id, title, content, published_at, rating) VALUES
    ('f27ebc99-9c0b-4ef8-bb6d-6bb9bd380a38', 'f28ebc99-9c0b-4ef8-bb6d-6bb9bd380a39', '00000000-0000-0000-0000-000000000001',
     (SELECT id FROM organizations WHERE name = 'Чайхана "Сказка Востока"'),
     'Вкусный плов!',
     'Подача и вкус на высоте. Восточная атмосфера отлично ощущается.',
     '2023-08-12 14:20:00', 5),

    ('f29ebc99-9c0b-4ef8-bb6d-6bb9bd380a40', 'f30ebc99-9c0b-4ef8-bb6d-6bb9bd380a41', '00000000-0000-0000-0000-000000000001',
     (SELECT id FROM organizations WHERE name = 'Кафе "Уральские пельмени"'),
     'Пельмени как у бабушки',
     'Аутентично, недорого, сытно. Отличное место для обеда.',
     '2023-08-20 13:10:00', 4);

INSERT INTO reviews (id, author_id, source_id, organization_id, title, content, published_at, rating, parent_review_id) VALUES
    ('f31ebc99-9c0b-4ef8-bb6d-6bb9bd380a42', 'f32ebc99-9c0b-4ef8-bb6d-6bb9bd380a43', '00000000-0000-0000-0000-000000000001',
     (SELECT id FROM organizations WHERE name = 'Кафе "Уральские пельмени"'),
     'Спасибо за отзыв!',
     'Мы рады, что вам понравилось. Будем ждать вас снова!',
     '2023-08-21 09:00:00',
     0,
     (SELECT id FROM reviews WHERE title = 'Пельмени как у бабушки' LIMIT 1));

INSERT INTO review_reactions (id, review_id, user_id, reaction_type, value) VALUES
    ('f33ebc99-9c0b-4ef8-bb6d-6bb9bd380a44',
     (SELECT id FROM reviews WHERE title = 'Вкусный плов!' LIMIT 1),
     'f34ebc99-9c0b-4ef8-bb6d-6bb9bd380a45', 'LIKE', 'L'),

    ('f35ebc99-9c0b-4ef8-bb6d-6bb9bd380a46',
     (SELECT id FROM reviews WHERE title = 'Пельмени как у бабушки' LIMIT 1),
     'f36ebc99-9c0b-4ef8-bb6d-6bb9bd380a47', 'LIKE', 'L');
