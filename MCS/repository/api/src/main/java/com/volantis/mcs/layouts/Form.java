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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Form.java,v 1.16 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Created.
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit method to
 *                              return a boolean and added methods to get the
 *                              preamble and postamble buffers.
 * 26-Jul-01    Paul            VBM:2001072301 - Wrap the outputting of the
 *                              form by calls to openFormFormat and
 *                              closeFormFormat methods in
 *                              VolantisProtocol.
 * 27-Jul-01    Paul            VBM:2001072603 - Cleaned up.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Nov-01    Paul            VBM:2001102403 - Updated to match namespace
 *                              changes made to DeviceLayout.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 14-Feb-02    Steve           VBM:2001101803 - FormFragmentation. Each
 *                              form can now be fragmented so the form stores
 *                              the names of all fragments and has methods to
 *                              retrieve the previous and next fragments
 * 04-Mar-02    Paul            VBM:2001101803 - Removed the getPane method as
 *                              it is does not work and hence is not required.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 - Removed setAttribute() and
 *                              setDeviceLayout().
 * 19-Feb-03    Allan           VBM:2003021803 - Implement equals() and
 *                              hashCode().
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 27-Mar-03    Allan           VBM:2003030603 - Removed checks againts
 *                              FormFragments in equals() and hashCode() since
 *                              they are covered by the child formats. Added to
 *                              do on addFormFragment. Made formFragments
 *                              and defaultPane protected.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method, and rethrow
 *                              FormatVisitorException as
 *                              RuntimeWrappingException in getDefaultPane().
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to
 *                              Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A format that contains a Form.
 *
 * @mock.generate base="Format"
 */
