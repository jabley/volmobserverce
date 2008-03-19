
Widget.Corners=Class.create();Object.extend(Widget.Corners.prototype,{initialize:function(id,cornersOpt){this.id=id;this.el=$(id);this.cornersOpt=cornersOpt;this.antiAliasing=false;this.topLabel=[];this.bottomLabel=[];this.textContainer=null;this.resizeY=0;this.setup();this.run();},setup:function(){this.fc='#FFFFFF';this.fc=this.el.getStyle('background-color')||'#FFFFFF';if(this.fc!='transparent'){this.fc=this.fc.parseColor();}
this.bc='#FFFFFF';this.bc=Element.getStyle(this.el.parentNode,'background-color');if(this.bc!='transparent'){this.bc=this.bc.parseColor();}
var pos=this.el.getStyle('position');if(pos=='absolute'||pos=='fixed'){this.bc='transparent';}
this.el.style.backgroundColor=this.bc;if(Prototype.msieBrowser()){this.el.style.background=this.bc;}
this.contentWidth=this.el.vfcGetRealDimensions().contentWidth;this.elDims=this.el.vfcGetRealDimensions();var div=document.createElement("div");var divInner=document.createElement("div");if(!Prototype.msieBrowser()){divInner.style.border='1px solid '+this.fc;divInner.style.backgroundColor='inherit';}
if(!Prototype.operaMobile()){div.style.width=this.elDims.contentWidth+this.elDims.borderLeftWidth+this.elDims.borderRightWidth+'px';}else{div.style.width=this.elDims.contentWidth+this.elDims.borderLeftWidth+this.elDims.borderRightWidth+this.elDims.paddingLeft+this.elDims.paddingRight+'px';}
div.style.overflow='hidden';div.style.height='100%';div.style.backgroundColor=this.fc;if(Prototype.msieBrowser()){div.style.background=this.fc;}
if(this.elDims.paddingLeft){div.style.paddingLeft=this.elDims.paddingLeft+'px';this.el.style.paddingLeft='0px';}
if(this.elDims.paddingRight){div.style.paddingRight=this.elDims.paddingRight+'px';this.el.style.paddingRight='0px';}
if(this.elDims.paddingTop){div.style.paddingTop=this.elDims.paddingTop+'px';this.el.style.paddingTop='0px';}
if(this.elDims.paddingBottom){div.style.paddingBottom=this.elDims.paddingBottom+'px';this.el.style.paddingBottom='0px';}
this.el.appendChild(div);div.appendChild(divInner);Element.cleanWhitespace(this.el);for(var i=0;i<this.el.childNodes.length;i++){if(div!=this.el.childNodes[i]){divInner.appendChild(this.el.childNodes[i]);}}
this.textContainer=div;this.setBordersSiteWidth();if(!Prototype.msieBrowser()){if(!(this.cornersOpt[2].length||this.cornersOpt[3].length)){div.style.borderBottom=this.elDims.borderBottomWidth+' '+Element.getStyle(this.el,'border-bottom-color')+' solid';this.el.style.borderBottom='none';}
if(!(this.cornersOpt[0].length||this.cornersOpt[1].length)){div.style.borderTop=this.elDims.borderTopWidth+' '+Element.getStyle(this.el,'border-top-color')+' solid';this.el.style.borderTop='none';}
div.style.height=this.elDims.contentHeight+'px'}},run:function(){if(this.cornersOpt[0].length||this.cornersOpt[1].length){this.addTopMarkup();}
if(this.cornersOpt[2].length||this.cornersOpt[3].length){this.addBottomMarkup();}
if(!Prototype.msieBrowser()){this.el.style.height=this.elDims.contentHeight
+this.elDims.borderTopWidth
+this.elDims.paddingTop
+this.elDims.paddingBottom
+this.elDims.borderBottomWidth
+this.resizeY
+'px';}},addTopMarkup:function(){var lX=Widget.getPXDimension(this.cornersOpt[0][0])||0;var lY=Widget.getPXDimension(this.cornersOpt[0][1])||0;var rX=Widget.getPXDimension(this.cornersOpt[1][0])||0;var rY=Widget.getPXDimension(this.cornersOpt[1][1])||0;var maxY=Math.max(lY,rY);if(maxY>0){this.resizeY+=maxY;}
var d=document.createElement("div");d.style.backgroundColor=this.bc;if(Prototype.msieBrowser()){d.style.background=this.bc;}
d.style.width=this.elDims.contentWidth+2*this.elDims.borderLeftWidth+2*this.elDims.borderRightWidth+this.elDims.paddingLeft+this.elDims.paddingRight+'px';d.className="rounded";var lastlArc=0;var lastrArc=0;var borderTopStyle=Element.getStyle(this.el,'border-top-style');var borderTopWidth=Element.getStyle(this.el,'border-top-width')||0;var borderTopColor=Element.getStyle(this.el,'border-top-color');this.borderPxValue=parseFloat(borderTopWidth)||0;if(borderTopStyle=='none'){this.borderPxValue=0;}
for(var i=1;i<=maxY;i++){var coverage,arc2,arc3;lArc=Math.sqrt(1.0-Math.sqr(1.0-i/lY))*lX;rArc=Math.sqrt(1.0-Math.sqr(1.0-i/rY))*rX;var l_bg=lX-Math.ceil(lArc);var l_fg=Math.floor(lastlArc);var l_aa=lX-l_bg-l_fg;var r_bg=rX-Math.ceil(rArc);var r_fg=Math.floor(lastrArc);var r_aa=rX-r_bg-r_fg;var x=document.createElement("div");x.style.height='1px';x.style.overflow='hidden';x.style.fontSize='0px';var y=d;if(i<lY){x.style.marginLeft=l_bg+"px";}
if(i<rY){x.style.marginRight=r_bg+"px";}
if(i<=this.borderPxValue&&this.borderPxValue){x.style.backgroundColor=borderTopColor;this.el.style.borderTopWidth='0px';}
if(this.borderLeft!==0){if(i<=lY){x.style.borderLeftWidth=l_aa+((this.borderLeft-1))+"px";}else{x.style.borderLeftWidth=this.borderLeftWidth;}
x.style.borderLeftStyle=this.borderLeftStyle;x.style.borderLeftColor=this.borderLeftColor;}
if(this.borderRight!==0){if(i<=rY){x.style.borderRightWidth=r_aa+((this.borderRight-1))+"px";}else{x.style.borderRightWidth=this.borderRightWidth;}
x.style.borderRightStyle=this.borderRightStyle;x.style.borderRightColor=this.borderRightColor;}
if(i>this.borderPxValue){x.style.backgroundColor=this.fc;if(Prototype.msieBrowser()){x.style.background=this.fc;}}
y.appendChild(x);lastlArc=lArc;lastrArc=rArc;}
if(this.borderLeft!==0){this.el.style.borderLeftWidth='0px';this.textContainer.style.borderLeftWidth=this.borderLeftWidth+'px';this.textContainer.style.borderLeftStyle=this.borderLeftStyle;this.textContainer.style.borderLeftColor=this.borderLeftColor;this.isLeftBorderSet=true;}
if(this.borderRight!==0){this.el.style.borderRightWidth='0px';this.textContainer.style.borderRightWidth=this.borderRightWidth+'px';this.textContainer.style.borderRightStyle=this.borderRightStyle;this.textContainer.style.borderRightColor=this.borderRightColor;this.isRightBorderSet=true;}
this.el.insertBefore(d,this.el.firstChild);},addBottomMarkup:function(){var rX=Widget.getPXDimension(this.cornersOpt[2][0])||0;var rY=Widget.getPXDimension(this.cornersOpt[2][1])||0;var lX=Widget.getPXDimension(this.cornersOpt[3][0])||0;var lY=Widget.getPXDimension(this.cornersOpt[3][1])||0;var maxY=Math.max(lY,rY);if(maxY>0){this.resizeY+=maxY;}
var d=document.createElement("div");d.style.backgroundColor=this.bc;if(Prototype.msieBrowser()){d.style.background=this.bc;}
d.style.width=this.elDims.contentWidth+2*this.elDims.borderLeftWidth+2*this.elDims.borderRightWidth+this.elDims.paddingLeft+this.elDims.paddingRight+'px';d.className="rounded";var lastlArc=0;var lastrArc=0;var borderBottomStyle=Element.getStyle(this.el,'border-bottom-style');var borderBottomWidth=Element.getStyle(this.el,'border-bottom-width')||0;var borderBottomColor=Element.getStyle(this.el,'border-bottom-color');this.borderPxValue=parseFloat(borderBottomWidth)||0;for(var i=1;i<=maxY;i++){var coverage,arc2,arc3;lArc=Math.sqrt(1.0-Math.sqr(1.0-i/lY))*lX;rArc=Math.sqrt(1.0-Math.sqr(1.0-i/rY))*rX;var l_bg=lX-Math.ceil(lArc);var l_fg=Math.floor(lastlArc);var l_aa=lX-l_bg-l_fg;var r_bg=rX-Math.ceil(rArc);var r_fg=Math.floor(lastrArc);var r_aa=rX-r_bg-r_fg;var x=document.createElement("div");x.style.height='1px';x.style.overflow='hidden';x.style.fontSize='0px';var y=d;if(i<lY){x.style.marginLeft=l_bg+"px";}
if(i<rY){x.style.marginRight=r_bg+"px";}
if(i<=this.borderPxValue&&this.borderPxValue){x.style.backgroundColor=borderBottomColor;this.el.style.borderBottomWidth='0px';}
if(this.borderLeft!==0){if(i<=lY){x.style.borderLeftWidth=l_aa+((this.borderLeft-1))+"px";}else{x.style.borderLeftWidth=this.borderLeftWidth;}
x.style.borderLeftStyle=this.borderLeftStyle;x.style.borderLeftColor=this.borderLeftColor;}
if(this.borderRight!==0){if(i<=rY){x.style.borderRightWidth=r_aa+((this.borderRight-1))+"px";}else{x.style.borderRightWidth=this.borderRightWidth;}
x.style.borderRightStyle=this.borderRightStyle;x.style.borderRightColor=this.borderRightColor;}
if(i>this.borderPxValue){x.style.backgroundColor=this.fc;if(Prototype.msieBrowser()){x.style.background=this.fc;}}
y.insertBefore(x,y.firstChild);lastlArc=lArc;lastrArc=rArc;}
if(this.borderLeft!==0&&!this.isLeftBorderSet){this.el.style.borderLeftWidth='0px';this.textContainer.style.borderLeftWidth=this.borderLeftWidth+'px';this.textContainer.style.borderLeftStyle=this.borderLeftStyle;this.textContainer.style.borderLeftColor=this.borderLeftColor;}
if(this.borderRight!==0&&!this.isRightBorderSet){this.el.style.borderRightWidth='0px';this.textContainer.style.borderRightWidth=this.borderRightWidth+'px';this.textContainer.style.borderRightStyle=this.borderRightStyle;this.textContainer.style.borderRightColor=this.borderRightColor;}
this.el.appendChild(d);},setBordersSiteWidth:function(){this.borderLeftStyle='solid';this.borderLeftWidth=this.elDims.borderLeftWidth;this.borderLeftColor=Element.getStyle(this.el,'border-left-color');this.borderRightStyle='solid';this.borderRightWidth=this.elDims.borderRightWidth;this.borderRightColor=Element.getStyle(this.el,'border-right-color');this.borderLeft=this.borderLeftWidth;this.borderRight=this.borderRightWidth;}});Math.sqr=function(x){return x*x;};