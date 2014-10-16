<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <link rel="stylesheet" href="/css/jquery-ui-1.10.4.custom.css" type="text/css" />
    <link rel="stylesheet" href="/css/jquery.contextMenu.css" type="text/css" />
    <link rel="stylesheet" href="/css/jquery.powertip.min.css" type="text/css" />
    <link rel="stylesheet" href="/css/tooltipster.css" type="text/css" />
    <link rel="stylesheet" href="/css/tooltipster-light.css" type="text/css" />
    <script type="text/javascript" src="/lib/jquery-2.1.0.js"></script>
    <script type="text/javascript" src="/lib/jquery-ui-1.10.4.custom.js"></script>
    <script type="text/javascript" src="/lib/jquery.contextMenu.js"></script>
    <script type="text/javascript" src="/lib/jquery.multisortable.js"></script>
    <script type="text/javascript" src="/lib/crafty-min.js"></script>
    <script type="text/javascript" src="/lib/jquery.powertip.min.js"></script>
    <script type="text/javascript" src="/lib/jquery.mousewheel.min.js"></script>
    <script type="text/javascript" src="/lib/jquery.tooltipster.min.js"></script>
    <script type="text/javascript" src="/lib/Chart.min.js"></script>
    <script type="text/javascript" src="/src/assets.js"></script>
    <script type="text/javascript" src="/src/helper.js"></script>
    <script type="text/javascript" src="/src/menus.js"></script>
    <script type="text/javascript" src="/src/update.js"></script>
    <script type="text/javascript" src="/src/cityEntity.js"></script>
    <script type="text/javascript" src="/src/roadEntity.js"></script>
    <script type="text/javascript" src="/src/formationEntity.js"></script>
    <script type="text/javascript" src="/src/caravanEntity.js"></script>
    <script type="text/javascript" src="/src/game.js"></script>
</head>
<body>
<div id="cr-stage">
</div>
<div id="wrapper">
    <div id="top">
        <div style="width:200px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/top/Coins.png" style="vertical-align:bottom; width:22px;" /> <span id="globalCoins">0</span> (<span id="globalBalance" class="green">0</span>)</div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/top/People.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalPeople">0</span></div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/top/Cities.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalCities">0</span></div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/top/Formations.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalFormations">0</span></div>
        <div style="width:100px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/top/Camels.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalCaravans">0</span></div>
        <div style="width:150px; margin-left:10px; margin-top:2px; float:left;"><img src="assets/top/Ranking.png" style="vertical-align:bottom; width:22px;" /> <span style="" id="globalRanking">0</span></div>
        <div style="width:80px; margin-right:10px; margin-top:4px; float:right; text-align:right;" class="logout"><a href="/logout">Logout</a></div>
    </div>
    <div id="header">
        <div id="menu1" class="menu menu-orange"><a>ranking</a></div>
        <div id="menu2" class="menu menu-yellow"><a>profil</a></div>
        <div id="menu3" class="menu menu-cyan"><a>mein reich</a></div>
        <div id="menu4" class="menu menu-green"><a>diplomatie</a></div>
        <div id="menu5" class="menu menu-red"><a>nachrichten</a></div>
    </div>
    <div id="body" style="display: none;"></div>
    <div id="objectives"></div>
    <div id="controls"></div>
    <div id="events"></div>
    <span id="caravanText" style="position:absolute; width:200px; margin:auto auto; left:50%; top:80%; font-family:'Philosopher-Bold'; font-size:28px; color:#000; text-align:center; z-index:30; display:none;">W&auml;hle eine Zielstadt aus!</span>
</div>
<script>
    $(function() {
        $("#events").css("height", window.innerHeight - 50 + "px");
    });
</script>
</body>
</html>