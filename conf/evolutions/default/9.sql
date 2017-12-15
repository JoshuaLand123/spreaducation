# --- !Ups

CREATE TABLE tutor_profile(
user_id uuid PRIMARY KEY,
gender text,
dob date,
main_language text,
description text,
institute_attended text,
occupation text,
occupation_degree text,
working_language text,
subject_1 text,
subject_1_level int,
subject_2 text,
subject_2_level int,
subject_3 text,
subject_3_level int,
subject_4 text,
subject_4_level int,
interest_1 text,
time_interest_1 int,
interest_2 text,
time_interest_2 int,
interest_3 text,
time_interest_3 int,
wished_salary double precision,
lesson_type text
);

# --- !Downs

DROP TABLE tutor_profile;
