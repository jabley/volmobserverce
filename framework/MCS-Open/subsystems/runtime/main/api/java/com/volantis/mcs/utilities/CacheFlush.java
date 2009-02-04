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
/*
 * 
 * $Header: /src/voyager/com/volantis/mcs/utilities/CacheFlush.java,v 1.3 2001/01/16 09:28:53 jwk Exp $
 *
 * (c) Volantis Systems Ltd 2000. 
 */

package com.volantis.mcs.utilities;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import java.lang.*;

// Read the file that contains the location of the cache directory and remove
// all files and directory beneath that. Files deleted will only be those
// allowed under the permission scheme ie. the server is started under a user,
// and this class must be called with the same user identity to remove cache
// files.

public class CacheFlush {

  private static String mark = "(c) Volantis Systems Ltd 2000. ";

    public static void main(String args[]) {

      try {

        if (java.lang.reflect.Array.getLength(args) == 0) {
           throw( new Exception("No Cache Flush Directory Specified") );
        }
        String line = args[0];

        // Locate the directory and list all the files within 
        File dir = new File(line);
        File[]  fileList = dir.listFiles();

        // Recursively remove the cache directory
        traverseDir( fileList );

      } catch (Exception e) {
        System.out.println("Error during cache flush: " + e);
        System.exit(1);
     }
   }

 //
 // Simple tree traversal. Descend into directories, deleting files, and then
 // the directories once they are empty. 

 public static void traverseDir( File[] listF ) {

      try {

        for (int i=0; i<java.lang.reflect.Array.getLength(listF); i++) {
           //
           // A regular file was found, delete it
           //
           if ( listF[i].exists() && listF[i].isFile() ) {
              listF[i].delete();

           // 
           // A directory was found, check if it has any files within it, if
           // not delete it, otherwise traverse next level
           // 
           } else if ( listF[i].isDirectory() ) {


              if ( java.lang.reflect.Array.getLength( listF[i].listFiles() ) == 0 ) {
                 // Does this directory contain anything? No. Just delete it.
                 listF[i].delete();

              } else { 

                 // Does this directory contain anything? Yes. Traverse again
                 // and then delete the directory on return
                 traverseDir( listF[i].listFiles() );
                 listF[i].delete();
              }
           }
        }
      } catch (Exception e) {
        System.out.println("Error during cache flush: " + e);
        System.exit(2);
     }
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
