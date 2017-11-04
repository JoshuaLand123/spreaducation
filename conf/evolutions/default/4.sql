# --- !Ups

ALTER TABLE questions ADD COLUMN psycho_category text;
ALTER TABLE questions ADD COLUMN psycho_subcategory text;
ALTER TABLE questions ADD COLUMN psycho_score_reverse boolean default false;
ALTER TABLE questions RENAME category to disc_category;

# --- !Downs

ALTER TABLE questions DROP COLUMN psycho_category;
ALTER TABLE questions DROP COLUMN psycho_subcategory;
ALTER TABLE questions DROP COLUMN psycho_score_reverse;
ALTER TABLE questions RENAME disc_category to category;
