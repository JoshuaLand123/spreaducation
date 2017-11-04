# --- !Ups

CREATE TABLE user_profile(
user_id uuid PRIMARY KEY,
gender text,
dob date,
class_level int,
school_name text,
main_language text,
subject_improve_1 text,
score_subject_improve_1 text,
interest_1 text,
time_interest_1 text,
subject_goodat_1 text,
score_subject_goodat_1 int
);

# --- !Downs

DROP INDEX user_profile;
