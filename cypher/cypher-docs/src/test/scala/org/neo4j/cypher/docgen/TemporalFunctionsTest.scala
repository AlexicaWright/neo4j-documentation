/*
 * Copyright (c) 2002-2018 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.docgen

import java.time._

import org.neo4j.cypher.docgen.tooling.{DocBuilder, DocumentingTest, ResultAssertions}

class TemporalFunctionsTest extends DocumentingTest {

  override def outputPath = "target/docs/dev/ql/functions"

  override def doc = new DocBuilder {
    doc("Temporal functions", "query-functions-temporal")
    synopsis(
      """Cypher provides functions allowing for the creation of values for each temporal type -- Date, Time, LocalTime, DateTime, LocalDateTime and Duration.""".stripMargin)
    p(
      """Each function bears the same name as the type, and construct the type they correspond to in one of four ways:
        |
        |* Capturing the current time
        |* Composing the components of the type
        |* Parsing a string representation of the temporal value
        |* Selecting and composing components from another temporal value by
        | ** either combining temporal values (such as combining a _Date_ with a _Time_ to create a _DateTime_), or
        | ** selecting parts from a temporal value (such as selecting the _Date_ from a _DateTime_).""".stripMargin)
    note {
      p("""See also <<cypher-temporal>> and <<query-operators-temporal>>.""")
    }
    p(
      """
        |* <<functions-date, _Date_ functions>>
        | ** <<functions-date-current, date(): getting the current _Date_>>
        | ** <<functions-date-calendar, date(): creating a calendar (Year-Month-Day) _Date_>>
        | ** <<functions-date-week, date(): creating a week (Year-Week-Day) _Date_>>
        | ** <<functions-date-quarter, date(): creating a quarter (Year-Quarter-Day) _Date_>>
        | ** <<functions-date-ordinal, date(): creating an ordinal (Year-Day) _Date_>>
        |* <<functions-datetime, _DateTime_ functions>>
        | ** <<functions-datetime-current, datetime(): getting the current _DateTime_>>
        | ** <<functions-datetime-calendar, datetime(): creating a calendar _DateTime_>>
        |* <<functions-localdatetime, _LocalDateTime_ functions>>
        | ** <<functions-localdatetime-current, localdatetime(): getting the current _LocalDateTime_>>
        | ** <<functions-localdatetime-calendar, localdatetime(): creating a calendar _LocalDateTime_>>
        |* <<functions-localtime, _LocalTime_ functions>>
        | ** <<functions-localtime-current, localtime(): getting the current _LocalTime_>>
        |* <<functions-time, _Time_ functions>>
        | ** <<functions-time-current, time(): getting the current _Time_>>
        |
        |""")
    section("_Date_ functions", "functions-date") {
      section("date(): getting the current _Date_", "functions-date-current") {
        p(
          """`date()` returns the current _Date_ value.""".stripMargin)
        function("date()", "A Date.")
        query(
          """RETURN date() AS currentDate""".stripMargin, ResultAssertions((r) => {
            val now = r.columnAs[LocalDate]("currentDate").next()
            now should be(a[LocalDate])
          })) {
          p("""The current date is returned.""")
          resultTable()
        }
      }
      section("date(): creating a calendar (Year-Month-Day) _Date_", "functions-date-calendar") {
        p(
          """`date()` returns a _Date_ value with the specified _year_, _month_ and _day_ component values.""".stripMargin)
        function("date({year [, month, day]})", "A Date.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("month", "An integer between `1` and `12` that specifies the month."), ("day", "An integer between `1` and `31` that specifies the day of the month."))
        considerations("The _day of the month_ component will default to `1` if `day` is omitted.", "The _month_ component will default to `1` if `month` is omitted.", "If `month` is omitted, `day` must also be omitted.")
        query(
          """UNWIND [date({year:1984, month:10, day:11}),
            | date({year:1984, month:10}),
            | date({year:1984})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            r.toList should equal(List(Map("theDate" -> LocalDate.of(1984, 10, 11)), Map("theDate" -> LocalDate.of(1984, 10, 1)), Map("theDate" -> LocalDate.of(1984, 1, 1))))
          })) {
          resultTable()
        }
      }
      section("date(): creating a week (Year-Week-Day) _Date_", "functions-date-week") {
        p(
          """`date()` returns a _Date_ value with the specified _year_, _week_ and _dayOfWeek_ component values.""".stripMargin)
        function("date({year [, week, dayOfWeek]})", "A Date.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("week", "An integer between `1` and `53` that specifies the week."), ("dayOfWeek", "An integer between `1` and `7` that specifies the day of the week."))
        considerations("The _day of the week_ component will default to `1` if `dayOfWeek` is omitted.", "The _week_ component will default to `1` if `week` is omitted.", "If `week` is omitted, `dayOfWeek` must also be omitted.")
        query(
          """UNWIND [date({year:1984, week:10, dayOfWeek:3}),
            | date({year:1984, week:10}),
            | date({year:1984})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            r.toList should equal(List(Map("theDate" -> LocalDate.of(1984, 3, 7)), Map("theDate" -> LocalDate.of(1984, 3, 5)), Map("theDate" -> LocalDate.of(1984, 1, 1))))
          })) {
          resultTable()
        }
      }
      section("date(): creating a quarter (Year-Quarter-Day) _Date_", "functions-date-quarter") {
        p(
          """`date()` returns a _Date_ value with the specified _year_, _quarter_ and _dayOfQuarter_ component values.""".stripMargin)
        function("date({year [, quarter, dayOfQuarter]})", "A Date.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("quarter", "An integer between `1` and `4` that specifies the quarter."), ("dayOfQuarter", "An integer between `1` and `92` that specifies the day of the quarter."))
        considerations("The _day of the quarter_ component will default to `1` if `dayOfQuarter` is omitted.", "The _quarter_ component will default to `1` if `quarter` is omitted.", "If `quarter` is omitted, `dayOfQuarter` must also be omitted.")
        query(
          """UNWIND [date({year:1984, quarter:3, dayOfQuarter: 45}),
            | date({year:1984, quarter:3}),
            | date({year:1984})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            r.toList should equal(List(Map("theDate" -> LocalDate.of(1984, 8, 14)), Map("theDate" -> LocalDate.of(1984, 7, 1)), Map("theDate" -> LocalDate.of(1984, 1, 1))))
          })) {
          resultTable()
        }
      }
      section("date(): creating an ordinal (Year-Day) _Date_", "functions-date-ordinal") {
        p(
          """`date()` returns a _Date_ value with the specified _year_ and _ordinalDay_ component values.""".stripMargin)
        function("date({year [, ordinalDay]})", "A Date.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("ordinalDay", "An integer between `1` and `366` that specifies the ordinal day of the year."))
        considerations("The _ordinal day of the year_ component will default to `1` if `ordinalDay` is omitted.")
        query(
          """UNWIND [date({year:1984, ordinalDay:202}),
            | date({year:1984})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            r.toList should equal(List(Map("theDate" -> LocalDate.of(1984, 7, 20)), Map("theDate" -> LocalDate.of(1984, 1, 1))))
          })) {
          p("""The date corresponding to `11 February 1984` is returned.""")
          resultTable()
        }
      } //Parsing a Date using the week date format:
      //
      //date("+2015-W13-4")
    }
    section("_DateTime_ functions", "functions-datetime") {
      section("datetime(): getting the current _DateTime_", "functions-datetime-current") {
        p(
          """`datetime()` returns the current _DateTime_ value.
            |If no time zone parameter is specified, the local time zone will be used.
          """.stripMargin)
        function("datetime()", "A DateTime.")
        query(
          """RETURN datetime() AS currentDateTime""".stripMargin, ResultAssertions((r) => {
            val now = r.columnAs[ZonedDateTime]("currentDateTime").next()
            now should be(a[ZonedDateTime])
          })) {
          p("""The current date and time using the local time zone is returned.""")
          resultTable()
        } //Parsing a DateTime using the calendar date format:
        //
        //datetime("2015-06-24T12:50:35.556+0100")
      }
      section("datetime(): creating a calendar (Year-Month-Day) _DateTime_", "functions-datetime-calendar") {
        p(
          """`datetime()` returns a _DateTime_ value with the specified _year_, _month_, _day_, _hour_, _minute_, _second_, _millisecond_, _microsecond_, _nanosecond_ and _timezone_ component values.""".stripMargin)
        function("datetime({year [, month, day, hour, minute, second, millisecond, microsecond, nanosecond, timezone]})", "A DateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("month", "An integer between `1` and `12` that specifies the month."), ("day", "An integer between `1` and `31` that specifies the day of the month."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."), ("timezone", "An expression that specifies the time zone."))
        considerations("The _month_ component will default to `1` if `month` is omitted.", "The _day of the month_ component will default to `1` if `day` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "The _timezone_ component will default to the `UTC` time zone if `timezone` is omitted.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `month`, `day`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year`, `month` and `day`, but specifying `year`, `month`, `day` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [datetime({year:1984, month:10, day:11, hour:12, minute:31, second:14, millisecond: 123, microsecond: 456, nanosecond: 789}),
            |   datetime({year:1984, month:10, day:11, hour:12, minute:31, second:14, millisecond: 645, timezone: '+01:00'}),
            |   datetime({year:1984, month:10, day:11, hour:12, minute:31, second:14, nanosecond: 645876123, timezone: 'Europe/Stockholm'}),
            |   datetime({year:1984, month:10, day:11, hour:12, minute:31, second:14, timezone: '+01:00'}),
            |   datetime({year:1984, month:10, day:11, hour:12, minute:31, second:14}),
            |   datetime({year:1984, month:10, day:11, hour:12, minute:31, timezone: 'Europe/Stockholm'}),
            |   datetime({year:1984, month:10, day:11, hour:12, timezone: '+01:00'}),
            |   datetime({year:1984, month:10, day:11, timezone: 'Europe/Stockholm'})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
      section("datetime(): creating a week (Year-Week-Day) _DateTime_", "functions-datetime-week") {
        p(
          """`datetime()` returns a _DateTime_ value with the specified _year_, _week_, _dayOfWeek_, _hour_, _minute_, _second_, _millisecond_, _microsecond_, _nanosecond_ and _timezone_ component values.""".stripMargin)
        function("datetime({year [, week, dayOfWeek, hour, minute, second, millisecond, microsecond, nanosecond, timezone]})", "A DateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("week", "An integer between `1` and `53` that specifies the week."), ("dayOfWeek", "An integer between `1` and `7` that specifies the day of the week."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."), ("timezone", "An expression that specifies the time zone."))
        considerations("The _week_ component will default to `1` if `week` is omitted.", "The _day of the week_ component will default to `1` if `dayOfWeek` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "The _timezone_ component will default to the `UTC` time zone if `timezone` is omitted.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `week`, `dayOfWeek`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year`, `week` and `dayOfWeek`, but specifying `year`, `week`, `dayOfWeek` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [datetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14, millisecond: 645}),
            |   datetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14, microsecond: 645876, timezone: '+01:00'}),
            |   datetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14, nanosecond: 645876123, timezone: 'Europe/Stockholm'}),
            |   datetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14, timezone: 'Europe/Stockholm'}),
            |   datetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14}),
            |   datetime({year:1984, week:10, dayOfWeek:3, hour:12, timezone: '+01:00'}),
            |   datetime({year:1984, week:10, dayOfWeek:3, timezone: 'Europe/Stockholm'})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
      section("datetime(): creating a quarter (Year-Quarter-Day) _DateTime_", "functions-datetime-quarter") {
        p(
          """`datetime()` returns a _DateTime_ value with the specified _year_, _quarter_, _dayOfQuarter_, _hour_, _minute_, _second_, _millisecond_, _microsecond_, _nanosecond_ and _timezone_ component values.""".stripMargin)
        function("datetime({year [, quarter, dayOfQuarter, hour, minute, second, millisecond, microsecond, nanosecond, timezone]})", "A DateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("quarter", "An integer between `1` and `4` that specifies the quarter."), ("dayOfQuarter", "An integer between `1` and `92` that specifies the day of the quarter."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."), ("timezone", "An expression that specifies the time zone."))
        considerations("The _quarter_ component will default to `1` if `quarter` is omitted.", "The _day of the quarter_ component will default to `1` if `dayOfQuarter` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "The _timezone_ component will default to the `UTC` time zone if `timezone` is omitted.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `quarter`, `dayOfQuarter`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year`, `quarter` and `dayOfQuarter`, but specifying `year`, `quarter`, `dayOfQuarter` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [datetime({year:1984, quarter:3, dayOfQuarter: 45, hour:12, minute:31, second:14, microsecond: 645876}),
            |   datetime({year:1984, quarter:3, dayOfQuarter: 45, hour:12, minute:31, second:14, timezone: '+01:00'}),
            |   datetime({year:1984, quarter:3, dayOfQuarter: 45, hour:12, timezone: 'Europe/Stockholm'}),
            |   datetime({year:1984, quarter:3, dayOfQuarter: 45})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
      section("datetime(): creating an ordinal (Year-Day) _DateTime_", "functions-datetime-ordinal") {
        p(
          """`datetime()` returns a _DateTime_ value with the specified _year_, _ordinalDay_, _hour_, _minute_, _second_, _millisecond_, _microsecond_, _nanosecond_ and _timezone_ component values.""".stripMargin)
        function("datetime({year [, ordinalDay, hour, minute, second, millisecond, microsecond, nanosecond, timezone]})", "A DateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("ordinalDay", "An integer between `1` and `366` that specifies the ordinal day of the year."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."), ("timezone", "An expression that specifies the time zone."))
        considerations("The _ordinal day of the year_ component will default to `1` if `ordinalDay` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "The _timezone_ component will default to the `UTC` time zone if `timezone` is omitted.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `ordinalDay`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year` and `ordinalDay`, but specifying `year`, `ordinalDay` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [datetime({year:1984, ordinalDay:202, hour:12, minute:31, second:14, millisecond: 645}),
            |   datetime({year:1984, ordinalDay:202, hour:12, minute:31, second:14, timezone: '+01:00'}),
            |   datetime({year:1984, ordinalDay:202, timezone: 'Europe/Stockholm'}),
            |   datetime({year:1984, ordinalDay:202})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
    }
    section("_LocalDateTime_ functions", "functions-localdatetime") {
      section("localdatetime(): getting the current _LocalDateTime_", "functions-localdatetime-current") {
        p(
          """`localdatetime()` returns the current _LocalDateTime_ value.""".stripMargin)
        function("localdatetime()", "A LocalDateTime.")
        query(
          """RETURN localdatetime() AS now""".stripMargin, ResultAssertions((r) => {
            val now = r.columnAs[LocalDateTime]("now").next()
            now should be(a[LocalDateTime])
          })) {
          p("""The current local date and time (i.e. in the local time zone) is returned.""")
          resultTable()
        } //Parsing a LocalDateTime using the ordinal date format:
        //
        //localdatetime("2015185T19:32:24")
      }
      section("localdatetime(): creating a calendar (Year-Month-Day) _LocalDateTime_", "functions-localdatetime-calendar") {
        p(
          """`localdatetime()` returns a _LocalDateTime_ value with the specified _year_, _month_, _day_, _hour_, _minute_, _second_, _millisecond_, _microsecond_ and _nanosecond_ component values.""".stripMargin)
        function("localdatetime({year [, month, day, hour, minute, second, millisecond, microsecond, nanosecond]})", "A LocalDateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("month", "An integer between `1` and `12` that specifies the month."), ("day", "An integer between `1` and `31` that specifies the day of the month."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."))
        considerations("The _month_ component will default to `1` if `month` is omitted.", "The _day of the month_ component will default to `1` if `day` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `month`, `day`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year`, `month` and `day`, but specifying `year`, `month`, `day` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [localdatetime({year:1984, month:10, day:11, hour:12, minute:31, second:14, nanosecond: 789, millisecond: 123, microsecond: 456}),
            |   localdatetime({year:1984, month:10, day:11, hour:12, minute:31})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
      section("localdatetime(): creating a week (Year-Week-Day) _LocalDateTime_", "functions-localdatetime-week") {
        p(
          """`localdatetime()` returns a _LocalDateTime_ value with the specified _year_, _week_, _dayOfWeek_, _hour_, _minute_, _second_, _millisecond_, _microsecond_ and _nanosecond_ component values.""".stripMargin)
        function("localdatetime({year [, week, dayOfWeek, hour, minute, second, millisecond, microsecond, nanosecond]})", "A LocalDateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("week", "An integer between `1` and `53` that specifies the week."), ("dayOfWeek", "An integer between `1` and `7` that specifies the day of the week."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."))
        considerations("The _week_ component will default to `1` if `week` is omitted.", "The _day of the week_ component will default to `1` if `dayOfWeek` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `week`, `dayOfWeek`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year`, `week` and `dayOfWeek`, but specifying `year`, `week`, `dayOfWeek` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [localdatetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14, millisecond: 645}),
            |   localdatetime({year:1984, week:10, dayOfWeek:3, hour:12, minute:31, second:14})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
      section("localdatetime(): creating a quarter (Year-Quarter-Day) _DateTime_", "functions-localdatetime-quarter") {
        p(
          """`localdatetime()` returns a _LocalDateTime_ value with the specified _year_, _quarter_, _dayOfQuarter_, _hour_, _minute_, _second_, _millisecond_, _microsecond_ and _nanosecond_ component values.""".stripMargin)
        function("localdatetime({year [, quarter, dayOfQuarter, hour, minute, second, millisecond, microsecond, nanosecond]})", "A LocalDateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("quarter", "An integer between `1` and `4` that specifies the quarter."), ("dayOfQuarter", "An integer between `1` and `92` that specifies the day of the quarter."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."))
        considerations("The _quarter_ component will default to `1` if `quarter` is omitted.", "The _day of the quarter_ component will default to `1` if `dayOfQuarter` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `quarter`, `dayOfQuarter`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year`, `quarter` and `dayOfQuarter`, but specifying `year`, `quarter`, `dayOfQuarter` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [localdatetime({year:1984, quarter:3, dayOfQuarter: 45, hour:12, minute:31, second:14, nanosecond: 645876123}),
            |   localdatetime({year:1984, quarter:3, dayOfQuarter: 45, hour:12, minute:31})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
      section("localdatetime(): creating an ordinal (Year-Day) _LocalDateTime_", "functions-localdatetime-ordinal") {
        p(
          """`localdatetime()` returns a _LocalDateTime_ value with the specified _year_, _ordinalDay_, _hour_, _minute_, _second_, _millisecond_, _microsecond_ and _nanosecond_ component values.""".stripMargin)
        function("localdatetime({year [, ordinalDay, hour, minute, second, millisecond, microsecond, nanosecond]})", "A LocalDateTime.", ("A single map consisting of the following:", ""), ("year", "An expression consisting of at least four digits that specifies the year TODOLINK."), ("ordinalDay", "An integer between `1` and `366` that specifies the ordinal day of the year."), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."))
        considerations("The _ordinal day of the year_ component will default to `1` if `ordinalDay` is omitted.", "The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `year`, `ordinalDay`, `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `year` and `ordinalDay`, but specifying `year`, `ordinalDay` and `minute` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [localdatetime({year:1984, ordinalDay:202, hour:12, minute:31, second:14, microsecond: 645876}),
            |   localdatetime({year:1984, ordinalDay:202, hour:12})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
    }
    section("_LocalTime_ functions", "functions-localtime") {
      section("localtime(): getting the current _LocalTime_", "functions-localtime-current") {
        p(
          """`localtime()` returns the current _LocalTime_ value.""".stripMargin)
        function("localtime()", "A LocalTime.")
        query(
          """RETURN localtime() AS now""".stripMargin, ResultAssertions((r) => {
            val now = r.columnAs[LocalTime]("now").next()
            now should be(a[LocalTime])
          })) {
          p("""The current local time (i.e. in the local time zone) is returned.""")
          resultTable()
        } //Parsing a LocalTime:
        //
        //localtime("12:50:35.556")
      }
      section("localtime(): creating a _LocalTime_", "functions-localtime-create") {
        p(
          """`localtime()` returns a _LocalDateTime_ value with the specified _hour_, _minute_, _second_, _millisecond_, _microsecond_ and _nanosecond_ component values.""".stripMargin)
        function("localtime({hour [, minute, second, millisecond, microsecond, nanosecond]})", "A LocalTime.", ("A single map consisting of the following:", ""), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."))
        considerations("The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `hour` and `minute`, but specifying `hour` and `second` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [localtime({hour:12, minute:31, second:14, nanosecond: 789, millisecond: 123, microsecond: 456}),
            |   localtime({hour:12, minute:31, second:14}),
            |   localtime({hour:12})] as theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
    }
    section("_Time_ functions", "functions-time") {
      section("time(): getting the current _Time_", "functions-time-current") {
        p(
          """`time()` returns the current _Time_ value.
            |If no time zone parameter is specified, the local time zone will be used.""".stripMargin)
        function("time([ {timezone} ])", "A Time.", ("A single map consisting of the following:", ""), ("timezone", "An expression that represents the time zone"))
        considerations("If no parameters are provided, `time()` should be invoked (`time({})` is invalid).")
        query(
          """RETURN time() AS currentTime""".stripMargin, ResultAssertions((r) => {
            val now = r.columnAs[OffsetTime]("currentTime").next()
            now should be(a[OffsetTime])
          })) {
          p("""The current time of day using the local time zone is returned.""")
          resultTable()
        }
        query(
          """RETURN time( {timezone: "America/Los Angeles"} ) AS currentTimeInLA""".stripMargin, ResultAssertions((r) => {
            val now = r.columnAs[OffsetTime]("currentTimeInLA").next()
            now should be(a[OffsetTime])
          })) {
          p("""The current time of day in California is returned.""")
          resultTable()
        } //Parsing a Time:
        //
        //time("125035.556+0100")
      }
      section("time(): creating a _Time_", "functions-time-create") {
        p(
          """`time()` returns a _Time_ value with the specified _hour_, _minute_, _second_, _millisecond_, _microsecond_, _nanosecond_ and _timezone_ component values.""".stripMargin)
        function("time({hour [, minute, second, millisecond, microsecond, nanosecond, timezone]})", "A Time.", ("A single map consisting of the following:", ""), ("hour", "An integer between `0` and `23` that specifies the hour of the day."), ("minute", "An integer between `0` and `59` that specifies the number of minutes."), ("second", "An integer between `0` and `59` that specifies the number of seconds."), ("millisecond", "An integer between `0` and `999` that specifies the number of milliseconds."), ("microsecond", "An integer between `0` and `999,999` that specifies the number of microseconds."), ("nanosecond", "An integer between `0` and `999,999,999` that specifies the number of nanoseconds."), ("timezone", "An expression that specifies the time zone."))
        considerations("The _hour_ component will default to `0` if `hour` is omitted.", "The _minute_ component will default to `0` if `minute` is omitted.", "The _second_ component will default to `0` if `second` is omitted.", "Any missing `millisecond`, `microsecond` or `nanosecond` values will default to `0`.", "The _timezone_ component will default to the `UTC` time zone if `timezone` is omitted.", "If `millisecond`, `microsecond` and `nanosecond` are given in combination (as part of the same set of parameters), the individual values must be in the range `0` to `999`.", "The least significant components in the set `hour`, `minute`, and `second` may be omitted; i.e. it is possible to specify only `hour` and `minute`, but specifying `hour` and `second` is not permitted.", "One or more of `millisecond`, `microsecond` and `nanosecond` can only be specified as long as `second` is also specified.")
        query(
          """UNWIND [time({hour:12, minute:31, second:14, nanosecond: 789, millisecond: 123, microsecond: 456}),
            |   time({hour:12, minute:31, second:14, nanosecond: 645876123}),
            |   time({hour:12, minute:31, second:14, microsecond: 645876, timezone: '+01:00'}),
            |   time({hour:12, minute:31, timezone: '+01:00'}),
            |   time({hour:12, timezone: '+01:00'})] AS theDate
            |RETURN theDate""".stripMargin, ResultAssertions((r) => {
            // CYPHER_TODO
            // starting off...r.toList should equal(List(Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 123456789, ZoneId.of("Z")))), Map("theDate" -> ZonedDateTime.of(1984, 10, 11, 12, 31, 14, 645, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1)))))
          })) {
          resultTable()
        }
      }
    }
  }.build()
}


//  test("should get current 'realtime' datetime") {
//    val result = executeWith(supported, "RETURN datetime.realtime() as now")
//
//    val now = single(result.columnAs[ZonedDateTime]("now"))
//
//    now shouldBe a[ZonedDateTime]
//  }
