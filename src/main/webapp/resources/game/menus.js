$(document).ready(function () {
    $(".menus").click(function () {
        var selected = !$(this).hasClass("selected");
        $(".menus").removeClass("selected");

        if (selected) {
            $(this).addClass("selected");

            if ($(this).find("a").attr("id") == "menu1")
                openMenu("ranking");
            else if ($(this).find("a").attr("id") == "menu2")
                openMenu("settings");
            else if ($(this).find("a").attr("id") == "menu3")
                openMenu("demography");
            else if ($(this).find("a").attr("id") == "menu4")
                openMenu("diplomacy");
        }
        else {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close no-titlebar", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: false });
});