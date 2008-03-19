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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormFragment.java,v 1.7 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 8-Feb-02     Steve           VBM:2001101803 - Created
 *                              Implementation of a form fragment format
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 28-Mar-02    Steve           VBM:2002021404 - Fixed for method name changes
 *                              in FormFragment.

 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 - Removed setDeviceLayout().
 *                              Modified setAttribute() to make better use of
 *                              super.setAttribute().
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 * A format that contains a FormFragment of a Layout.
 *
 * @mock.generate base="Format"
 */
public class FormFragment
  extends Format {

  private static String [] userAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
    FormatConstants.PARENT_FORM_NAME_PSEUDO_ATTRIBUTE,
    FormatConstants.NEXT_LINK_TEXT_ATTRIBUTE,
    FormatConstants.NEXT_LINK_POSITION_ATTRIBUTE,
    FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_TEXT_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_POSITION_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_STYLE_ATTRIBUTE,
    FormatConstants.RESET_ATTRIBUTE
  };

  private static String [] defaultAttributes = new String [] {
    FormatConstants.NEXT_LINK_TEXT_ATTRIBUTE,
    FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE,
    FormatConstants.NEXT_LINK_POSITION_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_TEXT_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_STYLE_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_POSITION_ATTRIBUTE,
  };

  private static String [] persistentAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
    FormatConstants.NEXT_LINK_TEXT_ATTRIBUTE,
    FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE,
    FormatConstants.NEXT_LINK_POSITION_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_TEXT_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_STYLE_ATTRIBUTE,
    FormatConstants.PREVIOUS_LINK_POSITION_ATTRIBUTE,
    FormatConstants.RESET_ATTRIBUTE
  };

  /**
   * The one and only constructor 
   */
  public FormFragment (CanvasLayout canvasLayout) {
    super (1, canvasLayout);
  }

  /**
   * Return the type of this Format  (FormFragment)
   */
  public FormatType getFormatType () {
    return FormatType.FORM_FRAGMENT;
  }

  /**
   * Return the attributes that can be set by the user
   */
  public String [] getUserAttributes () {
    return userAttributes;
  }

  /**
   * Return the attributes that have default values
   */
  public String [] getDefaultAttributes () {
    return defaultAttributes;
  }

  /**
   * Return the attributes that are saved.
   */
  public String [] getPersistentAttributes () {
    return persistentAttributes;
  }

  // Javadoc inherited from super class.
  public boolean visit (FormatVisitor visitor, Object object) 
          throws FormatVisitorException {
    return visitor.visit (this, object);
  }

  /** Set the name of the FormFragment as an attribute
   * @param name The name of the FormFragment
   */
  public void setName (String name) {
    setAttribute (FormatConstants.NAME_ATTRIBUTE, name);
  }

  /** Retrieve the name of the FormFragment
   * @return The name of the FormFragment
   */
  public String getName() {
    return (String) getAttribute (FormatConstants.NAME_ATTRIBUTE);
  }

  /** Setter for property linkText.
   * @param linkText New value of property linkText.
   */
  public void setNextLinkText(String linkText) {
    setAttribute (FormatConstants.NEXT_LINK_TEXT_ATTRIBUTE, linkText);
  }

  /** Getter for property linkText.
   * @return Value of property linkText.
   */
  public String getNextLinkText() {
    return (String) getAttribute (FormatConstants.NEXT_LINK_TEXT_ATTRIBUTE);
  }

  /** Setter for property backLinkText.
   * @param linkText New value of property linkText.
   */
  public void setPreviousLinkText(String linkText) {
    setAttribute (FormatConstants.PREVIOUS_LINK_TEXT_ATTRIBUTE, linkText);
  }
  
  /** Getter for property backLinkText.
   * @return Value of property linkText.
   */
  public String getPreviousLinkText() {
    return (String) getAttribute (FormatConstants.PREVIOUS_LINK_TEXT_ATTRIBUTE);
  }
  
  /** Setter for Link Styleclass attribute
   * @param style Name of style to use
   */
  public void setNextLinkStyleClass( String style )
  {
    setAttribute(FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE, style );
  }
  
  /** Getter for Link Styleclass attribute
   * @return Name of style 
   */
  public String getNextLinkStyleClass()
  {
    return (String)getAttribute(FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE );
  }

  /** Setter for Link Styleclass attribute
   * @param style Name of style to use
   */
  public void setPreviousLinkStyleClass( String style )
  {
    setAttribute(FormatConstants.PREVIOUS_LINK_STYLE_ATTRIBUTE, style );
  }
  
  /** Getter for Link Styleclass attribute
   * @return Name of style 
   */
  public String getPreviousLinkStyleClass()
  {
    return (String)getAttribute(FormatConstants.PREVIOUS_LINK_STYLE_ATTRIBUTE );
  }

    /**
     * Setter of link position
     *
     * @param before true if link is before form, false otherwise
     */
    public void setNextLinkBefore(boolean before) {
        if (before) {
            setNextLinkPosition("false");
        } else {
            setNextLinkPosition("true");
        }
    }

    public void setNextLinkPosition(String position) {
        setAttribute(FormatConstants.NEXT_LINK_POSITION_ATTRIBUTE, position);
    }

  /** Getter of link position
   * @return true if link is before form fields, false if it is after.
   */
  public boolean isNextLinkBefore()
  {
    String bl =
            getNextLinkPosition();
    if( bl == null ) return false;
    return bl.equals("false");
  }

    public String getNextLinkPosition() {
        return (String)getAttribute(FormatConstants.NEXT_LINK_POSITION_ATTRIBUTE);
    }

    /**
     * Setter of back link position
     *
     * @param before true if link is before form, false otherwise
     */
    public void setPreviousLinkBefore(boolean before) {
        if (before) {
            setPreviousLinkPosition("false");
        } else {
            setPreviousLinkPosition("true");
        }
    }

    public void setPreviousLinkPosition(String position) {
        setAttribute(FormatConstants.PREVIOUS_LINK_POSITION_ATTRIBUTE, position);
    }

  /** Getter of back link position
   * @return true if link is before form fields, false if it is after.
   */
  public boolean isPreviousLinkBefore()
  {
    String bl = getPreviousLinkPosition();
    if( bl == null ) return false;
    return bl.equals("false");
  }

    public String getPreviousLinkPosition() {
        return (String)
                    getAttribute(FormatConstants.PREVIOUS_LINK_POSITION_ATTRIBUTE);
    }


    /** Setter of parentFormName.
   * @param parentFormName the parent form name.
   */
  public void setParentFormName(String parentFormName)
  {
    setAttribute(FormatConstants.PARENT_FORM_NAME_PSEUDO_ATTRIBUTE,
                 parentFormName);
  }

  /** Getter of parentFormName.
   * @return the parent form name.
   */
  public String getParentFormName()
  {
      return (String)
              getAttribute(FormatConstants.PARENT_FORM_NAME_PSEUDO_ATTRIBUTE);
  }

  /** Setter of nextLinkStyle.
   * @param nextLinkStyle the next link style.
   */
  public void setNextLinkStyle(String nextLinkStyle)
  {
    setAttribute(FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE,
                 nextLinkStyle);
  }

  /** Getter of nextLinkStyle.
   * @return the next link style.
   */
  public String getNextLinkStyle()
  {
      return (String)
              getAttribute(FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE);
  }

    /**
     * Setter of reset button attribute
     *
     * @param mode true if link is before form, false otherwise
     */
    public void setReset(boolean mode) {
        if (mode) {
            setReset("true");
        } else {
            setReset("false");
        }
    }

    public void setReset( String mode ) {
        setAttribute(FormatConstants.RESET_ATTRIBUTE, mode);
    }

  /** Getter of reset button attribute
   * @return true if link is before form fields, false if it is after.
   */
  public boolean isResetEnabled()
  {
    String bl = getReset();
    if( bl == null ) return false;
    return bl.equals("true");
  }

    public String getReset() {
        return (String) getAttribute(FormatConstants.RESET_ATTRIBUTE);
    }

  /**
   * Override the SimpleAttributeContainer.setAttribute method to enable
   * special processing to be done.
   */
  public void setAttribute(String name, Object value) {
      if(name.equals(FormatConstants.PARENT_FORM_NAME_PSEUDO_ATTRIBUTE)) {
          throw new RuntimeException("Cannot set parent form name");
      } else {
          super.setAttribute(name, value);
      }
  }

  /**
   * Override the SimpleAttributeContainer.setAttribute method to enable
   * special processing to be done.
   */
  public Object getAttribute (String name) {
    if (name.equals (FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE)) {
      String defaultFormFragmentName =
              getLayout().getDefaultFormFragmentName ();
      String fragmentName = getName ();
      if (fragmentName.equals (defaultFormFragmentName)) {
        return "true";
      }
      return "false";
    }
    else if (name.equals(FormatConstants.PARENT_FORM_NAME_PSEUDO_ATTRIBUTE)) {
      Form enclosingForm = getEnclosingForm();
      if (enclosingForm != null) {
        return enclosingForm.getName ();
      }
      return null;
    }
    else {
      return super.getAttribute (name);
    }
  }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateFormFragmentAttributes(context);

        // Form Fragments may only appear within Forms and may not appear
        // within spatial and temporal iterators
        if (isContainedBy(SpatialFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("form-fragment-must-not-be-in-spatial",
                            this.getName()));
        }
        if (isContainedBy(TemporalFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("form-fragment-must-not-be-in-temporal",
                            this.getName()));
        }
        if (!isContainedBy(Form.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("form-fragment-must-be-in-form",
                            this.getName()));
        }

        validateChildren(context);
    }

    /**
     * NOTE: there is no schema attribute group for this, we created it
     * purely to make the code easier to read.
     *
     * @param context
     */
    private void validateFormFragmentAttributes(ValidationContext context) {

        String element = "formFragment";

        validateRequiredName(context);

        Step step = context.pushPropertyStep("allowReset");
        String allowReset = getReset();
        if (allowReset != null) {
            if (!LayoutTypeValidator.isBoolean(allowReset)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.ALLOW_RESET_ILLEGAL, allowReset));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("nextLinkStyleClass");
        final String nextLinkStyleClass = getNextLinkStyleClass();
        if (nextLinkStyleClass != null) {
            if (!LayoutTypeValidator.isQuotedComponentReferenceOrStyleClassType(
                    nextLinkStyleClass)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.NEXT_LINK_STYLE_CLASS_ILLEGAL,
                        nextLinkStyleClass));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("nextLinkPosition");
        final String nextLinkPosition = getNextLinkPosition();
        if (nextLinkPosition != null) {
            if (!LayoutTypeValidator.isFormFragmentLinkPositionType(element,
                    "nextLinkPosition", nextLinkPosition)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.NEXT_LINK_POSITION_ILLEGAL,
                        nextLinkPosition));
            }
        }
        context.popStep(step);

        // nextLinkText does not require validation.
        // QuotedComponentOrLiteralTextType == any string.

        step = context.pushPropertyStep("previousLinkStyleClass");
        final String previousLinkStyleClass = getPreviousLinkStyleClass();
        if (previousLinkStyleClass != null) {
            if (!LayoutTypeValidator.isQuotedComponentReferenceOrStyleClassType(
                    previousLinkStyleClass)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.PREVIOUS_LINK_STYLE_CLASS_ILLEGAL,
                        previousLinkStyleClass));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("previousLinkPosition");
        final String previousLinkPosition = getPreviousLinkPosition();
        if (previousLinkPosition != null) {
            if (!LayoutTypeValidator.isFormFragmentLinkPositionType(element,
                    "previousLinkPosition", previousLinkPosition)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.PREVIOUS_LINK_POSITION_ILLEGAL,
                        previousLinkPosition));
            }
        }
        context.popStep(step);

        // previousLinkText does not require validation.
        // QuotedComponentOrLiteralTextType == any string.

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

 ===========================================================================
*/
