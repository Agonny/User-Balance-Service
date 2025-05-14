create table user (
                        id bigint primary key,
                        name varchar(500) not null,
                        date_of_birth date,
                        password varchar(500) not null
);

create table account (
                        id bigint primary key,
                        user_id bigint references user(id),
                        balance decimal not null
);

create table email_data (
                        id bigint primary key,
                        user_id bigint references user(id),
                        email varchar(200) not null unique
);

create table phone_data (
                        id bigint primary key,
                        user_id bigint references user(id),
                        phone varchar(13) not null unique
);