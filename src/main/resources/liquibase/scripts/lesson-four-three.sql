-- liquibase formatted sql
-- changeset akislitcin:1
CREATE INDEX idx_student_name ON student (name);
-- changeset akislitcin:2
CREATE INDEX idx_faculty_name_color ON faculty (name, color);