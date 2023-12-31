-- noinspection SqlResolveForFile

TRUNCATE protocols.protocol RESTART IDENTITY CASCADE;
TRUNCATE protocols.feature RESTART IDENTITY CASCADE;
TRUNCATE protocols.default_feature_stat RESTART IDENTITY CASCADE;
TRUNCATE protocols.calculation_input_value RESTART IDENTITY CASCADE;

insert into protocols.protocol(id, name, low_welltype, high_welltype, previous_version, version_number, created_on, created_by)
values (1000, 'PKD High Content SD', 'NC', 'PC', null, '1.0.0-20220322.115833','2022-03-22T10:58:33.518+00:00','smarien');

insert into protocols.feature(id, name, alias, description, format, protocol_id, formula, formula_id, calc_trigger, calc_sequence, type)
values (1000, 'Test feature 1', 'TF1', null, '#.##', 1000, 'calc_formula TF1', 1000, null, 1, 'CALCULATION');

insert into protocols.feature(id, name, alias, description, format, protocol_id, formula, formula_id, calc_trigger, calc_sequence, type)
values (3000, 'Test feature 2', 'TF2', null, '#.##', 1000, 'calc_formula TF2', 1000, null, 2, 'CALCULATION');

insert into protocols.feature(id, name, alias, description, format, protocol_id, formula, formula_id, calc_trigger, calc_sequence, type)
values (4000, 'Test feature 3', 'TF3', null, '#.##', 1000, 'calc_formula TF3', 2000, null, 1, 'NORMALIZATION');

insert into protocols.feature(id, name, alias, description, format, protocol_id, formula, formula_id, calc_trigger, calc_sequence, type)
values (5000, 'Test feature 4', 'TF4', null, '#.##', 1000, 'calc_formula TF4', 2000, null, 2, 'NORMALIZATION');

insert into protocols.feature(id, name, alias, description, format, protocol_id, formula, formula_id, calc_trigger, calc_sequence, type)
values (6000, 'Test feature 5', 'TF5', null, '#.##', 1000, 'calc_formula TF5', 3000, null, 1, 'RAW');

insert into protocols.feature(id, name, alias, description, format, protocol_id, formula, formula_id, calc_trigger, calc_sequence, type)
values (7000, 'Test feature 6', 'TF6', null, '#.##', 1000, 'calc_formula TF6', 3000, null, 2, 'RAW');

insert into protocols.default_feature_stat
values (1000, TRUE, TRUE, 'test',1 );
