create index user_index on user(name);
create index email_data_index on email_data(email);
create index phone_data_index on phone_data(phone);

alter table balance add column if not exists initial_balance decimal not null;
alter table balance add column if not exists max_increment_balance decimal;