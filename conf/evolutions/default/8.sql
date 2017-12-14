# --- !Ups

alter table user_profile rename to tutee_profile;

# --- !Downs

alter table tutee_profile rename to user_profile;