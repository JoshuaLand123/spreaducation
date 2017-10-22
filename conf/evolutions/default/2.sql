# --- !Ups

CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    question_text text NOT NULL,
    question_user_type text NOT NULL,
    category text NOT NULL,
    question_order integer
);

CREATE TABLE answers (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users (user_id),
    question_id int NOT NULL REFERENCES questions (id),
    score int NOT NULL
);

CREATE UNIQUE INDEX user_answer_idx ON answers(user_id, question_id);

# --- !Downs

DROP TABLE answers;
DROP TABLE questions;
DROP INDEX user_answer_idx;
