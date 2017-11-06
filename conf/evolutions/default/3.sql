# --- !Ups

CREATE TABLE user_profile(
user_id uuid PRIMARY KEY,
gender text,
dob date,
class_level int,
school_name text,
main_language text,
subject_improve text,
score_subject_improve int,
subject_goodat text,
score_subject_goodat int,
interest_1 text,
time_interest_1 int,
interest_2 text,
time_interest_2 int,
interest_3 text,
time_interest_3 int
);

# --- !Downs

DROP TABLE user_profile;
