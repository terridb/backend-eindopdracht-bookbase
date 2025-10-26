INSERT INTO authors (first_name, middle_names, last_name, display_name, date_of_birth)
VALUES ('Sarah', 'Janet', 'Maas', 'Sarah J. Maas', '1986-03-05'),
       ('Suzanne', NULL, 'Collins', 'Suzanne Collins', '1962-08-10'),
       ('Ali', NULL, 'Hazelwood', 'Ali Hazelwood', '1989-12-11'),
       ('Rebecca', NULL, 'Yarros', 'Rebecca Yarros', '1982-04-14'),
       ('Taylor', 'Jenkins', 'Reid', 'Taylor Jenkins Reid', '1983-12-20'),
       ('Leigh', NULL, 'Bardugo', 'Leigh Bardugo', '1975-04-06'),
       ('Colleen', NULL, 'Hoover', 'Colleen Hoover', '1979-12-11'),
       ('Madeline', NULL, 'Miller', 'Madeline Miller', '1982-07-24');

INSERT INTO books (title, isbn, image_url, genre)
VALUES ('A Court of Thorns and Roses', '9781635575569', NULL, 'FANTASY'),
       ('A Court of Mist and Fury', '9781635575583', NULL, 'FANTASY'),
       ('The Hunger Games', '9780439023481', NULL, 'DYSTOPIAN'),
       ('Catching Fire', '9780439023498', NULL, 'DYSTOPIAN'),
       ('The Love Hypothesis', '9780593336823', NULL, 'ROMANCE'),
       ('Fourth Wing', '9781649374042', NULL, 'FANTASY'),
       ('The Seven Husbands of Evelyn Hugo', '9781501161933', NULL, 'ROMANCE'),
       ('Shadow and Bone', '9780805094596', NULL, 'FANTASY'),
       ('It Ends With Us', '9781501110368', NULL, 'ROMANCE'),
       ('The Song of Achilles', '9780062060624', NULL, 'FANTASY');

INSERT INTO book_authors (book_id, author_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 3),
       (6, 4),
       (7, 5),
       (8, 6),
       (9, 7),
       (10, 8);

INSERT INTO book_copies (book_id, tracking_number)
VALUES
    (1, 'BB-1-1'),
    (1, 'BB-1-2'),
    (2, 'BB-2-1'),
    (3, 'BB-3-1'),
    (3, 'BB-3-2'),
    (4, 'BB-4-1'),
    (5, 'BB-5-1'),
    (5, 'BB-5-2'),
    (6, 'BB-6-1'),
    (6, 'BB-6-2'),
    (7, 'BB-7-1'),
    (8, 'BB-8-1'),
    (8, 'BB-8-2'),
    (9, 'BB-9-1'),
    (10, 'BB-10-1');

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Karel', 'Kiwi', 'karel.kiwi@test.nl', '0612345678', 'test123', 'MEMBER'),
       ('Mara', 'Mango', 'mara.mango@test.nl', '0687654321', 'test123', 'MEMBER'),
       ('Barrie', 'Bosbes', 'barrie.bosbes@test.nl', '0658468953', 'test123', 'LIBRARIAN'),
       ('Pino', 'Pruim', 'pino.pruim@test.nl', '0626497852', 'test123', 'EMPLOYEE'),
       ('Ella', 'Eland', 'ella.eland@test.nl', '0665432198', 'test123', 'MEMBER'),
       ('Noah', 'Noten', 'noah.noten@test.nl', '0676543219', 'test123', 'MEMBER'),
       ('Lisa', 'Langpootmug', 'lisa.langpootmug@test.nl', '0611223344', 'test123', 'LIBRARIAN');

-- INSERT INTO reservations (reservation_date, reservation_status, user_id, book_copy_id)
-- VALUES ('2025-10-20', 'READY_FOR_PICKUP', 1, 1),
--        ('2025-10-19', 'COLLECTED', 2, 3),
--        ('2025-10-21', 'PENDING', 5, 9),
--        ('2025-10-21', 'READY_FOR_PICKUP', 6, 13),
--        ('2025-10-18', 'EXPIRED', 1, 11),
--        ('2025-10-17', 'COLLECTED', 2, 5);
--
INSERT INTO loans (loan_date, loan_period_in_days, loan_status, book_copy_id, user_id)
VALUES ('2025-09-01', 21, 'OVERDUE', 4, 1),
       ('2025-10-01', 21, 'ACTIVE', 3, 2),
       ('2025-09-10', 21, 'RETURNED', 5, 3),
       ('2025-09-25', 21, 'ACTIVE', 10, 5),
       ('2025-09-30', 21, 'ACTIVE', 13, 6),
       ('2025-09-18', 21, 'OVERDUE', 15, 1);

-- INSERT INTO fines (fine_amount, payment_status, loan_id)
-- VALUES (9.80, 'PAID', 1),
--        (5.50, 'PAID', 3),
--        (8.25, 'OVERDUE', 4);
