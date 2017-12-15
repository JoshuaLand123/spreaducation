# --- !Ups

ALTER TABLE tutor_profile ADD place text;

# --- !Downs

ALTER TABLE tutor_profile DROP place;
