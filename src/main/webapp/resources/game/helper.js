function openMenu(menu)
{
    if(menu != undefined)
    {
        $.get("/game/menu/" + menu, function(data) {
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
        $.get("/game/command/" + command, function(data) {
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