/**
 * (c) Volantis Systems Ltd 2006. 
 */
Widget.DeckLoad = Class.define(Widget.Load, {
  initialize: function(options) {
    this.initializeLoad(options)
  },
  
  doExecute: function() {
    // no-op
  }  
})
 
/**
 * Deck Page. Consists of Block Content and Effect Block for show/hide effects.
 */
Widget.DeckPage = Widget.define(Widget.Appearable, Widget.Disappearable,
{
  initialize: function(blockContent, effectBlockId, options) {
    this.initializeWidget(null, options)
    
    this.blockContent = blockContent
    
    this.effectBlockId = effectBlockId
  },
  
  getBlockContent: function() {
    return this.blockContent
  },
  
  getEffectBlock: function() {
    return $W(this.effectBlockId)
  }
})

/**
 * Deck Response. Contains array with requested pages
 * and information about total number of available pages.
 */
Widget.Internal.DeckResponse = Class.define(
{
  initialize: function(pages, totalPagesCount) {
	this.pages = pages
	this.totalPagesCount = totalPagesCount    
  },
  
  getPages: function() {
    return this.pages
  },
  
  getTotalPagesCount: function() {
    return this.totalPagesCount
  }
})

/**
 * The Deck widget.
 */
Widget.Deck = Widget.define(
{
  load: null,
  mode: "switch",
  
  initialize: function(blockContainer, options) {
    Widget.log("Deck", "Creating instance of Deck widget")
    
    this.initializeWidget(null, options)

    // This BlockContainer widget will hold BlockContent widgets
    // encapsulating deck pages.
    this.blockContainer = blockContainer
    
    // This array will contain all available pages, in the same order
    // as their respective BlockContents within the BlockContainer.
    this.pages = []
    
    // This array will map number of BlockContent into number
    // of page.
    this.pageNumbers = []

    // Until pages are downloaded, the total number of pages is unknown.      
    this.pagesCount = null
    
    // Initialize current page number and displayed page number.
    this.currentPageNumber = 1
    this.displayedPageNumber = null
    
    // Initialize a client for downloading page contents.
    this.client = new Widget.Internal.Client()

    // Listen to request success, failure, and interrupt.
    this.observe(this.client, "requestSucceeded", "clientRequestSucceeded")
    this.observe(this.client, "requestFailed", "clientRequestFailed")
    this.observe(this.client, "requestInterrupted", "clientRequestInterrupted")
    
    if (this.load == null) {
      this.doRequests = false
      
      var pages = this.getOption("pages") || []
      
      this.pagesCount = pages.length
      
      for (var i = 0; i < pages.length; i++) {
        var page = pages[i]
  
        this.insertPage(page, i+1)
      }
    
      this.update()
      
    } else {
      this.load.setOwner(this)

      this.doRequests = true
    
      if (this.load.getWhen() == "onload") {
        this.requestPages()
      
        // All pages are downloaded on startup, so further requests are
        // not needed.
        this.doRequests = false
        
      } else {
        this.update()
      }
    }
        
    // Initialize actions, properties and events.
    this.addAction("next-page")
    this.addAction("previous-page")
    this.addAction("first-page")
    this.addAction("last-page")

    this.addProperty("current-page-number")
    this.addProperty("displayed-page-number")
    this.addProperty("pages-count")
  },
  
  nextPage: function() {
    this.setCurrentPageNumber(this.currentPageNumber + 1)
  },
  
  canNextPage: function() {
    return this.pagesCount != null && this.currentPageNumber < this.pagesCount
  },
  
  previousPage: function() {
    this.setCurrentPageNumber(this.currentPageNumber - 1)
  },
  
  canPreviousPage: function() {
    return this.currentPageNumber > 1
  },
  
  firstPage: function() {
    this.setCurrentPageNumber(1)
  },
  
  canFirstPage: function() {
    return this.currentPageNumber != 1
  },
  
  lastPage: function() {
    if (this.pagesCount != null) {
      this.setCurrentPageNumber(this.pagesCount)
    }
  },
  
  canLastPage: function() {
    return this.pagesCount != null && this.currentPageNumber != this.pagesCount
  },
  
  getCurrentPageNumber: function() {
    return this.currentPageNumber
  },

  /**
   * Switches to the specified page.
   */
  setCurrentPageNumber: function(pageNumber) {
    // This method may be invoked at any moment, and must take care
    // of following situations:
    //  - pages are not yet downloaded from the Service (pagesCount is not known)
    //  - Deck is currently during page transition
    //  - Deck is in stable state
    
    // First, check if page number is in valid range. If invalid,
    // return immediately.
    if (this.pagesCount == null || pageNumber < 1 || pageNumber > this.pagesCount) {
      return
    }

    // Check, if there really is a change in page number. If no,
    // return immediately    
    if (this.currentPageNumber == pageNumber) {
      return
    }

    Widget.log("Deck", "Switching to page " + pageNumber)
    
    this._setCurrentPageNumber(pageNumber)

    this.update()
  },
  
  getPagesCount: function() {
    return this.pagesCount
  },

  _setPagesCount: function(pagesCount) {
    if (this.pagesCount != pagesCount) {
      this.pagesCount = pagesCount

      this.notifyObservers("pagesCountChanged")
      this.notifyObservers("canFirstPageChanged")
      this.notifyObservers("canLastPageChanged")
      this.notifyObservers("canNextPageChanged")
      this.notifyObservers("canPreviousPageChanged")
    }
  },

  _setCurrentPageNumber: function(number) {
    this.currentPageNumber = number
    
    this.notifyObservers("currentPageNumberChanged")
    
    this.notifyObservers("canFirstPageChanged")
    this.notifyObservers("canLastPageChanged")
    this.notifyObservers("canNextPageChanged")
    this.notifyObservers("canPreviousPageChanged")
  },
  
  isReady: function() {
    return this.pagesCount != null
  },

  getDisplayedPage: function() {
    if (this.displayedPageNumber != null) {
      return this.getPage(this.displayedPageNumber)
    }
  },

  getDisplayedPageNumber: function() {
    return this.displayedPageNumber
  },

  _setDisplayedPageNumber: function(number) {
    if (this.displayedPageNumber != number) {
      this.displayedPageNumber = number
      
      this.notifyObservers("displayedPageNumberChanged")
    }
  },

  getPage: function(pageNumber) {
    var contentNumber = this.findContentNumber(pageNumber)
    
    if (contentNumber > 0) {
      return this.getPageForContentNumber(contentNumber)
    }
  },
  
  getCurrentPage: function() {
    return this.getPage(this.currentPageNumber)
  },
  
  getPageNumberForContentNumber: function(contentNumber) {
    return this.pageNumbers[contentNumber - 1]
  },
  
  getPageForContentNumber: function(contentNumber) {
    return this.pages[contentNumber - 1]
  },
  
  insertPage: function(page, pageNumber) {
    var contentNumber = this.findContentNumber(pageNumber)

    if (contentNumber < 0) {    
	  contentNumber = -contentNumber
	  
      Widget.log("Deck", "Inserting page " + pageNumber + " into container at " + contentNumber)
    
	  // Add the page to the array of pages.
	  this.pages.splice(contentNumber - 1, 0, page)
	  
	  this.pageNumbers.splice(contentNumber - 1, 0, pageNumber)
	  
	  // Add BlockContent of the page to the BlockContainer.
	  this.blockContainer.addAt(page.getBlockContent(), contentNumber)
	  
	  // Start listening to all EffectBlock notifications
	  var effectBlock = page.getEffectBlock()
	  
	  this.observe(effectBlock, null, "effectBlockChanged", effectBlock)
    }
  },
  
  effectBlockChanged: function(notificationName, effectBlock) {
    if (notificationName == "hidden" || notificationName == "shown") {
      this.update()
    }
  },

  requestMissingPages: function(startPageNumber, endPageNumber) {
    // Look for the smallest range of pages, which needs to be requested.
    // First, look for the first missing page from the start.
    while (startPageNumber <= endPageNumber && this.getPage(startPageNumber) != null) {
      startPageNumber++
    }
    
    // Now, look for the first missing page from the end.
    while (endPageNumber >= startPageNumber && this.getPage(endPageNumber) != null) {
      endPageNumber--
    }
    
    // If there are missing pages, request them.
    if (startPageNumber <= endPageNumber) {
      this.requestPages(startPageNumber, endPageNumber)
    }
  },

  requestPages: function(startPageNumber, endPageNumber) {
    if (this.doRequests) {
      Widget.log("Deck", "Sending request for pages from "
        + (startPageNumber ? startPageNumber : 'the first page')
        + " to " 
        + (endPageNumber ? endPageNumber : 'the last page'))

      this.requestedStartPageNumber = startPageNumber
      this.requestedEndPageNumber = endPageNumber

      var parameters = {}
    
      if (startPageNumber != null) {
        parameters['mcs-start'] = startPageNumber
      }
    
      if (endPageNumber != null) {
        parameters['mcs-end'] = endPageNumber
      }
    
      this.client.setURL(this.load.getSrc())
      this.client.setParameters(parameters)
      this.client.sendRequest()
    }
  },
  
  requestPage: function(pageNumber) {
    this.requestPages(pageNumber, pageNumber)
  },

  clientRequestSucceeded: function(deckResponse) {
    Widget.log("Deck", "Request succeeded")
    
    var pages = deckResponse.getPages()
    
    var totalPagesCount = deckResponse.getTotalPagesCount()
    
    var startPageNumber = this.requestedStartPageNumber
    
    if (startPageNumber == null) {
    	startPageNumber = 1
	}
	    
    this.pagesReceived(pages, startPageNumber, totalPagesCount)
  },
  
  clientRequestFailed: function(message) {
    Widget.log("Deck", "Request failed")
  },
  
  clientRequestInterrupted: function() {
    Widget.log("Deck", "Request interrupted")
  },
  
  pagesReceived: function(pages, startPageNumber, totalPagesCount) {
    if (this.pagesCount == null) {
      this._setPagesCount(totalPagesCount)
    }
  
    var endPageNumber = (startPageNumber + pages.length - 1)
    
    Widget.log("Deck", "Received pages from " + startPageNumber + 
    	" to " + endPageNumber + 
    	" of " + totalPagesCount)
    
    for (var i = 0; i < pages.length; i++) {
      var pageNumber = startPageNumber + i
      
      var page = pages[i]
  
  	  this.insertPage(page, pageNumber)
    }
    
    this.update()
  },
  
  /**
   * Returns a content index, at which specified page number
   * is displayed (counted from 1).
   * If not found, it returns negative number, which absolute value
   * indicates an index of the content where the page should be placed
   *
   * TODO: Implement this using binary search.
   */
  findContentNumber: function(pageNumber) {
    for (var i = 0; i < this.pageNumbers.length; i++) {
      var pageNumberCandidate = this.pageNumbers[i]
      
      if (pageNumberCandidate == pageNumber) {
        return i + 1 // +1 because counting from 1
      } else if (pageNumberCandidate > pageNumber) {
        return -(i + 1)
      }
    }
      
    return -(this.pageNumbers.length + 1)
  },
  
  update: function() {    
    do {
      //Widget.log("Deck", "Updating")
    
      // Get currently displayed page, and check if it's the
      // correct one.
      var repeatUpdate = false

      var displayedPage = this.getDisplayedPage()

      if (this.mode == "switch") {
        var currentPage = this.getCurrentPage()

        if (currentPage == null) {
          Widget.log("Deck", "Update: Requesting page " + this.currentPageNumber)
          
          this.requestPage(this.currentPageNumber)
        } else if (displayedPage == null) {
          Widget.log("Deck", "Update: Showing page " + this.currentPageNumber)

          this._setDisplayedPageNumber(this.currentPageNumber)
          
          currentPage.getEffectBlock().show()
        } else {
          var effectBlock = displayedPage.getEffectBlock()
      
          if (effectBlock.getStatus() == "shown") {
            if (this.displayedPageNumber != this.currentPageNumber) {
              Widget.log("Deck", "Update: Hiding page " + this.displayedPageNumber)

              effectBlock.hide()
            }
          } else if (effectBlock.getStatus() == "hidden") {
            Widget.log("Deck", "Update: Showing page " + this.currentPageNumber)

            this._setDisplayedPageNumber(this.currentPageNumber)
          
            currentPage.getEffectBlock().show()
          }
        }
      } else {
        var currentPage = this.getCurrentPage()

        if (currentPage == null) {
          Widget.log("Deck", "Update: Requesting pages from 1 to " + this.currentPageNumber)

          this.requestMissingPages(1, this.currentPageNumber)
        } else if (displayedPage == null) {
          Widget.log("Deck", "Update: Showing page 1")

          this._setDisplayedPageNumber(1)
        
          // Since current page is not null, the first page isn't null neither.
          // It's safe to get the first page then.
          this.getPage(1).getEffectBlock().show()
        } else {
          var effectBlock = displayedPage.getEffectBlock()

          if (effectBlock.getStatus() == "shown") {
            if (this.currentPageNumber < this.displayedPageNumber) {
              Widget.log("Deck", "Update: Hiding page " + this.displayedPageNumber)

              effectBlock.hide()
            
            } else if (this.currentPageNumber > this.displayedPageNumber) {
              Widget.log("Deck", "Update: Showing page " + (this.displayedPageNumber + 1))

              var nextPage = this.getPage(this.displayedPageNumber + 1)
            
              this._setDisplayedPageNumber(this.displayedPageNumber + 1)
             
              nextPage.getEffectBlock().show()
            }
          } else if (effectBlock.getStatus() == "hidden") {
            this._setDisplayedPageNumber(this.displayedPageNumber - 1)
          
            repeatUpdate = true
          }
        }
      }
    } while (repeatUpdate)
  }
})