$(document).ready(function() {
    $(".menus").click(function() {
        var selected = !$(this).hasClass("selected");
        $(".menus").removeClass("selected");

        if(selected)
        {
            $(this).addClass("selected");

            if($(this).find("a").attr("id") == "menu1")
                openMenu("game/menu/ranking");
            else if($(this).find("a").attr("id") == "menu2")
                openMenu("game/menu/settings");
            else if($(this).find("a").attr("id") == "menu3")
                openMenu("game/menu/demography");
            else if($(this).find("a").attr("id") == "menu4")
                openMenu("game/menu/diplomacy");
        }
        else
        {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: false });
});