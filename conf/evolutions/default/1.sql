# --- !Ups

CREATE TABLE users (
  user_id UUID PRIMARY KEY,
  first_name text,
  last_name text,
  full_name text,
  email text,
  avatar_url text,
  user_type text,
  activated BOOLEAN DEFAULT FALSE
);

CREATE TABLE auth_tokens (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  expiry text
);

CREATE TABLE login_info (
  id SERIAL PRIMARY KEY,
  provider_id text NOT NULL,
  provider_key text NOT NULL
);

CREATE TABLE user_login_info (
  user_id UUID NOT NULL,
  login_info_id INTEGER NOT NULL
);

CREATE TABLE password_info (
  hasher text NOT NULL,
  password text NOT NULL,
  salt text,
  login_info_id INTEGER NOT NULL
);

CREATE TABLE oauth1_info (
  id SERIAL PRIMARY KEY,
  token text NOT NULL,
  secret text NOT NULL,
  login_info_id INTEGER NOT NULL
);

CREATE TABLE oauth2_info (
  id SERIAL PRIMARY KEY,
  access_token text NOT NULL,
  token_type text,
  expires_in INTEGER,
  refresh_token text,
  login_info_id INTEGER NOT NULL
);


# --- !Downs

DROP TABLE oauth2_info;
DROP TABLE oauth1_info;
DROP TABLE password_info;
DROP TABLE user_login_info;
DROP TABLE login_info;
DROP TABLE users;