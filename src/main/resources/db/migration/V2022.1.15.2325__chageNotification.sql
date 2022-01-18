ALTER TABLE `notifications`
    DROP COLUMN `receiver_id`;
ALTER TABLE `notifications`
    ADD `member_id` BIGINT NOT NULL;