public class Form
  extends Format {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(Form.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(Form.class);

  private static String [] userAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
  };

  private static String [] defaultAttributes = new String [] {
  };

  private static String [] persistentAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
  };

  protected Pane defaultPane;

  protected List formFragments;

  public Form (CanvasLayout canvasLayout) {
    super (1, canvasLayout);
    formFragments = new ArrayList();
  }

  public FormatType getFormatType () {
    return FormatType.FORM;
  }

  public String [] getUserAttributes () {
    return userAttributes;
  }

  public String [] getDefaultAttributes () {
    return defaultAttributes;
  }

  public String [] getPersistentAttributes () {
    return persistentAttributes;
  }

  // Javadoc inherited from super class.
  public boolean visit (FormatVisitor visitor, Object object)
          throws FormatVisitorException {
    return visitor.visit (this, object);
  }

  /**
   * Add a fragment to the form
   * @param fragment The fragment to add
   * @todo later it should not be possible to add a FormFragment that is not
   * a decendent of this Form. This method should be fixed along with the
   * related child operations.
   */
  public void addFormFragment( FormFragment fragment )
  {
    if(logger.isDebugEnabled()){
        logger.debug( "Added form fragment " + fragment.getName() );
    }
    formFragments.add( fragment );
  }

  /**
   * Get the name of the default form fragment for this form
   * @return The name of the default form fragment
   */
  public FormFragment getDefaultFormFragment()
  {
    if( isFragmented() )
    {
        FormFragment def = (FormFragment)formFragments.get(0);
        if(logger.isDebugEnabled()){
            logger.debug( "Default form fragment is " + def.getName() );
        }
        return def;
    } else {
        return null;
    }
  }

  /**
   * Get the previous fragment to the current one
   * @param current The current form fragment
   * @return the name of the form fragment preceding the current one
   */
  public FormFragment getPreviousFormFragment( FormFragment current )
  {
    if(logger.isDebugEnabled()){
        logger.debug( "Getting previous form fragment to " + current.getName() );
    // Ensure we are fragmented

    }    if( isFragmented() == false ) return null;

    int idx = findFragment( current );
    if( idx == -1 ) return null;

    // If we are at the start, then there is no previous fragment
    if( idx == 0 ) {
        if(logger.isDebugEnabled()){
            logger.debug( "There is no previous form fragment." );
        }
        return null;
    }


    FormFragment frag =  (FormFragment)formFragments.get( idx - 1 );
    if(logger.isDebugEnabled()){
        logger.debug( "Previous form fragment is " + frag.getName() );
    }
    return frag;
  }

  /**
   * Get the next fragment to the current one
   * @param current The current form fragment
   * @return the name of the form fragment following the current one
   */
  public FormFragment getNextFormFragment( FormFragment current )
  {
    if(logger.isDebugEnabled()){
        logger.debug( "Getting next form fragment from " + current.getName() );
    // Ensure we are fragmented

    }    if( isFragmented() == false ) return null;

    int idx = findFragment( current );
    if( idx == -1 ) return null;

    // If we are at the end, then there is no next fragment
    if( idx == formFragments.size() - 1 )
    {
        if(logger.isDebugEnabled()){
            logger.debug( "There is no next form fragment." );
        }
        return null;
    }

    FormFragment frag =  (FormFragment)formFragments.get( idx + 1 );
    if(logger.isDebugEnabled()){
        logger.debug( "Next form fragment is " + frag.getName() );
    }
    return frag;
  }

  /**
   * Search the list of fragments for a fragment and return its index.
   * @param fragment The fragment we are searching for
   * @return the index of the fragment or -1
   */
  private int findFragment( FormFragment fragment )
  {
    if( isFragmented() == false ) return -1;

    String fragName = fragment.getName();
    for( int i = 0; i < formFragments.size(); i++ )
    {
        FormFragment frag = (FormFragment)formFragments.get(i);
        if( frag.getName().equals( fragName ) )
        {
            return i;
        }
    }
    return -1;
  }



  /**
   * Determine if a form is fragmented. If there is a default fragment name
   * then the form is fragmented
   */
  public boolean isFragmented()
  {
    if( formFragments.size() == 0 ) {
        return false;
    } else {
        return true;
    }
  }

  /**
   * Set the name of the Form as an attribute
   * @param name The name of the Form
   */
  public void setName (String name) {
    setAttribute (FormatConstants.NAME_ATTRIBUTE, name);
  }

  /**
   * Retrieve the name of the Form
   * @return The name of the Form
   */
  public String getName() {
    return (String) getAttribute (FormatConstants.NAME_ATTRIBUTE);
  }

  /**
   * Override the SimpleAttributeContainer.setAttribute method to enable
   * special processing to be done.
   */
  public Object getAttribute (String name) {
    return super.getAttribute (name);
  }

  /**
   * Get the default pane for this form. This is the first pane that is
   * reached in a depth first traversal of the tree. This method is only
   * used at runtime.
   */
  public Pane getDefaultPane () {
    if (defaultPane == null) {
      FormatVisitor visitor = new FormatVisitorAdapter () {
          public boolean visit (ColumnIteratorPane pane, Object object) {
            defaultPane = pane;
            return true;
          }

          public boolean visit (DissectingPane pane, Object object) {
            defaultPane = pane;
            return true;
          }

          public boolean visit (Pane pane, Object object) {
            defaultPane = pane;
            return true;
          }

          public boolean visit (RowIteratorPane pane, Object object) {
            defaultPane = pane;
            return true;
          }
        };

        try {
            visitChildren (visitor, null);
        } catch (FormatVisitorException e) {
            // We don't expect any exceptions, but just in case...            
            throw new ExtendedRuntimeException(
                        exceptionLocalizer.format("unexpected-exception"), e);
        }
    }

    return defaultPane;
  }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateRequiredName(context);

        // Forms may not appear within a Form
        if (isContainedBy(Form.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("form-must-not-be-in-form",
                            this.getName()));
        }

        // Forms may not appear within iterators.
        if (isContainedBy(SpatialFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("form-must-not-be-in-spatial",
                            this.getName()));
        }
        if (isContainedBy(TemporalFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("form-must-not-be-in-temporal",
                            this.getName()));
        }

        validateChildren(context);
    }

    // javadoc inherited
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;

        final Form form = (Form) other;
        return ObjectHelper.equals(getDefaultPane(), form.getDefaultPane()) &&
            ObjectHelper.equals(getDefaultFormFragment(),
                form.getDefaultFormFragment());
    }

    // javadoc inherited
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += ObjectHelper.hashCode(getDefaultPane());
        hashCode += ObjectHelper.hashCode(getDefaultFormFragment());
        return hashCode;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
