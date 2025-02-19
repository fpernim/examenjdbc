-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema prueba
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema prueba
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `prueba` DEFAULT CHARACTER SET utf8 ;
USE `prueba` ;

-- -----------------------------------------------------
-- Table `prueba`.`task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prueba`.`task` (
  `task_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`task_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `prueba`.`department`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prueba`.`department` (
  `department_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`department_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `prueba`.`employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prueba`.`employee` (
  `employee_id` INT NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(45) NULL,
  `task_task_id` INT NOT NULL,
  `department_department_id` INT NOT NULL,
  PRIMARY KEY (`employee_id`),
  INDEX `fk_employee_task_idx` (`task_task_id` ASC) VISIBLE,
  INDEX `fk_employee_department1_idx` (`department_department_id` ASC) VISIBLE,
  CONSTRAINT `fk_employee_task`
    FOREIGN KEY (`task_task_id`)
    REFERENCES `prueba`.`task` (`task_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_employee_department1`
    FOREIGN KEY (`department_department_id`)
    REFERENCES `prueba`.`department` (`department_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `prueba`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prueba`.`project` (
  `project_id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`project_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `prueba`.`project_has_employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prueba`.`project_has_employee` (
  `project_project_id` INT NOT NULL,
  `employee_employee_id` INT NOT NULL,
  PRIMARY KEY (`project_project_id`, `employee_employee_id`),
  INDEX `fk_project_has_employee_employee1_idx` (`employee_employee_id` ASC) VISIBLE,
  INDEX `fk_project_has_employee_project1_idx` (`project_project_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_has_employee_project1`
    FOREIGN KEY (`project_project_id`)
    REFERENCES `prueba`.`project` (`project_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_project_has_employee_employee1`
    FOREIGN KEY (`employee_employee_id`)
    REFERENCES `prueba`.`employee` (`employee_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
