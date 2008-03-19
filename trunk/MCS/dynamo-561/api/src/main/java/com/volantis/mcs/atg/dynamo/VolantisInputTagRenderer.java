/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/atg/dynamo561/Attic/VolantisInputTagRenderer.java,v 1.1.2.4 2002/09/24 10:22:25 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *              Steve           VBM 2001090306 - Initial Release
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Fixed compile error in
 *                              logging.
 * 15-Aug-02    Chris W         VBM:2002081511 - Copied existing class to 
 *                              separate package for Dynamo5.6.1 We want to be
 *                              able to support the older Dynamo5.1.1 (?) version
 *                              as well.
 * 20-Aug-02    Chris W         VBM:2002081511 - Type defaults to text if not
 *                              supplied. Tags with type=submit or reset now set
 *                              the styleClass attribute too.
 * 27-Aug-02    Chris W         VBM:2002081511 - Store the names of graphical
 *                              submit buttons in the session to overcome fact that
 *                              IE sends buttonName.x=anInt and buttonName.y=anInt
 *                              but not buttonName=submit when the button is
 *                              clicked. Code added to renderInputTag(). We use
 *                              this in conjunction with the Remap servlet.
 * 24-Sep-02    Chris W         VBM:2002081511 - Instead of storing the form's
 *                              hidden fields directly in the session, we use the
 *                              HiddenFieldsSessionMap class. This makes it easier
 *                              to deal with multiple forms on a page.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.dynamo;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.XFActionAttributes;
import com.volantis.mcs.papi.XFImplicitAttributes;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.papi.XMLWriter;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 *
 * @author  mat
 */
public class VolantisInputTagRenderer
{    
    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(VolantisInputTagRenderer.class);

    /** Creates new VolantisInputTag */
    public VolantisInputTagRenderer()  {
    }

    
    public static void renderTag( MarinerRequestContext context, PageContext pageContext,
        String pTagName, String vname, PAPIAttributes attributes)
        throws JspTagException, IOException  {
        if (logger.isDebugEnabled()) {
            logger.debug ( "renderTag: " + pTagName );
        }
        
        if( pTagName.equals( "input" ) )
        {
            renderInputTag( context, pageContext, vname, (XFTextInputAttributes)attributes );
        }
    }
    
