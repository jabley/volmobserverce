/**
 * (c) Volantis Systems Ltd 2006. 
 */
var Gallery = {
  /**
   * Creates and returns new set of items.
   *
   * If this is first created set of items, it becomes
   * the default one. If there already is default set
   * of items, it'll be not default anymore.
   */
  createItems: function(options) {
    //Widget.log("Gallery", "Creating items")

    var items = new Gallery.Items(options)
    
    if (this.defaultItems === undefined) {
      this.defaultItems = items
      
    } else if (this.defaultItems != null) {
      this.defaultItems = null
    }
    
    return items
  },
    
  /**
   * Returns a set of items using given reference.
   *
   * If the reference is null, it returns default
   * set of items, if it exists.
   * If the reference is a string with widget ID,
   * it returns items registered under that ID.
   * In all other cases it assumes, that the reference
   * already is a set of items to return.
   */
  getItems: function(reference) {
    var items = null
    
    if (reference == null) {
      items = this.defaultItems
      
    } else if (typeof(reference) == 'string') {
      items = Widget.getInstance(reference)
      
    } else {
      items = reference
    }
    
    return items
  },

  /**
   * Creates and returns new Gallery widget.
   * If no set of items is specified, it uses
   * the default one.
   */
  createGallery: function(options) {
    //Widget.log("Gallery", "Creating Gallery widget")
    
    var displays = options.displays

	for (var index=0; index<displays.length; index++) {
	  displays[index] = Widget.getInstance(displays[index])
	}
    
    return new Gallery.Gallery(this.getItems(options.items), displays, options)
  },
  
  /**
   * Creates and returns new Slideshow widget.
   * If no set of items is specified, it uses
   * the default one.
   */
  createSlideshow: function(options) {
    //Widget.log("Gallery", "Creating Slideshow widget")

    var display = options.display
    
    if (display != null) {
	  display = Widget.getInstance(display)
	}
    
    return new Gallery.Slideshow(this.getItems(options.items), display, options)
  },
  
  /**
   * Returns the current request for items,
   * for the purpose of the JavaScript code
   * included within the response page.
   */
  getItemsRequest: function() {
    return this.itemsRequest
  }
}

/**
 * A slide contains content, appear/disappear effects and duration.
 */
Gallery.Slide = Class.define(Widget.OptionsContainer,
{
  duration: 5,
  
  initialize: function(content, options)
  {
    this.installOptions(options)
    
    this.content = content
  },
  
  getContent: function() {
    return this.content
  },
  
  getDuration: function() {
    return this.duration
  }
})

/**
 * An item consists of summary and detail slides.
 */
Gallery.Item = Class.define(Widget.OptionsContainer,
{
  /**
   * Initialization.
   */
  initialize: function(summarySlide, detailSlide, options)
  {
    this.installOptions(options)
    
    this.summarySlide = summarySlide
    this.detailSlide = detailSlide
  },

  /**
   * Returns the summary slide for this item.
   *
   * @returns The summary slide for this item.
   */
  getSummarySlide: function() {
    return this.summarySlide
  },

  /**
   * Returns the detail slide for this item.
   *
   * @returns The detail slide for this item.
   */
  getDetailSlide: function() {
    return this.detailSlide
  }
})

/**
 * An array of infinite capacity. 
 * Every index is valid (even negative).
 */
Gallery.InfiniteArray = Class.define(Widget.OptionsContainer,
{
  /**
   * Initialization
   */
  initialize: function(options) {
    this.installOptions(options)
    
    this.storage = {}
  },

  /**
   * Returns an element stored under specified index,
   * if anything is stored there.
   *
   * @param index An index of the element.
   * @returns an element under specified index.
   */
  get: function(index) {
    var element = this.storage[index.toString()]
    
    //Widget.log("Gallery.Storage", "Getting element: " + element + " at: " + index)
    
    return element
  },

  /**
   * Puts an element under given index, replacing
   * the existing one.
   *
   * @param index The index.
   * @param item The element.
   */
  put: function(index, element) {
    //Widget.log("Gallery.Storage", "Putting element: " + element + " at: " + index)
    
    this.storage[index.toString()] = element
  },

  /**
   * Removes element stored under index.
   *
   * @param index The index of the element to remove.
   */
  remove: function(index) {
    //Widget.log("Gallery.Storage", "Removing element: " + index)
    
    delete this.storage[index.toString()]
  }
})

