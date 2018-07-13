-- MySQL Script generated by MySQL Workbench
-- Mon May  7 09:02:49 2018
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema SEBServerDemo
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Table `exam`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exam` ;

CREATE TABLE IF NOT EXISTS `exam` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `status` INT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `client_connection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `client_connection` ;

CREATE TABLE IF NOT EXISTS `client_connection` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `connection_exam_ref_idx` (`exam_id` ASC),
  CONSTRAINT `client_connection_exam_ref`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `client_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `client_event` ;

CREATE TABLE IF NOT EXISTS `client_event` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NOT NULL,
  `client_id` BIGINT UNSIGNED NOT NULL,
  `type` INT(2) UNSIGNED NOT NULL,
  `timestamp` BIGINT UNSIGNED NOT NULL,
  `numeric_value` DECIMAL(10,4) NULL,
  `text` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `client_connection_ref_idx` (`client_id` ASC),
  CONSTRAINT `client_connection_ref`
    FOREIGN KEY (`client_id`)
    REFERENCES `client_connection` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `configuration`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `configuration` ;

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


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
  PRIMARY KEY (`id`),
  INDEX `parent_ref_idx` (`parent_id` ASC),
  CONSTRAINT `parent_ref`
    FOREIGN KEY (`parent_id`)
    REFERENCES `configuration_attribute` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `exam_configuration_map`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exam_configuration_map` ;

CREATE TABLE IF NOT EXISTS `exam_configuration_map` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NOT NULL,
  `configuration_id` BIGINT UNSIGNED NOT NULL,
  `client_info` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `exam_ref_idx` (`exam_id` ASC),
  INDEX `configuration_exam_ref_idx` (`configuration_id` ASC),
  CONSTRAINT `exam_map_ref`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `configuration_map_ref`
    FOREIGN KEY (`configuration_id`)
    REFERENCES `configuration` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT UNSIGNED NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `user_name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `active` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `userName_UNIQUE` (`user_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_role` ;

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` BIGINT UNSIGNED NOT NULL,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_ref_idx` (`user_id` ASC),
  CONSTRAINT `user_ref`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
