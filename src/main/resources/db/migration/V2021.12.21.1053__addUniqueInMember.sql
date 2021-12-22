ALTER TABLE `members`
    ADD CONSTRAINT `UC_Member` UNIQUE (`party_id`, `user_id`);
