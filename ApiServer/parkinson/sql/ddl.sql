CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  identifier VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,

  CONSTRAINT identifierUnique UNIQUE(identifier),
  CONSTRAINT emailFormatCheck CHECK(email LIKE '%@%.%'),
  CONSTRAINT emailUnique UNIQUE(email)
);

CREATE TABLE patients (
  patient_num BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  patient_name VARCHAR(255) NOT NULL,
  sleep_start_time TIME DEFAULT NULL,
  sleep_end_time TIME DEFAULT NULL,
  FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE medicine (
  medicine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  patient_num BIGINT NOT NULL,
  take_time TIME NOT NULL,
  is_take BIT DEFAULT 0,
  FOREIGN KEY (patient_num) REFERENCES patients (patient_num)
);

CREATE TABLE survey (
  survey_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  patient_num BIGINT NOT NULL,
  abnormal_movement BIT NOT NULL,
  medicinal_effect BIT NOT NULL,
  patient_condition DOUBLE NOT NULL,
  survey_time DATETIME NOT NULL,
  FOREIGN KEY (patient_num) REFERENCES patients (patient_num)
);

CREATE TABLE sensor (
  sensor_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  patient_num BIGINT NOT NULL,
  acc_sensor_x DOUBLE NOT NULL,
  acc_sensor_y DOUBLE NOT NULL,
  acc_sensor_z DOUBLE NOT NULL,
  sensor_time DATETIME NOT NULL,
  FOREIGN KEY (patient_num) REFERENCES patients (patient_num)
);