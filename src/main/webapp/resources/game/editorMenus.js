$(document).ready(function() {
    $(".menus").click(function() {
        var selected = !$(this).hasClass("selected");
        $(".menus").removeClass("selected");

        if(selected)
        {
            $(this).addClass("selected");

            if($(this).find("a").attr("id") == "menu1")
                openMenu("editor/menu/resource");
            else if($(this).find("a").attr("id") == "menu2")
                openMenu("editor/menu/production");
            else if($(this).find("a").attr("id") == "menu3")
                openMenu("editor/menu/building");
            else if($(this).find("a").attr("id") == "menu4")
                openMenu("editor/menu/unit");
        }
        else
        {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: true });
});