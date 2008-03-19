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
package com.volantis.mcs.eclipse.builder.wizards.variants;

import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.eclipse.common.DelayedTaskExecutor;
import com.volantis.mcs.eclipse.controls.FolderSelector;
import com.volantis.mcs.eclipse.controls.GenericAssetLabelProvider;
import com.volantis.mcs.eclipse.controls.GenericAssetTreeContentProvider;
import com.volantis.mcs.eclipse.controls.UnavailablePreview;
import com.volantis.mcs.eclipse.controls.images.DefaultImageProvider;
import com.volantis.mcs.eclipse.controls.images.ImageProvider;
import com.volantis.mcs.eclipse.controls.images.TIFFImageProvider;
import com.volantis.mcs.eclipse.controls.images.WBMPImageProvider;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.synergetics.io.IOUtils;
import com.volantis.synergetics.log.LogDispatcher;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.dialogs.ViewSorter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Allows multiple image variants to be created based on files on the local
 * filesystem. For each selected image, a variant is created, with values
 * pre-populated as much as possible.
 */
public class ImageVariantCreationPage extends WizardPage implements VariantWizardPage {
    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(ImageVariantCreationPage.class);

    /**
     * Factory for creating new image variants.
     */
    private static final PolicyFactory POLICY_FACTORY =
            PolicyFactory.getDefaultInstance();

    /**
     * Prefix for resource strings associated with this page.
     */
    private static final String RESOURCE_PREFIX = "ImageVariantCreationPage.";

    /**
     * Suggested width for the page.
     */
    private static final int PAGE_WIDTH_HINT =
            WizardMessages.getInteger("Wizard.page.width.hint").intValue();

    /**
     * Height for the image preview.
     */
    private static final int PREVIEW_HEIGHT_HINT =
            WizardMessages.getInteger(RESOURCE_PREFIX + "preview.height.hint").
            intValue();

    /**
     * Horizontal space to leave between buttons.
     */
    private static final int HORIZONTAL_BUTTON_GAP =
            WizardMessages.getInteger("Wizard.horizontal.button.gap").intValue();

    /**
     * Default spacing between components.
     */
    private static final int DEFAULT_COMPONENT_SPACING =
            WizardMessages.getInteger("Wizard.viewer.gap").intValue();

    /**
     * Time in milliseconds between selecting a root folder and scanning its
     * contents. Prevents typing a folder name in manually from hitting the
     * filesystem too frequently.
     */
    private static final int FOLDER_SCAN_DELAY = 500;

    /**
     * A composite containing a stack of previews.
     */
    private Composite stackComposite;

    /**
     * The layout for previews.
     */
    private StackLayout stack;

    /**
     * An image preview for an image that cannot be displayed.
     */
    private UnavailablePreview unavailablePreview;

    private Button selectAllButton;

    private Button deselectAllButton;

    /**
     * Viewer for the file tree.
     */
    private CheckboxTreeViewer treeViewer;

    /**
     * Component for selecting a folder on the filesystem.
     */
    private FolderSelector folderSelector;

    /**
     * Content provider for the file tree.
     */
    private GenericAssetTreeContentProvider contentProvider =
            new GenericAssetTreeContentProvider(new File("."));

    private DelayedTaskExecutor folderScanner;

    /**
     * A Map of ImageProviders keyed on file extension.
     */
    private static Map providers = new HashMap();

    static {
        ImageProvider defaultProvider = new DefaultImageProvider();
        ImageProvider tiffProvider = new TIFFImageProvider();
        ImageProvider wbmpProvider = new WBMPImageProvider();

        // TIFF images
        associateEncoding(ImageEncoding.TIFF, tiffProvider);

        // WBMP images
        associateEncoding(ImageEncoding.WBMP, wbmpProvider);

        // All flavours of JPEG
        associateEncoding(ImageEncoding.JPEG, defaultProvider);
        associateEncoding(ImageEncoding.PJPEG, defaultProvider);

        // GIF 87 & 89
        associateEncoding(ImageEncoding.GIF, defaultProvider);

        // PNG Images
        associateEncoding(ImageEncoding.PNG, defaultProvider);

        // ASCII art (VIDEOTEX)
        associateEncoding(ImageEncoding.VIDEOTEX, defaultProvider);

        // Windows bitmaps
        associateEncoding(ImageEncoding.BMP, defaultProvider);
    }

