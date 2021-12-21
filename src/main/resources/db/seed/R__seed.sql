INSERT INTO otts(id, name, english_name, subscription_fee, monthly_price, max_member_capacity, grade,
          created_at, updated_at)
VALUES
    (1, '넷플릭스', 'netflix', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (2, '왓챠', 'watcha', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (3, '웨이브', 'wavve', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (4, '티빙', 'tving', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (5, '디즈니 플러스', 'disneyPlus', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (6, '라프텔', 'laftel', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (7, '쿠팡 플레이', 'coupangPlay', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (8, '아마존 프라임', 'amazonPlay', 10000, 2500, 4, '프리미엄', '2021-11-01 06:30:00', '2021-11-01 06:30:00') as new_ott
    ON DUPLICATE KEY UPDATE
        name = new_ott.name,
        english_name = new_ott.english_name,
        subscription_fee = new_ott.subscription_fee,
        monthly_price = new_ott.monthly_price,
        max_member_capacity = new_ott.max_member_capacity,
        grade = new_ott.grade,
        created_at = new_ott.created_at,
        updated_at = new_ott.updated_at;

INSERT INTO rules(id, name, created_at, updated_at)
VALUES
    (1, '1인 1회선', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (2, '1인 1기기', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (3, '닉네임과 동일하게 프로필 네임 설정', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (4, '개인 사정 환불 불가', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (5, '계정양도 불가', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (6, '설정 임의 변경 불가', '2021-11-01 06:30:00', '2021-11-01 06:30:00'),
    (7, '19세 이상', '2021-11-01 06:30:00', '2021-11-01 06:30:00') as new_rule
    ON DUPLICATE KEY UPDATE
    name = new_rule.name,
    created_at = new_rule.created_at,
    updated_at = new_rule.updated_at;





