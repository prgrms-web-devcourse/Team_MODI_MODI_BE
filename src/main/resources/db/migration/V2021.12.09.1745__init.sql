DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username`      VARCHAR(255) NOT NULL,
    `role`          VARCHAR(255) NOT NULL,
    `date_of_birth` DATE         NOT NULL,
    `points`        INT          NOT NULL,
    `created_at`    DATETIME     NOT NULL,
    `updated_at`    DATETIME     NOT NULL,
    `deleted_at`    DATETIME     NULL,
    `provider`      VARCHAR(255) NULL,
    `provider_id`   VARCHAR(255) NULL
);

DROP TABLE IF EXISTS `parties`;
CREATE TABLE `parties`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `party_member_capacity`     INT          NOT NULL,
    `current_member`            INT          NOT NULL,
    `total_price`               INT          NOT NULL,
    `monthly_reimbursement`     INT          NOT NULL,
    `remaining_reimbursement`   INT          NULL,
    `start_date`                DATE         NOT NULL,
    `end_date`                  DATE         NOT NULL,
    `period`                    INT          NOT NULL,
    `must_filled`               BOOLEAN      NOT NULL,
    `shared_id`                 VARCHAR(255) NULL,
    `shared_password_encrypted` VARCHAR(255) NULL,
    `status`                    VARCHAR(255) NULL,
    `created_at`                DATETIME     NULL,
    `updated_at`                DATETIME     NULL,
    `deleted_at`                DATETIME     NULL,
    `ott_id`                    BIGINT       NOT NULL
);

DROP TABLE IF EXISTS `otts`;
CREATE TABLE `otts`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`                VARCHAR(255) NOT NULL,
    `subscription_fee`    INT          NOT NULL,
    `monthly_price`       INT          NOT NULL,
    `max_member_capacity` INT          NOT NULL,
    `grade`               VARCHAR(255) NOT NULL,
    `created_at`          DATETIME     NOT NULL,
    `updated_at`          DATETIME     NOT NULL
);

DROP TABLE IF EXISTS `members`;
CREATE TABLE `members`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `is_leader`  BOOLEAN  NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    `deleted_at` DATETIME NULL,
    `party_id`   BIGINT   NOT NULL,
    `user_id`    BIGINT   NOT NULL
);

DROP TABLE IF EXISTS `rules`;
CREATE TABLE `rules`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL
);

DROP TABLE IF EXISTS `party_rule`;
CREATE TABLE `party_rule`
(
    `party_id` BIGINT NOT NULL,
    `rule_id`  BIGINT NOT NULL
);

DROP TABLE IF EXISTS `point_history`;
CREATE TABLE `point_history`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `point_detail` VARCHAR(255) NULL,
    `amount`       int          NULL,
    `user_id`      BIGINT       NOT NULL,
    `created_at`   DATETIME     NOT NULL,
    `updated_at`   DATETIME     NOT NULL
);

DROP TABLE IF EXISTS `commission_history`;
CREATE TABLE `commission_history`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `commission_detail` VARCHAR(255) NULL,
    `amount`            int          NULL,
    `user_id`           BIGINT       NOT NULL,
    `created_at`        DATETIME     NULL,
    `updated_at`        DATETIME     NULL
);

ALTER TABLE `party_rule`
    ADD CONSTRAINT `PK_PARTY_RULE` PRIMARY KEY (`party_id`, `rule_id`);

ALTER TABLE `parties`
    ADD CONSTRAINT `FK_otts_TO_parties_1` FOREIGN KEY (`ott_id`)
        REFERENCES `otts` (`id`);

ALTER TABLE `members`
    ADD CONSTRAINT `FK_parties_TO_members_1` FOREIGN KEY (`party_id`)
        REFERENCES `parties` (`id`);

ALTER TABLE `members`
    ADD CONSTRAINT `FK_users_TO_members_1` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`);

ALTER TABLE `party_rule`
    ADD CONSTRAINT `FK_parties_TO_party_rule_1` FOREIGN KEY (`party_id`)
        REFERENCES `parties` (`id`);

ALTER TABLE `party_rule`
    ADD CONSTRAINT `FK_rules_TO_party_rule_1` FOREIGN KEY (`rule_id`)
        REFERENCES `rules` (`id`);

ALTER TABLE `point_history`
    ADD CONSTRAINT `FK_users_TO_point_history_1` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`);

ALTER TABLE `commission_history`
    ADD CONSTRAINT `FK_users_TO_commission_history_1` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`);

