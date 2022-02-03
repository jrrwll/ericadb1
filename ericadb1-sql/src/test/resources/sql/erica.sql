create database if not exists will;

use will;

create table jerry (
    id         bigint      not null auto_increment comment 'id of the table',
    created_at timestamp   not null default current_timestamp,
    updated_at timestamp   not null default current_timestamp on update current_timestamp,
    `code`     varchar(63) not null comment 'some code',
    `name`     varchar(63) not null default 'John Doe' comment 'some name',
    word       text        null     default 'love',
    `age`      smallint(6) not null comment 'the age',
    deleted    bit(1)      null     default b'0',
    primary key (id),
    unique index `uk_code` (code),
    index ix_created_at (`created_at`),
    index `ix_updated_at` (`updated_at`),
    key ix_name_word (name, `word`)
) engine = innodb default charset = utf8mb4 comment ='some table';

select *
from jerry;

select word, id
from jerry;

select word
from jerry
where id = 1;

insert into jerry(id, word)
values (1, 'a');

insert into jerry
values (2, 'b');

update jerry
set word = 'c'
where id = 1;

update jerry
set word = 'b1'
where word = 'b';

delete
from jerry
where word = 'b';

delete
from jerry
where word = 'a';
