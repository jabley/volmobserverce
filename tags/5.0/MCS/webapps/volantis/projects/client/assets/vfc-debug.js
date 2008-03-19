
Widget.Log=Class.define(Widget.OptionsContainer,{id:null,topics:null,initialize:function(options){this.installOptions(options)
if(this.id!=null){this.element=$(this.id)}else{this.element=document.createElement('div')
this.element.style.overflow='auto'
this.element.style.position='fixed'
this.element.style.height='20%'
this.element.style.width='100%'
this.element.style.bottom='0%'
this.element.style.left='0%'
this.element.style.borderTopWidth='2px'
this.element.style.borderTopStyle='solid'
this.element.style.borderTopColor='black'
this.element.style.backgroundColor='#EEEEFF'
document.body.appendChild(this.element)}
this.element.style.zIndex=10
Widget.addElementObserver(this.element,Widget.CLICK,this.clear.bindAsEventListener(this))
this.backgroundColors=["#BBBBFF","#DDDDFF"]
this.backgroundColorIndex=0
this.defaultTopic="Default"
if(this.topics!=null){this.topics=this.topics.split(",")}
Widget.currentLog=this},write:function(){if(arguments.length==0){}else if(arguments.length==1){this.writeContent(arguments[0])}else{this.writeTopicAndContent(arguments[0],arguments[1])}},writeTopicAndContent:function(topic,content){if(!this.includesTopic(topic)){return}
var scroll=(this.element.scrollTop==this.element.scrollHeight-this.element.clientHeight)
messageElement=document.createElement("div")
messageElement.style.backgroundColor=this.getBackgroundColor()
messageElement.innerHTML="<span style='font-weight:bold; font-style:italic'>"+topic+": </span>"+content
this.element.appendChild(messageElement)
if(scroll){this.element.scrollTop=this.element.scrollHeight-this.element.clientHeight}
this.switchBackgroundColor()},writeContent:function(content){this.writeTopicAndContent(this.defaultTopic,content)},clear:function(){this.element.innerHTML=""},switchBackgroundColor:function(){this.backgroundColorIndex++
if(this.backgroundColorIndex>=this.backgroundColors.length){this.backgroundColorIndex=0}},getBackgroundColor:function(){return this.backgroundColors[this.backgroundColorIndex]},includesTopic:function(topic){if(this.topics==null){return true}else{for(var i=0;i<this.topics.length;i++){if(this.topics[i]==topic){return true}}}
return false}})
Widget.log=function(){if(Widget.currentLog!=null){Widget.currentLog.write.apply(Widget.currentLog,arguments)}}
Widget.isLogEnabled=function(){return true}