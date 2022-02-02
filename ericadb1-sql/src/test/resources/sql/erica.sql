create
if not exists
database will;

use
will;

create table jerry (
    id bigint not null, word text null default 24
);

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
