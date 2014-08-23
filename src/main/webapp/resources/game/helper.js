var currentMenu = undefined;
var currentMenuRefresh = true;
var isRefreshing = false;

function openMenu(menu, refresh) {
    if (menu != undefined) {
        if(refresh != undefined)
            currentMenuRefresh = refresh;
        else
            currentMenuRefresh = true;
        currentMenu = menu;

        $.get("/game/menus/" + menu, function (data) {
            isRefreshing = true;
            $("#body").html(data);
            $("#body").show();
            isRefreshing = false;
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
            $("#controls").html(data);
            if (!$("#controls").dialog("isOpen"))
                $("#controls").dialog("open");
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

function updateTimestamp() {
    return $.get("/game/menus/timestamp", function (data) {
        lastUpdate = data;
    });
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
    if(!Editor) {
        $.when(loadTop(true), updateCities(true), updateFormations(true), updateCaravans(true), updateTimestamp(true)).done(function () {
            if (callback != undefined) {
                callback();
            }
        });
    } else {
        $.when(updateCities(true), updateTimestamp(true)).done(function () {
            if (callback != undefined) {
                callback();
            }
        });
    }
}

function updateGame() {
    if(!Editor) {
        loadTop();
        updateCities();
        updateFormations();
        updateCaravans();
        updateTimestamp();

        if (currentMenuRefresh && currentMenu != undefined && !stopMenuUpdate)
            openMenu(currentMenu);

        if (currentCommand != undefined)
            openCommand(currentCommand);
    } else {
        updateCities();
        updateTimestamp();
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