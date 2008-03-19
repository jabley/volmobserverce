
module ActionView 
  module Helpers
    # The XForms Date Helper primarily creates select/option tags for different kinds of dates and date elements. All of the select-type methods
    # share a number of common options that are as follows:
    #
    # * <tt>:prefix</tt> - overwrites the default prefix of "date" used for the select names. So specifying "birthday" would give
    #   birthday[month] instead of date[month] if passed to the select_month method.
    # * <tt>:include_blank</tt> - set to true if it should be possible to set an empty date.
    # * <tt>:discard_type</tt> - set to true if you want to discard the type part of the select name. If set to true, the select_month
    #   method would use simply "date" (which can be overwritten using <tt>:prefix</tt>) instead of "date[month]".
    module XformsDateHelper
      DEFAULT_PREFIX = 'date' unless const_defined?('DEFAULT_PREFIX')

      # Returns a set of select tags (one for year, month, and day) pre-selected for accessing a specified date-based attribute (identified by
      # +method+) on an object assigned to the template (identified by +object+). It's possible to tailor the selects through the +options+ hash,
      # which accepts all the keys that each of the individual select builders do (like :use_month_numbers for select_month) as well as a range of
      # discard options. The discard options are <tt>:discard_year</tt>, <tt>:discard_month</tt> and <tt>:discard_day</tt>. Set to true, they'll
      # drop the respective select. Discarding the month select will also automatically discard the day select. It's also possible to explicitly
      # set the order of the tags using the <tt>:order</tt> option with an array of symbols <tt>:year</tt>, <tt>:month</tt> and <tt>:day</tt> in
      # the desired order. Symbols may be omitted and the respective select is not included.
      #
      # Passing :disabled => true as part of the +options+ will make elements inaccessible for change.
      #
      # NOTE: Discarded selects will default to 1. So if no month select is available, January will be assumed.
      #
      # Examples:
      #
      #   date_select("post", "written_on")
      #   date_select("post", "written_on", :start_year => 1995)
      #   date_select("post", "written_on", :start_year => 1995, :use_month_numbers => true,
      #                                     :discard_day => true, :include_blank => true)
      #   date_select("post", "written_on", :order => [:day, :month, :year])
      #   date_select("user", "birthday",   :order => [:month, :day])
      #
      # The selects are prepared for multi-parameter assignment to an Active Record object.
      def xf_date_select(object_name, method, options = {})
        InstanceTag.new(object_name, method, self, nil, options.delete(:object)).xf_to_date_select_tag(options, @request)
      end

      # Returns a set of select tags (one for year, month, day, hour, and minute) pre-selected for accessing a specified datetime-based
      # attribute (identified by +method+) on an object assigned to the template (identified by +object+). Examples:
      #
      #   datetime_select("post", "written_on")
      #   datetime_select("post", "written_on", :start_year => 1995)
      #
      # The selects are prepared for multi-parameter assignment to an Active Record object.
      def xf_datetime_select(object_name, method, options = {})
        InstanceTag.new(object_name, method, self, nil, options.delete(:object)).xf_to_datetime_select_tag(options, @request)
      end

      # Returns a set of html select-tags (one for year, month, and day) pre-selected with the +date+.
      def xf_select_date(date = Date.today, options = {})
        select_year(date, options) + select_month(date, options) + select_day(date, options)
      end

      # Returns a set of html select-tags (one for year, month, day, hour, and minute) pre-selected with the +datetime+.
      def xf_select_datetime(datetime = Time.now, options = {})
        select_year(datetime, options) + select_month(datetime, options) + select_day(datetime, options) +
        select_hour(datetime, options) + select_minute(datetime, options)
      end

      # Returns a set of html select-tags (one for hour and minute)
      def xf_select_time(datetime = Time.now, options = {})
        h = select_hour(datetime, options) + select_minute(datetime, options) + (options[:include_seconds] ? select_second(datetime, options) : '')
      end

      # Returns a select tag with options for each of the seconds 0 through 59 with the current second selected.
      # The <tt>second</tt> can also be substituted for a second number.
      # Override the field name using the <tt>:field_name</tt> option, 'second' by default.
      def xf_select_second(datetime, options = {})
        second_options = []
        initialValue = ""
        0.upto(59) do |second|
          if datetime && (datetime.kind_of?(Fixnum) ? datetime : datetime.sec) == second
              initialValue = second.to_s
          end
          option = leading_zero_on_single_digits(second).to_s
          second_options += [{:label => option, :value => second.to_s}]
        end

        select_xforms(options[:field_name] || 'second', second_options, intialValue, options[:prefix], options[:include_blank], options[:discard_type])
      end

      # Returns a select tag with options for each of the minutes 0 through 59 with the current minute selected.
      # Also can return a select tag with options by <tt>minute_step</tt> from 0 through 59 with the 00 minute selected
      # The <tt>minute</tt> can also be substituted for a minute number.
      # Override the field name using the <tt>:field_name</tt> option, 'minute' by default.
      def xf_select_minute(datetime, options = {})
        minute_options = []
        initialValue = ""
        0.step(59, options[:minute_step] || 1) do |minute|
          if datetime && (datetime.kind_of?(Fixnum) ? datetime : datetime.min) == minute
            initialValue = minute.to_s
          end
          option = leading_zero_on_single_digits(minute).to_s
          minute_options += [{:label => option, :value => minute.to_s}]
        end

        select_xforms(options[:field_name] || 'minute', minute_options, initialValue, options[:prefix], options[:include_blank], options[:discard_type])
      end

      # Returns a select tag with options for each of the hours 0 through 23 with the current hour selected.
      # The <tt>hour</tt> can also be substituted for a hour number.
      # Override the field name using the <tt>:field_name</tt> option, 'hour' by default.
      def xf_select_hour(datetime, options = {})
        hour_options = []
        initialValue = ""
        0.upto(23) do |hour|
          if datetime && (datetime.kind_of?(Fixnum) ? datetime : datetime.hour) == hour
            initialValue = hour.to_s
          end
          option = leading_zero_on_single_digits(hour).to_s
          hour_options += [{:label => option, :value => hour.to_s}]
        end

        select_xforms(options[:field_name] || 'hour', hour_options, initialValue, options[:prefix], options[:include_blank], options[:discard_type])
      end

      # Returns a select tag with options for each of the days 1 through 31 with the current day selected.
      # The <tt>date</tt> can also be substituted for a hour number.
      # Override the field name using the <tt>:field_name</tt> option, 'day' by default.
      def xf_select_day(date, options = {})
        day_options = []
        initialValue = ""
        1.upto(31) do |day|
          if date && (date.kind_of?(Fixnum) ? date : date.day) == day
            initialValue = day.to_s
          end
          option = leading_zero_on_single_digits(day).to_s
          day_options += [{:label => option, :value => day.to_s}]
        end

        select_xforms(options[:field_name] || 'day', day_options, initialValue, options[:prefix], options[:include_blank], options[:discard_type])
      end

      # Returns a select tag with options for each of the months January through December with the current month selected.
      # The month names are presented as keys (what's shown to the user) and the month numbers (1-12) are used as values
      # (what's submitted to the server). It's also possible to use month numbers for the presentation instead of names --
      # set the <tt>:use_month_numbers</tt> key in +options+ to true for this to happen. If you want both numbers and names,
      # set the <tt>:add_month_numbers</tt> key in +options+ to true. Examples:
      #
      #   select_month(Date.today)                             # Will use keys like "January", "March"
      #   select_month(Date.today, :use_month_numbers => true) # Will use keys like "1", "3"
      #   select_month(Date.today, :add_month_numbers => true) # Will use keys like "1 - January", "3 - March"
      #
      # Override the field name using the <tt>:field_name</tt> option, 'month' by default.
      #
      # If you would prefer to show month names as abbreviations, set the
      # <tt>:use_short_month</tt> key in +options+ to true.
      def xf_select_month(date, options = {})
        month_options = []
        month_names = options[:use_short_month] ? Date::ABBR_MONTHNAMES : Date::MONTHNAMES
        initialValue = ""
        1.upto(12) do |month_number|
          month_name = if options[:use_month_numbers]
            month_number
          elsif options[:add_month_numbers]
            month_number.to_s + ' - ' + month_names[month_number]
          else
            month_names[month_number]
          end

          if date && (date.kind_of?(Fixnum) ? date : date.month) == month_number
            initialValue = month_number
          end
          month_options += [{:label => month_name, :value => month_number}]
        end

        select_xforms(options[:field_name] || 'month', month_options, initialValue, options[:prefix], options[:include_blank], options[:discard_type])
      end

      # Returns a select tag with options for each of the five years on each side of the current, which is selected. The five year radius
      # can be changed using the <tt>:start_year</tt> and <tt>:end_year</tt> keys in the +options+. Both ascending and descending year
      # lists are supported by making <tt>:start_year</tt> less than or greater than <tt>:end_year</tt>. The <tt>date</tt> can also be
      # substituted for a year given as a number. Example:
      #
      #   select_year(Date.today, :start_year => 1992, :end_year => 2007)  # ascending year values
      #   select_year(Date.today, :start_year => 2005, :end_year => 1900)  # descending year values
      #
      # Override the field name using the <tt>:field_name</tt> option, 'year' by default.
      def xf_select_year(date, options = {})
        year_options = []
        y = date ? (date.kind_of?(Fixnum) ? (y = (date == 0) ? Date.today.year : date) : date.year) : Date.today.year

        start_year, end_year = (options[:start_year] || y-5), (options[:end_year] || y+5)
        step_val = start_year < end_year ? 1 : -1
        initialValue = ""
        start_year.step(end_year, step_val) do |year|
          if date && (date.kind_of?(Fixnum) ? date : date.year) == year
            initialValue = year.to_s
          end
          year_options += [{:label => year.to_s, :value => year.to_s}]
        end

        select_xforms(options[:field_name] || 'year', year_options, initialValue, options[:prefix], options[:include_blank], options[:discard_type])
      end

      private
        def select_xforms (type, options, initialValue, prefix = nil, include_blank = false, discard_type = false)
            ref = prefix || DEFAULT_PREFIX
            ref << "[#{type}]" unless discard_type
            attrs = {:ref => ref, :initialValue => initialValue}
            if include_blank
                options.unshift({:label => "", :value => ""})
            end
            xf_select1(nil, nil, options, nil, attrs)
        end

        def leading_zero_on_single_digits(number)
          number > 9 ? number : "0#{number}"
        end
    end

    class InstanceTag #:nodoc:
      include XformsDateHelper
      include XformsHelper

      def xf_to_date_select_tag(options = {}, request = nil)
        @request = request
        defaults = { :discard_type => true }
        options  = defaults.merge(options)
        options_with_prefix = Proc.new { |position| options.merge(:prefix => "#{@object_name}[#{@method_name}(#{position}i)]") }
        date     = options[:include_blank] ? (value || 0) : (value || Date.today)

        date_select = ''
        options[:order]   = [:month, :year, :day] if options[:month_before_year] # For backwards compatibility
        options[:order] ||= [:year, :month, :day]

        position = {:year => 1, :month => 2, :day => 3}

        discard = {}
        discard[:year]  = true if options[:discard_year]
        discard[:month] = true if options[:discard_month]
        discard[:day]   = true if options[:discard_day] or options[:discard_month]

        options[:order].each do |param|
          date_select << self.send("xf_select_#{param}", date, options_with_prefix.call(position[param])) unless discard[param]
        end

        date_select
      end

      def xf_to_datetime_select_tag(options = {}, request = nil)
        @request = request
        defaults = { :discard_type => true }
        options  = defaults.merge(options)
        options_with_prefix = Proc.new { |position| options.merge(:prefix => "#{@object_name}[#{@method_name}(#{position}i)]") }
        datetime = options[:include_blank] ? (value || nil) : (value || Time.now)

        datetime_select  = xf_select_year(datetime, options_with_prefix.call(1))
        datetime_select << xf_select_month(datetime, options_with_prefix.call(2)) unless options[:discard_month]
        datetime_select << xf_select_day(datetime, options_with_prefix.call(3)) unless options[:discard_day] || options[:discard_month]
        datetime_select << ' - ' + xf_select_hour(datetime, options_with_prefix.call(4)) unless options[:discard_hour]
        datetime_select << ' : ' + xf_select_minute(datetime, options_with_prefix.call(5)) unless options[:discard_minute] || options[:discard_hour]

        datetime_select
      end
    end

    class FormBuilder
      def date_select(method, options = {})
        @template.date_select(@object_name, method, options.merge(:object => @object))
      end

      def datetime_select(method, options = {})
        @template.datetime_select(@object_name, method, options.merge(:object => @object))
      end
    end
  end # End Helper
end # End ActionView