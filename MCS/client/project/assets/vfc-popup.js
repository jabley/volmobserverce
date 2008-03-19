/**
 * (c) Volantis Systems Ltd 2006. 
 */

Widget.Popup = Widget.define({

  initialize: function(id, blockId, options) {
    this.initializeWidget(id, options);

    this.id = id;
    this.element = $(blockId);
    this.element.style.zIndex = 3;
    this.block = $W(blockId);

    this.fullScreen = false;
    Object.copyFields(this, options || {});

    this._applyWorkarounds();

    this.observe(this.block, "isHiddenChanged", "_blockHiddenChanged");

    // add actions supported by this widget
    this.addAction('dismiss');
    this.addAction('show');

    // add events supported by this widget
    this.addEvent("dismissed");
  },

  show: function() {
    if (this.canShow()) {
      this.block.show();
    }
  },

  dismiss: function(dismissType) {
    if (this.canDismiss(dismissType)) {
      this.block.hide();
      this.notifyObservers("dismissed", dismissType);
    }
  },

  // Needed for backward compatibility
  hide: function() {
    this.dismiss();
  },

  canShow: function() {
    return this.block.isHidden();
  },

  canDismiss: function() {
    return (!this.block.isHidden());
  },

  isHidden: function() {
    return this.block.isHidden();
  },

  _blockHiddenChanged: function() {
    this.notifyObservers("canShowChanged");
    this.notifyObservers("canDismissChanged");
  },

  _applyWorkarounds: function() {

    // On Opera Mobile 8.6 it is impossible to scroll div with position set to fixed.
    // If div height and width is both 100% it means that it should be displayed on
    // full screen. Position can be then changed to absolute and scrolling can be enabled.
    // See vbm: 2006113008
    // BUG: checking for window.opera is NOT the right way to detect Opera Mobile 8.6
    if (this.fullScreen && window.opera) {
    // and if popup is bigger then screen, if it's small don't do enything.
      if (window.innerHeight < this.element.scrollHeight || window.innerWidth < this.element.scrollWidth ) {
        Element.setStyle(this.element, {
          'position': 'absolute',
          'height':  this.element.scrollHeight + 'px',
          'width' : this.element.scrollWidth + 'px',
          'top': '0px',
          'left': '0px',
          'z-index': '2',
          'visibility': 'visible',
          'display': 'none'});
        // there is a bug in Opera 8.65: when div has position:absolute and width/height 100%
        // scrolling stopped before reaching end of scrolled area
        // so p is added to popup content do let whole area be presended correctly
        var p = document.createElement("p");
        this.element.firstChild.appendChild(p);
      }
    }

    // We cannot set display: none on the server side, because in such case
    // MCS does not render the element at all. Therefore for initially
    // invisible popup we set visibility: hidden and then change it to
    // display: none on the client side
    if (Element.getStyle(this.element,'visibility') != 'visible') {
      Element.setStyle(this.element,{display: 'none'});
      Element.setStyle(this.element,{visibility: 'visible'});
    }

    // MSIE 6.0 does not support position:fixed?
    if (Prototype.msieBrowser() && Element.getStyle(this.element,'position') == 'fixed') {
      Element.setStyle(this.element, {'position': 'absolute'});
    }
  }
});
