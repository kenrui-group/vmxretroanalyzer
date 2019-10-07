# Overview
VMXRetroAnalyzer assists to spot out misbehavior with application or mis-configuratoin latency monitoring appliance for tick to quote measurements.

# Features
* Ingest decoded packet capture written out as delimited text files into in memory database.
* Customizable file and field names for ids of interest.
* Ad hoc query via web browser at http://localhost:8082.
* Break down correlation ids (tick and quote id pairs) into 
    *   One tick to one quote
    *   One tick to multiple quote
    *   Different tick to one quote (not expected and highlight application error)
    *   One tick to no quote (not expected and highlight application error)
    *   Repeating pairs (not expected and highlight application error)
    
* Count total number of correlation id pairs (tick and quotie id pairs) that have its quote id found on the wire on its way to exchange.


## Quirks
* [Spring Boot Configuration Sequence](https://gooroo.io/GoorooTHINK/Article/17466/Lessons-Learned-Writing-Spring-Boot-Auto-Configurations/29652#.XZsDxEYzags)
* [Gravizo Workaround](https://gist.github.com/svenevs/ce05761128e240e27883e3372ccd4ecd)

# License
Apache License 2.0