-- Вставляем города
INSERT INTO cities (name) VALUES
                              ('Тверь'),
                              ('Москва'),
                              ('Санкт-Петербург');

-- Вставляем улицы для Твери
INSERT INTO streets (name, city_id) VALUES
                                        ('Советская', (SELECT id FROM cities WHERE name = 'Тверь')),
                                        ('Трехсвятская', (SELECT id FROM cities WHERE name = 'Тверь')),
                                        ('Новоторжская', (SELECT id FROM cities WHERE name = 'Тверь')),
                                        ('Беляковский переулок', (SELECT id FROM cities WHERE name = 'Тверь'));

-- Вставляем улицы для других городов
INSERT INTO streets (name, city_id) VALUES
                                        ('Тверская', (SELECT id FROM cities WHERE name = 'Москва')),
                                        ('Арбат', (SELECT id FROM cities WHERE name = 'Москва')),
                                        ('Невский проспект', (SELECT id FROM cities WHERE name = 'Санкт-Петербург'));

-- Вставляем здания в Твери
INSERT INTO buildings (number, street_id) VALUES
                                              ('10', (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Советская' AND c.name = 'Тверь')),
                                              ('22', (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Советская' AND c.name = 'Тверь')),
                                              ('15', (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Трехсвятская' AND c.name = 'Тверь')),
                                              ('5', (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Беляковский переулок' AND c.name = 'Тверь')));

-- Вставляем здания в других городах
INSERT INTO buildings (number, street_id) VALUES
                                              ('1', (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Тверская' AND c.name = 'Москва')),
                                              ('25', (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Невский проспект' AND c.name = 'Санкт-Петербург')));

-- Вставляем организации в Твери
INSERT INTO organizations (id, name, type, city_id, street_id, building_id) VALUES
                                                                                (gen_random_uuid(), 'Ресторан "Волга"', 'RESTAURANTS_AND_CAFES',
                                                                                 (SELECT id FROM cities WHERE name = 'Тверь'),
                                                                                 (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Советская' AND c.name = 'Тверь'),
                                                                                 (SELECT b.id FROM buildings b JOIN streets s ON b.street_id = s.id WHERE b.number = '10' AND s.name = 'Советская')),

                                                                                (gen_random_uuid(), 'Кафе "Старый мост"', 'RESTAURANTS_AND_CAFES',
                                                                                 (SELECT id FROM cities WHERE name = 'Тверь'),
                                                                                 (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Трехсвятская' AND c.name = 'Тверь'),
                                                                                 (SELECT b.id FROM buildings b JOIN streets s ON b.street_id = s.id WHERE b.number = '15' AND s.name = 'Трехсвятская')),

                                                                                (gen_random_uuid(), 'Гостиница "Оснабрюк"', 'HOTELS',
                                                                                 (SELECT id FROM cities WHERE name = 'Тверь'),
                                                                                 (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Советская' AND c.name = 'Тверь'),
                                                                                 (SELECT b.id FROM buildings b JOIN streets s ON b.street_id = s.id WHERE b.number = '22' AND s.name = 'Советская'));

-- Вставляем организации в других городах
INSERT INTO organizations (id, name, type, city_id, street_id, building_id) VALUES
                                                                                (gen_random_uuid(), 'Ресторан "Пушкин"', 'RESTAURANTS_AND_CAFES',
                                                                                 (SELECT id FROM cities WHERE name = 'Москва'),
                                                                                 (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Тверская' AND c.name = 'Москва'),
                                                                                 (SELECT b.id FROM buildings b JOIN streets s ON b.street_id = s.id WHERE b.number = '1' AND s.name = 'Тверская')),

                                                                                (gen_random_uuid(), 'Кафе "Север"', 'RESTAURANTS_AND_CAFES',
                                                                                 (SELECT id FROM cities WHERE name = 'Санкт-Петербург'),
                                                                                 (SELECT s.id FROM streets s JOIN cities c ON s.city_id = c.id WHERE s.name = 'Невский проспект' AND c.name = 'Санкт-Петербург'),
                                                                                 (SELECT b.id FROM buildings b JOIN streets s ON b.street_id = s.id WHERE b.number = '25' AND s.name = 'Невский проспект'));

-- Вставляем отзывы (авторские ID и source_id генерируются случайно)
INSERT INTO reviews (id, author_id, source_id, organization_id, title, content, published_at, rating) VALUES
                                                                                                          (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(),
                                                                                                           (SELECT id FROM organizations WHERE name = 'Ресторан "Волга"'),
                                                                                                           'Отличное место!',
                                                                                                           'Очень понравилась кухня и обслуживание. Вид на Волгу просто потрясающий!',
                                                                                                           '2023-05-15 18:30:00', 5),

                                                                                                          (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(),
                                                                                                           (SELECT id FROM organizations WHERE name = 'Кафе "Старый мост"'),
                                                                                                           'Уютное кафе',
                                                                                                           'Приятная атмосфера, вкусный кофе, но немного шумно из-за расположения в центре.',
                                                                                                           '2023-06-20 12:15:00', 4),

                                                                                                          (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(),
                                                                                                           (SELECT id FROM organizations WHERE name = 'Гостиница "Оснабрюк"'),
                                                                                                           'Хороший сервис',
                                                                                                           'Номера чистые, персонал вежливый. Расположение в центре Твери очень удобное.',
                                                                                                           '2023-07-10 09:45:00', 4);

-- Вставляем ответы на отзывы (parent_review_id)
INSERT INTO reviews (id, author_id, source_id, organization_id, title, content, published_at, parent_review_id) VALUES
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(),
     (SELECT id FROM organizations WHERE name = 'Ресторан "Волга"'),
     'Ответ администрации',
     'Благодарим за отзыв! Будем рады видеть вас снова!',
     '2023-05-16 10:00:00',
     (SELECT id FROM reviews WHERE title = 'Отличное место!' LIMIT 1));

-- Вставляем реакции на отзывы
INSERT INTO review_reactions (id, review_id, user_id, reaction_type, value) VALUES
                                                                                (gen_random_uuid(),
                                                                                 (SELECT id FROM reviews WHERE title = 'Отличное место!' LIMIT 1),
    gen_random_uuid(), 'LIKE', 'L'),

(gen_random_uuid(),
 (SELECT id FROM reviews WHERE title = 'Уютное кафе' LIMIT 1),
 gen_random_uuid(), 'LIKE', 'L'),

(gen_random_uuid(),
 (SELECT id FROM reviews WHERE title = 'Уютное кафе' LIMIT 1),
 gen_random_uuid(), 'LIKE', 'L');