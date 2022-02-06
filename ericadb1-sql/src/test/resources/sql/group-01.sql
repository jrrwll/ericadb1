create or replace view ds as
select a, b, c
from (
         select 1 as a, 1 as b, 0.1 as c
         union
         select 1 as a, 2 as b, 0.2 as c
         union
         select 1 as a, 3 as b, 0.3 as c
         union
         select 2 as a, 4 as b, 0.1 as c
         union
         select 2 as a, 5 as b, 0.15 as c
         union
         select 2 as a, 6 as b, 0.35 as c
         union
         select 3 as a, 7 as b, 0.04 as c
         union
         select 3 as a, 8 as b, 0.5 as c
     ) as tmp;


select *
from (
         select x.a, x.b, x.c, count(*) as r
         from ds as x cross join ds as y
                                 on x.a = y.a
         where x.c <= y.c
         group by a, b
         order by a, b
     ) x left join
     (
         select a, max(r) * 0.2 as m
         from (
                  select x.a, x.b, x.c, count(*) as r
                  from ds as x cross join ds as y
                                          on x.a = y.a
                  where x.c <= y.c
                  group by a, b
                  order by a, b) tmp
         group by a) y on x.a = y.a
where r < m;

select 1 and 2 or 3 in (1);


select a * b * c from ds group by a * b * c
order by a * b desc;