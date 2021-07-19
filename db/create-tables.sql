-- Drop all tables
drop table if exists feature_tag;
drop table if exists feature_class;
drop table if exists protocol;
drop table if exists feature;
drop table if exists tag;
drop table if exists classification;

create schema if not exists protocols;

-- Create protocol table
create table if not exists protocols.protocol
(
    id             serial primary key,
    name           text,
    description    text,
    editable       boolean default true,
    in_development boolean default true,
    low_welltype   text,
    high_welltype  text
);

-- Create feature table
create table if not exists protocols.feature
(
    id            serial,
    name          text not null,
    alias         text,
    description   text,
    format        text,
    protocol_id   bigint       not null,
    formula       text,
    language      text,
    formula_id    bigint,
    calc_trigger  text,
    calc_sequence integer,
    type          text,
    primary key (id),
    foreign key (protocol_id) references protocols.protocol (id) on update cascade
);

-- Create tag table
create table if not exists protocols.tag
(
    id   serial,
    name text not null,
    primary key (id)
);

-- Create classification table
create table if not exists protocols.classification
(
    id          serial  primary key,
    name        text    not null,
    description text,
    color       int,
    symbol      text,
    value       int     not null
);

-- Create feature_tag table
create table if not exists protocols.feature_tag
(
    feature_id bigint not null,
    tag_id     bigint not null,
    primary key (feature_id, tag_id),
    foreign key (feature_id) references feature (id) on update cascade,
    foreign key (tag_id) references tag (id) on update cascade
);

-- Create feature_class table
create table if not exists protocols.feature_class
(
    feature_id bigint not null,
    class_id   bigint not null,
    primary key (feature_id, class_id),
    foreign key (feature_id) references feature (id) on update cascade,
    foreign key (class_id) references classification (id) on update cascade
);