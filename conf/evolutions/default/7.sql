# --- !Ups

alter table user_profile add tutor_order integer;

# --- !Downs

alter table user_profile drop tutor_order;