$(document).ready(function() {
    $(".menus").click(function() {
        var selected = !$(this).hasClass("selected");
        $(".menus").removeClass("selected");

        if(selected)
        {
            $(this).addClass("selected");

            if($(this).find("a").attr("id") == "menu1")
                openMenu("editresources");
            else if($(this).find("a").attr("id") == "menu2")
                openMenu("editproduction");
            else if($(this).find("a").attr("id") == "menu3")
                openMenu("editbuildings");
            else if($(this).find("a").attr("id") == "menu4")
                openMenu("editunits");
        }
        else
        {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: true });
});