INSERT INTO users(id, username, role, date_of_birth, points, created_at, updated_at, deleted_at, provider, provider_id)
VALUES
    (1, '테스트 유저1', 'USER', '1997-01-13', 0, '2021-11-01 06:30:00', '2021-11-01 06:30:00', null, null, null),
    (2, '테스트 유저2', 'USER', '1997-02-13', 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, null, null),
    (3, '테스트 유저3', 'USER', '1997-03-13', 0, '2021-11-03 06:30:00', '2021-11-03 06:30:00', null, null, null),
    (4, '테스트 유저4', 'USER', '1997-04-13', 0, '2021-11-04 06:30:00', '2021-11-04 06:30:00', null, null, null),
    (5, '테스트 유저5', 'ADMIN', '1997-05-13', 0, '2021-11-05 06:30:00', '2021-11-05 06:30:00', null, null, null);

INSERT INTO otts(id, name, subscription_fee, monthly_fee, max_member_capacity, grade, created_at, updated_at)
VALUES
    (1, '넷플릭스', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (2, '디즈니+', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (3, '웨이브', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (4, '와챠', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (5, '티빙', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00');

INSERT INTO parties(id, party_member_capacity, current_member, total_fee, monthly_reimbursement, remaining_reimbursement, start_date, end_date,
                    must_filled, shared_id, shared_password_encrypted, status, created_at, updated_at, deleted_at, ott_id)
VALUES
    (1, 4, 4, 10000, 5000, 25000, '2021-11-02', '2022-05-02', 1, 'modi112@gmail.com', 'modi', 'ONGOING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 1),
    (2, 4, 4, 10000, 5000, 25000, '2021-11-02', '2022-05-02', 1, 'modi112@gmail.com', 'modi', 'ONGOING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 1),
    (3, 4, 4, 10000, 5000, 25000, '2021-11-02', '2022-05-02', 1, 'modi112@gmail.com', 'modi', 'ONGOING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 1),
    (4, 4, 2, 10000, null, null, '2021-12-26', '2022-05-02', 1, 'modi112@gmail.com', 'modi', 'RECRUITING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 1),
    (5, 4, 2, 10000, null, null, '2021-12-26', '2022-05-02', 1, 'modi112@gmail.com', 'modi', 'RECRUITING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 1),
    (6, 4, 2, 10000, null, null, '2021-12-12', '2022-05-02', 1, 'modi112@gmail.com', 'modi', 'RECRUITING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 2);

INSERT INTO rules(id, name, created_at, updated_at)
VALUES
    (1, '1인 1회선', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (2, '1인 2회선', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (3, '양도 금지', '2021-11-01 06:30:00', '2021-11-01 06:30:00');

INSERT INTO party_rule(party_id, rule_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1),
    (2, 2);

INSERT INTO members(id, is_leader, created_at, updated_at, deleted_at, party_id, user_id)
VALUES
    (1, 1, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 1, 1),
    (2, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 1, 2),
    (3, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 1, 3),
    (4, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 1, 4),

    (5, 1, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 2, 1),
    (6, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 2, 2),
    (7, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 2, 3),
    (8, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 2, 4),

    (9, 1, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 3, 1),
    (10, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 3, 2),
    (11, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 3, 3),
    (12, 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 3, 4),

    (13, 1, '2021-12-12 06:30:00', '2021-12-12 06:30:00', null, 4, 1),
    (14, 0, '2021-12-12 06:30:00', '2021-12-12 06:30:00', null, 4, 2),

    (15, 1, '2021-12-12 06:30:00', '2021-12-12 06:30:00', null, 5, 1),
    (16, 0, '2021-12-12 06:30:00', '2021-12-12 06:30:00', null, 5, 2);




