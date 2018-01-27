# --- !Ups

ALTER TABLE meetings RENAME meeting_type TO event_type;
ALTER TABLE meetings RENAME TO events;


# --- !Downs

ALTER TABLE events RENAME event_type TO meeting_type;
ALTER TABLE events RENAME TO meetings;