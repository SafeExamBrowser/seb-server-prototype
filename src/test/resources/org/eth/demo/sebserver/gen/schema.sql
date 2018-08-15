
-- -----------------------------------------------------
-- Schema SEBServerDemo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `institution`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `institution` ;

CREATE TABLE IF NOT EXISTS `institution` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `institution_id` BIGINT UNSIGNED NULL,
  `name` VARCHAR(255) NOT NULL,
  `user_name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `active` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `institutionRef_idx` (`institution_id` ASC),
  CONSTRAINT `institutionRef`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `exam`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exam` ;

CREATE TABLE IF NOT EXISTS `exam` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `institution_id` BIGINT UNSIGNED NOT NULL,
  `owner_id` BIGINT UNSIGNED NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(4000) NULL,
  `type` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `start_time` DATETIME NULL,
  `end_time` DATETIME NULL,
  `lms_exam_url` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `examOwnerRef_idx` (`owner_id` ASC),
  INDEX `examinstitutionRef_idx` (`institution_id` ASC),
  CONSTRAINT `examOwnerRef`
    FOREIGN KEY (`owner_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `examinstitutionRef`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `client_connection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `client_connection` ;

CREATE TABLE IF NOT EXISTS `client_connection` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NULL,
  `status` VARCHAR(45) NOT NULL,
  `connection_token` VARCHAR(255) NOT NULL,
  `user_identifier` VARCHAR(255) NOT NULL,
  `client_address` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `connection_exam_ref_idx` (`exam_id` ASC),
  UNIQUE INDEX `client_identifier_UNIQUE` (`user_identifier` ASC),
  CONSTRAINT `clientConnectionExamRef`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `client_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `client_event` ;

CREATE TABLE IF NOT EXISTS `client_event` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_identifier` VARCHAR(255) NOT NULL,
  `type` INT(2) UNSIGNED NOT NULL,
  `timestamp` BIGINT UNSIGNED NOT NULL,
  `numeric_value` DECIMAL(10,4) NULL,
  `text` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `eventUserIdentifierRef_idx` (`user_identifier` ASC),
  CONSTRAINT `eventUserIdentifierRef`
    FOREIGN KEY (`user_identifier`)
    REFERENCES `client_connection` (`user_identifier`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `indicator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `indicator` ;

CREATE TABLE IF NOT EXISTS `indicator` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `threshold1` DECIMAL(10,4) NULL,
  `threshold2` DECIMAL(10,4) NULL,
  `threshold3` DECIMAL(10,4) NULL,
  INDEX `indicator_exam_idx` (`exam_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `exam_ref`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `configuration_node`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `configuration_node` ;

CREATE TABLE IF NOT EXISTS `configuration_node` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `institution_id` BIGINT UNSIGNED NOT NULL,
  `owner_id` BIGINT UNSIGNED NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `configurationInstitutionRef_idx` (`institution_id` ASC),
  CONSTRAINT `configurationInstitutionRef`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `configuration`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `configuration` ;

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `configuration_node_id` BIGINT UNSIGNED NOT NULL,
  `version` VARCHAR(255) NULL,
  `version_date` DATETIME NULL,
  `followup` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `configurationNodeRef_idx` (`configuration_node_id` ASC),
  CONSTRAINT `configurationNodeRef`
    FOREIGN KEY (`configuration_node_id`)
    REFERENCES `configuration_node` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `configuration_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `configuration_attribute` ;

CREATE TABLE IF NOT EXISTS `configuration_attribute` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `parent_id` BIGINT UNSIGNED NULL,
  `resources` VARCHAR(255) NULL,
  `validator` VARCHAR(45) NULL,
  `dependencies` VARCHAR(255) NULL,
  `default_value` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `parent_ref_idx` (`parent_id` ASC),
  CONSTRAINT `parent_ref`
    FOREIGN KEY (`parent_id`)
    REFERENCES `configuration_attribute` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `configuration_value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `configuration_value` ;

CREATE TABLE IF NOT EXISTS `configuration_value` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `configuration_id` BIGINT UNSIGNED NOT NULL,
  `configuration_attribute_id` BIGINT UNSIGNED NOT NULL,
  `list_index` INT NOT NULL DEFAULT 0,
  `value` VARCHAR(255) NULL,
  `text` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `configuration_value_ref_idx` (`configuration_id` ASC),
  INDEX `configuration_attribute_ref_idx` (`configuration_attribute_id` ASC),
  CONSTRAINT `configuration_ref`
    FOREIGN KEY (`configuration_id`)
    REFERENCES `configuration` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `configuration_value_attribute_ref`
    FOREIGN KEY (`configuration_attribute_id`)
    REFERENCES `configuration_attribute` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `orientation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `orientation` ;

CREATE TABLE IF NOT EXISTS `orientation` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `config_attribute_id` BIGINT UNSIGNED NOT NULL,
  `view` VARCHAR(45) NOT NULL,
  `group` VARCHAR(45) NULL,
  `x_position` INT NOT NULL DEFAULT 0,
  `y_position` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `config_attribute_orientation_rev_idx` (`config_attribute_id` ASC),
  CONSTRAINT `config_attribute_orientation_rev`
    FOREIGN KEY (`config_attribute_id`)
    REFERENCES `configuration_attribute` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `exam_configuration_map`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exam_configuration_map` ;

CREATE TABLE IF NOT EXISTS `exam_configuration_map` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NOT NULL,
  `configuration_node_id` BIGINT UNSIGNED NOT NULL,
  `client_info` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `exam_ref_idx` (`exam_id` ASC),
  INDEX `configuration_map_ref_idx` (`configuration_node_id` ASC),
  CONSTRAINT `exam_map_ref`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `configuration_map_ref`
    FOREIGN KEY (`configuration_node_id`)
    REFERENCES `configuration_node` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_role` ;

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_ref_idx` (`user_id` ASC),
  CONSTRAINT `user_ref`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `oauth_access_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `oauth_access_token` ;

CREATE TABLE IF NOT EXISTS `oauth_access_token` (
  `token_id` VARCHAR(255) NULL,
  `token` BLOB NULL,
  `authentication_id` VARCHAR(255) NULL,
  `user_name` VARCHAR(255) NULL,
  `client_id` VARCHAR(255) NULL,
  `authentication` BLOB NULL,
  `refresh_token` VARCHAR(255) NULL);


-- -----------------------------------------------------
-- Table `oauth_refresh_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `oauth_refresh_token` ;

CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (
  `token_id` VARCHAR(255) NULL,
  `token` BLOB NULL,
  `authentication` BLOB NULL);


-- -----------------------------------------------------
-- Table `seb_lms_setup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seb_lms_setup` ;

CREATE TABLE IF NOT EXISTS `seb_lms_setup` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `institution_id` BIGINT UNSIGNED NOT NULL,
  `seb_clientname` VARCHAR(255) NOT NULL,
  `seb_clientsecret` VARCHAR(255) NOT NULL,
  `lms_clientname` VARCHAR(255) NOT NULL,
  `lms_clientsecret` VARCHAR(255) NOT NULL,
  `lms_url` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `setupInstitutionRef_idx` (`institution_id` ASC),
  CONSTRAINT `setupInstitutionRef`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
