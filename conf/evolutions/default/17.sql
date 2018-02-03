# --- !Ups

ALTER TABLE tutee_profile
ADD description text NOT NULL DEFAULT '',
ADD lesson_type text NOT NULL DEFAULT 'Online',
ADD street text,
ADD postal_code text,
ADD skype text;
ALTER TABLE tutor_profile RENAME COLUMN place TO postal_code;
ALTER TABLE tutee_profile DROP tutor_order, DROP tutor_id;

# --- !Downs

ALTER TABLE tutee_profile DROP description, DROP lesson_type, DROP street, DROP postal_code, DROP skype;
ALTER TABLE tutor_profile RENAME COLUMN postal_code TO place;
ALTER TABLE tutee_profile ADD tutor_order integer, ADD tutor_id UUID;