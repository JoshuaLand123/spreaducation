# --- !Ups

ALTER TABLE tutor_profile ADD image_byte bytea;

# --- !Downs

ALTER TABLE tutor_profile DROP tutor_profile;
