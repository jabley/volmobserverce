 /**
 * (c) Volantis Systems Ltd 2006.
 */

Widget.Tabs = Class.create();

Object.extend(Widget.Tabs.prototype, {
  /*
   *  id - id attribute from table element
   *  labels - array of id attribute from td elements (labels in StripTab)
   *  contents -  array of id attribute from DIV (tab content)
   *  urls - array of url for AJAX request
   *  unfoldStyles - array of object which contain styles for active tab state
   *  images - array which point at tab label is image or text type
   *  labelsContentIds - array which contains id attribute all images for labels (active and no active state) and also id text container for labels
   */
  initialize: function(id, labels, contents, urls, unfoldStyles, images, labelsContentIds, options) {
    this.id = id;
    this.tabsEl = $(id);
    this.activeTabIndex = -1;

    // id DIV (content tab) which is initially active after loading page
    this.activeTabId = '';

    // id TD element (container of content tab)
    this.contentId = '';

    //if style height in tabs element is set to 100% and position is absolute fullScreen will be set true by server, dafault is false
    this.fullScreen = false;

    this.labels = labels || [];
    this.urls = urls || [];
    this.unfoldStyles = unfoldStyles || [];
    this.labelsContentIds = labelsContentIds || [];
    this.images = images || [];
    this.contents = contents || [];

    Object.copyFields(this, options || {});

    this.contentEl = $(this.contentId);

    this.setup();

    // open tab
    this.select(this.activeTabId);
  },

  setup: function() {

    // array which contain information which tabs are already loaded by ajax or not
    this.loaded = [];

    this.urls.each(function(url) {
      // url is never null
      this.loaded.push(url === '');
    }.bind(this));

    this.activeTabIndex = this.getTabIndex(this.activeTabId);

    this.contentEl.style.padding = '0px';

  /* set height for content tab container (TD element) in px if user set 100% height for widget:tabs
  * This code below makes that widget is on full page (viewport)
  */
    var contentOffsetTop = 0;
    var viewportHeight = 0;
    var contentHeight = 0;

    if(this.fullScreen === true && Prototype.firefoxBrowser()) {
          contentOffsetTop = Position.cumulativeOffset(this.contentEl)[1];
          viewportHeight = self.innerHeight || (document.documentElement.clientHeight || document.body.clientHeight);
          contentHeight = viewportHeight - contentOffsetTop;
        }

    if(contentOffsetTop) {
      this.contentEl.style.height= contentHeight + 'px';
    }

    // bind handler to click event
    for(var i=0; i<this.labels.length; i++ ) {
      Widget.addElementObserver($(this.labels[i]), Widget.CLICK, this.showTab.bindAsEventListener(this));
    }

    // change visibility for each tab
    this.contents.each(function(id) {
      $(id).setStyle({'visibility':'visible', 'z-index': '0','position' :'static','display':'none'});
    });
  },

  // method invoked by click event
  showTab: function(evt) {
    var tabId = this.getTabId(Event.element(evt));
    this.select(tabId);
  },

  /**
  * get accociated Tab content DIV Id if event click fired on element
  */
  getTabId: function(element) {
    element = $(element);
    var tabId = null;
    do {
      if (element.id) {
        this.labels.each(function(id, index) {
          if(id == element.id) {
            tabId = this.contents[index];
          }
        }.bind(this));
      }
      element = element.parentNode;
    } while (tabId === null && element);
    return tabId;
  },

  /*
   * get index of tab id passed by argument
   */
  getTabIndex: function (tabId) {
    var myIndex = -1;
    this.contents.each(function(id, index) {
      if(id ==tabId ) {
        myIndex = index;
      }
    });
    return myIndex;
  },

  /**
  * Show tab
  * @param tabId - id agrument from tab content DIV
  * @volantis-api-include-in PublicAPI
  */
  select: function(tabId) {
    this.activeTabEl = $(tabId);
    var index = this.getTabIndex(tabId);

    //load content by Ajax if url is available and was not loaded before
    this.load(tabId);

    // return if selected tab is already active
    if(Element.getStyle(this.activeTabEl, 'display') != 'none') {
      return;
    }
    // hide all tabs before will be active new tab
    this.hideAll();

    // rewrite borders form DIV to parent TD element - (workaround for mobileOpera and Nokia OSS)
    // it might be for all browsers but for NetFront 3.4 on PC changing this borders are visible (with debbug options off) so it is do only for these browsers where it is necessary
    if(Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser() || Prototype.operaMobile()) {

      this.contentEl.style.borderBottomStyle = Element.getStyle(this.activeTabEl, 'border-bottom-style');
      this.contentEl.style.borderBottomWidth = Element.getStyle(this.activeTabEl, 'border-bottom-width');
      this.contentEl.style.borderBottomColor = Element.getStyle(this.activeTabEl, 'border-bottom-color');

      this.contentEl.style.borderLeftStyle = Element.getStyle(this.activeTabEl, 'border-left-style');
      this.contentEl.style.borderLeftWidth = Element.getStyle(this.activeTabEl, 'border-left-width');
      this.contentEl.style.borderLeftColor = Element.getStyle(this.activeTabEl, 'border-left-color');

      this.contentEl.style.borderRightStyle = Element.getStyle(this.activeTabEl, 'border-right-style');
      this.contentEl.style.borderRightWidth = Element.getStyle(this.activeTabEl, 'border-right-width');
      this.contentEl.style.borderRightColor = Element.getStyle(this.activeTabEl, 'border-right-color');

      this.activeTabEl.vfcReplaceStyle({'border-left-width': '0px', 'border-right-width': '0px', 'border-bottom-width': '0px'});
    }

    this.changeStripBottomBorder(index);

    // workaround for Opera PC, Konqueror
    this.activeTabEl.style.overflow = 'hidden';

    // activating tab
    // overflow:hidden is a workaround for Opera PC, Konqueror
    this.activeTabEl.setStyle({'display':'block','overflow':'hidden'});

    if(Prototype.nokiaOSSBrowser()){
        // for NokiaOSS only because there is kind of optimization
        // and view is not refreshed after position=static
        // so refresh is forced by adding and removing empty div.
        var div = document.createElement('div');
        var parent = this.activeTabEl.parentNode;
        parent.appendChild(div);
        parent.removeChild(parent.lastChild);
    }

    // check if label is image or text type
    if(this.images[index] === false) {
      // style for unfolded (active) state setting
      var stylableLabel = $(this.labels[index]);

      var borderColor = this.unfoldStyles[index]["background-color"] || Element.getStyle(stylableLabel, 'background-color');
      this.unfoldStyles[index]["border-bottom-color"] = borderColor;

      if(Prototype.msieBrowser() && borderColor == 'transparent') {
        this.unfoldStyles[index]["border-bottom-width"] = '0px';
      }

      // workaround for not inheritance color property from parent to anchor element in IE
      if(Prototype.msieBrowser()) {
        var stylableLabelAnchor = stylableLabel.firstChild.firstChild;
        stylableLabelAnchor = $(stylableLabelAnchor);
        stylableLabelAnchor.vfcReplaceStyle(this.unfoldStyles[index]);
      }
      stylableLabel.vfcReplaceStyle($(this.unfoldStyles[index]));
    } else {
      this.setActiveImage(index);
    }

    //remember actual active tab index
    this.activeTabIndex = index;
  },

  /*
   * Method set border-bottom in all tabStrip's elements
   */
  changeStripBottomBorder: function(index) {
    var labelSelected = $(this.labels[index]);

    // revert to orginal style border-bottom for this label
    labelSelected.vfcRevertStyle();

     if(Prototype.msieBrowser()) {
         var stylableLabelAnchor = labelSelected.firstChild.firstChild;
         stylableLabelAnchor = $(stylableLabelAnchor);
         stylableLabelAnchor.vfcRevertStyle();
     }

    var borderBottomColor = labelSelected.getStyle("border-bottom-color").parseColor();
    var borderBottomWidth = labelSelected.getStyle("border-bottom-width").parseColor();
    var borderBottomStyle = labelSelected.getStyle("border-bottom-style").parseColor();

    // for all td elements in TabStrip border-bottom is rewrite by border-bottom from selected label element
    // border-bottom style in label element (:mcs-label) is set by tab renderer from border-top style from widget:tab element, renderer remove also this border-top style from tab content element
    cells = this.tabsEl.firstChild.firstChild.getElementsByTagName("TD");
    for (var i = 0; i < cells.length; i++) {
      var cell = Element.extend(cells[i]);
      cell.vfcReplaceStyle({'border-bottom-color': borderBottomColor, 'border-bottom-width': borderBottomWidth, 'border-bottom-style': borderBottomStyle});
    }
  },

  /*
  * Set image for label in active state
  */
  setActiveImage: function(index) {
    var labelEl = $(this.labels[index]);
    $(this.labelsContentIds[index][0]).style.display = 'none';
    $(this.labelsContentIds[index][1]).style.display = 'inline';
  },

  /*
  * Set image for label in no active state
  * @param index - index of array which tab is active
  */
  unsetActiveImage: function(index) {
    $(this.labelsContentIds[index][0]).style.display = 'inline';
    $(this.labelsContentIds[index][1]).style.display = 'none';
  },

  /**
  * Hide all tabs
  */
  hideAll: function() {

    // revert border style for content DIV
    if(Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser() || Prototype.operaMobile()) {
      this.activeTabEl.vfcRevertStyle();
    }

    this.contents.each(function(id) { $(id).setStyle({'display': 'none'});});
    if(this.images[this.activeTabIndex] === false) {
      $(this.labels[this.activeTabIndex]).vfcRevertStyle();
      if(Prototype.msieBrowser()) {
          var stylableLabelAnchor = $(this.labels[this.activeTabIndex]).firstChild.firstChild;
          stylableLabelAnchor = $(stylableLabelAnchor);
          stylableLabelAnchor.vfcRevertStyle();
      }
    } else {
      this.unsetActiveImage(this.activeTabIndex);
    }
  },

  /**
  * Public API - load tab content
  * Content is loaded only once
  * @volantis-api-include-in PublicAPI
  */
  load: function(tabId) {
    var tabEl = $(tabId);
    var index = this.getTabIndex(tabId);

    // loading only if it was not loaded before
    if(this.loaded[index] === false) {
      new Widget.AjaxUpdater(tabEl, this.urls[index], {
        asynchronous:true,
        method: 'get'
      });
      // set that this tab was already loaded
      this.loaded[index] = true;
    }
  },

  /**
  * Public API - reload tab content
  * Reload content for tab which id is passed as argument
  * @volantis-api-include-in PublicAPI
  */
  reload: function(tabId) {
    var tabEl = $(tabId);
    var index = this.getTabIndex(tabId);
    if(this.urls[index] !== '') {
      new Widget.AjaxUpdater(tabEl, this.urls[index], {
        asynchronous:true,
        method: 'get'
      });
    }
  }
});
