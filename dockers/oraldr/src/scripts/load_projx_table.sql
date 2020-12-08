
set echo on
set linesize 32000
set trim on
set term on
set timing off
set pagesize 0

----
prompt Invoked to load external file &1 into table &2

@@/home/oracle/scripts/extdirs.sql
@@/home/oracle/scripts/procs.sql

----
prompt Reading CSV into external table...

call drop_table_if_exists('&2._ext');

-- see: https://www.oracle.com/technetwork/database/bi-datawarehousing/twp-data-loading-oracle-db-12c-2189777.pdf

create table &2._ext(
     RecordId integer,
     ProjectId integer,
     Cost varchar(20),
     AllocationMonthYear varchar2(20),
     TimeStamp varchar2(30)
)
organization external (
    type oracle_loader
    default directory inbox
    access parameters (
        records delimited by newline
        badfile outbox: '&1..bad'
        logfile outbox: '&1..log'
        preprocessor extbin: 'zcat'
        fields terminated by ","
    )
    location ('&1')
)
reject limit unlimited;

----
prompt Copying external to database table...

call drop_table_if_exists('&2');

create table &2
parallel 8
as select * from &2._ext;

select * from &2;

---- Challenge: SQL for deleting duplicate records from table
prompt Deleting duplicate record(s)

delete
from &2
where rowid not in(
    select min(rowid)
    from &2
    group by ProjectId, Cost, AllocationMonthYear
);

select * from &2;