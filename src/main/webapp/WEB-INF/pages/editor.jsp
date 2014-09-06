<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties - Editor</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <link rel="stylesheet" href="/css/jquery-ui-1.10.4.custom.css" type="text/css">
    <script type="text/javascript" src="/lib/jquery-2.1.0.js"></script>
    <script type="text/javascript" src="/lib/jquery-ui-1.10.4.custom.js"></script>
    <script type="text/javascript" src="/lib/crafty-min.js"></script>
    <script type="text/javascript" src="/lib/jquery.mousewheel.min.js"></script>
    <script type="text/javascript" src="/src/assets.js"></script>
    <script type="text/javascript" src="/src/helper.js"></script>
    <script type="text/javascript" src="/src/editorMenus.js"></script>
    <script type="text/javascript" src="/src/update.js"></script>
    <script type="text/javascript" src="/src/cityEntity.js"></script>
    <script type="text/javascript" src="/src/roadEntity.js"></script>
    <script type="text/javascript" src="/src/formationEntity.js"></script>
    <script type="text/javascript" src="/src/caravanEntity.js"></script>
    <script type="text/javascript" src="/src/editor.js"></script>
</head>
<body>
<div id="cr-stage"></div>
<div id="wrapper">
    <div id="top">
    </div>
    <div id="header">
        <div id="menu1" class="menu menu-orange"><a>st&auml;dte</a></div>
        <div id="menu2" class="menu menu-yellow"><a>spieler</a></div>
        <div id="menu3" class="menu menu-cyan"><a>geb&auml;ude</a></div>
        <div id="menu4" class="menu menu-green"><a>einheiten</a></div>
        <div id="menu5" class="menu menu-red"><a>logout</a></div>
    </div>
    <div id="body" style="display: none;">
    </div>
    <div id="controls"><button style="float:left;" onclick="SelectionMode = 0; $('#cr-stage').css('cursor', 'default');">Auswahl</button>
        <button style="margin-left:5px; float:left;" onclick="SelectionMode = 1; $('#cr-stage').css('cursor', 'url(assets/cities/1.png), crosshair');">Stadt</button>
        <button style="margin-left:5px; float:left;" onclick="SelectionMode = 2; $('#cr-stage').css('cursor', 'crosshair');">Weg</button></div>
</div>
</body>
</html>