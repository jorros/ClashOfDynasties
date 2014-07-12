<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <script type="text/javascript" src="/lib/jquery-2.1.0.js"></script>
</head>
<body>
<div id="wrapper">
    <div id="body">
        <h1>Wähle ein Volk</h1>
        <div id="content" style="overflow-y:hidden;">
            <a href="/step2?key=${key}&nation=1" class="chooser" style="float:left; margin-left:40px;"><img src="images/chooser/Gato.png" /></a>
            <a href="/step2?key=${key}&nation=2" class="chooser" style="float:right; margin-right:40px;"><img src="images/chooser/Nagori.png" /></a>
        </div>
    </div>
</div>
</body>
</html>