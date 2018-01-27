# --- !Ups

ALTER TABLE events ADD description text;
ALTER TABLE events ADD decline_reason text;

# --- !Downs

ALTER TABLE events DROP description;
ALTER TABLE events DROP decline_reason;