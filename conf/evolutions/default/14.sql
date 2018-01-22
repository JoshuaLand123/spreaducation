# --- !Ups

CREATE TABLE meetings (
    id SERIAL PRIMARY KEY,
    title text NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL,
    tutee_id UUID,
    tutor_ID UUID,
    meeting_type text
);


# --- !Downs

DROP TABLE meetings