
/**
 * (c) Volantis Systems Ltd 2008. 
 */


/**
 * A generic base class for Block and Inline widgets.
 */
Widget.Internal.Container = Widget.define(
{
  initializeContainer: function(id, options) {
    this.initializeWidget(id, options)

    this.children = []

    // Set initial content, of specified.
    if (this.getOption("content") != null) {
      this.setContent(this.getOption("content"))
    }
  },

  getAt: function(index) {
    if (index >= 1 && index <= this.children.length) {
      return this.children[index - 1]
    }
  },

  addLast: function(content) {
    this.addAt(content, this.children.length + 1)
  },

  addFirst: function(content) {
    this.addAt(content, 1)
  },

  addAt: function(content, index) {
    if (index < 1 || index > this.children.length + 1) {
      return
    }

    // If widget is already attached, do nothing.
    if (content.parent != null) {
      return
    }

    // Inform content, that it's going to be attached.
    content.adding()

    // Attach the widget.
    this.children.splice(index - 1, 0, content)

    content.parent = this

    // Attach the markup.
    var parentElement = this.getElement()

    var addedElement = content.getElement()

    if (index < this.children.length) {
      var existingElement = this.children[index].getElement()

      parentElement.insertBefore(addedElement, existingElement)
    } else {
      parentElement.appendChild(addedElement)
    }

    this.notifyObservers("sizeChanged")

    // Inform content, that it has been attached.
    content.added()
  },

  remove: function(content) {
    for (var i = 0; i < this.children.length; i++) {
      if (this.children[i] === content) {
        this.removeAt(i + 1)

        return
      }
    }
  },

  removeFirst: function() {
    this.removeAt(1)
  },

  removeLast: function() {
    this.removeAt(this.children.length)
  },

  removeAt: function(index) {
    // If index is out of bound, return immediately
    if (index < 1 || index > this.children.length) {
      return
    }

    var content = this.children[index - 1]

    // If widget is already detached or is not a child of
    // this widget, do nothing.
    if (content.parent !== this) {
      return
    }

    // Inform content, that it's goind to be detached.
    content.removing()

    // Detach the widget.
    this.children.splice(index - 1, 1)

    content.parent = null

    // Detach the markup.
    var parentElement = this.getElement()

    var childElement = content.getElement()

    parentElement.removeChild(childElement)

    this.notifyObservers("sizeChanged")

    // Inform content, that it has been detached.
    content.removed()
  },

  getSize: function() {
    return this.children.length
  },

  clearContainer: function() {
    this.getElement().innerHTML = "";
  },

  setContent: function(content) {
    if (content == null || content.parent == null) {

      this.clearContainer();

      while (this.getSize() > 0) {
        this.removeLast()
      }

      if (content != null) {
        this.addLast(content)
      }

      this.content = content

      this.notifyObservers("contentChanged")
    }
  },

  getContent: function() {
    return this.content
  }
})

/**
 * Block Container widget displays a live list of
 * BlockContent, InlineContent and PCDATAContent widgets.
 * The list of Content widgets may be updated live.
 */
Widget.Internal.BlockContainer = Class.define(Widget.Internal.Container,
{
  initialize: function(id, options) {
    this.initializeContainer(id, options)

    if (this.getOption("content") != null) {
      this.setContent(this.getOption("content"))
    }
  }
})

/**
 * Inline Container widget displays a live list of
 * InlineContent and PCDATAContent widgets.
 * The list of Content widgets may be updated live.
 */
Widget.Internal.InlineContainer = Class.define(Widget.Internal.Container,
{
  initialize: function(id, options) {
    this.initializeContainer(id, options)

    if (this.getOption("content") != null) {
      this.setContent(this.getOption("content"))
    }
  }
})

