function openMenu(path)
{
    if(path != null)
    {
        $.get(path, function(data) {
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

function openControl(path, title)
{
    if($("#controls").dialog("isOpen"))
        $("#controls").dialog("close");
    if(path != null)
    {
        $.get(path, function(data) {
            $("#controls").html(data);
            $("#controls").dialog("option", "title", title);
            if(!$("#controls").dialog("isOpen"))
                $("#controls").dialog("open");
        });
    }
}

function closeControl()
{
    $("#controls").dialog("close");
}