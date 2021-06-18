create table protocol
(
    protocol_id                serial primary key,
    protocol_name              varchar(100),
    description                varchar(200),
    default_feature_id         bigint,
    default_lims               varchar(25),
    default_layout_template    varchar(512),
    default_capture_config     varchar(512),
    is_editable                boolean     default true,
    is_in_development          boolean     default true,
    low_welltype               varchar(10),
    high_welltype              varchar(10),
    team_code                  varchar(25) default 'NONE'::character varying,
    upload_system              varchar(25) default 'NONE'::character varying,
    image_setting_id           bigint,
--         constraint hca_pclass_fk_image_settings
--             references phaedra.hca_image_setting
--             on delete set null,
    is_multi_dim_subwell_data  boolean     default false,
    default_multiplo_method    varchar(100),
    default_multiplo_parameter varchar(100)
);

create table feature_group
(
    group_id    serial primary key,
    group_name  varchar(100) not null,
    description varchar(250),
    group_type  integer      not null,
    protocol_id bigint       not null
--         constraint fk_feature_group_protocol
--             references protocol
--             on delete cascade
);

create table feature
(
    feature_id                   serial primary key,
    feature_name                 varchar(100) not null,
    short_name                   varchar(36),
    protocol_id                  bigint       not null,
--         constraint fk_feature_protocol
--             references protocol
--             on delete cascade,
    is_numeric                   boolean     default false,
    is_logarithmic               boolean     default false,
    is_required                  boolean     default true,
    is_key                       boolean     default true,
    is_uploaded                  boolean     default false,
    is_annotation                boolean     default false,
    is_classification_restricted boolean     default false,
    curve_normalization          varchar(25) default 'NONE'::character varying,
    normalization_language       varchar(30),
    normalization_formula        varchar(2000),
    normalization_scope          integer,
    description                  varchar(250),
    format_string                varchar(25),
    low_welltype                 varchar(10),
    high_welltype                varchar(10),
    calc_formula                 varchar(2000),
    calc_language                varchar(30),
    calc_formula_id              bigint,
    calc_trigger                 varchar(30),
    calc_sequence                integer,
    group_id                     bigint
--         constraint fk_feature_feature_group
--             references feature_group
--             on delete set null
);

create table image_setting
(
    image_setting_id serial primary key,
--         constraint hca_image_setting_pk
--             primary key,
    zoom_ratio       integer,
    gamma            integer,
    pixel_size_x     numeric,
    pixel_size_y     numeric,
    pixel_size_z     numeric
);