    /**
     * Associates an encoding with a specified image provider.
     *
     * @param encoding The encoding to match
     * @param provider The image provider to match it with
     */
    private static void associateEncoding(Encoding encoding, ImageProvider provider) {
        Iterator it = encoding.extensions();
        while (it.hasNext()) {
            providers.put(it.next(), provider);
        }
    }

    /**
     * The default expansion level for the tree viewer for this
     * implementation. A value of CheckboxTreeViewer.ALL_LEVELS
     * expands the tree fully, but can be time consuming.
     */
    private static final int DEFAULT_TREE_EXPANSION_LEVEL = 1;

    public ImageVariantCreationPage() {
        super("image.ImageVariantCreationPage");
    }

    // Javadoc inherited
    public void createControl(Composite parent) {
        // Create top level composite
        Composite topLevel = new Composite(parent, SWT.NONE);
        GridLayout gridl = new GridLayout();
        gridl.numColumns = 2;
        topLevel.setLayout(gridl);
        GridData gd = new GridData();
        gd.widthHint = PAGE_WIDTH_HINT;
        topLevel.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create FolderSelector component and attach listener
        Label folderSelectorLabel = new Label(topLevel, SWT.NONE);
        folderSelectorLabel.setText(WizardMessages.getString(RESOURCE_PREFIX +
                "folderSelector.label"));
        folderSelector = new FolderSelector(topLevel, SWT.NONE);
        GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
        gd1.grabExcessHorizontalSpace = true;
        folderSelector.setLayoutData(gd1);

        folderScanner =
                new DelayedTaskExecutor("GenericAssetCreationPage.folderScanner",
                        FOLDER_SCAN_DELAY) {
                    public void executeTask() {
                        scanFolders();
                    }
                };
        folderScanner.start();

        folderSelector.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                folderScanner.interrupt();
            }
        });

        // Create FilterCombo component and attach listener
        Label assetTypeFilterLabel = new Label(topLevel, SWT.NONE);
        assetTypeFilterLabel.setText(WizardMessages.getString(RESOURCE_PREFIX +
                "assetTypeFilter.label"));
        ComboViewer filterCombo = new ComboViewer(topLevel, SWT.READ_ONLY);
        filterCombo.setContentProvider(new EncodingCollectionContentProvider(true));
        filterCombo.setLabelProvider(new EncodingCollectionExtensionLabelProvider("image"));
        filterCombo.setInput(ImageEncoding.COLLECTION);

        filterCombo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Object selectedEncoding = selection.getFirstElement();
                if (selectedEncoding instanceof EncodingCollection) {
                    setFilter(new EncodingCollectionViewerFilter(
                            (EncodingCollection) selectedEncoding));
                } else {
                    setFilter(new EncodingViewerFilter(
                            (Encoding) selectedEncoding));
                }
            }
        });
        GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
        filterCombo.getControl().setLayoutData(gd2);

        // Add label for tree viewer
        Label componentLabel = new Label(topLevel, SWT.NONE);
        componentLabel.setText(WizardMessages.getString(RESOURCE_PREFIX +
                "tree.label"));

        // Create Composite which contains tree viewer and preview
        Composite treeAndPreviewComposite = new Composite(topLevel, SWT.NONE);
        GridLayout grid = new GridLayout();
        grid.numColumns = 2;
        grid.makeColumnsEqualWidth = true;
        grid.horizontalSpacing = DEFAULT_COMPONENT_SPACING;
        treeAndPreviewComposite.setLayout(grid);
        GridData gd3 = new GridData();
        gd3.grabExcessHorizontalSpace = true;
        gd3.grabExcessVerticalSpace = true;
        gd3.horizontalAlignment = GridData.FILL;
        gd3.verticalAlignment = GridData.FILL;
        gd3.horizontalSpan = 2;
        treeAndPreviewComposite.setLayoutData(gd3);

        // Create and add the tree viewer
        treeViewer = createCheckboxTreeViewer(treeAndPreviewComposite,
                SWT.BORDER);

        // Create a Composite with a StackLayout for displaying changing previews.
        // Some event handlers need access to stackComposite for creating new
        // previews.
        stackComposite = new Composite(treeAndPreviewComposite, SWT.NONE);
        GridData gd4 = new GridData();
        gd4.verticalAlignment = GridData.FILL;
        gd4.horizontalAlignment = GridData.FILL;
        gd4.grabExcessVerticalSpace = true;
        gd4.grabExcessHorizontalSpace = true;
        gd4.heightHint = PREVIEW_HEIGHT_HINT;
        gd4.horizontalSpan = 1;
        stackComposite.setLayoutData(gd4);
        stack = new StackLayout();
        stackComposite.setLayout(stack);

        // Create the initial unavailable preview.
        unavailablePreview = new UnavailablePreview(stackComposite, SWT.NONE);

        // Note: It is unclear why the StackLayout must access the Preview's
        // group here rather than just the preview. There may be a bug with
        // SWT groups/composites/stacklayouts. Within the Preview class,
        // delegating setVisible(boolean) to the Preview's group results
        // in no widget being displayed, as does delegating all Composite
        // methods to the Preview's group.
        stack.topControl = unavailablePreview.getGroup();
        stackComposite.layout();

        // Create a composite with a RowLayout to contain the
        // "Select All" and "Deselect All" buttons.
        Composite buttonComposite = new Composite(topLevel, SWT.NONE);
        RowLayout rowLayout = new RowLayout();
        rowLayout.spacing = HORIZONTAL_BUTTON_GAP;
        buttonComposite.setLayout(rowLayout);

        // Create the "Select All" button.
        selectAllButton = new Button(buttonComposite, SWT.PUSH);
        selectAllButton.setText(WizardMessages.getString(RESOURCE_PREFIX +
                "button.selectAll.label"));

        // Add the button's listener
        selectAllButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                    public void run() {
                        treeViewer.expandToLevel(contentProvider.
                                getActualRoot(),
                                CheckboxTreeViewer.ALL_LEVELS);
                        treeViewer.setSubtreeChecked(contentProvider.
                                getActualRoot(), true);
                        updateButtons();
                    }
                });
            }
        });
        // The "Select All" button is initially disabled.
        selectAllButton.setEnabled(false);

        // Create the "Select All" button.
        deselectAllButton = new Button(buttonComposite, SWT.PUSH);
        deselectAllButton.setText(WizardMessages.getString(RESOURCE_PREFIX +
                "button.deselectAll.label"));
        deselectAllButton.setEnabled(false);

        // Add the button's listener
        deselectAllButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                    public void run() {
                        treeViewer.setSubtreeChecked(contentProvider.
                                getActualRoot(), false);
                        updateButtons();
                    }
                });
            }
        });
        setErrorMessage(null);
        setMessage(null);
        setControl(topLevel);
        filterCombo.getCombo().select(0);
        setFilter(new EncodingCollectionViewerFilter(ImageEncoding.COLLECTION));
    }

    // Javadoc inherited
    public void performFinish(List variants) {
        Object[] selection = treeViewer.getCheckedElements();
        for (int i = 0; i < selection.length; i++) {
            File selectedFile = (File) selection[i];
            if (selectedFile.isFile()) {
                variants.add(createVariant(selectedFile));
            }
        }
    }

    /**
     * Creates an image variant builder from the contents of a file.
     *
     * @param file The image file for which to create the builder
     * @return The builder
     */
    private VariantBuilder createVariant(File file) {
        VariantBuilder variant = POLICY_FACTORY.createVariantBuilder(VariantType.IMAGE);
        ImageMetaDataBuilder metaData = POLICY_FACTORY.createImageMetaDataBuilder();

        ImageData data = getImageData(file);
        if (data != null) {
            metaData.setHeight(data.height);
            metaData.setWidth(data.width);
            metaData.setPixelDepth(data.depth);
        }

        String extension = IOUtils.getExtension(file);
        Encoding imageEncoding = ImageEncoding.COLLECTION.
                getEncodingForExtension(extension);
        if (imageEncoding != null) {
            metaData.setImageEncoding((ImageEncoding) imageEncoding);
        }

        File root = new File(folderSelector.getText().getText());
        if (root.getPath().length() < file.getPath().length()) {
            String url = file.getPath().substring(root.getPath().length());
            URLContentBuilder content = POLICY_FACTORY.createURLContentBuilder();
            content.setURL(url);
            variant.setContentBuilder(content);
        }

        variant.setMetaDataBuilder(metaData);
        return variant;
    }

    /**
     * Enable/disable a button's state depending on the state of the tree
     * viewer. Handles both the internal buttons (select/deselect all) and the
     * wizard buttons.
     */
    private void updateButtons() {
        selectAllButton.setEnabled(contentProvider != null);
        deselectAllButton.setEnabled(treeViewer.getCheckedElements().length > 0);
        getWizard().getContainer().updateButtons();
    }

    // Javadoc inherited
    public void dispose() {
        super.dispose();
        folderScanner.dispose();
    }

    /**
     * Create the viewer for the file tree.
     *
     * @param parent The parent composite
     * @param style The SWT style to use
     * @return A viewer for the file tree
     */
    private CheckboxTreeViewer createCheckboxTreeViewer(Composite parent, int style) {
        treeViewer = new CheckboxTreeViewer(parent, style);
        treeViewer.setLabelProvider(new GenericAssetLabelProvider());
        treeViewer.setAutoExpandLevel(2);

        // Add a sorter which sorts the files according to the locale's
        // collation, and which also sorts directories before files.
        // There is also a public int category(Object node) method
        // for the sorter, to sort objects by a user-defined category
        // (e.g. return -1 for a directory and 1 for a file). This
        // did not work.
        treeViewer.setSorter(new ViewSorter(null) {
            public int compare(Viewer viewer, Object node1, Object node2) {
                File file1 = (File) node1;
                File file2 = (File) node2;
                if (file1.isDirectory() && file2.isFile()) {
                    return -1;
                }
                if (file1.isFile() && file2.isDirectory()) {
                    return 1;
                }
                return collator.compare(((File) node1).getName(),
                        ((File) node2).getName());
            }
        });

        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;
        data.heightHint = 400; // VIEWER_HEIGHT_HINT;
        treeViewer.getTree().setLayoutData(data);
        treeViewer.setUseHashlookup(true);
        treeViewer.addCheckStateListener(new ICheckStateListener() {
            // Javadoc inherited
            public void checkStateChanged(final CheckStateChangedEvent event) {
                if (event.getChecked()) {
                    // Selection event
                    BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                        public void run() {
                            // The javadoc for the setSubTreeChecked method says that it
                            // assumes that the subtree has been expanded before. If this
                            // is not the case then the javadoc suggests doing an expandToLevel.
                            treeViewer.expandToLevel(event.getElement(),
                                    DEFAULT_TREE_EXPANSION_LEVEL);
                            treeViewer.setSubtreeChecked(event.getElement(), true);
                        }
                    });
                } else {
                    // Deselection event
                    BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                        public void run() {
                            // The javadoc for the setSubTreeChecked method says that it
                            // assumes that the subtree has been expanded before. If this
                            // is not the case then the javadoc suggests doing an expandToLevel.
                            treeViewer.expandToLevel(event.getElement(),
                                    DEFAULT_TREE_EXPANSION_LEVEL);
                            treeViewer.setSubtreeChecked(event.getElement(), false);
                        }
                    });
                }
                updateButtons();
            }
        });
        treeViewer.getTree().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Tree tree = (Tree) e.getSource();
                File file = null;
                if (tree.getSelectionCount() > 0) {
                    TreeItem treeItem = tree.getSelection()[0];
                    file = (File) treeItem.getData();
                }
                if (file != null && file.isFile()) {
                    displayAsset(file);
                } else {
                    displayAsset(null);
                }
            }
        });
        return treeViewer;
    }

    /**
     * The current filter applied to the tree view.
     */
    private ViewerFilter currentFilter = null;

    /**
     * Sets a filter against the checkbox tree for file selection, to remove
     * files that are not of the appropriate type.
     *
     * @param newFilter
     */
    private void setFilter(ViewerFilter newFilter) {
        if (currentFilter != null) {
            treeViewer.removeFilter(currentFilter);
        }

        if (newFilter != null) {
            treeViewer.addFilter(newFilter);
        }
        currentFilter = newFilter;
    }

    /**
     * Scan the folders in a separate thread and display a busy indicator.
     */
    private void scanFolders() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                // Show the busy cursor in its own thread too.
                BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                    public void run() {
                        handleTreeViewerRootChange(folderSelector.getValue());
                    }
                });
            }
        });
    }

    /**
     * Handles the logic for changing the root of the tree viewer.
     * The source of the change is the FolderSelector. The change
     * is only made if the new root is valid.
     * @param pathName The pathname of the new candidate root.
     */
    private void handleTreeViewerRootChange(String pathName) {
        pathName = pathName.trim();
        File newRoot = new File(pathName);
        if (newRoot.exists() && newRoot.isDirectory()) {
            contentProvider = new GenericAssetTreeContentProvider(newRoot);
            treeViewer.setContentProvider(contentProvider);
            treeViewer.setInput(contentProvider.getRoot());
            updateButtons();
        } else {
            if (contentProvider != null) {
                treeViewer.setInput(null);
            }
            contentProvider = null;
            updateButtons();
        }
        displayAsset(null);
    }

    /**
     * Display the currently selected asset from its file.
     * @param file The file containing the asset's data
     */
    private void displayAsset(File file) {
        if (file != null) {
            ImageAssetPreviewer assetPreviewer =
                    new ImageAssetPreviewer(stackComposite, SWT.NONE, getImageData(file));
            stack.topControl = assetPreviewer.getPreview().getGroup();
        } else {
            stack.topControl = unavailablePreview.getGroup();
        }
        stackComposite.layout();
    }

    /**
     * Get the image data from a specified file.
     *
     * @param imageFile The file to read image data from
     * @return The image data from the file
     */
    private ImageData getImageData(File imageFile) {
        String extension = IOUtils.getExtension(imageFile);
        ImageProvider provider = (ImageProvider) providers.get(extension);
        ImageData imageData = null;
        if (provider != null) {
            try {
                imageData = provider.provideImage(imageFile);
            } catch (IOException e) {
                LOGGER.error("unexpected-ioexception",
                             e);
            }
        }

        return imageData;
    }

    // Javadoc inherited
    public boolean isPageComplete() {
        return countSelectedFiles() > 0;
    }

    // Javadoc inherited
    public boolean canFlipToNextPage() {
        // It only makes sense to set the selection criteria if we are only
        // creating a single variant
        return countSelectedFiles() == 1;
    }

    /**
     * Returns the number of selected image files (not including folders if a
     * whole folder has been selected).
     *
     * @return The number of selected image files
     */
    private int countSelectedFiles() {
        Object[] selection = treeViewer.getCheckedElements();
        int files = 0;
        for (int i = 0; i < selection.length; i++) {
            File file = (File) selection[i];
            if (file.isFile()) {
                files += 1;
            }
        }
        return files;
    }
}
