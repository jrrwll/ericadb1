create database if not exists will;

create table if not exists  jerry (
    id bigint not null,
    word text null default 24
);

create or replace function a as