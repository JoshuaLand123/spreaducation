# --- !Ups

ALTER TABLE tutee_profile ADD tutor_id uuid;

# --- !Downs

ALTER TABLE tutee_profile DROP tutor_id;