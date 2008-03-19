rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002020102.sql,v 1.1 2002/03/28 16:25:03 payal Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 28-Mar-02    Payal            VBM:2002020102 - Created.
rem ---------------------------------------------------------------------------

update  vmresourcebundles  set value ='Top margin ' where 
	policykey='policies.margin-top/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Right margin' where 
	policykey='policies.margin-right/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Left margin'  where 
	policykey='policies.margin-left/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Bottom margin' where 
	policykey='policies.margin-bottom/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Left padding'  where 
	policykey='policies.padding-left/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Bottom padding '  where 
	policykey='policies.padding-bottom/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Right padding'  where 
	policykey='policies.padding-right/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Top padding'  where 
	policykey='policies.padding-top/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Left border width ' where 
	policykey='policies.border-left-width/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Right border width' where 
	policykey='policies.border-right-width/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Top border width'  where 
	policykey='policies.border-top-width/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Bottom border width' where 
	policykey='policies.border-bottom-width/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Border style' where 
	policykey='policies.border-style/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Font family' where 
	policykey='policies.font-family/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Font size' where 
	policykey='policies.font-size/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Style of font' where 
	policykey='policies.font-style/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Font variant ' where 
	policykey='policies.font-variant/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Font weight (i.e. how bold) ' where 
	policykey='policies.font-weight/short' and bundle ='stylePolicies';

 update  vmresourcebundles  set value ='List image (Volantis name for an image, not an image file name) ' where policykey='policies.list-style-image/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Bullet indentation' where 
	policykey='policies.list-style-position/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='List type' where 
	policykey='policies.list-style-type/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Height' where 
	policykey='policies.height/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Width' where 
	policykey='policies.width/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Space between the letters' where 
	policykey='policies.letter-spacing/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Distance between the  baselines of text ' where policykey='policies.line-height/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Text alignment' where 
	policykey='policies.text-align/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Text decoration' where 
	policykey='policies.text-decoration/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Text indentation' where 
	policykey='policies.text-indent/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Text capitalization' where 
	policykey='policies.text-transform/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='White space behaviour' where 
	policykey='policies.white-space/short' and bundle ='stylePolicies';

update  vmresourcebundles  set value ='Space between the words' where 
	policykey='policies.word-spacing/short' and bundle ='stylePolicies';

update vmresourcebundles set value='The orientation for the tag' where 
       policykey='policies.volantisOrientation/short'  and bundle ='stylePolicies';

update vmresourcebundles set value='Scrolling backround setting' where 
       policykey='policies.background-attachment/short'  and bundle ='stylePolicies';

update vmresourcebundles set value='Background color' where 
       policykey='policies.background-color/short'  and bundle ='stylePolicies';

update vmresourcebundles set value='Background component (Name of a Volantis component, not an image file)' where policykey='policies.background-image/short' and bundle ='stylePolicies';

update vmresourcebundles set value='Background component type (component type of specified background component)'  where policykey='policies.background-image-type/short' and bundle ='stylePolicies';

update vmresourcebundles set value='Position of background image' where 
       policykey='policies.background-position/short' and bundle ='stylePolicies';

update vmresourcebundles set value='Background image repitition' where 
       policykey='policies.background-repeat/short' and bundle ='stylePolicies';

update vmresourcebundles set value='Alignment of the visual' where 
       policykey='policies.align/short' and bundle ='stylePolicies';

update vmresourcebundles set value='Quality of visual' where 
       policykey='policies.quality/short' and bundle ='stylePolicies';

update vmresourcebundles set value='Scaled alignment of the visual' where 
       policykey='policies.salign/short' and bundle ='stylePolicies';

update vmresourcebundles set value='How the visual is placed when width and height are percentages'  where policykey='policies.scale/short' and bundle ='stylePolicies';

commit;

