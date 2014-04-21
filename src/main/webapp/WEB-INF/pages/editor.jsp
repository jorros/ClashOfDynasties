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
    <script type="text/javascript" src="/src/assets.js"></script>
    <script type="text/javascript" src="/src/helper.js"></script>
    <script type="text/javascript" src="/src/editorMenus.js"></script>
    <script type="text/javascript" src="/src/cities.js"></script>
    <script type="text/javascript" src="/src/cityEntity.js"></script>
    <script type="text/javascript" src="/src/roads.js"></script>
    <script type="text/javascript" src="/src/roadEntity.js"></script>
    <script type="text/javascript" src="/src/formationEntity.js"></script>
    <script type="text/javascript" src="/src/formations.js"></script>
    <script type="text/javascript" src="/src/editor.js"></script>
</head>
<body>
<div id="cr-stage"></div>
<div id="wrapper">
    <div id="top">
        <div style="width:200px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/Coins.png" style="width:22px;" /> <span id="globalCoins">10</span> (<span id="globalBalance" class="green">10</span>)</div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/People.png" style="width:22px;" /> <span id="globalPeople">100</span></div>
    </div>
    <div id="header">
        <ul>
            <li class="menus">
                <a id="menu1">Ressourcen</a>
            </li>
            <li class="menus">
                <a id="menu2">Produktion</a>
            </li>
            <li class="menus">
                <a id="menu3">Geb&auml;ude</a>
            </li>
            <li class="menus">
                <a id="menu4">Einheiten</a>
            </li>
            <li class="menus">
                <a id="menu5" href="/logout">logout</a>
            </li>
        </ul>
    </div>
    <div id="body" style="display: none;">
    </div>
    <div id="controls"><button style="float:left;" onclick="SelectionMode = 0; $('#cr-stage').css('cursor', 'default');">Auswahl</button>
        <button style="margin-left:5px; float:left;" onclick="SelectionMode = 1; $('#cr-stage').css('cursor', 'url(assets/cities/1.png), crosshair');">Stadt</button>
        <button style="margin-left:5px; float:left;" onclick="SelectionMode = 2; $('#cr-stage').css('cursor', 'crosshair');">Weg</button></div>
</div>
</body>
</html>