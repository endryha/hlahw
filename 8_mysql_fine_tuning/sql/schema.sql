CREATE TABLE `user`
(
    `id`    int(11) NOT NULL AUTO_INCREMENT,
    `first_name`  varchar(50) NOT NULL,
    `last_name` varchar(50) NOT NULL,
    `birthdate` DATE NOT NULL,
    `address` varchar(255) NOT NULL,
    `job` varchar(255) NOT NULL,
    `phone` varchar(25),
    `email` varchar(50),
    PRIMARY KEY (`id`)
);