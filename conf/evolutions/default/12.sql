# --- !Ups

ALTER TABLE tutor_profile DROP image_byte;
ALTER TABLE tutee_profile DROP image_byte;
ALTER TABLE users ADD image bytea;

# --- !Downs

ALTER TABLE users DROP image;
ALTER TABLE tutor_profile ADD image_byte bytea;
ALTER TABLE tutee_profile ADD image_byte bytea;