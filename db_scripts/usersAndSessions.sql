CREATE  TABLE IF NOT EXISTS `checkin`.`sessions` (
  `username` VARCHAR(50) NOT NULL,
  `id` BIGINT(10) NOT NULL AUTO_INCREMENT ,
  `session` VARCHAR(45) NULL DEFAULT NULL ,
  `creation_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
  `last_access_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `active` TINYINT(1) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) );


-- -----------------------------------------------------
-- Table `vsms_reflash`.`users`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `checkin`.`users` (
  `username` VARCHAR(50) NOT NULL ,
  `password` VARCHAR(50) NOT NULL ,
  `firstname` VARCHAR(50) NOT NULL ,
  `lastname` VARCHAR(50) NOT NULL ,
  `active` TINYINT(1) NOT NULL ,
  `creation_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`username`) );
