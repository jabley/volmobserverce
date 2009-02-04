/**
 * (c) Volantis Systems Ltd 2008.
 */

var Prototype = {
  netFrontMobile: function() {
    return false;
  },

  operaMobile: function() {
    return false;
  },

  firefoxBrowser: function() {
    return false;
  },
  
  msieBrowser: function() {
    return false;
  },

  nokiaOSSBrowser: function() {
    return true;
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

  // This browser has mouse cursor and events like: 
  // onBlur should be changed to onMouseOut
  // onFocus should be changed to onMouseOver
  useMouseAsSelect: function() {
    return true;
  },

  useEmulatedOpacity: function() {
    return true;
  },

  disableCache: function() {
    return false;
  },

  /**
  * Change input type. 
  * element - a reference to the input element
  * elType - value of the type property: 'text', 'password', 'button', etc.
  */
  changeInputType: function(element, elType) {
    element.type=elType;
  }

}

//standard
Object.extend = function(destination, source) {
  for (var property in source) {
    destination[property] = source[property];
  }
  return destination;
}

