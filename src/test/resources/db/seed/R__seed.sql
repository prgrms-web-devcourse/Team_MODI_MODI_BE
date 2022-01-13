INSERT INTO users(id, username, role, points, created_at, updated_at, deleted_at, provider,
                  provider_id)
VALUES (1, '테스트 유저1', 'USER', 0, '2021-11-01 06:30:00', '2021-11-01 06:30:00', null, 'naver',
        'naver_test_provider_id'),
       (2, '테스트 유저2', 'USER', 0, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, null, null),
       (3, '테스트 유저3', 'USER', 0, '2021-11-03 06:30:00', '2021-11-03 06:30:00', null, null, null),
       (4, '테스트 유저4', 'USER', 0, '2021-11-04 06:30:00', '2021-11-04 06:30:00', null, null, null),
       (5, '테스트 유저5', 'ADMIN', 0, '2021-11-05 06:30:00', '2021-11-05 06:30:00', null, null, null);

INSERT INTO otts(id, name, english_name, subscription_fee, monthly_price, max_member_capacity,
                 grade, created_at, updated_at)
VALUES (1, '넷플릭스', 'netflix', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (2, '왓챠', 'watcha', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (3, '웨이브', 'wavve', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (4, '티빙', 'tving', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (5, '디즈니 플러스', 'disneyPlus', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00',
        '2021-11-01 06:30:00'),
       (6, '라프텔', 'laftel', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (7, '쿠팡 플레이', 'coupangPlay', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00',
        '2021-11-01 06:30:00'),
       (8, '아마존 프라임', 'amazonPlay', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00',
        '2021-11-01 06:30:00');

INSERT INTO parties(id, party_member_capacity, current_member, total_price, monthly_reimbursement,
                    remaining_reimbursement, start_date, end_date, period, must_filled, shared_id,
                    shared_password_encrypted, status, created_at, updated_at, deleted_at, version,
                    ott_id)
VALUES (1, 4, 4, 10000, 5000, 25000, '2021-11-02', '2022-05-02', 6, 1, 'modi112@gmail.com',
        'ATEz+3fEwPVvr3MkcxjwPPdVwlMSX01oK4CEp07ZCZePRfyFiA==', 'ONGOING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', null, 1, 1),
       (2, 4, 4, 10000, 5000, 25000, '2021-11-02', '2022-05-02', 6, 1, 'modi112@gmail.com',
        'ATEz+3cGsXUFDlpstFXUOb1UO0UL+xVXx29/DVeY0nfcosxzrg==', 'ONGOING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', null, 1, 1),
       (3, 4, 4, 10000, 5000, 25000, '2021-11-02', '2022-05-02', 6, 1, 'modi112@gmail.com',
        'ATEz+3fEwPVvr3MkcxjwPPdVwlMSX01oK4CEp07ZCZePRfyFiA==', 'ONGOING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', null, 1, 1),
       (4, 4, 2, 10000, 0, 0, '2021-12-26', '2022-05-02', 5, 1, 'modi112@gmail.com',
        'ATEz+3cGsXUFDlpstFXUOb1UO0UL+xVXx29/DVeY0nfcosxzrg==', 'RECRUITING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', null, 1, 1),
       (5, 4, 2, 10000, 0, 0, '2021-12-26', '2022-05-02', 5, 1, 'modi112@gmail.com',
        'ATEz+3fEwPVvr3MkcxjwPPdVwlMSX01oK4CEp07ZCZePRfyFiA==', 'RECRUITING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', null, 1, 1),
       (6, 4, 2, 100000, 0, 0, '2021-12-26', '2022-05-02', 5, 1, 'modi112@gmail.com',
        'ATEz+3cGsXUFDlpstFXUOb1UO0UL+xVXx29/DVeY0nfcosxzrg==', 'RECRUITING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', null, 1, 1),
       (7, 4, 2, 100000, 0, 0, '2021-12-26', '2022-05-02', 5, 1, 'modi112@gmail.com',
        'ATEz+3fEwPVvr3MkcxjwPPdVwlMSX01oK4CEp07ZCZePRfyFiA==', 'RECRUITING', '2021-11-02 06:30:00',
        '2021-11-01 06:30:00', '2021-11-02 06:30:00', 1, 1),
       (8, 4, 1, 100000, 0, 0, '2022-02-12', '2022-05-22', 3, 1, 'modi112@gmail.com', 'modi',
        'RECRUITING', '2021-11-02 06:30:00', '2021-11-01 06:30:00', null, 1, 1);

INSERT INTO rules(id, name, created_at, updated_at)
VALUES (1, '1인 1회선', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (2, '1인 1기기', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (3, '닉네임과 동일하게 프로필 네임 설정', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (4, '개인 사정 환불 불가', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (5, '계정양도 불가', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (6, '설정 임의 변경 불가', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
       (7, '19세 이상', '2021-11-01 06:30:00', '2021-11-01 06:30:00');

INSERT INTO party_rule(party_id, rule_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2);


INSERT INTO members(id, is_leader, created_at, updated_at, deleted_at, party_id, user_id)
VALUES (1, 1, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 1, 1),
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
       (16, 0, '2021-12-12 06:30:00', '2021-12-12 06:30:00', null, 5, 2),

       (17, 1, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 8, 1),
       (18, 1, '2021-11-02 06:30:00', '2021-11-02 06:30:00', null, 6, 2);
