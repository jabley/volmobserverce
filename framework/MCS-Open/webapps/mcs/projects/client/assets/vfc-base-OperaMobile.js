/**
 * (c) Volantis Systems Ltd 2008. 
 */

var Prototype = {
  msieBrowser: function() {
    return false;
  },

  netFrontMobile: function() {
    return false;
  },

  operaMobile: function() {
    return true;
  },

  firefoxBrowser: function() {
    return false;
  },

  nokiaOSSBrowser: function() {
    return false;
  },

  konquerorBrowser: function() {
    return false;
  },

  operaPC: function() {
    return false;
  },

  netFront: function() {
    return false;
  },

  // needed as it appears in many places in vfc-... files
  hiddenAttr: function(attr) {
    return false;
  },

  useMouseAsSelect: function() {
    return false;
  },

  useEmulatedOpacity: function() {
/*    var element = document.documentElement;
    if(Element.getStyle(element,'opacity') != 1){
      return true;
    } else {
      return false;
    }*/
    return true;
  },

  disableCache: function() {
    return true;
  },

  /**
  * Change input type. 
  * element - a reference to the input element
  * elType - value of the type property: 'text', 'password', 'button', etc.
  */
  changeInputType: function(element, elType) {
    element.type=elType;
  }

};

Object.extend = function(destination, source) {
  for (var property in source) {
    destination[property] = source[property];
  }
  return destination;
}
