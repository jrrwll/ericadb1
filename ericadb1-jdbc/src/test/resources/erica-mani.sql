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
from jerry;