    private static void renderInputTag( MarinerRequestContext context, PageContext pageContext,
        String vname, XFTextInputAttributes inputAttributes )
        throws JspTagException, IOException 
    {
        HttpSession session = pageContext.getSession();
        HiddenFieldsSessionMap sessionMap = (HiddenFieldsSessionMap)session.getAttribute("_HIDDENATGFIELDS");
        XMLWriter writer = new XMLWriter(pageContext.getOut());
        try {
            
            // Type attribute is optional. If not present, it defaults to text
            if (inputAttributes.getType()==null)
            {
                inputAttributes.setType("text");
            }
            
            // Submit button... we need an XFAction instead of an XFInput
            if( ( inputAttributes.getType().equals( "submit" ) ) || ( inputAttributes.getType().equals( "reset" ) ) )
            {                                
                XFActionAttributes actionAttributes = new XFActionAttributes();                
                actionAttributes.setType( inputAttributes.getType() );
                actionAttributes.setStyleClass( inputAttributes.getStyleClass() );
                actionAttributes.setShortcut( inputAttributes.getShortcut() );
                actionAttributes.setCaption( inputAttributes.getCaption() );
                actionAttributes.setName( "i_" + vname );
                actionAttributes.setHelp( inputAttributes.getHelp() );
                actionAttributes.setPrompt( inputAttributes.getPrompt() );
                actionAttributes.setActive( "true" );
                actionAttributes.setCaptionPane( inputAttributes.getCaptionPane() );
                actionAttributes.setEntryPane( inputAttributes.getEntryPane() );

                writer.openElement(actionAttributes);
                writer.closeElement(actionAttributes);

                if (logger.isDebugEnabled()) {
                    logger.debug ( "Submit input. For group "+ getFormName(pageContext)+" setting attribute _V" + vname + " to " + inputAttributes.getName() );
                }
                sessionMap.setAttribute(getFormName(pageContext), "_V"+vname, inputAttributes.getName());
                
                // Deal with IE and image submit buttons.
                // IE sends i_button.x=anInt&i_button.y=anInt
                // Mozilla: i_button.x=anInt&i_button.y=anInt&i_button=valueOfCaptionAttribute
                // So we store a hashtable containing the name and the value of the caption                
                sessionMap.setButton(getFormName(pageContext), "i_"+vname, inputAttributes.getCaption());
                
            } else {
                if( inputAttributes.getType().equals( "hidden" ) == false ) 
                {
                    if (logger.isDebugEnabled()) {
                        logger.debug ( "Visible input. For group "+getFormName(pageContext)+ " setting attribute _V" + vname + " to " + inputAttributes.getName() );
                    }
                    sessionMap.setAttribute(getFormName(pageContext), "_V"+vname, inputAttributes.getName());
                    
                    inputAttributes.setName( "i_" + vname );

                    writer.openElement(inputAttributes);
                    writer.closeElement(inputAttributes);

                } else {
                    if( ( vname != null ) && (vname.length() > 0 ) )
                    {
                        if( inputAttributes.getName().startsWith( "_D" ) )
                        {
                            // _DARGS is a special case
                            if( inputAttributes.getName().equals( "_DARGS" ) )
                            {
                                if (logger.isDebugEnabled()) {
                                    logger.debug ( "Hidden input. For group "+getFormName(pageContext)+" setting attribute " + vname + " to " + inputAttributes.getInitial() );
                                }
                                sessionMap.setAttribute(getFormName(pageContext), "_DARGS", inputAttributes.getInitial());
                            } else {
                                if (logger.isDebugEnabled()) {
                                    logger.debug ( "Hidden input. For group "+getFormName(pageContext)+" setting attribute _D" + vname + " to " + inputAttributes.getName() );
                                }
                                sessionMap.setAttribute(getFormName(pageContext), "_D"+vname, inputAttributes.getName());
                            }
                        } else {                            
                            XFImplicitAttributes ia = new XFImplicitAttributes();
                            ia.setName( "i_" + vname );
                            ia.setValue( inputAttributes.getInitial() );

                            writer.openElement(ia);
                            writer.closeElement(ia);

                            if (logger.isDebugEnabled()) {
                                logger.debug ( "Hidden input. For group "+getFormName(pageContext)+" setting attribute _V" + vname + " to " + inputAttributes.getName() );
                            }
                            sessionMap.setAttribute(getFormName(pageContext), "_V"+vname, inputAttributes.getName());
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug ("hidden-input-undefined");
                        }
                        XFImplicitAttributes ia = new XFImplicitAttributes();
                        ia.setName( inputAttributes.getName() );
                        ia.setValue( inputAttributes.getInitial() );

                        writer.openElement(ia);
                        writer.closeElement(ia);
                                                
                    }
                }
            }
        }
 
        catch (Exception ex) {
            // Had a problem so log it and throw a JspTagException to report it
            logger.error ("unexpected-exception", ex);
            throw(new IOException("Exception caused by previously logged error"));
        }
    }

    /**
     * Returns the name of this form as specified by the formName attribute
     * of the xfform tag.
     * @param context The PageContext.
     * @return String The form's name.
     */
    public static String getFormName(PageContext context) {
        return (String) context.getSession().getAttribute(
                VolantisDSPFormTag.VOLANTIS_FORM_NAME_KEY);
    }
    
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 10-Jan-05	6565/1	adrianj	VBM:2004122902 Created Dynamo 7 version of Volantis custom tags

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/4	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 27-Apr-04	3843/2	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
