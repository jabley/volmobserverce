/**
 * (c) Volantis Systems Ltd 2006. 
 */

/**
 * Response for the TableBody element.
 */
Widget.TableBodyResponse = Class.define(
{
  /**
   * Initializes respose with total number of rows (may be null),
   * and rows content as HTML string with list of <tr> elements.
   */
  initialize: function(totalRowsCount, content) {
  	this.totalRowsCount = totalRowsCount;

    // Parse HTML content with table rows into JavaScript
    // array with TR elements.
  	this.rows = [];

  	// Create TABLE and TBODY elements to place HTML content into
  	var element = document.createElement("div");

  	// Place rows content into TBODY element, and read-back
  	// parsed list of TR elements
  	element.innerHTML = "<table><tbody>" + content + "</tbody></table>";
  	var children = element.firstChild.firstChild.childNodes;
  	for (var i = 0; i < children.length; i++) {
  	  var node = children.item(i);
  	  if (node.nodeType == 1 && node.tagName.toLowerCase() == "tr") {
	     this.rows.push(node);
	    }
  	}
  },

  /**
   * Returns total number of rows, or null if not known.
   */
  getTotalRowsCount: function() {
    return this.totalRowsCount;
  },

  /**
   * Returns array with <TR> HTML DOM elements (may be empty).
   */
  getRows: function() {
    return this.rows;
  }
});

/**
 * The Load widget for use within TableBodyWidget.
 */
Widget.TableBodyLoad = Class.define(Widget.Load,
{
  doExecute: function() {
    // Redirect execution to the widget itself.
    if (this.owner != null) {
      this.owner._load();
    }
  }
});

/**
 * Widget handling all body-related function of a table.
 * It's associated with the TBODY HTML element.
 * Provides following functions:
 *  - splitting rows into pages
 *  - loading rows through AJAX
 */
