$(document).ready(function () {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

});



function openSocket(contextPath) {
    var wsUri = "ws://" + location.host + contextPath + "/graph";
    console.log("CONNECT CALLED on URI " + wsUri);
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (event) {
        onOpen(event)
    };
    websocket.onclose = function (event) {
        onClose(event)
    };
    websocket.onerror = function (event) {
        onError(event)
    };

    websocket.onmessage = function (event) {
        var object = JSON.parse(event.data);
        var x = (new Date()).getTime();
        var y = object.RandomPrice;
        var chart = getChart(object.symbol);
        chart.series[0].addPoint([x, y], true, true, false);
    }

    function onOpen(event) { }
    function onError(event) { }
    function onClose(event) { }
}

function requestSse(sseUri, containerId) {
    var source = new EventSource(sseUri);
    source.onmessage = function (event) {
        onMessage(event)
    };

    console.log("CONNECT CALLED");

    function onMessage(event) {
        var object = JSON.parse(event.data);
        var x = (new Date()).getTime();
        var y = object.RandomPrice;
        var chart = getChart(object.symbol, containerId);
        if (chart) {
            chart.series[0].addPoint([x, y], true, true, false);
        } else {
            console.log("Chart with symbol " + object.symbol + " not found.");
        }
    }

}

function newChart(symbol, containerId) {
    var templateContainer = document.getElementById(containerId);
    var clonedContainer = templateContainer.cloneNode(true);
    templateContainer.parentNode.appendChild(clonedContainer);
    clonedContainer.id = clonedContainer.id + symbol;

    return new Highcharts.Chart({
        chart: {
            renderTo: clonedContainer.id,
            defaultSeriesType: 'spline',
            marginRight: 10,
            plotBackgroundColor: '#FFFFFF',
            plotShadow: true,
            animation: {
                duration: 200
            }
        },
        title: {
            text: symbol + ' Stock Price'
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 150
        },
        yAxis: {
            title: {
                text: 'Price ($)'
            },
            plotLines: [{
                    value: 0,
                    width: 2,
                    color: '#808080'
                }]
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>$' +
                        Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true
        },
        exporting: {
            enabled: false
        },
        series: [{
                name: 'PAYARA ' + symbol,
                data: (function () {
                    // generate an array of random data
                    var data = [],
                            time = (new Date()).getTime(),
                            i;

                    for (i = -19; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: 0
                        });
                    }
                    return data;
                })()
            }]
    });
    
}

function getChart(symbol, containerId) {
    if (!document.chart) {
        document.chart = [];
    }
    if (!document.chart[symbol]) {
        document.chart[symbol] = newChart(symbol, containerId);
    }

    return document.chart[symbol];
}