/**
 * A set of items, which may be displayed in Gallery or Slideshow widgets.
 *
 * Items may be specified during initialization, or may be loaded
 * from external source specified by URL in two modes:
 *  - immediate 
 *  - on-demand
 *
 * In immediate mode all items are loaded on initialization,
 * while in on-demand mode, items are loaded when requested.
 *
 * To retrieve an item in mode-independent way, following actions
 * must be taken:
 *  1. Get item under specified index.
 *  2. If there's no item, add callback function which would be 
 *     invoked when the item arrives.
 *  3. Request the item and wait for it to arrive.
 *
 * The same may be achieved without callbacks, using active pending:
 *  1. Get item under specified index.
 *  2. While there's no item under specified index do the following:
 *     a) request the item
 *     b) wait for a while (ie 1 second)
 *     c) get the item again
 *
 * Items may be requested individually or in ranges, which may reduce
 * network traffic.
 */
Gallery.Items = Widget.define(
{
  // If the URL is specified, the items will be loaded
  // from external source. In that case, the loadOnDemand flag
  // controls if all items are loaded once, or should each item
  // be loaded on demand.
  loadURL: null,
  loadOnDemand: false,

  /**
   * Initialization
   */
  initialize: function(options) {
    this.initializeWidget(null, options)

    // Create an instance of Array, which would store the items.
    // Items in the storage are indexed from 1.
    // For given item index, an array would hold one of three possible values:
    //  1) null - item does not exist, and it hasn't been requested
    //  2) 'requested' - item does not exist, but it has been requested
    //  3) Gallery.Item - item exists
    this.storage = new Gallery.InfiniteArray()
    
    // If the URL is not specified, it means that the set of items is
    // static, and specified on initialization. 
    // In that case fill the storage with items.
    // Otherwise leave the storage empty (skipping the 'items' options),
    // and load items from the URL.
    if (this.loadURL == null) {
      // Initialize size to 0.
      // It'll be increased after every added item.
      this.size = 0
      
      // It items were specified, add them to the storage.
      // Remember that items in the storage are indexed from 1.
      var items = this.getOption('items')

      if (items != null) {
        for (var i = 0; i < items.length; i++) {
          this.storage.put(i+1, items[i])
          
          this.size++
        }
      }
    } else {
      // Initialize size with null, since it's not known yet.
      // It'll be initialized with correct value upon first request.
      this.size = null
    
      // If items are not to be loaded on demand,
      // load them all immediately.
      if (!this.loadOnDemand) {
        this.sendImmediateRequest()
      }
    }
  },

  /** 
   * Returns true, if items are ready
   */
  isReady: function() {
    return this.size != null
  },

  setSize: function(size) {
    this.size = size
      
    this.notifyObservers("sizeChanged")
  },

  /**
   * Puts item into the gallery.
   * Note: this method is not public.
   */
  putItem: function(index, item) {
    //Widget.log("Gallery", "Putting item at index: " + index)
    
    // Put item into storage
    this.storage.put(index, item)
    
    // Invoke callback informing of the change.
    this.notifyObservers("itemPut", index)
  },
  
  /** 
   * Sends immediate request for all items.
   * This method is invoked on initialization,
   * if loadURL is specified and loadOnDemand is false.
   */
  sendImmediateRequest: function() {
    new Gallery.ItemsRequest(this.loadURL, {
      onSuccess: this.immediateRequestSucceeded.bind(this),
      onFailure: this.immediateRequestFailed.bind(this)})
  },

  /**
   * Invoked on immediate request success.
   */  
  immediateRequestSucceeded: function(items, size) {
    // Set gallery size.
    this.setSize(size)
    
    // Make sure, that actual number of items to put does
    // not go outside the gallery size.
    itemsCount = items.length
    itemsCount = Math.min(itemsCount, this.size)

    // Put all items to storage.
    // Remember that items in the storage are indexed from 1.
    for (var index = 0; index < itemsCount; index++) {
      this.putItem(index + 1, items[index])
    }
  },
  
  /**
   * Invoked on immediate request failure.
   */  
  immediateRequestFailed: function() {
    // Nothing we can really do here.
  },

  /**
   * Requests an item at given index to be loaded
   * if it does not exist, or hasn't been requested yet.
   *
   * This method does something only if loadURL is specified 
   * and loadOnDemand is true. Otherwise it does nothing.
   */
  requestItem: function(index) {
    this.requestItems(index, index)
  },

  /**
   * Requests given range of items to be loaded
   * if they does not exist, or haven't been requested yet.
   *
   * This method does something only if loadURL is specified 
   * and loadOnDemand is true. Otherwise it does nothing.
   */
  requestItems: function(from, to) {
    // If the loadURL is not specified, or items are not to be loaded
    // on demand, do nothing and return immediately.
    if (this.loadURL == null || !this.loadOnDemand) {
      return
    }
    
    // Validate 'from' and 'to' parameters
    from = Math.max(from, 1)
    to = (this.size != null) ? Math.min(to, this.size) : to
    
    // If from > to, no items needs to be requested.
    if (from > to) return

    // Note: Requesting range of items may produce more than one actual request
    // to be send, if the array of items is fragmented. Following code
    // looks for all unloaded fragments in the specified range,
    // and for each fragment sends actual request.    
    var requestFrom = from
    
    while (requestFrom <= to) {
      if (this.storage.get(requestFrom) != null) {
        requestFrom++
        
      } else {
        var requestTo = requestFrom
      
        while (requestTo < to && this.storage.get(requestTo+1) == null) {
          requestTo++
        }

        this.sendDemandRequest(requestFrom, requestTo)
       
        requestFrom = requestTo + 2
      }
    }
  },

  /**
   * Sends on-demand request for the items to be loaded.
   * From and to attributes must not be null, and must
   * be valid (not exceeding gallery size and from <= to).
   */
  sendDemandRequest: function(from, to) {
    //Widget.log("Gallery", "Sending request on demand for items from " + from + " to " + to)

    this.markItemsRequested(from, to)

    new Gallery.ItemsRequest(this.loadURL, {
      from: from,
      to: to,
      onSuccess: this.demandRequestSucceeded.bind(this, from, to),
      onFailure: this.demandRequestFailed.bind(this, from, to)})
  },

  /**
   * Invoked on on-demand request success
   */
  demandRequestSucceeded: function(from, to, items, size) {
    // Update the size of the gallery.
    this.setSize(size)
    
    // Make sure, that actual number of items to put does
    // not go outside the gallery size.
    to = Math.min(to, this.size)

    // Transfer items from request to the storage.    
    itemsIndex = 0
    storageIndex = from
    
    while((itemsIndex <= items.length) && (storageIndex <= to)) {
      this.putItem(storageIndex, items[itemsIndex])
      
      itemsIndex++
      storageIndex++
    }

    // Unmark items, which were marked as being requested
    this.unmarkItemsRequested(from, to)    
  },
  
  /**
   * Invoked on on-demand request failure
   */
  demandRequestFailed: function(from, to) {
    this.unmarkItemsRequested(from, to)
  },

  /**
   * Marks given range of items as being requested.
   */
  markItemsRequested: function(from, to) {
    for (var index = from; index <= to; index++) {
      this.storage.put(index, 'requesting')
    }
  },

  /**
   * Unmarks given range of items as not being requested anymore.
   */
  unmarkItemsRequested: function(from, to) {
    for (var index = from; index <= to; index++) {
      if (this.storage.get(index) == 'requesting') {
        this.storage.remove(index)
      }
    }
  },

  /**
   * Returns item at given index, if it exists.
   */
  getItem: function(index) {
    var item = this.storage.get(index)
    
    if (item == 'requesting') {
      item = null
    }
    
    return item
  },

  /**
   * Returns the size of this set of items.
   */
  getSize: function() {
    return (this.size != null) ? this.size : 0
  }  
})

