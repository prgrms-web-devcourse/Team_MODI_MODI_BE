ALTER TABLE `members`
    DROP CONSTRAINT `FK_parties_TO_members_1`;

ALTER TABLE `members`
    ADD CONSTRAINT `FK_parties_TO_members_1` FOREIGN KEY (`party_id`)
        REFERENCES `parties` (`id`) ON DELETE CASCADE;

ALTER TABLE `party_rule`
    DROP CONSTRAINT `FK_parties_TO_party_rule_1`;

ALTER TABLE `party_rule`
    ADD CONSTRAINT `FK_parties_TO_party_rule_1` FOREIGN KEY (`party_id`)
        REFERENCES `parties` (`id`) ON DELETE CASCADE;
