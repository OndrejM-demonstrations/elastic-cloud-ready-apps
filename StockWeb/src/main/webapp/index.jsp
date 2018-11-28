<!DOCTYPE HTML>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Payara Micro Stock Ticker</title>

    <script type="text/javascript" src="jquery-1.8.1.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

//            newChart("", "container");
            requestSse("rest/stock", "container");
        });

    </script>
  </head>
  <body>
    <script type="text/javascript" src="js/highcharts.js"></script>
    <script type="text/javascript" src="js/modules/exporting.js"></script>
    
    <h1>Stock Ticker DEMO</h1>

    <div id="container" style="width: 80%; height: 80%; margin: 0 auto"></div>

  </body>
</html>
