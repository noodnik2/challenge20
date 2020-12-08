
-- defines the needed procedures

create or replace procedure drop_table_if_exists(table_name varchar) as
begin
    execute immediate 'drop table ' || table_name;
exception
    when others then
        if sqlcode != -942 then
            raise;
        end if;
end;
/

