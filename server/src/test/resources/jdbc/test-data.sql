-- noinspection SqlResolveForFile

TRUNCATE protocols.protocol RESTART IDENTITY CASCADE;
TRUNCATE protocols.feature RESTART IDENTITY CASCADE;

insert into protocols.protocol
values (1000, 'PKD High Content SD', null, true, true, 'NC', 'PC');

insert into protocols.feature
values (1000, 'Test feature 1', 'TF1', null, '#.##', 1000, 'calc_formula TF1', 'R', 0, null, 1, 'CALCULATION');

insert into protocols.feature
values (3000, 'Test feature 2', 'TF2', null, '#.##', 1000, 'calc_formula TF2', 'R', 0, null, 2, 'CALCULATION');

insert into protocols.feature
values (4000, 'Test feature 3', 'TF3', null, '#.##', 1000, 'calc_formula TF3', 'R', 0, null, 1, 'NORMALIZATION');

insert into protocols.feature
values (5000, 'Test feature 4', 'TF4', null, '#.##', 1000, 'calc_formula TF4', 'R', 0, null, 2, 'NORMALIZATION');

insert into protocols.feature
values (6000, 'Test feature 5', 'TF5', null, '#.##', 1000, 'calc_formula TF5', 'R', 0, null, 1, 'RAW');

insert into protocols.feature
values (7000, 'Test feature 6', 'TF6', null, '#.##', 1000, 'calc_formula TF6', 'R', 0, null, 2, 'RAW');