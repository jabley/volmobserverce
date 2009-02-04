/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * A Script widget
 */
Widget.Script = Widget.define(
{
  initialize: function(content, options) {
    this.initializeWidget(null, options)

    eval("this.func = function(){" + content + "}")

    this.addAction("invoke")
  },

  invoke: function() {
    this.func.apply(this, arguments)
  }
})
