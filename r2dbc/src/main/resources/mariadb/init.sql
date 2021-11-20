create table users (
    id int auto_increment,
    name varchar(50),
    balance int,
    primary key (id)
);

create table user_transaction(
    id int auto_increment,
    user_id int,
    amount int,
    transaction_date timestamp,
    primary key(id),
    foreign key (user_id) references users (id) on delete cascade
);

insert into users
    (name, balance)
    values
    ('sam', 1000),
    ('mike', 1200),
    ('jake', 800),
    ('marshal', 2000);