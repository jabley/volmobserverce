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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/repository/ImageRepositoryManagerTestCase.java,v 1.8 2003/04/16 10:23:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Aug-02    Ian             VBM:2002081303 - Created.
 * 06-Sep-02    Ian             VBM:2002081307 - Change to refect change in 
 *                              identity for ConvertibleImageAsset.
 * 18-Feb-03    Geoff           VBM:2003021901 - Remove all test methods that
 *                              don't actually test anything as part of 
 *                              re-enabling the running of this test.
 * 11-Mar-03    Allan           VBM:2003022103 - Added tests for adding and 
 *                              removing components. 
 * 17-Apr-03    Geoff           VBM:2003041505 - Comment out redundant test
 *                              which was causing IDEA Junit to give false 
 *                              failure, and add extra line to prevent state
 *                              "leaking" between tests.
 * 08-May-03    Allan           VBM:2003050704 - Caches are now in the 
 *                              synergetics package. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository;

import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.components.ButtonImageComponent;
import com.volantis.mcs.components.ImageComponent;
import com.volantis.mcs.components.RolloverImageComponent;
import com.volantis.mcs.repository.xml.XMLRepository;
import junit.framework.Assert;


/**
 * This class unit tests the ImageRepositoryManager
 */
public class ImageRepositoryManagerTestCase
    extends RepositoryManagerTestAbstract {

    /**
     * Test that adding an ImageComponent adds the component along with a
     * Folder reference.
     */ 
    public void testAddImageComponent()
            throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                ImageComponent ic = new ImageComponent("/testAddImageComponent.mimg");
                ImageRepositoryManager manager =
                        new ImageRepositoryManager(repository);
                manager.addImageComponent(repositoryConnection, ic);

                containerExists(ic.getName(), false, repositoryConnection,
                        repository);
            }
        });
    }

    /**
     * Test that removing an ImageComponent removes the component along with
     * the Folder reference.
     */ 
    public void testRemoveImageComponent()
            throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                ImageComponent ic = new ImageComponent("/testRemoveImageComponent.mimg");
                ImageRepositoryManager manager =
                        new ImageRepositoryManager(repository);
                manager.addImageComponent(repositoryConnection, ic);
                containerExists(ic.getName(), false, repositoryConnection,
                        repository);

                manager.removeImageComponent(repositoryConnection,
                        ic.getName());

                containerExists(ic.getName(), true, repositoryConnection,
                        repository);
            }
        });
    }
    
    /**
     * Test that adding a ButtonImageComponent adds the component along with a
     * Folder reference.
     */ 
    public void testAddButtonImageComponent()
            throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                ButtonImageComponent ic =
                        new ButtonImageComponent("/testAddButtonImageComponent.mbtn");
                ic.setUpImageComponentName("/up.mimg");
                ic.setOverImageComponentName("/over.mimg");
                ic.setDownImageComponentName("/down.mimg");
                ImageRepositoryManager manager =
                        new ImageRepositoryManager(repository);
                manager.addButtonImageComponent(repositoryConnection, ic);

                containerExists(ic.getName(), false, repositoryConnection,
                        repository);
            }
        });
    }

    /**
     * Test that removing a ButtonImageComponent removes the component along 
     * with the Folder reference.
     */ 
    public void testRemoveButtonImageComponent()
            throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                ButtonImageComponent ic =
                        new ButtonImageComponent("/testRemoveButtonImageComponent.mbtn");
                ic.setUpImageComponentName("/up.mimg");
                ic.setOverImageComponentName("/over.mimg");
                ic.setDownImageComponentName("/down.mimg");
                ImageRepositoryManager manager =
                        new ImageRepositoryManager(repository);

                manager.addButtonImageComponent(repositoryConnection, ic);

                containerExists(ic.getName(), false, repositoryConnection,
                        repository);

                manager.removeButtonImageComponent(repositoryConnection,
                        ic.getName());

                containerExists(ic.getName(), true, repositoryConnection,
                        repository);
            }
        });
    }
        
    /**
     * Test that adding a RolloverImageComponent adds the component along with 
     * a Folder reference.
     */ 
    public void testAddRolloverImageComponent()
            throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
        RolloverImageComponent ic = 
                new RolloverImageComponent("/testAddRolloverImageComponent.mrlv");
                ic.setNormalImageComponentName("/normal.mimg");
                ic.setOverImageComponentName("/over.mimg");
        ImageRepositoryManager manager =
               new ImageRepositoryManager(repository);
        manager.addRolloverImageComponent(repositoryConnection, ic);

                containerExists(ic.getName(), false, repositoryConnection, repository);
            }
        });
    }

    /**
     * Test that removing a RolloverImageComponent removes the component along 
     * with the Folder reference.
     */ 
    public void testRemoveRolloverImageComponent()
            throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                RolloverImageComponent ic =
                        new RolloverImageComponent("/testRemoveRolloverImageComponent.mrlv");
                ic.setNormalImageComponentName("/normal.mimg");
                ic.setOverImageComponentName("/over.mimg");
                ImageRepositoryManager manager =
                        new ImageRepositoryManager(repository);
                manager.addRolloverImageComponent(repositoryConnection, ic);

                containerExists(ic.getName(), false, repositoryConnection,
                        repository);

                manager.removeRolloverImageComponent(repositoryConnection,
                        ic.getName());

                containerExists(ic.getName(), true, repositoryConnection,
                        repository);
            }
        });
    }
          
    // This is redundant, it's a copy and paste of part of the next test
    // and it leaves the cache in an unknown state which can affect later 
    // tests, so I have disabled it.
