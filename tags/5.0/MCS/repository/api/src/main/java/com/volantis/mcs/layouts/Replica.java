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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Replica.java,v 1.6 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Jan-02	Steve			VBM 2002011412: Created
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 - Modified findReplicant() to
 *                              use retrieveFormat. Modified setAttribute() to
 *                              convert replicant types into FormatType.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() and findGrid() methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Set;

/**
 * A format that is a replica for a pane.
 *
 * @mock.generate base="Format"
 */
public class Replica extends Format {

  /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(Replica.class);

  public static final String REPLICANT_ATTRIBUTE = "Replicant";
  public static final String REPLICANT_TYPE_ATTRIBUTE = "ReplicantType";

  private Format replicant = null;
  private Grid searchGrid = null;
  private String searchName = null;


  private static String [] userAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
	REPLICANT_ATTRIBUTE,
	REPLICANT_TYPE_ATTRIBUTE
  };

  private static String [] defaultAttributes = new String [] {
  };

  private static String [] persistentAttributes = userAttributes;

  /**
   * Create a new replica
   * @param canvasLayout The Layout to which this pane belongs
   */
  public Replica (CanvasLayout canvasLayout) {
    super (0, canvasLayout);
	replicant = null;
  }

  public FormatType getFormatType () {
    return FormatType.REPLICA;
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

  /**
   * Set the name of the replica as an attribute
   * @param name The name of the Replica
   */
  public void setName (String name) {
    setAttribute (FormatConstants.NAME_ATTRIBUTE, name);
  }

  /**
   * Set the name of the replicant pane as an attribute
   * @param name The name of the replicant format
   */
  public void setReplicant( String name ) {
	setAttribute (REPLICANT_ATTRIBUTE, name ); 
  }

    /**
     * Set the type of the replicant pane as an attribute
     *
     * @param type The type of the replicant format
     */
    public void setReplicantType(FormatType type) {

        final String typeName = type.getTypeName();
        setReplicantTypeString(typeName);
    }

    public void setReplicantTypeString(final String typeName) {
        setAttribute (REPLICANT_TYPE_ATTRIBUTE, typeName );
    }

    /**
   * Retrieve the name of the Replica
   * @return The name of the Replica
   */
  public String getName() {
	String name = (String)getAttribute(FormatConstants.NAME_ATTRIBUTE);
    return name;
  }

  /**
   * Get the name of the replicant format
   * @return The name of the Replicant
   */
  public String getReplicant() {
	String name = (String)getAttribute( REPLICANT_ATTRIBUTE );
    return name;
  }

    /**
     * Get the type of the replicant format
     *
     * @return The type of the Replicant
     */
    public FormatType getReplicantType() {

        String replicantType = getReplicantTypeString();
        return FormatTypeMapper.getFormatType(replicantType);
    }

    public String getReplicantTypeString() {
        return (String) getAttribute(REPLICANT_TYPE_ATTRIBUTE);
    }

  private Grid findGrid( String name, Layout layout )
          throws FormatVisitorException {
	Format format = layout.getRootFormat();
	searchGrid = null;
	searchName = name;
	format.visit( new GridFinder(), layout );
	return searchGrid;
  }

  protected void visitGrid( Grid grid )
  {
	String name = grid.getName();
	if( name != null )
	{
		if( grid.getName().equals( searchName ) ){
			searchGrid = grid;
		}
	}
  }

  public void findReplicant( Layout dl )
          throws FormatVisitorException {
	  if( ( getReplicant() == null ) || ( getReplicantType() == null ) )
	  {
		replicant = null;
		return;
	  }

	  FormatType type = getReplicantType();
	  String replicaName = getReplicant();

      if(type.equals(FormatType.GRID)) {
          replicant = findGrid(replicaName, dl); 
      } else if(type.equals(FormatType.FORM) ||
              type.equals(FormatType.PANE) || // todo: bug? what about other pane types? use namespace instead
              type.equals(FormatType.REGION)) {
          replicant = dl.retrieveFormat(replicaName, type);
      } else {
          replicant = null;
      }

	  if(logger.isDebugEnabled()){
      logger.debug( "Replicant type " + type + " found with name " + replicaName + " = " + replicant );
  }
  }

  /**
   * Retrieves the replicant format
   * @return The format that this is a replicant of or null
  */ 
  public Format getFormat() {
	return replicant;
  }

  // Javadoc inherited from super class.
  public boolean visit (FormatVisitor visitor, Object object) 
          throws FormatVisitorException {
    return visitor.visit (this, object);
  }

  public String toString()
  {
	String txt = new String( "Replica: " );
    txt = txt + " Name=" + getName();
	txt = txt + " Replicant=" + getReplicant();
	txt = txt + " Replicant Format=" + getFormat();
	return txt;
  }

  protected class GridFinder extends FormatVisitorAdapter
  {
	public boolean visit( Grid grid, Object object )
                throws FormatVisitorException {
		visitGrid( grid );
		return grid.visitChildren( this, object );
	}
  }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateReplicaAttributes(context);

        // Replicas may only appear within a Fragment
        if (!isContainedBy(Fragment.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("replica-must-be-in-fragment",
                            this.getName()));
        }

    }

    /**
     * NOTE: there is no schema attribute group for this, we created it
     * purely to make the code easier to read.
     *
     * @param context
     */
    private void validateReplicaAttributes(ValidationContext context) {

        final String element = "replicaFormat";

        validateOptionalName(context);

        Step step = context.pushPropertyStep("sourceFormatName");
        final String sourceFormatName = getReplicant();
        if (sourceFormatName != null) {
            if (!LayoutTypeValidator.isFormatNameType(sourceFormatName)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.SOURCE_FORMAT_NAME_ILLEGAL,
                        sourceFormatName));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.SOURCE_FORMAT_NAME_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("sourceFormatType");
        final String sourceFormatType = getReplicantTypeString();
        if (sourceFormatType != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords(element,
                    "sourceFormatType",
                    new String[] {"grid", "pane", "form", "region"});
            if (!keywords.contains(sourceFormatType)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.SOURCE_FORMAT_TYPE_ILLEGAL,
                        sourceFormatType));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.SOURCE_FORMAT_TYPE_UNSPECIFIED));
        }
        context.popStep(step);
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

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
