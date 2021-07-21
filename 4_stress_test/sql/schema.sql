CREATE TABLE `transaction`
(
    `id`    int(11) NOT NULL AUTO_INCREMENT,
    `uuid`  varchar(36) NOT NULL,
    `value` bigint(11) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX (`value`),
    INDEX (`uuid`)
);