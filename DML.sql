INSERT INTO Exercise_Routines (member_id, exercise_description, duration) 
VALUES (1, 'Jump Squats', 30);


INSERT INTO Fitness_Achievements (member_id, achievement_type, value, achieved_date) 
VALUES (1, 'Weight Loss', 3, '2024-05-01');


INSERT INTO Health_Metrics (member_id,weight,height,last_checkup_date,blood_pressure,cholesterol_level)
VALUES (1, 150.2, 160, '2024-04-10', '120/80', 200);


INSERT INTO Trainer (first_name, last_name, email, password)
VALUES ('Jamie', 'Jackson', 'j.j@gmail.com', 'jj2');

INSERT INTO Trainer (first_name, last_name, email, password)
VALUES ('Rebecca', 'Mills', 'Becca.mills@gmail.com', 'mills123');

INSERT INTO Trainer (first_name, last_name, email, password)
VALUES ('Fran', 'Green', 'greenf@hotmail.com', 'frann8');

INSERT INTO Schedule (trainer_id, session_type, date, start_time, end_time, room_id)
VALUES (1, 'Personal Training', '2024-04-15', '10:00:00', '11:00:00', 1);

INSERT INTO Room (room_number, Date, description, start_time, end_time)
VALUES ('101', '2024-04-15', 'Yoga Studio', '10:00:00','11:00:00' );

INSERT INTO Administrator (first_name, last_name, email, password)
VALUES 
    ('Mike', 'Johnson', 'mikejohnson@example.com', 'abc123'),
    ('Emily', 'Davis', 'emilydavis@example.com', 'securepass'),
    ('Alice', 'Williams', 'alicewilliams@example.com', 'alicepass');

INSERT INTO Equipment (name, description, maintenance_date)
VALUES
    ('Dumbbell Set', 'Cleaned weights and inspected for damage.', '2024-02-01'),
    ('Rowing Machine', 'Tightened screws and oiled moving parts.', '2024-03-10'),
     ('Yoga Mat', 'Washed and sanitized.', '2024-05-30');
