insert into protocol
values (null, 'PKD High Content SD', null, 115, 'LIMS', null, 'glpg.hcs.pkd.columbus.capture', true, true, 'NC', 'PC', 'NONE', 'NONE', null, false, null, '');

insert into feature
values ( null,'Test feature 1', 'TF1', 10, true, true, false, false, false, false, false, 'NONE',
        'R', 'normalization_formula TF1', 0, 'TF1', '#.##', 'NC', 'PC',
        'calc_formula TF1', 'R', 0, 'NONE', 1, 20);
insert into feature
values ( null,'Test feature 2', 'TF2', 10, true, true, false, false, false, false, false, 'NONE',
         'JS', 'normalization_formula TF2', 0, 'TF2', '#.##', 'NC', 'PC',
         null, null, 0, 'NONE', 2, null);
insert into feature
values ( null,'Test feature 3', 'TF3', 10, true, true, false, false, false, false, false, 'NONE',
         'R', 'normalization_formula TF3', 0, 'TF3', '#.##', 'NC', 'PC',
         'calc_formula TF3', 'R', 0, 'NONE', 3, 20);
insert into feature
values ( null,'Test feature 4', 'TF4', 10, true, true, false, false, false, false, false, 'NONE',
         'JS', 'normalization_formula TF4', 0, 'TF4', '#.##', 'NC', 'PC',
         null, null, 0, 'NONE', 4, null);