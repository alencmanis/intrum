create sequence user_seq;

create table user
(
   id bigint default user_seq.nextval primary key,
   name varchar(255) not null,
   password varchar(255) not null
);

create sequence customer_seq;

create table customer
(
   id bigint default customer_seq.nextval primary key,
   firstName varchar(255) not null,
   lastName varchar(255) not null,
   email varchar2(255) not null,
   mobike varchar2(255) not null,
   dateOfBirth Date not null,
   user_id integer not null
);

create table debt
(
   id bigint not null,
   amount decimal(19,2) not null,
   dueDate Date not null,
   customer_id bigint not null,
   payed boolean not null default false,
   primary key(id)
);