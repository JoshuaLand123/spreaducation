# --- !Ups

ALTER TABLE events DROP title;
UPDATE events SET event_type = 'Available' WHERE event_type = 'Availability';

# --- !Downs

ALTER TABLE events ADD title text;
UPDATE events SET event_type = 'Availability' WHERE event_type = 'Available';