/**
 * A response.
 */
Gallery.Response = Class.define(
{
  initialize: function(items, count) {
    this.items = items
    this.count = count
  },
  
  getItems: function() {
    return this.items
  },
  
  getCount: function() {
    return this.count
  }
})

/**
 * A request for items from specified range under given URL.
 * Invokes a callback when requests is finished.
 */
Gallery.ItemsRequest = Class.define(Widget.OptionsContainer,
{
  // Requested start item number
  from: 1,
  
  // Requested end item number (all items if null).
  to: null,
  
  // Request success callback function.
  onSuccess: null,
  
  // Request failure callback function.
  onFailure: null,
  
  /**
   * Initialization.
   */
  initialize: function(url, options) {
    this.installOptions(options)
    
    this.url = url
  
    // Prepare an array for incoming items.
    this.items = []
    
    // Create (and send) AJAX request. One of the methods will be invoked
    // depending on the result of the request:
    //  - ajaxRequestSucceeded()
    //  - ajaxRequestFailed()
    this.createAJAXRequest()
  },
  
  /**
   * Creates and sends AJAX request.
   */
  createAJAXRequest: function() {
    // Prepare query parameters for the AJAX request.
    var parameters = null

    if ((this.from != null) || (this.to != null)) {
      parameters = {}
      if (this.from != null) parameters['mcs-start'] = this.from
      if (this.to != null) parameters['mcs-end'] = this.to
      parameters = $H(parameters).toQueryString()
    }

    //Widget.log("Gallery", "Sending AJAX request from " + this.from + " to " + this.to)
	//Widget.log("Gallery", "URL: " + this.url + ", parameters: " + parameters)

    // Create AJAX request.
    return new Widget.AjaxRequest(this.url, {
                parameters: parameters,
                method: "get",
                onSuccess: this.ajaxRequestSucceeded.bind(this),
                onFailure: this.ajaxRequestFailed.bind(this),
                onException: this.ajaxRequestFailed.bind(this)})
  },
  
  /**
   * Invoked on AJAX request success.
   */
  ajaxRequestSucceeded: function(request) {
    //Widget.log("Gallery", "AJAX request succeeded");

    // Get response text out of the request.
    var responseText = request.responseText

    // Insert response content, stripping unnessecary scripts.
    this.getResponseArea().innerHTML = responseText.stripScripts()

    // Store this request on global field for the JavaScript code
    // included within the response content.
    // Because JavaScript is single-threaded, that solution
    // is safe.
    Gallery.itemsRequest = this

    //Widget.log("Gallery", "Evaluating JavaScript response...")
      
    // Evaluate the JavaScript from the response content.
    responseText.evalScripts()

    // After JavaScript is evaludated, clean the global variable.
    Gallery.itemsRequest = null
    
    this.requestSucceeded()
  },
  
  /**
   * Invoked on AJAX request failure.
   */
  ajaxRequestFailed: function() {
    //Widget.log("Gallery", "AJAX request failed");
  },
  
  /**
   * Invoked on this request success.
   */
  requestSucceeded: function() {
    //Widget.log("Gallery", "Items loaded: " + this.items.length + ", gallery size: " + this.size);

    // Invoke the callback, if specified.
    if (this.onSuccess != null) {
      this.onSuccess(this.response.getItems(), this.response.getCount())
    }
  },
  
  /**
   * Invoked on this request failure.
   */
  requestFailed: function() {
    // Invoke the callback, if specified.
    if (this.onFailure != null) {
      this.onFailure()
    }
  },
  
  /**
   * Invoked from the script embeeded in the request to add next item.
   */
  setResponse: function(response) {
    //Widget.log("Gallery", "Item added in response")

    this.response = response
  },

  /*
   * Creates and returns new HTML element, which will act
   * as a placeholder for response content.
   */
  createResponseArea: function() {
    var responseArea = document.createElement("div")

    var responseAreaElement = Element.extend(responseArea)

    responseAreaElement.setStyle({display: 'none'})

    responseAreaElement.setStyle({visibility: 'hidden'})

    document.body.appendChild(responseArea)

    return responseAreaElement
  },

  /*
   * Returns a HTML element, which acts as a placeholder 
   * for response content.
   */
  getResponseArea: function() {
    if (!this.responseArea) {
      this.responseArea = this.createResponseArea()
    }

    return this.responseArea
  }
})

