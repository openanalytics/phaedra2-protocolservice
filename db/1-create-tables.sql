-- drop all tables if exist
drop table if exists protocols.feature_tag;
drop table if exists protocols.feature_class;
drop table if exists protocols.protocol;
drop table if exists protocols.feature;
drop table if exists protocols.tag;
drop table if exists protocols.classification;

-- create protocol table
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
grant all on table protocols.protocol to phaedra_usr;

-- create feature table
create table if not exists protocols.feature
(
    id            serial,
    name          text not null,
    alias         text,
    description   text,
    format        text,
    protocol_id   bigint       not null,
    formula       text,
    script_language      text,
    formula_id    bigint,
    calc_trigger  text,
    calc_sequence integer,
    type          text,
    primary key (id),
    foreign key (protocol_id) references protocols.protocol (id) on update cascade on delete cascade
);
grant all on table protocols.feature to phaedra_usr;

-- create calculation_input_value table
create table if not exists protocols.calculation_input_value
(
    id                   serial primary key,
    feature_id           bigint not null,
    source_meas_col_name text,
    source_feature_id    bigint,
    variable_name        text not null,
    foreign key (feature_id) references protocols.feature (id) on update cascade,
    unique(feature_id, source_meas_col_name),
    unique(feature_id, source_feature_id),
    unique(feature_id, variable_name)
);