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
 * $Header: /src/mps/com/volantis/testtools/TestGen.java,v 1.1 2002/12/10 09:48:58 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Aug-02    Allan           VBM:2002082906 - Created from Ian W's 
 *                              original - just added some more javadoc and 
 *                              this history and changed the package from 
 *                              test to testtools.
 * 10-Oct-02    Allan           VBM:2002100712 - Changed the names of test
 *                              methods start with "notest" instead of "test"
 *                              so they are disabled by default. Also modified
 *                              the tab settings to 4 from 2.
 * 11-Oct-02    Ian             VBM:2002090602 - Fixed handling of arrays to
 *                              build test case.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools;

import java.lang.ClassNotFoundException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * This class provides a command line tool for generating a skeleton JUnit
 * testsuite for the given class.
 *
 * To run: <code>java com/volantis/mcs/testtools/TestGen <class> </code>
 *      - where <class> is the fully qualified class name (e.g.
 *      "com.volantis.mcs.assets.TextAsset").
 *
 * Output goes to stdout and should be redirected to the appropriate java file
 * e.g. testsuite/com/volantis/mcs/assets/TextAssetTestCase.java.         
 */
public class TestGen {

  protected StringBuffer buffer=new StringBuffer();
  protected StringBuffer body=new StringBuffer();
  protected ArrayList tests=new ArrayList();
  protected TreeSet imports=new TreeSet();

  protected ArrayList simpleTypes=new ArrayList();

  public TestGen(String classToTest) {
    initialiseSimpleTypes();
    // The class object of the class we want to test
    Class testClass;
    // The package of the class we want to test
    Package testPackage;


    try {
      //
      // Load the class
      //
      testClass=Class.forName(classToTest);
      addImports(testClass);

      this.generateHeader(testClass);
      this.generateConstructorTests(testClass);
      this.generateMethodTests(testClass);
      this.generateSuite(testClass);
      this.generateFooter(testClass);
      this.generateImports();
      System.out.print(buffer.toString());

    } catch (ClassNotFoundException e) {
      System.err.println();
      System.err.println("Class " + classToTest
                         + " cannot be found in the classpath.");
      System.err.println();
    }
  }

  public static void main (String[] args) {

    if (args.length==1) {
      TestGen instance=new TestGen(args[0]);
    } else {
      usage();
    }
  }

  /**
   * Print out command usage to syserr.
   */
  protected static void usage() {
    System.err.println();
    System.err.println("Usage: TestGen class");
    System.err.println();
  }

  /**
   * Generate the default package and imports and the class headers.
   * @param testClass The class being tested.
   */
  protected void generateHeader(Class testClass) {

    Package testPackage=testClass.getPackage();
    String packageName=testPackage.getName();
    String className=testClass.getName().substring(packageName.length()+1);

    buffer.append("package "+packageName+";\n\n");
    buffer.append("import junit.framework.*;\n\n");
    body.append("\n");
    body.append("/**\n");
    body.append(" * This class unit test the " + className + "class.\n");
    body.append(" */\n");
    body.append("public class "+className+"TestCase \n");
    body.append("    extends TestCase {\n\n");
    body.append("    public "+className+"TestCase(String name) {\n");
    body.append("        super(name);\n");
    body.append("    }\n\n");
  }


  protected void generateConstructorTests(Class testClass) {

    StringBuffer constructorSyntax=new StringBuffer();
    char filler=' ';

    Constructor[] constructors=testClass.getConstructors();
    body.append("    /**\n");
    body.append("     * This method tests the constructors for\n");
    body.append("     * the "+testClass.getName()+" class.\n");
    body.append("     */\n");
    body.append("    public void testConstructors() {\n");

    for ( int n=0; n<constructors.length; n++ ) {
      if (constructors[n].getDeclaringClass().equals(testClass)) {
        this.addImports(constructors[n].getExceptionTypes());
        this.addImports(constructors[n].getParameterTypes());
        Class[] parameters=constructors[n].getParameterTypes();
        constructorSyntax=new StringBuffer();
        constructorSyntax.append("public ");
        constructorSyntax.append(this.stripPackageName(constructors[n]));
        constructorSyntax.append(" (");
        filler=' ';
        for ( int p = 0; p < parameters.length; p++ ) {
          constructorSyntax.append(filler);
          constructorSyntax.append(this.stripPackageName(parameters[p]));
          filler=',';
        }
        constructorSyntax.append(" )");
        body.append("        //\n");
        body.append("        // Test "+constructorSyntax+
                      " constructor\n");
        body.append("        //\n");
        body.append("        Assert.fail(\""+constructorSyntax+
                      " not tested.\");\n");
      }

    }
    body.append("    }\n\n");

  }