Widget.TableBody = Widget.define(
{
  rows: null,
  rowsPerPage: null,
  load: null,
  query: null,
  sortColumnNumber: null,
  sortOrder: null,
  cachedPagesCount: 0,

  initialize: function(id, options) {
    this.initializeWidget(id, options);

    if (this.load == null) {
      // Case 1: No AJAX.
      if (this.rowsPerPage == null) {
        // Case 1a: No AJAX + no pagination.
        Widget.log("Table", "No AJAX + no pagination");

        this.rows = null;
        this.rowsCount = this._calculateRowsCount();
        this.startRowIndex = 0;
        this.endRowIndex = this.rowsCount - 1;

      } else {
        // Case 1b: No AJAX + pagination.
        Widget.log("Table", "No AJAX + pagination");

        this.rows = this._retrieveRows();
        this.rowsCount = this.rows.length;
        this._gotoRow(0);
      }

    } else {
      // Case 2: AJAX
      this.client = new Widget.Internal.Client();
      this.observe(this.client, "requestSucceededWithoutError", "_clientRequestSucceededWithoutError");
      this.observe(this.client, "requestSucceededWithError", "_clientRequestSucceededWithError");
      this.observe(this.client, "requestFailed", "_clientRequestFailed");

      this.load.setOwner(this);

      // TODO: Fix this hack
      if (this._widgetId == null) {
        Widget.register(Math.random().toString(), this.load);
      }

      if (this.rowsPerPage == null) {
        // Case 2a: AJAX + no pagination.
        Widget.log("Table", "AJAX + no pagination");

        this.rows = null;
        this.rowsCount = this._calculateRowsCount();
        this.startRowIndex = 0;
        this.endRowIndex = this.rowsCount - 1;

      } else {
        // Case 2b: AJAX + pagination.
        Widget.log("Table", "AJAX + pagination");

        this.rows = null;
        this.rowsCount = this._calculateRowsCount();
        this.bufferedPageRows = [];
        this.bufferedPageIndices = 0;

        // Initialize the buffer size, in pages.
        // Value of 2 will guarantee, that current,
        // previous and next page are buffered.
        this.maxBufferedPagesCount = (this.cachedPagesCount < 2) ? 2 : this.cachedPagesCount;
        this.startRowIndex = 0;
        this.endRowIndex = this.rowsCount - 1;
      }

      if (this.load.getWhen() == "onload") {
        this.load.execute();
      }
    }

    // Expose functions to APE.
    this.addAction("next-page");
    this.addAction("previous-page");
    this.addAction("next-row");
    this.addAction("previous-row");
    this.addAction("first-page");
    this.addAction("last-page");

    this.addProperty("rows-count");
    this.addProperty("start-row-number");
    this.addProperty("end-row-number");

    this.addProperty("load", {type: "widget"});
    this.addProperty("load-error-message");
  },

  /**
   * Returns total number of rows, or null if it's not known.
   */
  getRowsCount: function() {
    return this.rowsCount;
  },

  /**
   * Goes to the next page, if it's possible.
   */
  nextPage: function() {
    this.setStartRowNumber(this.getStartRowNumber() + this.rowsPerPage);
  },

  /**
   * Returns true if it's possible to go to next page, false otherwise.
   */
  canNextPage: function() {
    return (this.rowsPerPage != null) && this._isRowNumberValid(this.getStartRowNumber() + this.rowsPerPage);
  },

  /**
   * Goes to the previous page, if it's possible.
   */
  previousPage: function() {
    this.setStartRowNumber(this.getStartRowNumber() - this.rowsPerPage);
  },

  /**
   * Returns true if it's possible to go to previous page, false otherwise.
   */
  canPreviousPage: function() {
    return (this.rowsPerPage != null) && (this.getStartRowNumber() > 1);
  },

  /**
   * Goes to the next row, if it's possible.
   */
  nextRow: function() {
    this.setStartRowNumber(this.getStartRowNumber() + 1);
  },

  /**
   * Returns true if it's possible to go to next row, false otherwise.
   */
  canNextRow: function() {
    return (this.rowsPerPage != null) && this._isRowNumberValid(this.getEndRowNumber() + 1);
  },

  /**
   * Goes to the previous row, if it's possible.
   */
  previousRow: function() {
    this.setStartRowNumber(this.getStartRowNumber() - 1);
  },

  /**
   * Returns true if it's possible to go to previous row, false otherwise.
   */
  canPreviousRow: function() {
    return (this.rowsPerPage != null) && (this.getStartRowNumber() > 1);
  },

  /**
   * Goes to the first page, if it's possible.
   */
  firstPage: function() {
    this.setStartRowNumber(1);
  },

  /**
   * Returns true if it's possible to go to first page, false otherwise.
   */
  canFirstPage: function() {
    return (this.rowsPerPage != null) && (this.getStartRowNumber() > 1);
  },

  /**
   * Goes to the last page, if it's possible.
   */
  lastPage: function() {
    if (this.canLastPage()) {
      this.setEndRowNumber(this.rowsCount);
    }
  },

  /**
   * Returns true if it's possible to go to last page, false otherwise.
   */
  canLastPage: function() {
    return (this.rowsPerPage != null) && (this.rowsCount != null) &&
    	(this.getEndRowNumber() != this.rowsCount);
  },

  /**
   * Returns current start row number (the first displayed row).
   */
  getStartRowNumber: function() {
    return this.startRowIndex + 1;
  },

  /**
   * Tries to scroll table rows, so that specified row number is
   * displayed as the first row.
   */
  setStartRowNumber: function(number) {
    if (this.rowsPerPage == null) {
      // Case 1: No pagination = NOP

    } else {
      // Case 2: Pagination.
      this._gotoRow(number - 1);
    }
  },

  /**
   * Returns current end row number (the last displayed row).
   */
  getEndRowNumber: function() {
    return this.endRowIndex + 1;
  },

  /**
   * Tries to scroll table rows, so that specified row number is
   * displayed as the last row.
   */
  setEndRowNumber: function(number) {
    if (this.rowsPerPage == null) {
      // Case 1: No pagination = NOP

    } else {
      // Case 2: Pagination.
      this._gotoRow(number - this.rowsPerPage);
    }
  },

  /**
   * Returns Load widget responsible for loading table content from external source,
   * or null if table is off-line.
   */
  getLoad: function() {
    return this.load;
  },

  getLoadErrorMessage: function() {
    return this.loadErrorMessage;
  },

  _setStartRowIndex: function(index) {
    if (this.startRowIndex != index) {
      this.startRowIndex = index;

      // As a result of changing startRowIndex, following things
      // will/may change.
      this.notifyObservers("startRowNumberChanged");
      this.notifyObservers("endRowNumberChanged");
      this.notifyObservers("canNextPageChanged");
      this.notifyObservers("canPreviousPageChanged");
      this.notifyObservers("canNextRowChanged");
      this.notifyObservers("canPreviousRowChanged");
      this.notifyObservers("canFirstPageChanged");
      this.notifyObservers("canLastPageChanged");
    }
  },

  _setEndRowIndex: function(index) {
    if (this.endRowIndex != index) {
      this.endRowIndex = index;

      // As a result of changing endRowIndex, following things
      // will/may change.
      this.notifyObservers("startRowNumberChanged");
      this.notifyObservers("endRowNumberChanged");
      this.notifyObservers("canNextPageChanged");
      this.notifyObservers("canPreviousPageChanged");
      this.notifyObservers("canNextRowChanged");
      this.notifyObservers("canPreviousRowChanged");
      this.notifyObservers("canFirstPageChanged");
      this.notifyObservers("canLastPageChanged");
    }
  },

  _setRowsCount: function(count) {
    if (this.rowsCount != count) {
      this.rowsCount = count;

      // As a result of changing rowsCount, following things
      // will/may change.
      this.notifyObservers("rowsCountChanged");
      this.notifyObservers("canNextPageChanged");
      this.notifyObservers("canPreviousPageChanged");
      this.notifyObservers("canNextRowChanged");
      this.notifyObservers("canPreviousRowChanged");
    }
  },

  /**
   * Precondition: pagination.
   *
   * Goes to the specified row.
   */
  _gotoRow: function(index, forceFullPage) {
    // Validate index range.
    index = this._validateStartRowIndex(index, forceFullPage);

    // Make request for rows, and handle it in "_gotoRowWithRows" method.
    this._requestRows(index, this._gotoRowHandler.bind(this, index));
  },

  _gotoRowHandler: function(index, rows) {
    if (rows.length > 0) {
      this._setStartRowIndex(index);

      this._setEndRowIndex(index + rows.length - 1);

      this._replaceRows(rows);
    }
  },

  /**
   * Precondition: pagination.
   *
   * Validates range for row index, and returns validated value.
   * Additional flag 'forceFullPage' may be used, to guarantee that
   * last page contains full set of rows, if possible.
   */
  _validateStartRowIndex: function(index, forceFullPage) {
    if (this.rowsCount != null) {
      if (index >= this.rowsCount) {
      	index = this.rowsCount - 1;
      }

      if (forceFullPage && index > this.rowsCount - this.rowsPerPage) {
        index = this.rowsCount - this.rowsPerPage;
      }
    }

    if (index < 0) { index = 0; }

    return index;
  },

  /**
   * Preconditions: AJAX
   */
  _load: function() {
    Widget.log("Table", "(Re)loading table rows...");

    // Implementation note: two sets of fields exists which holds
    // query, sort column number and sort order.
    // The first set of fields represents current value for the end-user.
    // The second set of fields is used to perform actual AJAX requests.
    // TODO: Explain why.
    this.loadQuery = this.query;
    this.loadSortColumnNumber = this.sortColumnNumber;
    this.loadSortOrder = this.sortOrder;

    if (this.rowsPerPage == null) {
      // Case 1: No pagination.
      // In that case full list of rows needs to be downloaded,
      // and displayed.
      this._sendClientRequest(null, null, this._replaceRows.bind(this));

    } else {
      // Case 2: Pagination.
      // In that case, pages with rows are buffered/cached in finite-memory buffer.
      // Flush all buffered/cached pages, and goto first row to update the display.
      Widget.log("Table", "Clearing buffer...");

      this.bufferedPageRows = [];
      this.bufferedPageIndices = [];

      this._gotoRow(0);
    }
  },

  _isRowNumberValid: function(number) {
    var rowsCount = this.getRowsCount();

    return (number > 0) && ((rowsCount == null) ? true : number <= rowsCount);
  },

  /**
   * Replaces displayed rows with specified ones.
   */
  _replaceRows: function(rows) {
    // Step 1: Remove old rows
    var body = this.getElement();
    var continueLoop = true;

    while (continueLoop) {
        var lastChild = body.lastChild;

        if (lastChild == null) {
          // No more children to remove, so leave the loop.
          continueLoop = false;

      } else if (lastChild.nodeType == 1 && lastChild.tagName.toLowerCase() == "tr") {
        // Child is a <tr> element, so remove it.
        body.removeChild(lastChild);

      } else if (lastChild.nodeType == 3) {
        // Child is Text node (possibly only whitespaces), so remove it.
        body.removeChild(lastChild);

      } else {
        // Otherwise leave the loop.
        continueLoop = false;
      }
    }

    for (var i = 0; i < rows.length; i++) {
      body.appendChild(rows[i]);
    }
  },

  /**
   * Calculates the endRowIndex field based on startRowIndex field
   * and a number of currently displayed TR elements.
   */
  _calculateRowsCount: function() {
    var count = 0;

    var nodes = this.getElement().childNodes;

    for (var i = 0; i < nodes.length; i++) {
      var node = nodes.item(i);

      if (node.nodeType == 1 && node.tagName.toLowerCase() == "tr") {
        count++;
      }
    }

    return count;
  },

  /**
   * Retrieves array of rows from the page.
   */
  _retrieveRows: function() {
    var rows = [];

    var nodes = this.getElement().childNodes;

    for (var i = 0; i < nodes.length; i++) {
      var node = nodes.item(i);

      if (node.nodeType == 1 && node.tagName.toLowerCase() == "tr") {
        rows.push(node);
      }
    }

    return rows;
  },

  /**
   * Preconditions: pagination.
   *
   * Requests page of rows starting from specified index.
   */
  _requestRows: function(startRowIndex, handler) {
    Widget.log("Table", "Requesting " + this.rowsPerPage + " rows from index " + startRowIndex + "...");

    var endRowIndex =  startRowIndex + this.rowsPerPage - 1;

    if (this.load == null) {
      // Case 1: No AJAX.
      // All rows are stored in 'rows' field.
      // Just slice part of them.
      Widget.log("Table", "No AJAX, so rows are already in place.");

      handler(this.rows.slice(startRowIndex, endRowIndex + 1));

    } else {
      // Case 2: AJAX.
      // Pages are buffered. Request them.
      var startPageIndex = this._getPageIndexFor(startRowIndex);
      var endPageIndex = this._getPageIndexFor(endRowIndex);

      if (startPageIndex == endPageIndex) {
        // Case 2a: Requested rows span over one page.
        // Request that page.
        Widget.log("Table", "Requested rows span 1 page.");

        this._requestPageRows(startPageIndex, handler);

      } else {
        // Case 2b: Requested rows span over two pages.
        // Request first page, request second page,
        // and then slice and concatenate them.
        Widget.log("Table", "Requested rows span 2 pages.");

        this._requestPageRows(startPageIndex,
          this._requestRowsPartialHandler.bind(this, startRowIndex, startPageIndex, handler));
      }
    }
  },

  _requestRowsPartialHandler: function(startRowIndex, startPageIndex, handler, startPageRows) {
    var endRowIndex =  startRowIndex + this.rowsPerPage - 1;

    var endPageIndex = this._getPageIndexFor(endRowIndex);

    this._requestPageRows(endPageIndex, this._requestRowsHandler.bind(this,
      startRowIndex, endPageIndex, handler, startPageRows));
  },

  _requestRowsHandler: function(startRowIndex, endPageIndex, handler, startPageRows, endPageRows) {
    handler(startPageRows.
        slice(startRowIndex % this.rowsPerPage).
        concat(endPageRows.slice(0, startRowIndex % this.rowsPerPage)));
  },

  /**
   * Precondition: AJAX + pagination.
   *
   * Makes request for rows from specified page index.
   */
  _requestPageRows: function(index, handler) {
    Widget.log("Table", "Requesting rows for page " + index + "...");

    var rows = this._getBufferedPageRows(index);

    if (rows !== null && rows !== undefined) {
      // Case 1: Rows are already buffererd.
      Widget.log("Table", "Page " + index + " is already buffered.");

      handler(rows);

    } else {
      // Case 2: Rows need to be requested through AJAX
      Widget.log("Table", "Page " + index + " is not buffered.");

      this._sendClientRequest(index * this.rowsPerPage, this.rowsPerPage,
        this._requestPageRowsHandler.bind(this, index, handler));
    }
  },

  _requestPageRowsHandler: function(index, handler, rows) {
    this._addBufferedPage(index, rows);

    handler(rows);
  },

  /**
   * Sends AJAX request. Invokes handler with rows array and total rows count (if provided).
   */
  _sendClientRequest: function(startRowIndex, rowsCount, handler) {
    // We don't allow sending request while current request is busy.
    if (this.client.isBusy()) { return; }

    this.ajaxRequestHandler = handler;

    // Prepare HTTP query parameters.
    var parameters = {};

    var loadSortColumnNumber = (this.loadSortColumnNumber !== undefined) ?
                                this.loadSortColumnNumber : null;
    var loadSortOrder = (this.loadSortOrder !== undefined) ?
                         this.loadSortOrder : null;

    if (startRowIndex != null) { parameters['mcs-start'] = startRowIndex + 1; }
    if (rowsCount != null) { parameters['mcs-count'] = rowsCount; }
    if (this.loadQuery != null) { parameters['mcs-query'] = this.loadQuery; }
    if (loadSortColumnNumber !== null || loadSortOrder !== null) {
      parameters['mcs-sort-column'] =
        (loadSortColumnNumber !== null ? loadSortColumnNumber : "") +
        (loadSortOrder !== null ? (loadSortOrder == "ascending" ? "A" : "D") : "");
    }

    // Send a request.
    this.client.setURL(this.load.getSrc());

    this.client.setParameters(parameters);

    Widget.log("Table", "Sending AJAX request.");

    this.client.sendRequest();
  },

  /**
   * Invoked on client request success (no user-error)
   */
  _clientRequestSucceededWithoutError: function(response) {
    // Clear the error message.
    this._setLoadErrorMessage(null);

    // Invoke handler.
    this._setRowsCount(response.getTotalRowsCount());

    this.ajaxRequestHandler(response.getRows());
  },

  /**
   * Invoked on client request success (user-error)
   */
  _clientRequestSucceededWithError: function(error) {
    // Set new error message.
    this._setLoadErrorMessage(error.getMessage());
  },

  _clientRequestFailed: function(message) {
    // This is internal error. It's not handled by the widget.
    // In the future all internal errors may be handled externally.
    //alert(message)
  },

  /**
   * Returns page index containing specified row index.
   */
  _getPageIndexFor: function(rowIndex) {
    if (this.rowsPerPage != null) {
      return (rowIndex - (rowIndex % this.rowsPerPage)) / this.rowsPerPage;
    }
  },

  /**
   * Returns buffered array of rows for specified page index,
   * or null if no rows are buffered.
   */
  _getBufferedPageRows: function(index) {
    for (var i = 0; i < this.bufferedPageIndices.length; i++) {
      if (this.bufferedPageIndices[i] == index) {
        return this.bufferedPageRows[i];
      }
    }
  },

  /**
   * Returns true, if rows for specified page index are buffered.
   */
  _isPageBuffered: function(index) {
    for (var i = 0; i < this.bufferedPageIndices.length; i++) {
      if (this.bufferedPageIndices[i] == index) {
        return true;
      }
    }
    return false;
  },

  /**
   * Adds rows to the buffer, for specified page index.
   * Least recently used rows are removed, in case buffer size
   * exceeds its limits.
   */
  _addBufferedPage: function(index, rows) {
    Widget.log("Table", "Adding page " + index + " to buffer");

    if (this.bufferedPageIndices.length == this.maxBufferedPagesCount) {
      Widget.log("Table", "Buffer overflow, page " + this.bufferedPageIndices[0] + " needs to be removed.");

      this.bufferedPageIndices.splice(0, 1);
      this.bufferedPageRows.splice(0, 1);
    }

    this.bufferedPageIndices.push(index);
    this.bufferedPageRows.push(rows);
  },

  _setLoadErrorMessage: function(message) {
    if (this.loadErrorMessage != message) {
      this.loadErrorMessage = message;

      this.notifyObservers("loadErrorMessageChanged");
    }
  }
});

/**
 * Table widget.
 */
Widget.Table = Widget.define(
{
  // No methods.
});
