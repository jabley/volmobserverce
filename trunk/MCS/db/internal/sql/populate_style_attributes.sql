insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('text-indent',
	'text indentation (<value>[<units>] | <value>%)',
	'text',
	'0',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('text-align',
	'text alignment (left | center | right | justify)',
	'select',
	'left',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('white-space',
	'white space behaviour',
	'select',
	'normal',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('line-height',
	'distance between baselines of text (<value>[<units>] | <value>% | normal',
	'editselect',
	'normal',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('word-spacing',
	'space between words (normal | <value>)',
	'editselect',
	'normal',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('letter-spacing',
	'space between letters (normal | <value>)',
	'editselect',
	'normal',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('text-transform',
	'text capitalization',
	'select',
	'none',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('text-decoration',
	'text decoration',
	'select',
	'none',
	'text');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('font-family',
	'font family ([[<family-name> | <generic-family>],] [<family-name> | <generic-family>]',
	'text',
	'',
	'fonts');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('font-weight',
	'font weight (i.e. how bold)',
	'select',
	'normal',
	'fonts');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('font-size',
	'font size (xx-small | x-small | small | medium | large | x-large | xx-large | smaller | larger | <value> | <value>%',
	'editselect',
	'medium',
	'fonts');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('font-style',
	'style of font',
	'select',
	'normal',
	'fonts');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('font-variant',
	'font variant',
	'select',
	'normal',
	'fonts');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('color',
	'pen color (<color> i.e. #<rrggbb> | rgb(<red>%,<green>%,<blue>%))',
	'editselect',
	'black',
	'bgrnd/colors');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('background-color',
	'background color (#<rrggbb> | rgb(<red>%,<green>%,<blue>%) | transparent)',
	'editselect',
	'transparent',
	'bgrnd/colors');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('background-image',
	'background image (Volantis name for an image, not an image file)',
	'editselect',
	'none',
	'bgrnd/colors');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('background-repeat',
	'background image repitition',
	'select',
	'no-repeat',
	'bgrnd/colors');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('background-position',
	'position of background image ([<value>% | <value> | top | center | bottom] || [left | center | right])',
	'text',
	'none',
	'bgrnd/colors');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('background-attachment',
	'scrolling backround setting',
	'select',
	'none',
	'bgrnd/colors');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('width',
	'width (<value> | <value>% | auto)',
	'editselect',
	'auto',
	'size');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('height',
	'height (<value> | auto)',
	'editselect',
	'auto',
	'size');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('margin-top',
	'top margin (<value> | <value>% | auto',
	'editselect',
	'auto',
	'edges');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('margin-right',
	'right margin (<value> | <value>% | auto',
	'editselect',
	'auto',
	'edges');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('margin-bottom',
	'bottom margin (<value> | <value>% | auto',
	'editselect',
	'auto',
	'edges');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('margin-left',
	'left margin (<value> | <value>% | auto',
	'editselect',
	'auto',
	'edges');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('border-style',
	'border style',
	'select',
	'none',
	'edges');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category, sync_group) 
values
	('border-top-width',
	'top border width',
	'editselect',
	'medium',
	'edges',
        'border-width');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('border-right-width',
	'right border width',
	'editselect',
	'medium',
	'edges',
        'border-width');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('border-bottom-width',
	'bottom border width',
	'editselect',
	'medium',
	'edges',
        'border-width');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('border-left-width',
	'left border width',
	'editselect',
	'medium',
	'edges',
        'border-width');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('padding-top',
	'top padding (<value> | <value>%)',
	'text',
	'0',
	'edges',
        'padding');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('padding-right',
	'right padding (<value> | <value>%)',
	'text',
	'0',
	'edges',
        'padding');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('padding-bottom',
	'bottom padding (<value> | <value>%)',
	'text',
	'0',
	'edges',
        'padding');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category,sync_group) 
values
	('padding-left',
	'left padding (<value> | <value>%)',
	'text',
	'0',
	'edges',
        'padding');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('list-style-type',
	'list type',
	'select',
	'none',
	'lists');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('list-style-image',
	'list image (Volantis name for an image, not an image file name)',
	'editselect',
	'none',
	'lists');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('list-style-position',
	'bullet indentation',
	'select',
	'outside',
	'lists');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('quality',
	'quality of visual',
	'select',
	'high',
	'dynvis');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('scale',
	'how the visual is placed when width and height are percentages',
	'select',
	'showall',
	'dynvis');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('align',
	'alignment of the visual',
	'select',
	'center',
	'dynvis');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('salign',
	'scaled alignment of the visual',
	'select',
	'center',
	'dynvis');
insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('wmode',
	'Windows activex transparency attribute',
	'select',
	'window',
	'dynvis');

insert into style_attributes 
       (name, helptext, ui_type, default_value, category) 
values
	('menu',
	'The type of menu to be displayed i.e. full (true) or abriged (false)',
	'boolean',
	'true',
	'dynvis');


commit;

