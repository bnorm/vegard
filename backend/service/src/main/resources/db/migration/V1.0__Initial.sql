create schema if not exists vegard;

create table vegard.users
(
    id         bigint generated always as identity primary key,
    email      text not null unique,
    password   text not null,
    first_name text not null,
    last_name  text not null
);

create table vegard.controllers
(
    id            bigint generated always as identity primary key,
    serial_number text not null unique,
    mac_address   text not null unique
);

create table vegard.controller_readings
(
    id                  bigint generated always as identity primary key,
    controller_id       bigint references vegard.controllers (id),
    timestamp           timestamptz not null,
    ambient_temperature double precision,
    ambient_humidity    double precision,
    soil_moisture       double precision
);

create index index_name on vegard.controller_readings (timestamp asc);
