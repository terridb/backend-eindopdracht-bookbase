INSERT INTO authors (first_name, middle_names, last_name, date_of_birth)
VALUES ('Sarah', 'Janet', 'Maas', '1986-03-05'),
       ('Suzanne', NULL, 'Collins', '1962-08-10'),
       ('Ali', NULL, 'Hazelwood', '1989-12-11'),
       ('Rebecca', NULL, 'Yarros', '1982-04-14'),
       ('James', NULL, 'Yarros', '1980-09-02');

INSERT INTO books (title, isbn, image_url, genre)
VALUES ('A Court of Thorns and Roses', '9781635575569', NULL, 'FANTASY'),
       ('A Court of Mist and Fury', '9781635575583', NULL, 'FANTASY'),
       ('The Love Hypothesis', '9780593336823', NULL, 'ROMANCE'),
       ('The Hungergames', '9780439023481', NULL, 'DYSTOPIAN'),
       ('Fourth Wing', '9781649374042', NULL, 'FANTASY');

INSERT INTO book_authors (book_id, author_id)
VALUES (1, 1),
       (2, 1),
       (3, 3),
       (4, 2),
       (5, 4),
       (5, 5);

INSERT INTO book_copies (book_id, tracking_number)
VALUES (1, NULL),
       (1, NULL),
       (1, NULL),
       (2, NULL),
       (2, NULL),
       (3, NULL),
       (4, NULL),
       (4, NULL),
       (5, NULL);

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Karel', 'Kiwi', 'member1@test.nl', '0612345678', 'test123', 'MEMBER'),
       ('Mara', 'Mango', 'member2@test.nl', '0687654321', 'test123', 'MEMBER'),
       ('Barrie', 'Bosbes', 'librarian1@test.nl', '0658468953', 'test123', 'LIBRARIAN'),
       ('Pino', 'Pruim', 'employee1@test.nl', '0626497852', 'test123', 'EMPLOYEE');

INSERT INTO reservations (reservation_date, reservation_status, user_id, book_copy_id)
VALUES ('2025-10-13', 'READY_FOR_PICKUP', 1, 1),
       ('2025-10-12', 'COLLECTED', 2, 3);

INSERT INTO loans (loan_date, loan_period_in_days, loan_status, book_copy_id, user_id)
VALUES ('2025-09-01', 21, 'OVERDUE', 4, 1),
       ('2025-10-12', 21, 'ACTIVE', 3, 2);

INSERT INTO fines (fine_amount, payment_status, loan_id)
VALUES (19.99, 'PAID', 1);