# --- !Ups

ALTER TABLE events DROP title;
UPDATE TABLE events SET event_type = 'Available' WHERE event_type = 'Availability';

# --- !Downs

ALTER TABLE events ADD title text;
UPDATE TABLE events SET event_type = 'Availability' WHERE event_type = 'Available';