/**
 * A widget which displays an item (its summary or detail part).
 */
Gallery.ItemDisplay = Widget.define(
{
  item: null,

  /**
   * Initialization
   */
  initialize: function(block, mode, options) {
    this.initializeWidget(null, options)

    // Create a Block widget for displaying item's content.
    this.block = block
    this.mode = mode
    
    this.updateBlock()

    this.observe(this.block, "contentShown", "contentShown")
    this.observe(this.block, "contentHidden", "contentHidden")
    this.observe(this.block, "displayedContentChanged", "displayedContentChanged")
  },

  /**
   * Sets an item to be displayed.
   */
  setItem: function(item) {
    if (this.item !== item) {
      this.item = item
      
      this.updateBlock()
    }
  },
  
  updateBlock: function() {
    if (this.item == null) {
      this.block.setContent(null)
    } else {
      if (this.mode == 'summary') {
        this.block.setContent(this.item.getSummarySlide().getContent())
      } else {
        this.block.setContent(this.item.getDetailSlide().getContent())
      }    
    }
  },
  
  /**
   * Returns item, if it's specified.
   */
  getItem: function() {
    return this.item
  },
  
  /**
   * Returns item which is currently displayed.
   *
   * Explanation: after setting an item using setItem(),
   * the old item is being displayed for some time,
   * until the disappear effect is executed. After that
   * the displayed item changes to the new item, using 
   * appear effect.
   */
  getDisplayedItem: function() {
    return this.displayedItem
  },
  
  /**
   * Sets display mode: 'summary' or 'detail'.
   */
  setMode: function(mode) {
    if (this.mode != mode) {
      this.mode = mode

      this.updateBlock()
    }
  },
  
  /**
   * Returns display mode: 'summary' or 'detail'.
   */
  getMode: function() {
    return this.mode
  },

  contentShown: function() {
    this.notifyObservers('itemAppeared')
  },
  
  contentHidden: function() {
    this.notifyObservers('itemDisappeared')
  },
  
  displayedContentChanged: function() {
    this.displayedItem = this.item
      
    this.notifyObservers('displayedItemChanged')
  },
  
  /**
   * Returns the HTML element representing this widget.
   */
  getElement: function() {
    return this.block.getElement()
  }
})

