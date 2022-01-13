DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `content`     VARCHAR(255) NOT NULL,
    `read_check`  BOOLEAN      NOT NULL,
    `created_at`  DATETIME     NOT NULL,
    `receiver_id` BIGINT       NOT NULL,
    `party_id`    BIGINT       NOT NULL
);
