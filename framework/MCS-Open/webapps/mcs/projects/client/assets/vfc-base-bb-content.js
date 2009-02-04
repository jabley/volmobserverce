
/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * A generic base class for Content widgets.
 */
Widget.Internal.Content = Widget.define(
{
  /**
   * Initialization.
   *
   * The 'id' and 'html' arguments are exclusive.
   *
   * If 'id' is specified, it means that the HTML content
   * of this element is already rendered on the page, inside
   * the parent container. The 'parent' option is required
   * in that case.
   *
   * If 'html' is specified, it means that the content
   * is held as a HTML string. The optional 'script' option
   * contains a script to execute, when the content is attached
   * to the page.
   *
   * A HTML parameter may be a string, or DOM element.
   */
  initializeContent: function(id, html, parent, script, options) {
    this.initializeWidget(id, options)

    this.html = html
    this.parent = parent
    this.script = script

    if (id == null) {
      if (typeof(html) == "string") {
        // Case 1: A html parameter is a HTML string
        var tempElement = document.createElement("div")

        tempElement.innerHTML = html.stripScripts()

        var scripts = html.extractScripts()

        var script = ""

        for (var i = 0; i < scripts.length; i++) {
          script += scripts[i] + ";"
        }

        this.script = script

        this.element = tempElement.firstChild

      } else {
        // Case 2: A html parameter is DOM element.
        this.element = html
      }
    } else {
      this.parent.children.push(this)
    }
  },

  getContainer: function() {
    return this.parent
  },

  runScript: function() {
    if (this.script != null) {
      eval(this.script)
    }
  },

  adding: function() {
    this.notifyObservers("adding")
  },

  added: function() {
    this.runScript()

    this.notifyObservers("added")

    this.notifyObservers("containerChanged")
  },

  removing: function() {
    this.notifyObservers("removing")
  },

  removed: function() {
    this.notifyObservers("removed")
  }
})

/**
 * BlockContent encapsulated any MixedFlow content.
 * It can be placed in a Block widget.
 */
Widget.Internal.BlockContent = Class.define(Widget.Internal.Content,
{
  initialize: function(id, html, parent, script, options) {
    this.initializeContent(id, html, parent, script, options)
  },

  clone: function() {
    return new Widget.Internal.BlockContent(null, this.getElement().cloneNode(true), null, null, this)
  }
})

/**
 * InlineContent encapsulated any MixedText content.
 * It can be placed in a Block and Inline widgets.
 */
Widget.Internal.InlineContent = Class.define(Widget.Internal.Content,
{
  initialize: function(id, html, parent, script, options) {
    this.initializeContent(id, html, parent, script, options)
  },

  clone: function() {
    return new Widget.Internal.InlineContent(null, this.getElement().cloneNode(true), null, null, this)
  }
})

/**
 * PCDATAContent encapsulated a String content.
 * It can be placed in a Block and Inline widgets.
 */
Widget.Internal.PCDATAContent = Class.define(Widget.Internal.Content,
{
  initialize: function(id, string, parent, options) {
    if (id != null) {
      this.initializeContent(id, null, parent, options)
    } else {
      this.initializeContent(null, "<span>" + string.escapeHTML() + "</span>", parent, options)
    }
  },

  clone: function() {
    return new Widget.Internal.PCDATAContent(null, this.getElement().innerHTML.unescapeHTML(), null, this)
  }
})