//  /**
//   * This method tests the method setConvertibleAssetCache
//   * for the com.volantis.mcs.repository.ImageRepositoryManager class.
//   */
//  public void testSetConvertibleAssetCache()
//      throws Exception {
//    //
//    // Test public void setConvertibleAssetCache(GenericCache) method.
//    //
//
//    //
//    // Setup a basic cache for the test.
//    //
//    GenericCache cache=GenericCacheFactory.createCache(null,-1,-1);
//
//    ImageRepositoryManager imageRepositoryManager=
//        new ImageRepositoryManager(repository);
//
//    ImageComponent component=
//        new ImageComponent("MyAsset");
//    ConvertibleImageAsset asset =
//        new ConvertibleImageAsset("MyAsset",
//                                  100,
//                                  200,
//                                  32,
//                                  ConvertibleImageAsset.COLOR,
//                                  ConvertibleImageAsset.JPEG,
//                                  "MyAssetGroup",
//                                  "MyValue");
//    imageRepositoryManager.setConvertibleAssetCache(cache);
//    imageRepositoryManager.addImageComponent(repositoryConnection,component);
//    imageRepositoryManager.addConvertibleImageAsset(repositoryConnection,asset);
//
//    Assert.assertTrue("public void setConvertibleAssetCache(GenericCache) cache does not contain asset.",cache.containsKey(asset.getIdentity()));
//  }

  /**
   * This method tests the method addConvertibleImageAsset
   * for the com.volantis.mcs.repository.ImageRepositoryManager class.
   */
  public void testAddConvertibleImageAsset()
      throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
    //
    // Test public void com.volantis.mcs.repository.ImageRepositoryManager.addConvertibleImageAsset(com.volantis.mcs.repository.RepositoryConnection,com.volantis.mcs.assets.ConvertibleImageAsset) throws com.volantis.mcs.repository.RepositoryException method.
    //
    String assetName="/AddConvertibleImageAsset.mimg";
    int pixelsX=100;
    int pixelsY=200;
    int pixelDepth=32;
    int rendering=ConvertibleImageAsset.COLOR;
    int encoding=ConvertibleImageAsset.JPEG;
    String assetGroup="/MyAssetGroup.mgrp";
    String assetValue="MyValue";

    ImageRepositoryManager imageRepositoryManager=
        new ImageRepositoryManager(repository);

    ImageComponent component=
        new ImageComponent(assetName);
    ConvertibleImageAsset asset =
        new ConvertibleImageAsset(assetName,
                                  pixelsX,
                                  pixelsY,
                                  pixelDepth,
                                  rendering,
                                  encoding,
                                  assetGroup,
                                  assetValue);

    imageRepositoryManager.addImageComponent(repositoryConnection,component);
    imageRepositoryManager.addConvertibleImageAsset(repositoryConnection,asset);
    ConvertibleImageAsset asset2 = imageRepositoryManager.
                                   retrieveConvertibleImageAsset (
                                   repositoryConnection,
                                   assetName);
    Assert.assertTrue("public void addConvertibleImageAsset(RepositoryConnection,ConvertibleImageAsset) throws RepositoryException asset not equal.",
                        asset.equals(asset2));
  }
        });
    }

  /**
   * This method tests the method copyConvertibleImageAsset
   * for the com.volantis.mcs.repository.ImageRepositoryManager class.
   */
  public void notestCopyConvertibleImageAsset()
      throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
    //
    // Test public void copyConvertibleImageAsset(RepositoryConnection,String,int,int,int,int,int,String) throws RepositoryException method.
    //
    String assetName="/CopyConvertibleImageAsset.mimg";
    String newAssetName="/CopyAsset.mimg";
    int pixelsX=100;
    int pixelsY=200;
    int pixelDepth=32;
    int rendering=ConvertibleImageAsset.COLOR;
    int encoding=ConvertibleImageAsset.JPEG;
    String assetGroup="/MyAssetGroup.mgrp";
    String assetValue="MyValue";

    ImageRepositoryManager imageRepositoryManager=
        new ImageRepositoryManager(repository);

    ImageComponent component=
        new ImageComponent(assetName);
    ConvertibleImageAsset asset =
        new ConvertibleImageAsset(assetName,
                                  pixelsX,
                                  pixelsY,
                                  pixelDepth,
                                  rendering,
                                  encoding,
                                  assetGroup,
                                  assetValue);

    imageRepositoryManager.addImageComponent(repositoryConnection,component);
    imageRepositoryManager.addConvertibleImageAsset(repositoryConnection,asset);
    //
    // Make a copy of our convertible image asset.
    //
    component.setName(newAssetName);
    imageRepositoryManager.addImageComponent(repositoryConnection,component);
    imageRepositoryManager.copyConvertibleImageAsset(repositoryConnection,
                                                     assetName,
                                                     newAssetName);
    ConvertibleImageAsset asset2 = imageRepositoryManager.
                                   retrieveConvertibleImageAsset (
                                   repositoryConnection,
                                   newAssetName);
    Assert.assertTrue("public void copyConvertibleImageAsset(RepositoryConnection,String,String) throws RepositoryException asset group name not equal.",asset.getAssetGroupName().equals(asset2.getAssetGroupName()));
    Assert.assertTrue("public void copyConvertibleImageAsset(RepositoryConnection,String,String) throws RepositoryException value not equal.",asset.getValue().equals(asset2.getValue()));
  }
        });
  }

  /**
   * This method tests the method removeConvertibleImageAsset
   * for the com.volantis.mcs.repository.ImageRepositoryManager class.
   */
  public void testRemoveConvertibleImageAsset()
      throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
    //
    // Test public void removeConvertibleImageAsset(RepositoryConnection,String,int,int,int,int,int) throws RepositoryException method.
    //
    String assetName="/RemoveConvertibleImageAsset.mimg";
    int pixelsX=100;
    int pixelsY=200;
    int pixelDepth=32;
    int rendering=ConvertibleImageAsset.COLOR;
    int encoding=ConvertibleImageAsset.JPEG;
    String assetGroup="/MyAssetGroup.mgrp";
    String assetValue="MyValue";

    ImageRepositoryManager imageRepositoryManager=
        new ImageRepositoryManager(repository);

    ImageComponent component=
        new ImageComponent(assetName);
    ConvertibleImageAsset asset =
        new ConvertibleImageAsset(assetName,
                                  pixelsX,
                                  pixelsY,
                                  pixelDepth,
                                  rendering,
                                  encoding,
                                  assetGroup,
                                  assetValue);

    imageRepositoryManager.addImageComponent(repositoryConnection,component);
    imageRepositoryManager.addConvertibleImageAsset(repositoryConnection,asset);
    imageRepositoryManager.removeConvertibleImageAsset(repositoryConnection,
                                                       assetName);

    ConvertibleImageAsset asset2 = imageRepositoryManager.
                                   retrieveConvertibleImageAsset (
                                   repositoryConnection,
                                   assetName);
    Assert.assertEquals("public void removeConvertibleImageAsset(RepositoryConnection,String,int,int,int,int,int) throws RepositoryException asset not null.",asset2,null);
  }
        });
    }

  /**
   * This method tests the method retrieveConvertibleImageAsset
   * for the com.volantis.mcs.repository.ImageRepositoryManager class.
   */
  public void testRetrieveConvertibleImageAsset()
      throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
    //
    // Test public ConvertibleImageAsset retrieveConvertibleImageAsset(RepositoryConnection,String,int,int,int,int,int) throws RepositoryException method.
    //
    String assetName="/AddConvertibleImageAsset.mimg";
    int pixelsX=100;
    int pixelsY=200;
    int pixelDepth=32;
    int rendering=ConvertibleImageAsset.COLOR;
    int encoding=ConvertibleImageAsset.JPEG;
    String assetGroup="/MyAssetGroup.mgrp";
    String assetValue="MyValue";

    ImageRepositoryManager imageRepositoryManager=
        new ImageRepositoryManager(repository);

    ImageComponent component=
        new ImageComponent(assetName);
    ConvertibleImageAsset asset =
        new ConvertibleImageAsset(assetName,
                                  pixelsX,
                                  pixelsY,
                                  pixelDepth,
                                  rendering,
                                  encoding,
                                  assetGroup,
                                  assetValue);

    imageRepositoryManager.addImageComponent(repositoryConnection,component);
    imageRepositoryManager.addConvertibleImageAsset(repositoryConnection,asset);
    ConvertibleImageAsset asset2 = imageRepositoryManager.
                                   retrieveConvertibleImageAsset (
                                   repositoryConnection,
                                   assetName);
    Assert.assertTrue("public ConvertibleImageAsset retrieveConvertibleImageAsset(RepositoryConnection,String) throws RepositoryException not equal.",asset.equals(asset2));
  }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 17-Feb-05	6974/3	emma	VBM:2005021411 Reworking tests to use testtools TempFileManager mechanism

 16-Feb-05	6974/1	emma	VBM:2005021411 Fixing tests to provide absolute repository directory

 02-Feb-05	6828/1	matthew	VBM:2005012601 Allow new Cache mechanism to work with MCS (not optimally though)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jan-04	2595/1	andy	VBM:2004011404 changed internal representation of policy names

 23-Dec-03	2252/1	andy	VBM:2003121703 change to default name for non existant repository in test suite

 19-Nov-03	1964/3	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 19-Nov-03	1964/1	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