  protected void generateMethodTests(Class testClass) {

    String previousMethod=null;
    String methodName=null;
    String testMethodName=null;
    boolean inMethod=false;
    StringBuffer  methodSyntax=new StringBuffer();
    char filler=' ';

    Method[] methods=testClass.getMethods();
    for ( int n=0; n<methods.length; n++ ) {
      if ( methods[n].getDeclaringClass().equals(testClass) ) {
        methodName=methods[n].getName();
        if (!methodName.equals(previousMethod)) {
          if (inMethod) {
            body.append("    }\n\n");
            inMethod=false;
          }
          Class returnType=methods[n].getReturnType();
	  System.err.println();
	  System.err.println("method: " + methodName);
	  System.err.println("returnType" + returnType);
          Class parameters[] = methods[n].getParameterTypes();
          methodSyntax = new StringBuffer();
          methodSyntax.append("public ");
          methodSyntax.append(this.stripPackageName(returnType));
          methodSyntax.append(' ');
          methodSyntax.append(this.stripPackageName(methods[n]));
          methodSyntax.append(" (");
          filler=' ';
          for ( int p = 0; p < parameters.length; p++ ) {
            methodSyntax.append(filler);
            methodSyntax.append(this.stripPackageName(parameters[p]));
            filler=',';
          }
          methodSyntax.append(" )");
          testMethodName="notest"+methodName.substring(0,1).toUpperCase()+
                         methodName.substring(1);
          body.append("    /**\n");
          body.append("     * This method tests the method "+methodSyntax+"\n");
          body.append("     * for the "+testClass.getName()+" class.\n");
          body.append("     */\n");
          body.append("    public void "+testMethodName+"()\n");
          body.append("        throws Exception {\n");
          inMethod=true;
          tests.add(testMethodName);
        }
        body.append("        //\n");
        body.append("        // Test "+methodSyntax+" method.\n");
        body.append("        //\n");
        body.append("        Assert.fail(\""+methodSyntax+
                      " not tested.\");\n");
        previousMethod=methodName;
        this.addImports(methods[n].getExceptionTypes());
        this.addImports(methods[n].getParameterTypes());
        this.addImports(methods[n].getReturnType());
      }
    }
    if (inMethod) {
      body.append("    }\n\n");
      inMethod=false;
    }

  }

  /**
   * This generates the closing class braces.
   */
  protected void generateFooter(Class testClass) {
    body.append("}\n");
  }

  /**
   * Generate the testSuite method.
   */
  protected void generateSuite(Class testClass) {
    Package testPackage=testClass.getPackage();
    String packageName=testPackage.getName();
    String className=testClass.getName().substring(packageName.length()+1);
    body.append("    /**\n");
    body.append("     * This method runs the entire test suite\n");
    body.append("     * for the "+testClass.getName()+" class.\n");
    body.append("     */\n");
    body.append("    public static Test suite() {\n");
    body.append("        return new TestSuite("+className+"TestCase.class);\n");
    body.append("    }\n\n");
    body.append("    public static void main(String args[]) {\n");
    body.append("        junit.textui.TestRunner.run(suite());\n");
    body.append("    }\n\n");
  }

  /**
   * Generate import statements for all the classes used by the class we
   * are testing.
   */
  protected void generateImports() {
    Iterator i=imports.iterator();
    while (i.hasNext()) {
      buffer.append("import "+(String)i.next()+";\n");
    }
    buffer.append(body);
  }

  protected void addImports(Class[] classes) {

    for ( int n=0; n<classes.length; n++) {
      addImports(classes[n]);
    }
  }

  protected void addImports(Class singleClass) {

    if ( !simpleTypes.contains(singleClass.getName()) ) {
      if ( singleClass.getName().indexOf('[') == -1 ) {  
	int index=singleClass.getName().indexOf('$');
	if (index>0) {
	  imports.add(singleClass.getName().substring(0,index));
	} else {
	  imports.add(singleClass.getName());
	}
      }
    }
  }

  protected String stripPackageName(Class theClass) {
    Package thePackage=theClass.getPackage();
    System.err.println("thePackage: " + thePackage);
    String className=theClass.getName();
    if ( thePackage != null  ) {
      if ( className.indexOf('.') != -1 ) {
        int start=thePackage.getName().length()+1;
        className=className.substring(start);
      }
    }
    return className;
  }

  protected String stripPackageName(Method theMethod) {
    Class theClass=theMethod.getDeclaringClass();
    Package thePackage=theClass.getPackage();
    String methodName=theMethod.getName();
    if ( thePackage != null ) {
      if ( methodName.indexOf('.') != -1 ) {
        int start=thePackage.getName().length()+1;
        methodName=methodName.substring(start);
      }
    } 
    return methodName;
  }

  protected String stripPackageName(Constructor theConstructor) {
    Class theClass=theConstructor.getDeclaringClass();
    Package thePackage=theClass.getPackage();
    String constructorName=theConstructor.getName();
    if ( thePackage != null ) {
      if ( constructorName.indexOf('.') != -1 ) {
        int start=thePackage.getName().length()+1;
        constructorName=constructorName.substring(start);
      }
    }
    return constructorName;
  }

  /**
   * Initialise simpleTypes table with the
   * default java simple types.
   */
  protected void initialiseSimpleTypes() {
    simpleTypes.add("boolean");
    simpleTypes.add("char");
    simpleTypes.add("double");
    simpleTypes.add("float");
    simpleTypes.add("int");
    simpleTypes.add("long");
    simpleTypes.add("void");

  }


}
