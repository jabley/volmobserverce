/**
 * (c) Volantis Systems Ltd 2006. 
 */

Widget.Autocompleter = Class.create();

Object.extend(Widget.Autocompleter.prototype, Widget.Appearable);
Object.extend(Widget.Autocompleter.prototype, Widget.Disappearable);

Object.extend(Object.extend(Widget.Autocompleter.prototype, Autocompleter.Base.prototype), {
  initialize: function(element, update, options) {
    this.id = update;
    this.baseInitialize(element, update, options);
    this.options.asynchronous  = true;
    this.options.onComplete    = this.onComplete.bind(this);
    this.options.defaultParams = this.options.parameters || null;
    this.url = '';
    // lock for disappear effects use
    this.isHideProgress = false;

    // default parameters
    this.lagTime = 1;
    this.itemLimit = 5;

    // default styles for items in active state
    this.defaultActiveStyle = {"background-color" : "yellow"};

    // fixed parameter name passing entered by user value to AJAX request
    this.options.paramName = 'mcs-value';

    Object.copyFields(this, options || {});

    // delay of showing items list
    this.options.frequency = this.lagTime;

    this.options.defaultParams = 'mcs-item-limit=' + this.itemLimit;

    // styles for active state
    this.activeStyles = [];

    // remember CSS property width for list items container setted by user
    this.setup();
  },

  // this method remember width and height of list items container before inserting content from response.
  setup: function () {
    this.update.style.position = 'absolute';
    //width is direct set by user so set it inline in order to remembering this value for later usage
    if(! Prototype.operaPC()) {
      if(this.update.getStyle('width') != '0px') {
        this.update.style.width = this.update.getStyle('width');
      }
    } else {
      // only for opera PC browser getStyle('width') calculate width by adding border width to it, so we need substract width of left and right border and then check if value is more than 0,  browser by getStyle('width') always return value width in px unit even though width is set in another unit by user
      var originalWidth = parseInt(this.update.getStyle('width')) - parseInt(this.update.getStyle('border-left-width')) - parseInt(this.update.getStyle('border-right-width'));
      if(originalWidth != 0) {
        this.update.style.width = originalWidth + 'px';
      }
    }
  },

  getUpdatedChoices: function() {
    entry = encodeURIComponent(this.options.paramName) + '=' +
      encodeURIComponent(this.getToken());

    this.options.parameters = this.options.callback ?
      this.options.callback(this.element, entry) : entry;

    if (this.options.defaultParams) {
      this.options.parameters += '&' + this.options.defaultParams;
    }

    this.options.onSuccess = this.onAJAXRequestSuccess.bind(this);
    this.options.method = "get";

    new Ajax.Request(this.url, this.options);
  },

  onComplete: function(request) {
    this.updateChoices(this.response.stripScripts());
  },

 // move from Base.Autocomplete
  updateChoices: function(choices) {
    if(!this.changed && this.hasFocus) {
      // update autocomplete container if AJAX response content has at least one LI element
      if(choices.indexOf("li") != -1) {

        // remove items if there are more than limit
        this.limitExecutor(choices);

        this.update.innerHTML = this.responseHtml;
        Element.cleanWhitespace(this.update);
        Element.cleanWhitespace(this.update.firstChild);

        if(this.update.firstChild && this.update.firstChild.childNodes) {
          this.entryCount =
            this.update.firstChild.childNodes.length;
          for (var i = 0; i < this.entryCount; i++) {
            var entry = this.getEntry(i);
            entry.autocompleteIndex = i;
            this.addObservers(entry);
          }
        } else {
          this.entryCount = 0;
        }
      } else {
          this.entryCount = 0;
      }

      // workaround for NokiaOSS - add anchors because sometimes it is
      // impossible focus item by OSS browser mouse without link inside
      if (Prototype.nokiaOSSBrowser()) {
        this.prepereItems();
      }
      this.stopIndicator();

      this.index = 0;
      this.render();
    }
  },

  /*
   * This method remove items from response if it is not limited by service
   * and there are more items than parameter itemLimit.
   * It remove items from string response.
   */
  limitExecutor: function(items) {
    this.entryCount = 0;
    this.responseHtml = items;
    var lastIndex = this.responseHtml.lastIndexOf('</ul>');
    var firstIndex = this._getPosition(0);
    // if nothing found we do not use truncate because all content fit
    if(firstIndex != -1) {
      this.responseHtml = this.responseHtml.substring(0, firstIndex) + this.responseHtml.substring(lastIndex, this.responseHtml.length);
    }
  },

  /*
   * Method get first position of element which should be removed because it is beyond limit parameter
   */
  _getPosition: function(startIndex){
    var poz = this.responseHtml.indexOf("<li", startIndex);
    if(poz != -1) {
      this.entryCount++;
      if(this.entryCount > this.itemLimit) {
        return poz;
      } else {
        return this._getPosition(poz + 1);
      }
    } else {
      return -1;
    }
  },

  /*
   * Method add anchors in LI elements in order to mouse on Nokia OSS browser can select each element
   * If there is no link in LI element sometimes mouse skip some items and it is hard to select those items
   */
   prepereItems: function() {
    var textNode, textValue;
    var i = 0;
    while(i < this.entryCount) {
      var itemNode = this.getEntry(i);
      // if it is only one text node
      var a = document.createElement("a");
      a.href="javascript:void(null)";
      a.style.textDecoration = 'none';
      a.style.color = 'black';
      if(itemNode.childNodes.length == 1 && itemNode.firstChild.nodeType == 3) {
        textNode = itemNode.firstChild;
        textValue = itemNode.innerHTML;
        a.appendChild(document.createTextNode(textValue));
        itemNode.replaceChild(a, textNode);
      } else {
        // add A with href to text node nested in entry item
        textNode = this.findTextNode(itemNode);
        textValue = textNode.nodeValue;

        a.appendChild(document.createTextNode(textValue));
        textNode.parentNode.replaceChild(a, textNode);
      }
      i++;
    }
  },

  /* Method used in prepareItems - it finds text nodes in autocomplete item's markup */
  findTextNode: function(item) {
    var textNode;
    for(var i = 0; i < item.childNodes.length; i++) {
      var child = item.childNodes[i];
      if(child.nodeType == 3) {
        return child;
      } else {
        textNode = this.findTextNode(child);
        if(textNode) {
          return textNode;
        }
      }
    }
  },

  addObservers: function(element) {
    Event.observe(element, "mouseover", this.onHover.bindAsEventListener(this));
    Event.observe(element, "click", this.onClick.bindAsEventListener(this));
  },

  /*
   * This method invoke all scripts included in AJAX response
   * Scripts are only for those items which have defined style for active state
   */
  evalResponseScripts: function() {
    var scripts = this.response.extractScriptsWithSubstring();
    scripts.each(function(script) {
      eval('this.' +script);
    }.bind(this));
  },

  /*
   * Register active styles for response items in array
   */
   registerActiveStyle: function(idLi, style) {
    this.activeStyles[idLi] = style;
   },

  // evaluate scripts for success response
  onAJAXRequestSuccess: function (httpRequest) {
    this.response = httpRequest.responseText;
    // Evaluates scripts from the response content.
    this.evalResponseScripts();
  }
});