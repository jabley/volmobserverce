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
 * $Header: /src/voyager/com/volantis/mcs/cli/MarinerImport.java,v 1.7 2002/07/29 10:48:46 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Jan-02    Allan           VBM:2001121703 - Created from marinerimport.
 *                              Corrected spelling.
 * 31-Jan-02    Allan           VBM:2002013002 - Updated usage.
 * 06-Feb-02    Paul            VBM:2001122103 - Added support for scripts.
 * 19-Feb-02    Allan           VBM:2002021304 - Added support for
 *                              pluginDefinition objects. Removed logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 29-Jul-02    Byron           VBM:2002061304 - Modified command line option
 *                              for replacing the destination file to display
 *                              '-replace' and not 'replace'. Unused 'append'
 *                              option has been removed. Improved formatting
 *                              of cli options.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.cli;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.localization.Category;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

/**
 * Class to export from a JDBC repository to an XML file
 */
public class MarinerImport {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer messageLocalizer =
        LocalizationFactory.createMessageLocalizer(Exporter.class);

    /**
     * Main entry point for this utility
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        UserInterface console = new ConsoleUserInterface();

        try {

            // Set logging level, change to ALL to enable debug messages.
            Logger.getRoot().setLevel(Level.FATAL);

            BasicConfigurator.configure(
                new ConsoleAppender(new PatternLayout()));

            Exporter importer = new Exporter(args, console);

            importer.transfer(Exporter.XML_REPOSITORY,
                              Exporter.JDBC_REPOSITORY);

        } catch (Exporter.MissingArgumentException mae) {
            console.reportError(messageLocalizer.format("missing-cli-arg", mae.getMessage()));
            console.reportError(messageLocalizer.format("import-cli-usage"));
            System.exit(1);
        } catch (Throwable t) {
            console.reportError("The import has failed.");
            console.reportException(t);
            System.exit(-1);
        }
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Dec-05	10797/1	pszul	VBM:2005121205 Assorted usability fixes

 29-Nov-05	10496/1	pabbott	VBM:2005111811 Provide Exporter public API

 29-Nov-05	10490/1	pabbott	VBM:2005111811 Provide Exporter public API

 29-Nov-05	10413/3	pabbott	VBM:2005111811 Provide Exporter public API

 24-Mar-05	7081/5	rgreenall	VBM:2005022301 Removed SQLException import that is not required.

 23-Mar-05	7081/3	rgreenall	VBM:2005022301 Post review improvements

 23-Mar-05	7081/1	rgreenall	VBM:2005022301 setString method no longer called directly on a JDBC PreparedStatement.

 21-Dec-04	6531/1	doug	VBM:2004122005 Enhancements to the MessageLocalizer interface

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 26-Feb-04	3225/2	tony	VBM:2004022409 usage message debranding and externalisation

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 13-Jan-04	2491/3	tony	VBM:2004010801 changed capitilisation in error messages

 13-Jan-04	2491/1	tony	VBM:2004010801 error msg fix and comment updates

 30-Dec-03	2252/4	andy	VBM:2003121703 changed file suffix constants to correct values

 19-Dec-03	2265/1	andy	VBM:2003121713 import and export tested and working

 23-Dec-03	2252/1	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 ===========================================================================
*/