/**
 * Updates the content of the Gallery.ItemDisplay widget with an
 * item stored under specified index in the Gallery.Items container.
 */
Gallery.ItemDisplayController = Widget.define(
{
  // If true, this controller will automatically request
  // items to be displayed. If false, the user of this controller
  // is responsible for requesting the item.
  autoRequest: true,
 
  /**
   * Initialization
   */ 
  initialize: function(items, display, options) {
    this.initializeWidget(null, options)

    this.items = items
    this.display = display
    this.index = null

    this.observe(this.items, "itemPut", "itemPut")

    this.updateDisplay()
  },

  /**
   * Sets an index of the item from the Gallery.Items container
   * to be displayed in the Gallery.ItemDisplay widget.
   */
  setItem: function(index) {
    // Update the index property.
    this.index = index

    if (index == null || index < 1 || index > this.items.getSize()) {
      // If index is null, update the ItemDisplay widget immediately
      // to display nothing.
      this.updateDisplay()
      
    } else {
      // If index is specified (not null), get the item to display.
      var item = this.items.getItem(this.index)
      
      if (item != null) {
        // If an item was found, display it immediately.
        this.updateDisplay()
        
      } else {
        // If no item was found, it needs to be requested (downloaded via AJAX).
        // If 'autoRequest' facility is enabled, request item immediately.
        // The itemChanged() callback will be invoked when the item arrives.
        if (this.autoRequest) {
          this.items.requestItem(index)
        }

		this.updateDisplay()
      }
    }
  },

  /**
   * Callback method invoked when item arrives in the 
   * Gallery.Items container under given index.
   */
  itemPut: function(index) {
    // If the index of changed item is equal to the one displayed
    // by this controller, update the display now.
    if (this.index == index) {
      this.updateDisplay()
    }
  },

  /**
   * Updates the content of the Gallery.ItemDisplay widget
   * with current item under specified index.
   */
  updateDisplay: function() {
    var item = null

    if (this.index != null) {
      item = this.items.getItem(this.index)
    }

    this.display.setItem(item)
  }
})

/**
 * The Gallery widget.
 */
