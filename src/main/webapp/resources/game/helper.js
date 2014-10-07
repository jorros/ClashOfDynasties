var currentMenu = undefined;
var currentMenuRefresh = true;
var isRefreshing = false;

var tempScrollX = 0;
var tempScrollY = 0;

function openMenu(menu, refresh) {
    if (menu != undefined) {
        if(refresh != undefined)
            currentMenuRefresh = refresh;
        else
            currentMenuRefresh = true;
        currentMenu = menu;

        $.get("/game/menus/" + menu, function (data) {
            if(currentMenu == menu) {
                isRefreshing = true;
                $("#body").html(data).show();
                isRefreshing = false;
            }
        });
    }
}

function closeMenu() {
    $("#body").hide();
    $(".menu").removeClass("selected");
    currentMenu = undefined;
}

var currentCommand = undefined;

function openCommand(command, title) {
    if ($("#controls").dialog("isOpen") && command != currentCommand)
        $("#controls").dialog("close");
    if (command != undefined) {
        currentCommand = command;

        $.get("/game/commands/" + command, function (data) {
            if(currentCommand == command) {
                $("#controls").html(data);
                if (!$("#controls").dialog("isOpen"))
                    $("#controls").dialog("open");
            }
        });
    }
}

function hideRoute() {
    if (!Editor) {
        if (tempRouteEntity != null && tempRouteEntity != "") {
            tempRouteEntity.destroy();
        }

        if (tempRoute != null && tempRouteEntity != "") {
            $.each(tempRoute, function (index, road) {
                RoadEntities[road].mark(false);
            });
        }

        tempRouteEntity = null;
        tempRoute = null;
        routeShown = false;
    }
}

function deselect() {
    if (Selected != null)
        Selected.deselect();

    closeMenu();
    closeControl();

    hideRoute();
}

function closeControl() {
    $("#controls").dialog("close");
    currentCommand = undefined;
}

function sendDelete (url, data, callback) {
    var c = url;

    if(data != undefined)
        c += "?" + $.param(data);
    $.ajax(c, { type: "DELETE", success: function (data, textStatus, jqXHR) {
        if (callback != undefined)
            callback(data);
    }});
}

function loadGame(callback) {
    $.when(updateGameContent(), $.getJSON("/game/core/scroll", function(data) {
        tempScrollX = data.x;
        tempScrollY = data.y;
    })).done(function () {
        if (callback != undefined) {
            callback();
        }
    });
}

function updateGame() {
    $.when(updateGameContent()).done(function() {
        updateGameEntities();
    });

    if(!Editor) {
        if (currentMenuRefresh && currentMenu != undefined && !stopMenuUpdate)
            openMenu(currentMenu);

        if (currentCommand != undefined)
            openCommand(currentCommand);
    }
}

function getColor(colorID) {
    switch(colorID) {
        case 0:
            return "#EEEEEE";

        case 1:
            return "#4096EE";

        case 2:
            return "#D01F3C";

        case 3:
            return "#F5B800";

        case 4:
            return "#CCFF33";

        case 5:
            return "#33FF66";

        case 6:
            return "#FF794D";

        case 7:
            return "#FF4D79";

        case 8:
            return "#B88A00";

        case 9:
            return "#FE6F71";

        case 10:
            return "#CCCCCC";

        case 11:
            return "#E45EA7";

        case 12:
            return "#7DB2AB";

        case 13:
            return "#D8FBE2";

        case 14:
            return "#E8F5BB";

        case 15:
            return "#6CDFEA";

        case 16:
            return "#84C9FF";

        case 17:
            return "#7FAF1B";

        case 18:
            return "#D9FFA9";
    }
}

function forceUpdate() {
    window.clearInterval(timeoutID);
    updateGame();
    timeoutID = window.setInterval(updateGame, 5000);
}

(function ($) {
    $.put = function (url, data, callback) {
        $.ajax(url + "?" + $.param(data), { type: "PUT", success: function (data, textStatus, jqXHR) {
            if (callback != undefined)
                callback(data);
        }});
    };

    $.delete = function (url, data, callback) {
        if(data == undefined) {
            sendDelete(url);
        } else if(typeof(data) == "function") {
            sendDelete(url, {}, data);
        } else {
            sendDelete(url, data, callback);
        }
    }

}(jQuery));