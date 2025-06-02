-- ================================
-- Вставка организаций
-- ================================
INSERT INTO organizations (id, name, city, type, address) VALUES
-- Москва
('fa7a9b47-bd14-4ff4-b58f-0e43197e1a01', 'Кафе Пушкинъ', 'Москва', 'RESTAURANTS_AND_CAFES', 'Тверской бульвар, 26А'),
('cd403508-0c43-4fe2-8dbf-12ac6d918d02', 'Кинотеатр Октябрь', 'Москва', 'CINEMA_AND_CONCERTS', 'ул. Новый Арбат, 24'),
('88e8fe38-3c77-4f44-8b71-c1c6c241bc03', 'Парк Горького', 'Москва', 'PARKS_AND_MUSEUMS', 'ул. Крымский Вал, 9'),
('62a0c8f1-31f4-4cf4-9c68-9c4e94f6c404', 'ГУМ', 'Москва', 'SHOPPING_AND_STORES', 'Красная площадь, 3'),
('8a989f31-1659-4bfc-99f9-2585b73e6a05', 'Отель Метрополь', 'Москва', 'HOTELS_AND_HOSTELS', 'Театральный проезд, 2'),

-- Санкт-Петербург
('9271e9a1-3086-4f6f-a626-9f473ac69b06', 'Кафе Zoom', 'Санкт-Петербург', 'RESTAURANTS_AND_CAFES', 'ул. Гороховая, 22'),
('237c3ae6-0d3d-4056-8613-c16034b40607', 'Кинотеатр Аврора', 'Санкт-Петербург', 'CINEMA_AND_CONCERTS', 'Невский проспект, 60'),
('03f0c738-d54c-4f4d-bbcf-0639fa58a508', 'Эрмитаж', 'Санкт-Петербург', 'PARKS_AND_MUSEUMS', 'Дворцовая пл., 2'),
('1ebd8729-55cb-4b60-8d95-2ff24b409409', 'Галерея СПб', 'Санкт-Петербург', 'SHOPPING_AND_STORES', 'Лиговский пр., 30А'),
('4fa38050-1f7e-4b77-8144-1c6d01e8ab10', 'Отель Астория', 'Санкт-Петербург', 'HOTELS_AND_HOSTELS', 'ул. Большая Морская, 39'),

-- Казань
('aa1e9d3f-0aa0-4ae1-9cd1-9fcb1539c911', 'Бар Соль', 'Казань', 'RESTAURANTS_AND_CAFES', 'ул. Баумана, 21'),
('f3e6277e-10d6-4bd6-b8e6-59cb91c16712', 'Кинотеатр Родина', 'Казань', 'CINEMA_AND_CONCERTS', 'ул. Петербургская, 1'),
('f8f932e5-8c9d-4c8a-93dc-5fddf2bbd213', 'Казанский Кремль', 'Казань', 'PARKS_AND_MUSEUMS', 'Кремль'),
('43f70c59-7d4f-4c80-a7cc-b51e52ea2614', 'ТРЦ Мега', 'Казань', 'SHOPPING_AND_STORES', 'Оренбургский тракт, 22'),
('94a708bb-ff19-4c76-a6ac-1579d823fd15', 'Отель Корстон', 'Казань', 'HOTELS_AND_HOSTELS', 'ул. Ершова, 1А');

-- ================================
-- Вставка отзывов
-- ================================
INSERT INTO reviews (id, author_id, source_id, organization_id, title, content, published_at, parent_review_id, rating) VALUES
('a0cb9e6e-d88c-4a7a-bacc-ec87d731eb01', 'bcc6f9bb-b2ab-4c9e-a3c4-1c08e76ec101', 'be89ec23-d7aa-44cc-a3ed-f05a2b201001', 'fa7a9b47-bd14-4ff4-b58f-0e43197e1a01',
 'Прекрасное кафе', 'Очень атмосферно, вкусно и уютно. Вернусь ещё!', '2024-10-12 14:00:00', NULL, 5),

('a0cb9e6e-d88c-4a7a-bacc-ec87d731eb02', 'bcc6f9bb-b2ab-4c9e-a3c4-1c08e76ec102', 'be89ec23-d7aa-44cc-a3ed-f05a2b201001', '237c3ae6-0d3d-4056-8613-c16034b40607',
 'Хорошее кино', 'Фильм отличный, кресла удобные. Но попкорн дорогой.', '2024-11-03 18:30:00', NULL, 4),

('a0cb9e6e-d88c-4a7a-bacc-ec87d731eb03', 'bcc6f9bb-b2ab-4c9e-a3c4-1c08e76ec103', 'be89ec23-d7aa-44cc-a3ed-f05a2b201001', 'f8f932e5-8c9d-4c8a-93dc-5fddf2bbd213',
 'Историческое место', 'Кремль прекрасен, но хотелось бы больше указателей.', '2024-12-01 12:00:00', NULL, 4),

('a0cb9e6e-d88c-4a7a-bacc-ec87d731eb04', 'bcc6f9bb-b2ab-4c9e-a3c4-1c08e76ec104', 'be89ec23-d7aa-44cc-a3ed-f05a2b201001', 'cd403508-0c43-4fe2-8dbf-12ac6d918d02',
 'Ожидал большего', 'Октябрь — легенда, но зал нуждается в ремонте.', '2024-08-20 19:45:00', NULL, 3),

('a0cb9e6e-d88c-4a7a-bacc-ec87d731eb05', 'bcc6f9bb-b2ab-4c9e-a3c4-1c08e76ec105', 'be89ec23-d7aa-44cc-a3ed-f05a2b201001', 'fa7a9b47-bd14-4ff4-b58f-0e43197e1a01',
 'Согласен!', 'Тоже был недавно — все понравилось!', '2024-10-13 10:20:00', 'a0cb9e6e-d88c-4a7a-bacc-ec87d731eb01', 5);

-- ================================
-- Вставка реакций на отзывы
-- ================================
INSERT INTO review_reactions (id, review_id, user_id, reaction_type, value, created_at) VALUES
('5c8a7800-539e-4c1b-81b7-fc2fef412c01', 'a0cb9e6e-d88c-4a7a-bacc-ec87d731eb01', '96b1783e-8e4d-4db1-a798-e1dc69ea9001', 'LIKE', '+', now()),
('5c8a7800-539e-4c1b-81b7-fc2fef412c02', 'a0cb9e6e-d88c-4a7a-bacc-ec87d731eb02', '96b1783e-8e4d-4db1-a798-e1dc69ea9002', 'LIKE', '+', now()),
('5c8a7800-539e-4c1b-81b7-fc2fef412c03', 'a0cb9e6e-d88c-4a7a-bacc-ec87d731eb01', '96b1783e-8e4d-4db1-a798-e1dc69ea9003', 'DISLIKE', '-', now()),
('5c8a7800-539e-4c1b-81b7-fc2fef412c04', 'a0cb9e6e-d88c-4a7a-bacc-ec87d731eb04', '96b1783e-8e4d-4db1-a798-e1dc69ea9004', 'LIKE', '+', now());
