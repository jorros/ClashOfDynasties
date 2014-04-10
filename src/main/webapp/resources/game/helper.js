function openMenu(menu)
{
    if(menu != undefined)
    {
        $.get("/game/menus/" + menu, function(data) {
            $("#body").html(data);
            $("#body").show();
        });
    }
}

function closeMenu()
{
    $("#body").hide();
    $(".menus").removeClass("selected");
}

function openCommand(command, title)
{
    if($("#controls").dialog("isOpen"))
        $("#controls").dialog("close");
    if(command != undefined)
    {
        $.get("/game/commands/" + command, function(data) {
            $("#controls").html(data);
            $("#controls").dialog("option", "title", title);
            if(!$("#controls").dialog("isOpen"))
                $("#controls").dialog("open");
        });
    }
}

function hideRoute()
{
    if(tempRouteEntity != null && tempRouteEntity != "") {
        tempRouteEntity.destroy();
    }

    if(tempRoute != null && tempRouteEntity != "") {
        $.each(tempRoute, function(index, road) {
            RoadEntities[road.id].mark(false);
        });
    }

    tempRouteEntity = null;
    tempRoute = null;
    routeShown = false;
}

function deselect()
{
    if(Selected != null)
        Selected.deselect();

    closeMenu();
    closeControl();

    hideRoute();
}

function closeControl()
{
    $("#controls").dialog("close");
}

(function ($)
{
    $.put = function(url, data, callback) {
        var first = true;
        $.each(data, function(param, value) {
            if(first) {
                url += "?";
                first = false;
            }
            else
                url += "&";

            url += param + "=" + value
        })
        $.ajax(url, { type: "PUT", success: function(data, textStatus, jqXHR) {
            if(callback != undefined)
                callback(data);
        }});
    };

    $.delete = function(url, callback) {
        $.ajax(url, { type: "DELETE", success: function(data, textStatus, jqXHR) {
            if(callback != undefined)
                callback(data);
        }});
    }

}( jQuery ));