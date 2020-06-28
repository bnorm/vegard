create schema if not exists vegard;

create table vegard.users
(
    id         bigint generated always as identity primary key,
    email      text not null unique,
    password   text not null,
    first_name text not null,
    last_name  text not null
);
