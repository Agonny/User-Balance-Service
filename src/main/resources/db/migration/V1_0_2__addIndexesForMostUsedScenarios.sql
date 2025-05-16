create index user_index on public.user(name);
create index email_data_index on public.email_data(email);
create index phone_data_index on public.phone_data(phone);

alter table public.account add column if not exists initial_balance decimal not null;
alter table public.account add column if not exists max_increment_balance decimal;