Gallery.Gallery = Widget.define(
{
  items: null,
  displays: null,
  slideshow: null,
  slideshowPopup: null,
  slideshowLaunchDelay: null,
  
  /**
   * Initialization
   */
  initialize: function(items, displays, options) {
    this.initializeWidget(null, options)

    this.items = items
    
    // Initialize the index of the first item in the block
    // and the index of the displayed page.
    this.index = 1
    this.pageNumber = 1

    this.observe(this.items, "sizeChanged", "itemsSizeChanged")

    // Create item display controllers, which would update each of
    // the item display widgets. Disable the autoRequest facility,
    // because the gallery will request the items in Panes.
    this.controllers = []

    for (var i = 0; i < displays.length; i++) {
      this.controllers[i] = new Gallery.ItemDisplayController(this.items, displays[i], {autoRequest: false})
    }
    
    // If there's a slideshow associated with the gallery,
    // handle the CLICK event.
    for (var i = 0; i < displays.length; i++) {
      var button = new Widget.Internal.Button(displays[i].getElement())
        
      this.observe(button, "pressed", "sendToSlideshow", i+1)
    }
    
    // Schedule slideshow launch (if there's slideshow and launch delay specified)
    this.scheduleSlideshowLaunch()
    
    this.update()

    // Initialize properties and actions
    this.addAction('next')
    this.addAction('previous')
    
    this.addProperty('start-item-number')
    this.addProperty('end-item-number')
    this.addProperty('items-count')
    this.addProperty('page-number')
    this.addProperty('pages-count')
  },
  
  itemsSizeChanged: function() {
    this.notifyChanges()
  },

  /** 
   * Updates actions and properties.
   */
  notifyChanges: function() {
    this.notifyObservers('startItemNumberChanged')
    this.notifyObservers('endItemNumberChanged')
    this.notifyObservers('itemsCountChanged')
    this.notifyObservers('pageNumberChanged')
    this.notifyObservers('pagesCountChanged')
    this.notifyObservers('canNextChanged')
    this.notifyObservers('canPreviousChanged')
  },
  
  /**
   * Goes to the previous page of the gallery,
   * if not already on the first page
   *
   * @volantis-api-include-in PublicAPI
   */
  previous: function() {
    if (this.canPrevious()) {
      this.skipSlideshowLaunch()

      this.index -= this.controllers.length
      
      this.pageNumber--

      this.update()

      this.notifyChanges()
    }
  },
  
  canPrevious: function() {
    return this.getStartItemNumber() > 1
  },

  /**
   * Goes to the next page of the gallery,
   * if not already on the last page
   *
   * @volantis-api-include-in PublicAPI
   */
  next: function() {
    if (this.canNext()) {
      this.skipSlideshowLaunch()

      this.index += this.controllers.length
      
      this.pageNumber++

      this.update()

      this.notifyChanges()
    }
  },
  
  canNext: function() {
    return this.getEndItemNumber() < this.getItemsCount()
  },
  
  getWidget: function(idOrWidget) {
    if (typeof(idOrWidget) == 'string') {
      return Widget.getInstance(idOrWidget)
    } else {
      return idOrWidget
    }
  },

  getSlideshow: function() {
    return this.getWidget(this.slideshow)
  },

  getSlideshowPopup: function() {
    if (this.popupWidget == null) {
      this.popupWidget = this.getWidget(this.slideshowPopup);
      var slideshow = this.getSlideshow();
    }
    this.observe(this.popupWidget, "dismissed", "stop");
    return this.popupWidget
  },

  /**
   * Returns number of displayed page.
   */
  getPageNumber: function() {
    return this.pageNumber
  },
  
  /**
   * Return number of items displayed per page
   */
  getPageSize: function() {
    return this.controllers.length
  },
  
  /**
   * Returns number of pages.
   */
  getPagesCount: function() {
    if (this.getPageSize() != 0) {
      return Math.ceil(this.getItemsCount() / this.getPageSize())
    }
  },
  
  /**
   * Returns number of first item displayed on current page.
   */
  getStartItemNumber: function() {
    return this.index
  },

  /**
   * Returns number of last item displayed on current page.
   */
  getEndItemNumber: function() {
    return Math.min(this.index + this.controllers.length - 1, this.items.getSize())
  },

  /**
   * Returns number of items in this gallery.
   */
  getItemsCount: function() {
    return this.items.getSize()
  },
  
  /**
   * Sends n-th item displayed on current page to the slideshow,
   * if there's a slideshow specified for this Gallery.
   *
   * @param itemNumber The number of the item displayed on current page.
   */
  sendToSlideshow: function(itemNumber) {
    this.skipSlideshowLaunch()

    var slideshow = this.getSlideshow()

    if (slideshow != null) {
      // Calculate index of an item.
      var index = this.index + itemNumber - 1
      
      // If an index is valid, open slideshow.
      if (index <= this.items.getSize()) {
        // Open the popup, if there's any.
        var popup = this.getSlideshowPopup()
        
        if (popup != null) {
          popup.show()
        }

        // Move the slideshow to given slide.
        // Note: I choose to do it using timer, because if there's
        // no timer and there is popup, the first slide appears
        // instantly, without effect. There may be issues if the popup
        // has it's own effect.
        //this.slideshow.gotoItem(this.index + itemNumber - 1)
        setTimeout(slideshow.gotoItem.bind(slideshow, this.index + itemNumber - 1), 100)
      }
    }
  },

  /**
   * Launches the slideshow, if it's specified.
   *
   * @volantis-api-include-in PublicAPI
   */
  launchSlideshow: function() {
    var slideshow = this.getSlideshow()
    
    if (slideshow != null) {
      // Open the popup, if there's any.
      var popup = this.getSlideshowPopup()
      
      if (popup != null) {
        popup.show()
      }

      // Start the slideshow.
      slideshow.play()
    }
  },

  /**
   * Schedules the slideshow to be launched automatically  
   * after given idle time (slideshowLaunchDelay property).
   * If there's no slideshow or delay specified, it does nothing.
   */
  scheduleSlideshowLaunch: function() {
    if (this.slideshowLaunchDelay != null) {
      this.slideshowTimer = setTimeout(this.launchSlideshow.bind(this), this.slideshowLaunchDelay * 1000)
    }
  },
  
  /**
   * Skips slideshow launch, if it was scheduled.
   */
  skipSlideshowLaunch: function() {
    if (this.slideshowTimer != null) {
      this.slideshowTimer = clearTimeout(this.slideshowTimer)
    }
  },

  /**
   * Updates displayed items.
   */
  update: function() {
    this.items.requestItems(this.index, this.index + this.controllers.length - 1)
  
    for (var i = 0; i < this.controllers.length; i++) {
      this.controllers[i].setItem(this.index + i)
    }
  }
})

