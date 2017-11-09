# --- !Ups

ALTER TABLE user_profile RENAME subject_improve to subject_improve_1;
ALTER TABLE user_profile RENAME score_subject_improve to score_subject_improve_1;
ALTER TABLE user_profile RENAME subject_goodat to subject_goodat_1;
ALTER TABLE user_profile RENAME score_subject_goodat to score_subject_goodat_1;

ALTER TABLE user_profile ADD COLUMN subject_improve_2 text;
ALTER TABLE user_profile ADD COLUMN score_subject_improve_2 int;
ALTER TABLE user_profile ADD COLUMN subject_goodat_2 text;
ALTER TABLE user_profile ADD COLUMN score_subject_goodat_2 int;

UPDATE user_profile SET
subject_improve_2 = 'Other',
score_subject_improve_2 = 3,
subject_goodat_2 = 'Other',
score_subject_goodat_2 = 3
WHERE subject_improve_2 IS NULL;

# --- !Downs

ALTER TABLE user_profile RENAME subject_improve_1 to subject_improve;
ALTER TABLE user_profile RENAME score_subject_improve_1 to score_subject_improve;
ALTER TABLE user_profile RENAME subject_goodat_1 to subject_goodat;
ALTER TABLE user_profile RENAME score_subject_goodat_1 to score_subject_goodat;

ALTER TABLE user_profile DROP COLUMN subject_improve_2;
ALTER TABLE user_profile DROP COLUMN score_subject_improve_2;
ALTER TABLE user_profile DROP COLUMN subject_goodat_2;
ALTER TABLE user_profile DROP COLUMN score_subject_goodat_2;