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
 * $Header: /src/voyager/com/volantis/mcs/atg/VolantisInputTagRenderer.java,v 1.8 2003/03/20 15:15:29 sumit Exp $
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
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.XFActionAttributes;
import com.volantis.mcs.papi.XFImplicitAttributes;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.papi.XMLWriter;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 *
 * @author  mat
 */
public class VolantisInputTagRenderer
{

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(VolantisInputTagRenderer.class);

    /** Creates new VolantisInputTag */
    public VolantisInputTagRenderer() 
    {
    }

    
    public static void renderTag( MarinerRequestContext context, PageContext pageContext,
        String pTagName, String vname, PAPIAttributes attributes) 
        throws IOException
    {
        if(logger.isDebugEnabled()){
            logger.debug ( "renderTag: " + pTagName );
        }
        
        if( pTagName.equals( "input" ) )
        {
            renderInputTag( context, pageContext, vname, (XFTextInputAttributes)attributes );
        }
    }
    
    private static void renderInputTag( MarinerRequestContext context, PageContext pageContext,
        String vname, XFTextInputAttributes inputAttributes ) 
        throws IOException
    {
        HttpSession session = pageContext.getSession();
        XMLWriter writer = new XMLWriter(pageContext.getOut());
        try {
            // Submit button... we need an XFAction instead of an XFInput
            if( ( inputAttributes.getType().equals( "submit" ) ) || ( inputAttributes.getType().equals( "reset" ) ) )
            {
                if(logger.isDebugEnabled()){
                    logger.debug ( "Submit input. Setting session attribute _V" + vname + " to " + inputAttributes.getName() );
                }   
                session.setAttribute( new String( "_V"+vname ), new String( inputAttributes.getName() ) );
                
                XFActionAttributes actionAttributes = new XFActionAttributes();                
                actionAttributes.setType( inputAttributes.getType() );
                actionAttributes.setShortcut( inputAttributes.getShortcut() );
                actionAttributes.setCaption( inputAttributes.getCaption() );
                actionAttributes.setName( "i_" + vname );
                actionAttributes.setHelp( inputAttributes.getHelp() );
                actionAttributes.setPrompt( inputAttributes.getPrompt() );
                actionAttributes.setActive( "true" );
                actionAttributes.setCaptionPane( inputAttributes.getCaptionPane() );
                actionAttributes.setEntryPane( inputAttributes.getEntryPane() );

                // write the xdime to the page
                writer.openElement(actionAttributes);
                writer.closeElement(actionAttributes);

            } else {
                if( inputAttributes.getType().equals( "hidden" ) == false ) 
                {
                    if(logger.isDebugEnabled()){
                        logger.debug ( "Visible input. Setting session attribute _V" + vname + " to " + inputAttributes.getName() );
                    }   
                    session.setAttribute( new String( "_V"+vname ), new String( inputAttributes.getName() ) );
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
                                if(logger.isDebugEnabled()){
                                    logger.debug ( "Hidden input. Setting session attribute " + vname + " to " + inputAttributes.getInitial() );
                                }   
                                session.setAttribute( new String( "_DARGS" ), new String( inputAttributes.getInitial() ) );
                            } else {
                                if(logger.isDebugEnabled()){
                                    logger.debug ( "Hidden input. Setting session attribute _D" + vname + " to " + inputAttributes.getName() );
                                }   
                                session.setAttribute( new String( "_D"+vname ), new String( inputAttributes.getName() ) );
                            }
                        } else {
                            if(logger.isDebugEnabled()){
                                logger.debug ( "Hidden input. Setting session attribute _V" + vname + " to " + inputAttributes.getName() );
                            }   
                            session.setAttribute( new String( "_V"+vname ), new String( inputAttributes.getName() ) );

                            XFImplicitAttributes ia = new XFImplicitAttributes();
                            ia.setName( "i_" + vname );
                            ia.setValue( inputAttributes.getInitial() );

                            writer.openElement(ia);
                            writer.closeElement(ia);
                        }
                    } else {
                        if(logger.isDebugEnabled()){
                            logger.debug ( "Hidden input  vname is undefined" );
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

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
