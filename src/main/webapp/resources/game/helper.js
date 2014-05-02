function openMenu(menu) {
    if (menu != undefined) {
        $.get("/game/menus/" + menu, function (data) {
            $("#body").html(data);
            $("#body").show();
        });
    }
}

function closeMenu() {
    $("#body").hide();
    $(".menu").removeClass("selected");
}

function openCommand(command, title) {
    if ($("#controls").dialog("isOpen"))
        $("#controls").dialog("close");
    if (command != undefined) {
        $.get("/game/commands/" + command, function (data) {
            $("#controls").html(data);
            $("#controls").dialog("option", "title", title);
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
}

function updateTimestamp() {
    $.get("/game/menus/timestamp", function (data) {
        lastUpdate = data;
    });
}

function sendDelete (url, data, callback) {
    $.ajax(url + "?" + $.param(data), { type: "DELETE", success: function (data, textStatus, jqXHR) {
        if (callback != undefined)
            callback(data);
    }});
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