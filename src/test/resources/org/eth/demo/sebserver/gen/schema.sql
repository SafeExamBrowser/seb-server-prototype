-- -----------------------------------------------------
-- Table `exam`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exam` ;

CREATE TABLE IF NOT EXISTS `exam` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `status` INT(1) NOT NULL,
  `configuration_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `client_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `client_event` ;

CREATE TABLE IF NOT EXISTS `client_event` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `exam_id` BIGINT UNSIGNED NOT NULL,
  `client_id` BIGINT NOT NULL,
  `type` INT(2) UNSIGNED NOT NULL,
  `timestamp` BIGINT UNSIGNED NOT NULL,
  `numeric_value` DECIMAL(10,4) NULL,
  `text` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `ClientEventExamID_idx` (`exam_id` ASC),
  CONSTRAINT `client_event_exam_ref`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
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
  INDEX `ExamRef_idx` (`exam_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `exam_ref`
    FOREIGN KEY (`exam_id`)
    REFERENCES `exam` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `indicator_value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `indicator_value` ;

CREATE TABLE IF NOT EXISTS `indicator_value` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `indicator_id` BIGINT UNSIGNED NOT NULL,
  `client_id` BIGINT NOT NULL,
  `value` DECIMAL(10,4) NULL,
  `timestamp` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ExamIndicatorRef_idx` (`indicator_id` ASC),
  CONSTRAINT `indicator_ref`
    FOREIGN KEY (`indicator_id`)
    REFERENCES `indicator` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

