create sequence hibernate_sequence start with 1 increment by 1;

create table person (
  id integer not null default hibernate_sequence.nextval,
  created date,
  modified date,
  name varchar(255),
  primary key (id)
);

insert into person (created, modified, name) values ('2021-02-01', '2021-02-01', 'Den');
insert into person (created, modified, name) values ('2021-03-01', '2021-03-01', 'Bill');
insert into person (created, modified, name) values ('2021-04-01', '2021-04-01', 'John');