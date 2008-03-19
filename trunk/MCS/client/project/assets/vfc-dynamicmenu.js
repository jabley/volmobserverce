/**
 * (c) Volantis Systems Ltd 2006. 
*/


Widget.DynamicMenu = Class.create();

Object.extend(Widget.DynamicMenu.prototype, Widget.Appearable);
Object.extend(Widget.DynamicMenu.prototype, Widget.Disappearable);

Object.extend(Widget.DynamicMenu.prototype, {
  // status
  FOLDED : 1,

  UNFOLDED : 2,


  initialize: function(id, options) {
    this.id = id;
    this.element = $(id);
    this.activeLevel = 1;
    this.activeBranch = this.id;
    this.focusInMenu = true;
    this.dir ='ltr';

    // lock/unlock effect transition
    this._semaphore = 0;

    // table with active submenus (unfolded)
    this.aActiveMenus = new Array();

    Object.copyFields(this, options || {});

    this.prepareMenu(this.element);
    this.clearSpareDivs(this.element);
    this.addLevels(this.element);
    this.setMainOrientation();
    this.addObservers(this.element);
  },


  /**
  *  bind function to event for menu items
  * @param element top level DIV node of submenu/menu
  */
  addObservers: function(element) {
    var node, outerDiv, innerDiv;
    var typeEvent = 'focus';
    var targetOfEvent;

    if(element.getAttribute('vfc-openEvent') != undefined) {
      typeEvent = element.getAttribute('vfc-openEvent');
    }
    innerDiv = element.lastChild.firstChild;
    for(var k=0; k < innerDiv.childNodes.length; k++) {
      node =  innerDiv.childNodes[k];
      if(node.firstChild.nodeName == 'DIV' && node.getAttribute('vfc-orientation') != undefined) { // node is submenu

        targetOfEvent = node.firstChild.firstChild;
        if(typeEvent == 'click') {
            Widget.addElementObserver(targetOfEvent,Widget.CLICK,
              this.showSubMenu.bindAsEventListener(this));
            Widget.addElementObserver(targetOfEvent,Widget.FOCUS,
              this.setFocusInMenu.bindAsEventListener(this));
        } else {
            Widget.addElementObserver(targetOfEvent,Widget.FOCUS,  this.showSubMenu.bindAsEventListener(this));
        }
         Widget.addElementObserver(targetOfEvent, Widget.BLUR, this.blurHandler.bindAsEventListener(this));
        this.addObservers(node);

      } else { //divs with A href - leaf of tree
        if(typeEvent == 'focus') {
            Widget.addObserversToFocusableElements(node,Widget.FOCUS,
              this.focusLinkHandler.bindAsEventListener(this));
            Widget.addObserversToFocusableElements(node,Widget.BLUR,
              this.blurHandler.bindAsEventListener(this));
        }
        //click on leaf node
        Widget.addObserversToFocusableElements(node,Widget.CLICK,
          this.foldAllDynamicMenu.bindAsEventListener(this));
      }
    }
    return;
  },

  /**
  *  set orientation for top level menu
  */
  setMainOrientation : function() {
    var menuDiv = $(this.id); //higher div in main menu
    var menuWidth, dim, itemLeftPoz = 0, maxHeight = 0, itemTopPoz = 0, maxWidth = 0; maxTextWidth = 0;
    var maxItemMargin = 0;

    var orientation = menuDiv.getAttribute('vfc-orientation');
    var innerDiv = menuDiv.lastChild.firstChild; // inner items DIV
    var itemDiv = null;
    var maxPaddingBorder = 0;

    for(var i=0; i < innerDiv.childNodes.length; i++) { //for each items main menu
      itemDiv = innerDiv.childNodes[i];

      var submenuLabelDiv = null;

      // if item is submenu
      if(itemDiv.getAttribute('vfc-orientation')) {
        submenuLabelDiv = itemDiv.firstChild;

        // save bottomPaddingBorderWidth
        // the way it that innerDIV will be set 0px for paddings and borders - measure Height and revert oryginal style for border and padding
        if(submenuLabelDiv.getStyle('border-bottom-width')!='0px' || submenuLabelDiv.getStyle('padding-bottom')!='0px') {
          submenuLabelDiv = Element.extend(submenuLabelDiv);
          var menuLineHeight = submenuLabelDiv.offsetHeight;
          submenuLabelDiv.vfcReplaceStyle({'border-bottom-width': '0px', 'padding-bottom':'0px'});
          var maxItemPaddingBorder = menuLineHeight - submenuLabelDiv.offsetHeight;
          if(maxItemPaddingBorder > maxPaddingBorder) {
            maxPaddingBorder = maxItemPaddingBorder;
          }
          submenuLabelDiv.vfcRevertStyle();
        }
      }

      // horizontal menu
      if(orientation == 'horizontal') {
        itemDiv.style.display = 'inline';
        if(submenuLabelDiv != null) {
          submenuLabelDiv.style.display = 'inline';
        }
      } else {
        itemDiv.style.width = 'auto';
      }
    }

    innerDiv.style.position = 'absolute';

    if(orientation == 'horizontal') {
        menuDiv.style.height = innerDiv.offsetHeight + (maxPaddingBorder * 2) + 'px';
    } else {
        menuDiv.style.height = innerDiv.offsetHeight + 'px';
    }
    menuDiv.bottomPaddingBorderWidth = maxPaddingBorder;
  },

  /**
  *  set orientation for submenu, calculate width, height, left and top for absolute position
  * @param element top level DIV node of submenu
  */
  setSubOrientation : function(menuDiv) { //element - node div of submenu
    var menuWidth, dim, itemLeftPoz = 0, itemDiv, maxHeight = 0, itemTopPoz = 0, maxWidth = 0, maxTextWidth = 0;
    var parentMenuDiv = menuDiv.parentNode.parentNode.parentNode;
    var parentOrientation = parentMenuDiv.getAttribute('vfc-orientation');
    var parentLevel = parentMenuDiv.getAttribute('level');

    var orientation = menuDiv.getAttribute('vfc-orientation');
    var innerDiv = menuDiv.lastChild.firstChild;
    var outerDiv = menuDiv.lastChild;
    var labelDiv = menuDiv.firstChild;

    outerDiv.style.visibility = 'hidden';
    outerDiv.style.display = 'block';

    outerDiv.style.position = 'absolute';

    var maxPaddingBorder = 0;
    for(var i=0; i < innerDiv.childNodes.length; i++) {
      itemDiv = innerDiv.childNodes[i];
      var submenuLabelDiv = Element.extend(itemDiv.firstChild);
      if(orientation == 'horizontal') {
        itemDiv.style.display = 'inline';
      }

      var submenuLabelDiv = null;
      if(itemDiv.getAttribute('vfc-orientation')) {
        submenuLabelDiv = Element.extend(itemDiv.firstChild);
        submenuLabelDiv.style.display = 'inline';
        // save bottomPaddingBorderWidth
        // the way it that innerDIV will be set 0px for paddings and borders - measure Height and revert oryginal style for border and padding
        if(submenuLabelDiv.getStyle('border-bottom-width')!='0px' || submenuLabelDiv.getStyle('padding-bottom')!='0px') {
          var menuLineHeight = submenuLabelDiv.offsetHeight;
          submenuLabelDiv.vfcReplaceStyle({'border-bottom-width': '0px', 'padding-bottom':'0px'});
          var maxItemPaddingBorder = menuLineHeight - submenuLabelDiv.offsetHeight;
          if(maxItemPaddingBorder > maxPaddingBorder) {
            maxPaddingBorder = maxItemPaddingBorder;
          }
          submenuLabelDiv.vfcRevertStyle();
        }
      }
    }
    menuDiv.bottomPaddingBorderWidth = maxPaddingBorder;

    if(orientation == 'horizontal') {
      //horizontal in vertical
      if(parentOrientation == 'vertical' ) {
        outerDiv.style.top = menuDiv.offsetTop + parentMenuDiv.bottomPaddingBorderWidth + 'px';
        outerDiv.style.left = labelDiv.offsetWidth + 'px';
      } else {
      // horizontal in horizontal
        outerDiv.style.top = labelDiv.offsetHeight - parentMenuDiv.bottomPaddingBorderWidth + 'px';
        outerDiv.style.left = labelDiv.offsetLeft + 'px';
      }
      //workaround for FF and effect with Scale. Width property has to be set to absolute value (px) if absolute div has inline elements in its contents
      if(Prototype.firefoxBrowser()) {
        outerDiv.style.width = outerDiv.offsetWidth + 'px';
      }
    }

    if(orientation == 'vertical') {

      if(parentOrientation == 'horizontal' ) {
        //get position of parent and height of parent and set top
        outerDiv.style.top = menuDiv.offsetHeight + parentMenuDiv.bottomPaddingBorderWidth + 'px';
        outerDiv.style.left = menuDiv.offsetLeft + 'px';
      } else {
        outerDiv.style.top = menuDiv.offsetTop + parentMenuDiv.bottomPaddingBorderWidth + 'px';
        outerDiv.style.left = menuDiv.offsetLeft + labelDiv.offsetWidth + 'px';
      }

      if(parentOrientation == 'vertical' ) { //vertical in vertical - could be overlay
        //checking if overlap is needed
        var dim = Position.cumulativeOffset(outerDiv);
        var screenWidth = document.body.clientWidth;
        var lackRightScreen = dim[0] + maxWidth - screenWidth;
        var overlayRight = (lackRightScreen / outerDiv.parentNode.offsetWidth) * 100;

        if(lackRightScreen > 0) { //lackRight
          if(overlayRight < 25) {
            outerDiv.style.left = (outerDiv.parentNode.offsetWidth - lackRightScreen) + 'px';
          } else {
            dim = Position.cumulativeOffset(outerDiv.parentNode);
            var lackLeftScreen = dim[0] - maxWidth;
            var overlayLeft = (lackLeftScreen / outerDiv.parentNode.offsetWidth) * 100;

            if(lackLeftScreen > 0) { //there is free space on the left
              outerDiv.style.left = (- maxWidth) + 'px';
            } else {
              if((overlayRight - overlayLeft) < 25) {
                //right side - no changes - do nothing
              } else {
                outerDiv.style.left = (- (maxWidth + lackLeftScreen)) + 'px';
              }
            }
          }
        }
      }
    }

    outerDiv.style.visibility = 'visible';
    outerDiv.style.display = 'none';
  },

  /**
  *  show submenu on menu event
  * @param evt event from menu
  */
  showSubMenu: function(evt) {
    // @todo - this is temporary solution
    if(this._semaphore == 1) {
      return;
    }

    var labelDiv, outerDiv;
    var sourceOfEvent;

    if(Prototype.nokiaOSSBrowser()) {
      sourceOfEvent = Event.element(evt).parentNode;
      if(sourceOfEvent.nodeName != 'A') {
      //focus bring from text node in span - marker
        sourceOfEvent = sourceOfEvent.parentNode;
      }
    } else { //focusable tag i.e. a */
      sourceOfEvent = Event.element(evt);
    }

    labelDiv = sourceOfEvent.parentNode;
    outerDiv = labelDiv.nextSibling;
    menuDiv = sourceOfEvent.parentNode.parentNode;

    // only for submenu with onfocus event
    if(this.triggeredByFocus(evt.type)) {
      this.focusInMenu = true;
      var aActiveMenusOld = new Array();
      this.aActiveMenus.each(function(menu){
        aActiveMenusOld.push(menu);
      });
      this.aActiveMenus.clear();
      this.setActiveBranch(menuDiv);
      this.foldOldBranch(aActiveMenusOld);
    }

    this.activeLevel = menuDiv.getAttribute('level');
    // hide submenu and his submenus if clicked on unfolded menu (second click)
    if(Element.getStyle(outerDiv, 'display') != 'none' && evt.type=='click') {
      //hide all children unfolded submenus
      this.hideChildSubmenus(menuDiv);
      return;
    }

    // common operation for while submenu is opening
    if(outerDiv.style.display == 'none') {
      var unfoldedStyle = labelDiv.getAttribute('vfc-unfoldedstyle');
      if (unfoldedStyle) {
        labelDiv = Element.extend(labelDiv);
        labelDiv.vfcReplaceStyle(this.string2Object(unfoldedStyle));
      }

      this.setUnfoldedMarker(labelDiv);
      this.setSubOrientation(labelDiv.parentNode);

      Element.setStyle(menuDiv, {zIndex: menuDiv.getAttribute("level") + 1});
      var id = this.id;
      var it = this;
      Widget.TransitionFactory.createAppearEffect(outerDiv,this, {
          afterFinish : function(effect) {
            it._semaphore = 0;
          }
      }
      );
      this._semaphore = 1;
    }
  },


  /**
  * Fold all submenus wich were unfolded before next focus event and not match to new active branch.
  * branch - it is ordered table with id div menus actually unfolded
  * @param aActiveMenusOld - old list (div's id) opened submenus
  */
  foldOldBranch: function(aActiveMenusOld) {
    // @todo - this is temporary solution
    if(this._semaphore == 1) {
      return;
    }

    var parentMenuDiv;
    for(var i=0; i < aActiveMenusOld.length; i++) {
      // workaround for opera mobile, sometimes opera mobile contains empty entry in table with last active submenus
      if(aActiveMenusOld[i] == '') {
        continue;
      }
      var toFolding = true;
      for(var j=0; j < this.aActiveMenus.length; j++) {
        if(aActiveMenusOld[i] == this.aActiveMenus[j]) {
          toFolding = false;
        }
      }

      if(toFolding) {
        var outerDiv = $(aActiveMenusOld[i]).lastChild;
        var labelDiv = $(aActiveMenusOld[i]).firstChild;
        parentMenuDiv = $(aActiveMenusOld[i]).parentNode.parentNode.parentNode;

        if(parentMenuDiv.getAttribute('vfc-openEvent')=='click') {
          continue;
        }

        Element.setStyle($(aActiveMenusOld[i]), {zIndex: $(aActiveMenusOld[i]).getAttribute("level")});
        var id = this.id;
        var it = this;
        Widget.TransitionFactory.createDisappearEffect(outerDiv, this, {
          afterFinish : function(effect) {
            Widget.getInstance(id).setFoldedMarker(labelDiv);
            labelDiv.vfcRevertStyle();
            it._semaphore = 0;
          }
        }
      );
      this._semaphore = 1;

      }
    }
    return;
  },

  /**
  * Set active branch for opened submenu.
  * Store id arguments for submenus which are opened.
  * There are stroed in aActiveMenus in order from most deep to top level menu's id argument
  * @param menuDiv - submenu div ID
  */
  setActiveBranch: function(menuDiv) {
    var parentMenuDiv;
    var divNode;
    parentMenuDiv = menuDiv.parentNode.parentNode.parentNode;
    if(parentMenuDiv.getAttribute('id') == this.id) {
      this.aActiveMenus.push(menuDiv.getAttribute('id'));
      return;
    } else {
      this.aActiveMenus.push(menuDiv.getAttribute('id'));
      this.setActiveBranch(parentMenuDiv);
      return;
    }
  },

  /**
   * init markers - set status to folded or unfolded for further use
   */
  _initMarker : function(markerElement){
    var display = markerElement.getStyle('display');
    if(Prototype.nokiaOSSBrowser() && display === null){
      // there is known issue for nokia OSS when element display = none
      // then getStyle returns null
      markerElement._status = this.UNFOLDED;
    } else if(display == 'none'){
      // when explicitly set to none - unfolded
      markerElement._status = this.UNFOLDED;
    } else if(display == 'inline'){
      // else folded
      markerElement._status = this.FOLDED;
    }
  },

  /**
  *  change marker according to fold state label's submenu (submenu is closing)
  * @param element label DIV node of submenu
  */
  setFoldedMarker: function(element) {
    markers = element.getElementsByTagName("SPAN");
    for (var i = 0; i < markers.length; i++) {
      var markerEl = $(markers[i]);
      if(!markerEl._status){
        this._initMarker(markerEl);
      }
      if (markerEl._status == this.FOLDED) {
        markerEl.style.display = 'inline';
      }
      if (markerEl._status == this.UNFOLDED) {
        markerEl.style.display = 'none';
      }
    }
  },

  /**
  *  change marker according to unfold state label's submenu (submenu is opening)
  * @param element label DIV node of submenu
  */
  setUnfoldedMarker: function(element) {
    markers = element.getElementsByTagName("SPAN");
    for (var i = 0; i < markers.length; i++) {
      var markerEl = $(markers[i]);
      if(!markerEl._status){
        this._initMarker(markerEl);
      }
      if (markerEl._status == this.FOLDED) {
        markerEl.style.display = 'none';
      }
      if (markerEl._status == this.UNFOLDED) {
       markerEl.style.display = 'inline';
      }
    }
  },


  /**
  *  handler of focus event on link items (leaf items)
  * @param evt event form element where focus invoke
  */
  focusLinkHandler: function(evt) {
    var sourceOfEvent, eventLevel, eventBranch;
    this.focusInMenu = true;
    if(Prototype.nokiaOSSBrowser()){
      sourceOfEvent = Event.element(evt).parentNode;
      if(sourceOfEvent.nodeName != 'A') {
      //focus bring from span - marker text node
        sourceOfEvent = sourceOfEvent.parentNode;
      }
    } else {
      sourceOfEvent = Event.element(evt);
    }

    // search for submenu element for which focused link belongs
    var menuDiv = this.findMenuDiv(sourceOfEvent);

    var aActiveMenusOld = new Array();
    this.aActiveMenus.each(function(menu){
      aActiveMenusOld.push(menu);
    });

    this.aActiveMenus.clear();

    // if it is only common link in top level menu (not label in submenu)
    // then only fold active branch (previous opened submenus) and clear array stored that branch
    // so only for focusing link placed deeply inside menu (in submenus) new active branch will be setting again
    if(menuDiv != this.element) {
      this.setActiveBranch(menuDiv);
    }

    this.foldOldBranch(aActiveMenusOld);
  },

  /**
  * find and return the nearest menu DIV element - search in ancestors
  * @param element on which event was invoked
  */
    findMenuDiv: function(element) {
    element = $(element);
    while (element = element.parentNode) {
      if (element.getAttribute('vfc-orientation') != undefined) {
        return element;
      }
    }
  },

  /**
  *  handler of blur event
  */
  blurHandler: function() {
    this.focusInMenu = false;
    window.setTimeout("Widget.getInstance('"+this.id+"').checkFocusInMenu()",200); //200 - 0.2 sek
    //set timer and after for example 1 sek check focusInMenu. If it is true - do nothing, if false - fold all menus
  },

  /**
  *  invoked by timer in order to check if new focus after blur is in menu widget
  */
  checkFocusInMenu: function() {
    if(this.focusInMenu == false) {
      // only focusabled submenus will be folded, so second parameter is false
      this.foldMenu(this.element, false);
    }
  },

  /**
  *  set focusInMenu on true if focus is inside menu
  */
  setFocusInMenu: function() {
    this.focusInMenu = true;
  },

  /**
  * fold all unfolded submenus from dynamic menu widget
  * Function bind with event
  */
  foldAllDynamicMenu: function() {
    this.foldMenu(this.element, true);
  },

  /**
  * fold all unfolded submenus (only unfolded by focus event) from dynamic menu widget
  * @param menuDiv - div element of submenu (menu)
  * @param allSubmenu - true if all submenu will be folded except level 1 (used if focus or click fired on leaf item of menu),
          false - only submenu unfolded on focus event should be folded
  */
  foldMenu: function(menuDiv, allSubmenu) {
    // @todo - this is temporary solution
    if(this._semaphore == 1) {
      return;
    }

    var node, parentMenuDiv;
    var innerDiv = menuDiv.lastChild.firstChild;
    var outerDiv = menuDiv.lastChild;
    var labelDiv = menuDiv.firstChild;
    if(menuDiv != this.element) {
      parentMenuDiv = menuDiv.parentNode.parentNode.parentNode;
    }

    //for all children
    for(var i = 0; i < innerDiv.childNodes.length; i++) {
      node = innerDiv.childNodes[i];
      // it has submenu, check deeper
      if(node.getAttribute('vfc-orientation')) {
        this.foldMenu(node, allSubmenu);
      }
    }

    if(menuDiv.getAttribute('level') > 1) {
      if(!allSubmenu && parentMenuDiv.getAttribute('vfc-openEvent')=='click') {
        return;
      }
      Element.setStyle(menuDiv, {zIndex: menuDiv.getAttribute("level")});
      if(outerDiv.style.display != 'none') {
        var id = this.id;
        var it = this;
        Widget.TransitionFactory.createDisappearEffect(outerDiv, this, {
            afterFinish : function(effect) {
              Widget.getInstance(id).setFoldedMarker(labelDiv);
              labelDiv.vfcRevertStyle();
              it._semaphore = 0;
            }
          }
        );
      this._semaphore = 1;

      }
    }
    return;
  },

  /**
  * Hide all submenus in branch and level more than level in parameters
  * @param menuDiv - id top level DIV from submenu
  * @param level - start level to closing submenu
  */
  hideLevelSubmenu: function(menuDiv, level) {
    var node;
    var innerDiv = menuDiv.lastChild.firstChild;
    var labelDiv = menuDiv.firstChild;
    for(var i = 0; i < innerDiv.childNodes.length; i++) {
      node = innerDiv.childNodes[i];
      if(node.firstChild.nodeName == 'DIV' && node.getAttribute('vfc-orientation') != undefined) { //submenu
        if(Element.getStyle(node.lastChild, 'display') != 'none' && node.getAttribute('level') > level) { //outer DIV
          this.hideChildSubmenus(node);
        }
      }
    }
    return;
  },

  /**
  * Hide all submenus of element
  * @param element - top level DIV node from submenu
  */
  hideChildSubmenus: function(element) {
    if(this._semaphore == 1) {
      return;
    }

    var node, labelDiv, outerDiv;
    var innerDiv = element.lastChild.firstChild;
    outerDiv = element.lastChild;
    labelDiv = Element.extend(element.firstChild);

    for(var i = 0; i < innerDiv.childNodes.length; i++) {
      node = innerDiv.childNodes[i];
      if(node.firstChild.nodeName == 'DIV' && node.getAttribute('vfc-orientation') != undefined) { //submenu
        this.hideChildSubmenus(node);
      }
    }

    Element.setStyle(element, {zIndex: element.getAttribute("level")});

    if(Element.getStyle(outerDiv, 'display') != 'none') {
      var id = this.id;
      var it = this;
      Widget.TransitionFactory.createDisappearEffect(outerDiv, this, {
        afterFinish : function(effect) {
          Widget.getInstance(id).setFoldedMarker(labelDiv);
          labelDiv.vfcRevertStyle();
          it._semaphore = 0;
        }
      });
      this._semaphore = 1;

    }
    return;
  },

  /**
  * Hide all submenus in one branch
  * @param element - top level DIV node from submenu
  */
  hideBranchSubmenus: function(element) {
    if(this._semaphore == 1) {
      return;
    }

    var node, labelDiv;
    var innerDiv = element.lastChild.firstChild;
    labelDiv = element.firstChild;
    for(var i = 0; i < innerDiv.childNodes.length; i++) {
      node = innerDiv.childNodes[i];
      if(node.firstChild.nodeName == 'DIV' && node.getAttribute('vfc-orientation') != undefined) { //submenu
        this.hideBranchSubmenus(node);
      }
    }
    var outerDiv = element.lastChild;

    Element.setStyle(element, {zIndex: element.getAttribute("level")});
    if(Element.getStyle(outerDiv, 'display') != 'none') {
      var id = this.id;
      var it = this;
      Widget.TransitionFactory.createDisappearEffect(outerDiv, this, {
        afterFinish : function(effect) {
          Widget.getInstance(id).setFoldedMarker(labelDiv);
          labelDiv.vfcRevertStyle();
          it._semaphore = 0;
        }
      });
      this._semaphore = 1;
    }

    return;
  },


  /**
  * Clean whitespaces from hierarchical structure of widget
  * @param element - top level DIV node from submenu
  */
  prepareMenu: function(element) {  //inner items div
    var node;
    Element.cleanWhitespace(element);
    for(var k=0; k < element.childNodes.length; k++) {
      node =  element.childNodes[k];
      this.prepareMenu(node);
    }
    return;
  },

  /**
  * Clean spare Divs from hierarchical structure of widget
  * @param element - top level DIV node from submenu
  */
  clearSpareDivs: function(element) {
    var node;
    for(var k=0; k < element.childNodes.length; k++) {
      node =  element.childNodes[k];
      if(node.firstChild)
      {
        if(element.nodeName == 'DIV' && node.firstChild.nodeName == 'DIV') {
          if(node.firstChild.getAttribute("vfc-orientation")) {
            var tmp = node.firstChild;
            node.parentNode.replaceChild(tmp, node);
            node=tmp;
          }
          this.clearSpareDivs(node);
        }
      }
    }
    return;
  },

  /**
  * Add level atribute to each DIV element
  * @param id - id of top level DIV from submenu
  */
  addLevels: function(id) {
    var node, node2;
    $(id).setAttribute('level', this.activeLevel);
     $(id).firstChild.setAttribute('level', this.activeLevel);
     $(id).lastChild.setAttribute('level', this.activeLevel);

    Element.setStyle($(id), {zIndex: this.activeLevel});
    Element.setStyle($(id).firstChild, {zIndex: this.activeLevel});
    Element.setStyle($(id).lastChild, {zIndex: this.activeLevel});

    node = $(id).lastChild.firstChild; // inner DIV  items
    node.setAttribute('level', this.activeLevel);
    if(this.activeLevel > 1) {
      Element.setStyle(node, {zIndex: this.activeLevel});
    }

    for(var i=0; i < node.childNodes.length; i++) {
      node2 = node.childNodes[i];

      node2.setAttribute('level', this.activeLevel);
      Element.setStyle(node2, {zIndex: this.activeLevel});

      if(node2.firstChild.nodeName == 'DIV' && node2.getAttribute('vfc-orientation') != undefined) { //submenu
        this.activeLevel++;
        this.addLevels(node2.getAttribute('id'));
      }
    }
    this.activeLevel--;
    return ;
  },

  string2Object: function(string) {
       eval("var result = " + string);
       return result;
  },

  triggeredByFocus: function(eventType){
    if(eventType == Widget.FOCUS) {
      return true;
    }
    if(Prototype.nokiaOSSBrowser() && (eventType == Widget.MOUSEOVER)) {
      return true;
    }
    return false;
  }
});

