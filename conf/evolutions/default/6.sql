# --- !Ups

update questions set psycho_subcategory = 'Working-techniques' where psycho_subcategory = 'Working techniques';
update questions set psycho_subcategory = 'Learning-techniques' where psycho_subcategory = 'Learning techniques';

# --- !Downs

update questions set psycho_subcategory = 'Working techniques' where psycho_subcategory = 'Working-techniques';
update questions set psycho_subcategory = 'Learning techniques' where psycho_subcategory = 'Learning-techniques';