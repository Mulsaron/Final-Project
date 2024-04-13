CREATE TABLE Member (
    member_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE Fitness_Goals (
    goal_id SERIAL PRIMARY KEY,
    member_id INT,
    goal_type VARCHAR(100),
    number_value NUMERIC,
    target_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Member(member_id)
);

CREATE TABLE Health_Metrics (
    metric_id SERIAL PRIMARY KEY,
    member_id INT,
    weight DECIMAL(5,2),              
    height DECIMAL(5,2),                             
    last_checkup_date DATE,           
    blood_pressure VARCHAR(20),       
    cholesterol_level DECIMAL(5,2), 
    FOREIGN KEY (member_id) REFERENCES Member(member_id)
);


CREATE TABLE Exercise_Routine (
    routine_id SERIAL PRIMARY KEY,
    member_id INT,
    exercise_description TEXT,
    duration INT,
    FOREIGN KEY (member_id) REFERENCES Member(member_id)
);

CREATE TABLE Fitness_Achievements (
    achievement_id SERIAL PRIMARY KEY,
    member_id INT,
    achievement_type VARCHAR(100), 
    value NUMERIC, 
    achieved_date DATE,
    FOREIGN KEY (member_id) REFERENCES Member(member_id)
);

CREATE TABLE Trainer (
    trainer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE Trainer_Availability (
    availability_id SERIAL PRIMARY KEY,
    trainer_id INT,
    session_type VARCHAR(100),
    availability_date DATE,
    start_time TIME,
    end_time TIME,
    preferred_room_id VARCHAR(100),
    FOREIGN KEY (trainer_id) REFERENCES Trainer(trainer_id),
    FOREIGN KEY (preferred_room_id) REFERENCES Room(room_id)
);

CREATE TABLE Room(
    room_id SERIAL PRIMARY KEY,
    room_number VARCHAR(50),
    Date DATE,
    description TEXT,
    start_time TIME,
    end_time TIME
    managed_by INT,
    FOREIGN KEY (managed_by) REFERENCES Administrator(administrator_id)
);
CREATE TABLE Schedule (
    session_id SERIAL PRIMARY KEY,
    trainer_id INT,
    session_type VARCHAR(100),
    date DATE,
    start_time TIME,
    end_time TIME,
    room_id INT,
    is_booked BOOLEAN DEFAULT FALSE,
    managed_by INT,
    FOREIGN KEY (managed_by) REFERENCES Administrator(administrator_id),
    FOREIGN KEY (trainer_id) REFERENCES Trainer(trainer_id),
    FOREIGN KEY (room_id) REFERENCES Room(room_id)
);

CREATE TABLE Administrator (
    administrator_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE Equipment (
    equipment_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    maintenance_date DATE
    managed_by INT,
    FOREIGN KEY (managed_by) REFERENCES Administrator(administrator_id)
);

CREATE TABLE Billing (
    billing_id SERIAL PRIMARY KEY,
    member_id INT,
    session_id INT,
    amount DECIMAL(10,2),
    description TEXT,
    payment_status VARCHAR(20) DEFAULT 'Pending',
    payment_date DATE DEFAULT CURRENT_DATE,
    managed_by INT,
    FOREIGN KEY (managed_by) REFERENCES Administrator(administrator_id),
    FOREIGN KEY (member_id) REFERENCES Member(member_id),
    FOREIGN KEY (session_id) REFERENCES Schedule(session_id)
);
