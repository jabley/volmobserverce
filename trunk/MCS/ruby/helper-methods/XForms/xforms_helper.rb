require 'cgi'
require File.dirname(__FILE__) + '/date_helper'
require File.dirname(__FILE__) + '/tag_helper'

module ActionView 
  module Helpers
    # Provides a set of methods for working with XDIME 2 forms
    module XformsHelper
        # Sets the namespace prefix for the XHTML2 namespace.
        def set_xhtml2_prefix (prefix)
            @request.env[:xhtml2prefix] = prefix
        end
        
        # Sets the namespace prefix for the XForms namespace.
        def set_xf_prefix (prefix)
            @request.env[:xfprefix] = prefix
        end
        
        # Sets the namespace prefix for the SI namespace.
        def set_si_prefix (prefix)
            @request.env[:siprefix] = prefix
        end
        
        # Namespace definition for the XHTML2 namespace.
        def xmlns_xhtml2
            "xmlns#{xhtml2prefix false}=\"http://www.w3.org/2002/06/xhtml2\""
        end
        
        # Namespace definition for the XForms namespace.
        def xmlns_xf
            "xmlns#{xfprefix false}=\"http://www.w3.org/2002/xforms\""
        end
        
        # Namespace definition for the SI namespace.
        def xmlns_si
            "xmlns#{siprefix false}=\"http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si\""
        end
        
        # Model definition for the collected model items.
        # 
        # The model definition should appear in the head element. Use this method in the layout so the XForms elements used
        # in the rhtml file are evaluated first.
        def xf_model
            result = ""
            models = @request.env[:models]
            unless models.nil?
                models.each{|name, model|
                    result += model_start name
                    result += model.submission
                    result += model_end model
                }
            end
            styles = @request.env[:styles]
            unless styles.nil?
                result += "<style type=\"text/css\">" + styles + "</style>"
            end
            return result
        end
        
        # Creates "submit" element and registers the submission element in the model.
        # 
        # The first parameter defines a label (defaults to "Submit") for the submit control.
        # The second parameter is a Hash for the action to be executed for the submit event. This takes the same options 
        # as url_for. For a list, see the documentation for ActionController::Base#url_for.
        # Third parameter is the submission id of the control (defaults to "submit")
        # Fourth parameter is a Hash containing the extra attributes added to the control element.
        def xf_submit (label = "Submit", target_options = {}, id = "submit", attrs = {})
            attrs = attrs.symbolize_keys()
            attrs[:submission] = id
            labelAttrs = attrs.delete(:labelAttrs)
            label = label(label, labelAttrs)
            method = attrs.delete(:method)
            model = model(attrs[:model])
            model.submission = submission(method, url_for(target_options), {:id => id})
            typeWithAttrs("submit", label, attrs)
        end
        
        # Renders a general select element tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # Additional options on the select tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_select("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}])
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-rows: 2;}</style> in xf_model
        #     <xf:select ref="user[name]" id="user_name"><xf:label/><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select>
        #     
        #   xf_select("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name")
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-rows: 2;}</style> in xf_model
        #     <xf:select ref="user[name]" id="user_name"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select>
        #     
        #   xf_select("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name", :rows => 3, :id => "username", :model => "user_model")
        #     <xf:model id="user_model"><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#username {mcs-rows: 3;}</style> in xf_model
        #     <xf:select model="user_model" ref="user[name]" id="username"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select>
        def xf_select(object_name, method, items = [], label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            rows = attrs.delete(:rows)
            result = select("select", object_name, method, label, items, attrs)
            if rows.nil?
                rows = items.length > 5 ? 5 : items.length
            end
            addStyle(attrs[:id], "mcs-rows", rows.to_s)
            return result
        end
        
        # Renders a select element with checkbox style information tailored for accessing a specified attribute
        # (identified by method) on an object assigned to the template (identified by object_name).
        # Additional options on the select tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_check_box("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}])
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-selection-list-style: controls;}</style> in xf_model
        #     <xf:select ref="user[name]" id="user_name"><xf:label/><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select>
        #     
        #   xf_check_box("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name")
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-selection-list-style: controls;}</style> in xf_model
        #     <xf:select ref="user[name]" id="user_name"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select>
        #     
        #   xf_check_box("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name", :id => "username", :model => "user_model")
        #     <xf:model id="user_model"><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#username {mcs-selection-list-style: controls;}</style> in xf_model
        #     <xf:select model="user_model" ref="user[name]" id="username"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select>
        def xf_check_box(object_name, method, items = [], label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            result = select("select", object_name, method, label, items, attrs)
            addStyle(attrs[:id], "mcs-selection-list-style", "controls")
            return result
        end
        
        # Renders a general select1 element tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # Additional options on the select1 tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_select1("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}])
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:select1 ref="user[name]" id="user_name"><xf:label/><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select1>
        #     
        #   xf_select1("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name")
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:select1 ref="user[name]" id="user_name"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select1>
        #     
        #   xf_select1("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name", :rows => 3, :id => "username", :model => "user_model")
        #     <xf:model id="user_model"><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:select1 model="user_model" ref="user[name]" id="username"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select1>
        def xf_select1(object_name, method, items = [], label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            select("select1", object_name, method, label, items, attrs)
        end
        
        # Renders a select1 element with radio button style tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # Additional options on the select1 tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_select1("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}])
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-selection-list-style: controls;}</style> in xf_model
        #     <xf:select1 ref="user[name]" id="user_name"><xf:label/><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select1>
        #     
        #   xf_select1("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name")
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-selection-list-style: controls;}</style> in xf_model
        #     <xf:select1 ref="user[name]" id="user_name"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select1>
        #     
        #   xf_select1("user", "name", [{:label=>"Albert", :value=>"a"}, {:label=>"Bob", :value=>"b"}], "User name", :rows => 3, :id => "username", :model => "user_model")
        #     <xf:model id="user_model"><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model><style type="text/css">*|*#user_name {mcs-selection-list-style: controls;}</style> in xf_model
        #     <xf:select1 model="user_model" ref="user[name]" id="username"><xf:label>User name</xf:label><xf:item><xf:label>Albert</xf:label><xf:value>a</xf:value></xf:item><xf:item><xf:label>Bob</xf:label><xf:value>b</xf:value></xf:item></xf:select1>
        def xf_radio_button(object_name, method, items = [], label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            result = select("select1", object_name, method, label, items, attrs)
            addStyle(attrs[:id], "mcs-selection-list-style", "controls")
            return result
        end
        
        # Returns an input tag tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # Additional options on the input tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_text_field("user", "name")
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:input ref="user[name]" id="user_name"><xf:label/></xf:input>
        #
        #   <%= xf_text_field "user", "name", "Name"%>  
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:input ref="user[name]" id="user_name"><xf:label>Name</xf:label></xf:input>
        #     
        #   <%= xf_text_field "user", "name", "Name", :ref => "username" %>
        #     <xf:model><xf:instance><si:instance><si:item name="username"></si:item></si:instance></xf:instance></xf:model>
        #     <xf:input ref="username" id="user_name"><xf:label>Name</xf:label></xf:input>
        #     
        #   <%= xf_text_field "user", "name", "Name", :ref => "username", :id => "user_name_id" %>
        #     <xf:model><xf:instance><si:instance><si:item name="username"></si:item></si:instance></xf:instance></xf:model>
        #     <xf:input ref="username" id="user_name_id"><xf:label>Name</xf:label></xf:input>
        def xf_text_field (object_name, method, label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            text ("input", object_name, method, label, attrs)
        end
        
        # Returns a textarea tag tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # Additional options on the textarea tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_text_area("user", "name")
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:textarea ref="user[name]" id="user_name"><xf:label/></xf:textarea>
        #
        #   xf_text_area "user", "name", "Name"
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:textarea ref="user[name]" id="user_name"><xf:label>Name</xf:label></xf:textarea>
        #     
        #   xf_text_area "user", "name", "Name", :ref => "username"
        #     <xf:model><xf:instance><si:instance><si:item name="username"></si:item></si:instance></xf:instance></xf:model>
        #     <xf:textarea ref="username" id="user_name"><xf:label>Name</xf:label></xf:textarea>
        #     
        #   xf_text_area "user", "name", "Name", :ref => "username", :id => "user_name_id"
        #     <xf:model><xf:instance><si:instance><si:item name="username"></si:item></si:instance></xf:instance></xf:model>
        #     <xf:textarea ref="username" id="user_name_id"><xf:label>Name</xf:label></xf:textarea>
        def xf_text_area (object_name, method, label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            text ("textarea", object_name, method, label, attrs)
        end
        
        # Returns a secret tag tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # Additional options on the secret tag can be passed as a hash with attrs.
        # 
        # Examples (call, result):
        #   xf_password_field "user", "name"
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:secret ref="user[name]" id="user_name"><xf:label/></xf:secret>
        #
        #   xf_password_field "user", "name", "Name"
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        #     <xf:secret ref="user[name]" id="user_name"><xf:label>Name</xf:label></xf:secret>
        #     
        #   xf_password_field "user", "name", "Name", :ref => "username"
        #     <xf:model><xf:instance><si:instance><si:item name="username"></si:item></si:instance></xf:instance></xf:model>
        #     <xf:secret ref="username" id="user_name"><xf:label>Name</xf:label></xf:secret>
        #     
        #   xf_password_field "user", "name", "Name", :ref => "username", :id => "user_name_id"
        #     <xf:model><xf:instance><si:instance><si:item name="username"></si:item></si:instance></xf:instance></xf:model>
        #     <xf:secret ref="username" id="user_name_id"><xf:label>Name</xf:label></xf:secret>
        def xf_password_field (object_name, method, label = nil, attrs = {})
            attrs = attrs.symbolize_keys()
            text ("secret", object_name, method, label, attrs)
        end
        
        # Adds an item to the model tailored for accessing a specified attribute (identified by method)
        # on an object assigned to the template (identified by object_name).
        # 
        # Examples (call, result):
        #   xf_hidden_field "user", "name"
        #     <xf:model><xf:instance><si:instance><si:item name="user[name]"></si:item></si:instance></xf:instance></xf:model> in xf_model
        def xf_hidden_field (object_name, method)
            addRefAttribute(object_name, method, {})
            addSiItem(object_name, method, {})
            return ""
        end
        
        # Returns an object tag with the provided src used as src attribute.
        # Additional options on the image tag can be passed as a hash with attrs.
        # 
        # width and height attributes are transformed to the appropriate MCS parameters.
        # 
        # caption attribute is transformed to a <caption> nested element.
        # 
        # Examples (call, result):
        #   xf_image_tag "http://www.example.com/image.jpg"
        #     <object src="http://www.example.com/image.jpg"></object>
        #
        #   xf_image_tag "http://www.example.com/image.jpg", :caption => "This is the caption"
        #     <object src="http://www.example.com/image.jpg"><caption>This is the caption</caption></object>
        #     
        #   xf_image_tag "http://www.example.com/image.jpg", :caption => "This is the caption", :width => "640", :height => "480"
        #     <object src="http://www.example.com/image.jpg">
        #       <caption>This is the caption</caption>
        #       <param value="640" name="mcs-aspect-ratio-width"/>
        #       <param value="480" name="mcs-aspect-ratio-height"/>
        #     </object>
        #     
        #   xf_image_tag "http://www.example.com/image.jpg", :caption => "This is the caption", :width => "640", :height => "480", :id => "image"
        #     <object src="http://www.example.com/image.jpg" id="image">
        #       <caption>This is the caption</caption>
        #       <param value="640" name="mcs-aspect-ratio-width"/>
        #       <param value="480" name="mcs-aspect-ratio-height"/>
        #     </object>
        def xf_image_tag (src, attrs = {})
            attrs = attrs.symbolize_keys()
            content = ""
            caption = attrs.delete(:caption)
            unless caption.nil?
                content += typeWithAttrs("caption", caption, nil, xhtml2prefix)
            end
            width = attrs.delete(:width)
            unless width.nil?
                content += typeWithAttrs("param", nil, {:name => "mcs-aspect-ratio-width", :value => width}, xhtml2prefix)
            end
            height = attrs.delete(:height)
            unless height.nil?
                content += typeWithAttrs("param", nil, {:name => "mcs-aspect-ratio-height", :value => height}, xhtml2prefix)
            end
            attrs[:src] = src
            typeWithAttrs("object", content, attrs, xhtml2prefix)
        end
        
        private
        
        def xhtml2prefix beforeColon = true
            return render_prefix :xhtml2prefix, "", beforeColon
        end
        
        def xfprefix beforeColon = true
            return render_prefix :xfprefix, "xf", beforeColon
        end
        
        def siprefix beforeColon = true
            return render_prefix :siprefix, "si", beforeColon
        end
        
        def render_prefix (name, defaultPrefix, beforeColon)
            if @request.env[name].nil?
                @request.env[name] = defaultPrefix
            end
            prefix = @request.env[name]
            if prefix.length > 0
                if beforeColon
                    prefix += ":"
                else
                    prefix = ":" + prefix
                end
                
            end
            return prefix
        end
        
        def model_start (modelName)
            if modelName.length == 0
                "<#{xfprefix}model>"
            else 
                "<#{xfprefix}model id=\"#{modelName}\">"
            end
        end
        
        def submission(method, url, attrs)
            if method.nil?
                method = "POST"
            end
            attrs.update({:method => method, :action => url})
            typeWithAttrs("submission", nil, attrs)
        end
    
        def model_end (model)
            result = ""
            unless model.siItems.empty?
                result += "<#{xfprefix}instance>"
                result += "<#{siprefix}instance>"
                model.siItems.each{|item| result += "<#{siprefix}item name=\"#{item[0]}\">#{item[1]}</#{siprefix}item>"}
                result += "</#{siprefix}instance>"
                result += "</#{xfprefix}instance>"
            end
            result += "</#{xfprefix}model>"
        end
        
        def label (label, attrs = {})
            typeWithAttrs("label", label, attrs)
        end
        
        def xhtmlAttributes(attrs)
            result = ""
            unless attrs.nil?
                attrs.each{|name, value| result += " #{name}=\"#{value}\""}
            end
            return result
        end
        
        def typeWithAttrs (type, content, attrs, prefix = nil)
            if prefix.nil?
                prefix = xfprefix
            end
            if content.nil?
                result = "<#{prefix}#{type}#{xhtmlAttributes(attrs)}/>"
            else
                result = "<#{prefix}#{type}#{xhtmlAttributes(attrs)}>#{content}</#{prefix}#{type}>"
            end
            return result
        end
        
        def labeledTypeWithAttrs (type, label, attrs)
            attrs = attrs.symbolize_keys()
            labelAttrs = attrs.delete(:labelAttrs)
            label = label(label, labelAttrs)
            typeWithAttrs(type, label, attrs)
        end
        
        def select (type, object_name, method, label, items, attrs = {})
            labelAttrs = attrs.delete(:labelAttrs)
            addRefAttribute(object_name, method, attrs)
            addSiItem(object_name, method, attrs)
            content = label(label, labelAttrs)
            items.each {|item|
                item = item.symbolize_keys()
                content += "<#{xfprefix}item>"
                content += label(item[:label], item[:labelAttrs])
                content += typeWithAttrs("value", item[:value], item[:valueAttrs])
                content += "</#{xfprefix}item>"
            }
            typeWithAttrs(type, content, attrs)
        end

        def text (type, object_name, method, label = nil, attrs = {})
            rows = attrs.delete(:rows)
            columns = attrs.delete(:columns)
            addRefAttribute(object_name, method, attrs)
            addSiItem(object_name, method, attrs)
            # at this point we have the id attribute
            unless rows.nil?
                addStyle(attrs[:id], "mcs-rows", rows.to_s)
            end
            unless columns.nil?
                addStyle(attrs[:id], "mcs-columns", columns.to_s)
            end
            labeledTypeWithAttrs(type, label, attrs)
        end
        
        def addRefAttribute (object_name, method, attrs)
            object_name = object_name.to_s
            method = method.to_s
            if attrs[:ref].nil?
                ref = object_name + "[" + method + "]"
                attrs[:ref] = ref
            end
            if attrs[:id].nil?
                id = attrs[:ref].tr("[]()", "_").squeeze("_").chomp("_")
                attrs[:id] = id
            end
        end
        
        def addSiItem (object_name, method, attrs = {})
            id = attrs[:ref]
            value = attrs.delete(:initialValue)
            if value.nil?
                object_name = object_name.to_s
                method = method.to_s
                object = self.instance_variable_get("@#{object_name}")
                unless object.nil?
                    value = object.respond_to?(method + "_before_type_cast") ?
                        object.send(method + "_before_type_cast") :
                        object.send(method)
                end
            end
            model = model(attrs[:model])
            model.addSiItem(id, value)
        end
        
        def model (modelName)
            if modelName.nil?
                modelName = ""
            end
            modelName = modelName.to_s
            models = @request.env[:models]
            if models.nil?
                models = {}
                @request.env[:models] = models
            end
            model = models[modelName]
            if model.nil?
                model = Model.new
                models[modelName] = model
            end
            return model
        end
        
        def addStyle (id, name, value)
            styles = @request.env[:styles]
            if styles.nil?
                styles = ""
            end
            styles += "*|*#" + id + " {#{name}: #{value};}\n"
            @request.env[:styles] = styles
        end
    end
    
    class Model
        def initialize
            @siitems = []
            @submission = ""
        end
        
        def addSiItem (id, value)
            @siitems += [[id, value]]
        end
        
        def siItems
            @siitems
        end
        
        def submission= (submission)
            @submission = submission
        end
        
        def submission
            return @submission
        end
    end
  end # End Helper
end # End ActionView
