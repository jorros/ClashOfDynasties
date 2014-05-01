<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <link rel="stylesheet" href="/css/jquery-ui-1.10.4.custom.css" type="text/css">
    <link rel="stylesheet" href="/css/jquery.contextMenu.css" type="text/css">
    <link rel="stylesheet" href="/css/jquery.powertip.min.css" type="text/css">
    <script type="text/javascript" src="/lib/jquery-2.1.0.js"></script>
    <script type="text/javascript" src="/lib/jquery-ui-1.10.4.custom.js"></script>
    <script type="text/javascript" src="/lib/jquery.contextMenu.js"></script>
    <script type="text/javascript" src="/lib/jquery.multisortable.js"></script>
    <script type="text/javascript" src="/lib/crafty-min.js"></script>
    <script type="text/javascript" src="/lib/jquery.powertip.min.js"></script>
    <script type="text/javascript" src="/lib/jquery.mousewheel.min.js"></script>
    <script type="text/javascript" src="/lib/Chart.min.js"></script>
    <script type="text/javascript" src="/src/assets.js"></script>
    <script type="text/javascript" src="/src/helper.js"></script>
    <script type="text/javascript" src="/src/menus.js"></script>
    <script type="text/javascript" src="/src/top.js"></script>
    <script type="text/javascript" src="/src/cities.js"></script>
    <script type="text/javascript" src="/src/cityEntity.js"></script>
    <script type="text/javascript" src="/src/roadEntity.js"></script>
    <script type="text/javascript" src="/src/formationEntity.js"></script>
    <script type="text/javascript" src="/src/formations.js"></script>
    <script type="text/javascript" src="/src/caravanEntity.js"></script>
    <script type="text/javascript" src="/src/caravans.js"></script>
    <script type="text/javascript" src="/src/roads.js"></script>
    <script type="text/javascript" src="/src/game.js"></script>
</head>
<body>
<div id="cr-stage">
</div>
<div id="wrapper">
    <div id="top">
        <div style="width:200px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/Coins.png" style="vertical-align:bottom; width:22px;" /> <span id="globalCoins">0</span> (<span id="globalBalance" class="green">0</span>)</div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/People.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalPeople">0</span></div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/Cities.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalCities">0</span></div>
    </div>
    <div id="header">
        <ul>
            <li class="menus">
                <a id="menu1">ranking</a>
            </li>
            <li class="menus">
                <a id="menu2">profil</a>
            </li>
            <li class="menus">
                <a id="menu3">demographie</a>
            </li>
            <li class="menus">
                <a id="menu4">diplomatie</a>
            </li>
            <li class="menus">
                <a id="menu5" href="/logout">logout</a>
            </li>
        </ul>
    </div>
    <div id="body" style="display: none;">
    </div>
    <div id="controls"></div>
    <div id="events"></div>
    <span id="caravanText" style="position:absolute; width:200px; margin:auto auto; left:50%; top:80%; font-family:'Philosopher-Bold'; font-size:28px; color:#000; text-align:center; z-index:30; display:none;">W&auml;hle eine Zielstadt aus!</span>
</div>
<script>
    $(function() {
        $("#events").css("height", window.innerHeight - 140 + "px");
    });
</script>
</body>
</html>