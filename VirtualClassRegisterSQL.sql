-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema VirtualClassRegister
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema VirtualClassRegister
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `VirtualClassRegister` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci ;
USE `VirtualClassRegister` ;

-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`Class`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`Class` (
  `idClass` INT NOT NULL AUTO_INCREMENT,
  `tutor_idUser` INT NOT NULL,
  `name` VARCHAR(65) NOT NULL,
  PRIMARY KEY (`idClass`),
  INDEX `fk_Class_User_idx` (`tutor_idUser` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  CONSTRAINT `fk_Class_User`
    FOREIGN KEY (`tutor_idUser`)
    REFERENCES `VirtualClassRegister`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `idClass` INT NULL,
  `forename` VARCHAR(65) NOT NULL,
  `surname` VARCHAR(85) NOT NULL,
  `email` VARCHAR(120) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` ENUM('STUDENT', 'TEACHER', 'ADMINISTRATOR') NOT NULL,
  `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idUser`),
  INDEX `fk_User_Class1_idx` (`idClass` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_User_Class1`
    FOREIGN KEY (`idClass`)
    REFERENCES `VirtualClassRegister`.`Class` (`idClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`Subject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`Subject` (
  `idSubject` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(85) NOT NULL,
  PRIMARY KEY (`idSubject`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`TeacherTeachesSubject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`TeacherTeachesSubject` (
  `idTeacherTeachesSubject` INT NOT NULL AUTO_INCREMENT,
  `teacher_idUser` INT NOT NULL,
  `idSubject` INT NOT NULL,
  PRIMARY KEY (`idTeacherTeachesSubject`),
  INDEX `fk_TeacherTeachesSubject_User1_idx` (`teacher_idUser` ASC) VISIBLE,
  INDEX `fk_TeacherTeachesSubject_Subject1_idx` (`idSubject` ASC) VISIBLE,
  UNIQUE INDEX `idSubject_UNIQUE` (`idSubject` ASC, `teacher_idUser` ASC),
  CONSTRAINT `fk_TeacherTeachesSubject_User1`
    FOREIGN KEY (`teacher_idUser`)
    REFERENCES `VirtualClassRegister`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TeacherTeachesSubject_Subject1`
    FOREIGN KEY (`idSubject`)
    REFERENCES `VirtualClassRegister`.`Subject` (`idSubject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`Semester`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`Semester` (
  `idSemester` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(65) NOT NULL,
  `start` DATETIME NOT NULL,
  `end` DATETIME NOT NULL,
  PRIMARY KEY (`idSemester`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`Lesson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`Lesson` (
  `idLesson` INT NOT NULL AUTO_INCREMENT,
  `idSemester` INT NOT NULL,
  `idClass` INT NOT NULL,
  `idTeacherTeachesSubject` INT NOT NULL,
  `day` ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
  `start` TIME NOT NULL,
  `end` TIME NOT NULL,
  PRIMARY KEY (`idLesson`),
  INDEX `fk_Lesson_Semester1_idx` (`idSemester` ASC) VISIBLE,
  INDEX `fk_Lesson_Class1_idx` (`idClass` ASC) VISIBLE,
  INDEX `fk_Lesson_TeacherTeachesSubject1_idx` (`idTeacherTeachesSubject` ASC) VISIBLE,
  CONSTRAINT `fk_Lesson_Semester1`
    FOREIGN KEY (`idSemester`)
    REFERENCES `VirtualClassRegister`.`Semester` (`idSemester`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Lesson_Class1`
    FOREIGN KEY (`idClass`)
    REFERENCES `VirtualClassRegister`.`Class` (`idClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Lesson_TeacherTeachesSubject1`
    FOREIGN KEY (`idTeacherTeachesSubject`)
    REFERENCES `VirtualClassRegister`.`TeacherTeachesSubject` (`idTeacherTeachesSubject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`GradeType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`GradeType` (
  `idGradeType` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(65) NOT NULL,
  `weightage` DECIMAL(3,2) NOT NULL,
  PRIMARY KEY (`idGradeType`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`Grade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`Grade` (
  `idGrade` INT NOT NULL AUTO_INCREMENT,
  `student_idUser` INT NOT NULL,
  `idLesson` INT NOT NULL,
  `idGradeType` INT NOT NULL,
  `re-approach_idGrade` INT NULL,
  `value` DECIMAL(3,2) NOT NULL,
  `note` TEXT NULL,
  `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idGrade`),
  INDEX `fk_Grade_GradeType1_idx` (`idGradeType` ASC) VISIBLE,
  INDEX `fk_Grade_Lesson1_idx` (`idLesson` ASC) VISIBLE,
  INDEX `fk_Grade_User1_idx` (`student_idUser` ASC) VISIBLE,
  INDEX `fk_Grade_Grade1_idx` (`re-approach_idGrade` ASC) VISIBLE,
  CONSTRAINT `fk_Grade_GradeType1`
    FOREIGN KEY (`idGradeType`)
    REFERENCES `VirtualClassRegister`.`GradeType` (`idGradeType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Grade_Lesson1`
    FOREIGN KEY (`idLesson`)
    REFERENCES `VirtualClassRegister`.`Lesson` (`idLesson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Grade_User1`
    FOREIGN KEY (`student_idUser`)
    REFERENCES `VirtualClassRegister`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Grade_Grade1`
    FOREIGN KEY (`re-approach_idGrade`)
    REFERENCES `VirtualClassRegister`.`Grade` (`idGrade`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`BehaviourPoints`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`BehaviourPoints` (
  `idBehaviourPoints` INT NOT NULL AUTO_INCREMENT,
  `student_idUser` INT NOT NULL,
  `idClass` INT NOT NULL,
  `idSemester` INT NOT NULL,
  `value` TINYINT NOT NULL,
  `note` TEXT NULL,
  `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idBehaviourPoints`),
  INDEX `fk_BehaviourPoints_Semester1_idx` (`idSemester` ASC) VISIBLE,
  INDEX `fk_BehaviourPoints_Class1_idx` (`idClass` ASC) VISIBLE,
  INDEX `fk_BehaviourPoints_User1_idx` (`student_idUser` ASC) VISIBLE,
  CONSTRAINT `fk_BehaviourPoints_Semester1`
    FOREIGN KEY (`idSemester`)
    REFERENCES `VirtualClassRegister`.`Semester` (`idSemester`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BehaviourPoints_Class1`
    FOREIGN KEY (`idClass`)
    REFERENCES `VirtualClassRegister`.`Class` (`idClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BehaviourPoints_User1`
    FOREIGN KEY (`student_idUser`)
    REFERENCES `VirtualClassRegister`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `VirtualClassRegister`.`Announcement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VirtualClassRegister`.`Announcement` (
  `idAnnouncement` INT NOT NULL AUTO_INCREMENT,
  `idLesson` INT NOT NULL,
  `title` VARCHAR(85) NOT NULL,
  `content` TEXT NOT NULL,
  `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idAnnouncement`),
  INDEX `fk_Announcement_Lesson1_idx` (`idLesson` ASC) VISIBLE,
  CONSTRAINT `fk_Announcement_Lesson1`
    FOREIGN KEY (`idLesson`)
    REFERENCES `VirtualClassRegister`.`Lesson` (`idLesson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