/**
 * A slideshow widget.
 */
Gallery.Slideshow = Widget.define(
{
  items: null,
  display: null,
  order: 'normal', // 'normal' | 'reverse' | 'random'
  repetitions: 1, // integer or 'infinite'
  autoPlay: true, // If true, slideshow will automatically start playing.

  /**
   * Initialization.
   */
  initialize: function(items, display, options) {
    this.initializeWidget(null, options)

    this.items = items

    this.observe(this.items, "itemPut", "itemsChanged")
    this.observe(this.items, "sizeChanged", "itemsChanged")

    this.display = display
    this.controller = new Gallery.ItemDisplayController(items, display)
    this.playing = false
    this.index = null
    this.displayedIndex = null
    this.slideCounter = null
    
    this.observe(display, "itemAppeared", "displayItemAppeared")
    
    this.update()
    
    if (this.autoPlay) {
      this.play();
    }

    // Initialize properties and actions
    this.addAction('next')
    this.addAction('previous')
    this.addAction('play')
    this.addAction('pause')
    this.addAction('stop')
    
    this.addProperty('item-number')
    this.addProperty('items-count')    
  },

  /** 
   * Updates actions and properties.
   */
  notifyChanges: function() {
    this.notifyObservers('canNextChanged')
    this.notifyObservers('canPreviousChanged')
    this.notifyObservers('canPlayChanged')
    this.notifyObservers('canPauseChanged')
    this.notifyObservers('canStopChanged')
  
    this.notifyObservers('itemNumberChanged')
    this.notifyObservers('itemsCountChanged')
  },

  /**
   * Stops the slideshow, positioning it just before the first item.
   *
   * @volantis-api-include-in PublicAPI
   */
  stop: function() {
    if (this.canStop()) {
      this.gotoItem(null)
    }
  },
  
  canStop: function() {
    return this.index != null
  },
  
  /**
   * Starts playing the slideshow, if not already playing.
   *
   * @volantis-api-include-in PublicAPI
   */
  play: function() {
    if (this.canPlay()) {
      //Widget.log("Gallery", "Playing...")
      
      this.playing = true
      
      if (this.items.isReady()) {
        // If items are ready, start playing immediately
        this.doPlay()
      } else {
        // Otherwise do nothing now, and wait for the items to get ready.
      }
      
      this.notifyChanges()
    }
  },
  
  doPlay: function() {
    if (this.repetitions != 'infinite') {
      this.slideCounter = this.getItemsCount() * this.repetitions
    } else {
      this.slideCounter = null
    }
      
    this.nextSlide()
  },
  
  canPlay: function() {
    return !this.playing
  },

  /**
   * Pauses the slideshow, if not already paused.
   *
   * @volantis-api-include-in PublicAPI
   */
  pause: function() {
    if (this.canPause()) {
      //Widget.log("Gallery", "Pausing...")

      this.playing = false
      
      this.slideCounter = null
      
      this.timer = clearTimeout(this.timer)

      this.notifyChanges()
    }
  },
  
  canPause: function() {
    return this.playing   
  },

  /**
   * Goes to the previous item, if not already displaying first one.
   *
   * @volantis-api-include-in PublicAPI
   */
  previous: function() {
    if (this.canPrevious()) {
      //Widget.log("Gallery", "Going previous...")

      this.pause()

      this.index -= 1

      this.update()
    }
  },
  
  canPrevious: function() {
    return (this.index != null) && (this.index > 1)
  },

  /**
   * Goes to the next item, if not already displaying last one.
   *
   * @volantis-api-include-in PublicAPI
   */
  next: function() {
    if (this.canNext()) {
      //Widget.log("Gallery", "Going next...")

      this.pause()

      this.index += 1

      this.update()
    }
  },
  
  canNext: function() {
    return (this.index != null) && (this.index < this.getItemsCount())
  },

  /**
   * Moves the slideshow to display an item specified by its number (index).
   * If index is null, the slideshow is re-set.
   *
   * @volantis-api-include-in PublicAPI
   */
  gotoItem: function(index) {
    if (this.isIndexValid(index)) {
      this.pause()

      this.index = index
    
      this.update()
    }
  },

  /**
   * Returns true, if the index on an item is valid.
   * Index is valid if it's null, or is contained in range [1..itemsCount]
   */
  isIndexValid: function(index) {
    return (index == null) || ((index >= 1) && (index <= this.getItemsCount()))
  },

  /**
   * Plays next slide.
   */
  nextSlide: function() {
    var nextIndex = this.nextSlideIndex()

    if (nextIndex == this.index) {
      this.pause()
      
    } else {
      //Widget.log("Gallery", "Switching to next slide " + nextIndex)

      this.index = nextIndex
    
      if (this.slideCounter != null) {
        this.slideCounter--
      }
    
      this.nextRandomIndex = null
    
      this.update()

      // Request (preload) next slide
      this.requestNextSlide()
    }
  },
  
  requestNextSlide: function() {
    var nextItem = this.nextSlideIndex()
    
    if (nextItem != null) {
      //Widget.log("Gallery", "Preloading item " + nextItem)
      
      this.items.requestItem(nextItem)
    }
  },
    
  /**
   * Returns an index of the next item to show during slideshow.
   * Returns null if the current slide should disappear.
   * Returns current slide index, if there's no next slide to display.
   */
  nextSlideIndex: function() {
    var currentIndex = this.index
   
    if (this.getItemsCount() == 0) {
      // If there are no slides, there is no next slide index.
      nextIndex = null
      
    } else if (this.slideCounter == 0) {
      // If current slide is the last slide to show, 
      // keep it displayed.
      nextIndex = currentIndex
      
    } else {
      // If there are still slides to show, calculate its index
      // depending on slideshow order.
      if (this.order == 'normal') {
        if (currentIndex == null) {
          // If there was no slide currently displayed,
          // display the first one
          nextIndex = 1
          
        } else {
          // If there was a slide displayed, switch to the next one.
          nextIndex = currentIndex + 1
        
          if (nextIndex > this.getItemsCount()) {
            nextIndex = 1
          }
        }
      } else if (this.order == 'reverse') {
        if (currentIndex == null) {
          // If there was no slide currently displayed,
          // display the last one
          nextIndex = this.getItemsCount()

        } else {
          // If there was a slide displayed, switch to the next one.
          nextIndex = currentIndex - 1

          if (nextIndex < 1) {
            nextIndex = this.getItemsCount()
          }
        }
      } else if (this.order == 'random') {
        if (this.nextRandomIndex == null) {
          // Generate random number between 1 and itemsCount.
          // The Math.random() function returns floating point value between 0 and 1.
          // After multiplying by itemsCount, we get float value between 0 and itemsCount.
          // When Math.ceil() is applied, we get integral value between 0 and itemsCount,
          // where the probability of each value other than 0 is equal to 1/itemsCount,
          // and the probability of the 0 value is ZERO.
          // The Math.max() function takes care, that the 0 value is translated to 1.
          // After that, we get random value between 1 and itemsCount, each with the probability
          // of 1/itemsCount.
          this.nextRandomIndex = Math.max(Math.ceil(Math.random() * this.getItemsCount()), 1)
        }
        
        nextIndex = this.nextRandomIndex
      }
    }

    return nextIndex
  },

  /**
   * Returns true, if slideshow is currently playing
   */
  isPlaying: function() {
    return this.playing
  },

  /**
   * Returns number of displayed item.
   */
  getItemNumber: function() {
    return this.displayedIndex
  },

  /**
   * Returns number of items in the slideshow
   */
  getItemsCount: function() {
    return this.items.getSize()
  },

  /**
   * Updates displayed item in the slideshow.
   */
  update: function() {
    if (this.index == null) {
      if (this.playing && this.items.isReady()) {
        this.doPlay()
        
        return
      }
    }

    this.controller.setItem(this.index)

    this.displayedIndex = this.index
   
    this.notifyChanges()
  },
  
  /**
   * Invoked on ItemDisplay widget change.
   */
  displayItemAppeared: function() {
    // Schedule the next slide after previous one appeared.
    if (this.playing) {    
      this.scheduleNextSlide()
    }
  },
  
  /**
   * Returns slide interval in seconds.
   */
  getInterval: function() {
    var item = this.display.getItem()
    
    var interval = 5 // default interval = 5 seconds
    
    if (item != null) {
      var duration = item.getDetailSlide().getDuration()
      
      if (duration != 'normal') {
        interval = duration
      }
    }

    return interval
  },
  
  /**
   * Schedules next slide to be displayed.
   */
  scheduleNextSlide: function() {
    this.timer = setTimeout(this.nextSlide.bind(this), this.getInterval() * 1000)
  },
  
  /**
   * Invoked when items changes.
   */
  itemsChanged: function() {
    this.update()
  }
})
