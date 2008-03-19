insert into policy_attributes values ('BMP','Bitmap format supported? true/false
','radio',' '); 
insert into policy_attributes values ('GIF','GIF format supported?true/false','radio',' '); 
insert into policy_attributes values ('GPS','Graphical Positioning System (GPS) available?true/false','radio',' '); 
insert into policy_attributes values ('JPEG','JPEG format supported? true/false'
,'radio',' ');
insert into policy_attributes values ('MP3','Is MP3 format supported for audio?true/false','radio',' '); 
insert into policy_attributes values ('UPF','Uni Picture Format supported? true/
false','radio',' ');
insert into policy_attributes values ('US_ASCII','US ASCII character set support
ed? true/false','radio',' '); 
insert into policy_attributes values ('Unicode','Unicode character set supported ? true/false','radio',' '); 
insert into policy_attributes values ('WAP_BMP','WAP Bitmap format supported? tr
ue/false','radio',' '); 
insert into policy_attributes values ('WAV','WAV format supported? true/false','radio',' ');
insert into policy_attributes values ('WMA','Must find out what the hell WMA sta
nd for - but it is some recorded format for audio (windows media audio??) - defi
nitely supported by jornada','radio',' ');
insert into policy_attributes values ('WTAI_GSMINT_C001','Supports GSM Network W
TAI library identifier','text',' ');
insert into policy_attributes values ('WTAI_GSMINT_C002','Supports GSM Network W
TAI function identifiers','text',' ');
insert into policy_attributes values ('WTAI_INT_011','Supports WTAI Miscellaneou
s library identifier','text',' ');
insert into policy_attributes values ('WTAI_INT_012','Supports WTAI Miscellaneou
s function identifiers','text',' ');
insert into policy_attributes values ('WTAI_INT_C001','Supports Public WTAI libr
ary identifier','text',' ');
insert into policy_attributes values ('WTAI_INT_C002','Supports Public WTAI func
tion identifiers','text',' ');
insert into policy_attributes values ('WTAI_INT_C003','Supports Voice Call Contr
ol library identifier','text',' '); 
insert into policy_attributes values ('WTAI_INT_C004','Supports Voice Call Contr
ol function identifiers','text',' '); 
insert into policy_attributes values ('WTAI_INT_C005','Supports WTAI Network Tex
t library identifier','text',' ');
insert into policy_attributes values ('WTAI_INT_C006','Supports WTAI Network Tex
t function identifiers','text',' ');
insert into policy_attributes values ('WTAI_INT_C007','Supports WTAI Phonebook l
ibrary identifier','text',' '); 
insert into policy_attributes values ('WTAI_INT_C008','Supports WTAI Phonebook f
unction identifiers','text',' '); 
insert into policy_attributes values ('WTAI_INT_C009','Supports WTAI Call Logs l
ibrary identifier','text',' '); 
insert into policy_attributes values ('WTAI_INT_C010','Supports WTAI Call Logs l
ibrary identifiers','text',' ');
insert into policy_attributes values ('battery_life','typical battery life (hours)','select',' ');
insert into policy_attributes values ('body-close','Tag used to close the body of the document (non-head)','text',' ');
insert into policy_attributes values ('body-open','Tag used to open the body of the document','text',' '); 
insert into policy_attributes values ('colour_composition',' ','text',' '); 
insert into policy_attributes values ('content-type',' ','text',' ');
insert into policy_attributes values ('cookies','are cookies supported?true/fa
lse','radio',' ');
insert into policy_attributes values ('css-capable','Can the device use cascading style sheets','radio',' '); 
insert into policy_attributes values ('data_bandwidth','scalar policy 1=GSM (9.6) 2=GPRS (28.8) 3=9.6k-ISDN 6=28.8-cable 7=28.8-T1','select',' '); 
insert into policy_attributes values ('default_advertising_alignment',' ','select',' ');
insert into policy_attributes values ('display_size_diagonal','scalar policy - 
1=1-3 inches 
2=3-12 inches
3=13-28 inches 
4=13-wall size','select',' ');
insert into policy_attributes values ('formfactor','Scalar policy - formfactor of the device','select',' ');
insert into policy_attributes values ('fallback','Fallback device','select',' '); 
insert into policy_attributes values ('header-close','Closing header tag','text',' '); 
insert into policy_attributes values ('header-open','Opening header tag','text',
' '); 
insert into policy_attributes values ('header_override','allow header values to override specific device valuse?','radio',' '); 
insert into policy_attributes values ('input-capability','Scalar policy - ease of input e.g. keyboard, mouse, stylus, remote control, voice','select',' '); 
insert into policy_attributes values ('keyboard-support','scalar policy - ease of keyboard input e.g. none, low, optional, standard qwerty','radio',' ');
insert into policy_attributes values ('keyboard_usability',' ','text',' ');
insert into policy_attributes values ('max-network-speed','Maximum speed for network, expressed in kbps','text',' ');
insert into policy_attributes values ('mouse-support','Does the device have a mouse ','radio',' '); 
insert into policy_attributes values ('mulitple_screen','does the device support multiple screens?true/false','radio',' ');
insert into policy_attributes values ('pixel_desnsity','scalar policy - pixel density
0=N/A
1=40 dpi 
2=70-100 dpi 
3=100-150','select',' ');
insert into policy_attributes values ('protocol-type','Describe fundamental type
 of protocol eg. WML, HTML','select',' ');
insert into policy_attributes values ('protocol-version','Where appropriate, ide
ntify a version of the fundamental protocol.','text',' ');
insert into policy_attributes values ('screen-resolution-char','screen resolutio
n in characters
scalar policy - resolution by class of device e.g. PC, PDA, phone
1=.05 VGA
2=.25 VGA - full VGA 
3=640x480 VGA
4=NTSC/PAL/SECAM analog == 640x444','text',' ');
insert into policy_attributes values ('screen-resolution-pixel','screen resoluti
on in pixels 
scalar policy - resolution by class of device e.g. PC, PDA, phone
1=.05 VGA
2=.25 VGA - full VGA 
3=640x480 VGA
4=NTSC/PAL/SECAM analog == 640x444','text',' ');
insert into policy_attributes values ('screen-resolution-scalar','scalar policy 
- resolution by class of device e.g. PC, PDA, phone
1=.05 VGA
2=.25 VGA - full VGA 
3=640x480 VGA
4=NTSC/PAL/SECAM analog == 640x444','select',' ');
insert into policy_attributes values ('show-title',' ','text',' '); 
insert into policy_attributes values ('showtitle','Show the title tag?','radio',
' '); 
insert into policy_attributes values ('viewing-distance','scalar policy- typic
al viewing distance for device e.g. TV is 10 to 15 feet
1=1-2 feet 
5=10-15 feet','select',' ');
insert into policy_attributes values ('display_colours','scalar value
1=2-8 bit, b/w 
2=2-8 bit, colour
3=8,16,24 bit colour 
8=NTSC/PAL/SECAM','select',' ');
