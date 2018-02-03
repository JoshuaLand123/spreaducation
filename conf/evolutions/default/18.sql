# --- !Ups

ALTER TABLE tutee_profile RENAME main_language TO learning_language;
ALTER TABLE users add phone_number text;
ALTER TABLE tutor_profile DROP main_language;
ALTER TABLE tutor_profile RENAME working_language TO teaching_language;

# --- !Downs

ALTER TABLE tutee_profile RENAME learning_language TO main_language;
ALTER TABLE users DROP phone_number;
ALTER TABLE tutor_profile ADD main_language text;
ALTER TABLE tutor_profile RENAME teaching_language TO working_language;