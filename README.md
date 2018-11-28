# Elastic and Cloud-ready Applications
Demo project for presentation about cloud-ready applications with Payara Micro

## Build

```
mvn install
```

## Run

### StockWeb service

```
cd StockWeb
mvn payara-micro:start
```

After a while, an app is accessible at http://10.31.0.130:8080/ROOT/

### StockTicker service

```
cd StockTicker
mvn payara-micro:start
```

When it starts, it should be sending data to StockWeb and the browser page should automatically update with data in a graph.

## Evolve to Kafka broker

By default, messages are sent over Payara distributed CDI event bus (https://docs.payara.fish/documentation/payara-server/public-api/cdi-events.html)

To switch to Kafka, first start a Kafka broker on localhost:9092 with a topic named "stock". Then modify the following:

* in StockTicker, `StockTicker.java`, replace `@Outbound` with `@Kafka`. This routes messages to `KafkaStockPublisher.java`
* in StockWeb, `StockTickerResource.java`, replace `@Inbound` with `@Kafka`. The observer method will be getting events from `KafkaStockMDB.java`
* restart both services

After the switch, the behavior of the application shouldn't change. But it's possible to see messages in the Kafka queue. 

## Configuration

StockTicker sends fictive stock prices with a symbol (name), by default "Dow Jones". This is specified by a configuration key `stockticker.symbol`, which is evaluated in `StockTicker.java` every time when a stock price is generated. The symbol is then displayed above the chart in the web page in StockWeb app.

Modify the symbol displayed in the web page:

* stop StockTicker and only leave StockWeb running (alternatively just start StockWeb)
* set environment variable stockticker_symbol to a value of your custom symbol
* start StockTicker with `mvn payara-micro:start`

An example for Linux:

```
cd StockTicker
export stockticker_symbol="My symbol" && mvn payara-micro:start
```

You can also implement your custom config source to read data from any source. Follow the MicroProfile Config documentation. 

You can also try using some built-in config sources in Payara Micro, more info here: https://docs.payara.fish/documentation/microprofile/config.html

A collection of reusable config sources is available at https://www.microprofile-ext.org/.

## Metrics

Some standard metrics are available automatically for both services using the `/metrics` endpoint.

For StockWeb, open http://10.31.0.130:8080/metrics (for JSON output, specify also "application/json" for the "Accept" HTTP header).

# Health

For StockWeb, access http://10.31.0.130:8080/health. It gives either

* HTTP 200 if all is OK
* HTTP 503 if the service isn't healthy
* HTTP 500 if service produced an internal error