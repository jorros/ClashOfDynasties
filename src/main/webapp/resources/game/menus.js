$(document).ready(function () {
    $(".menu").click(function () {
        var selected = !$(this).hasClass("selected");
        $(".menu").removeClass("selected");

        if (selected) {
            $(this).addClass("selected");

            if ($(this).attr("id") == "menu1")
                openMenu("ranking", false);
            else if ($(this).attr("id") == "menu2")
                openMenu("settings", false);
            else if ($(this).attr("id") == "menu3")
                openMenu("demography", false);
            else if ($(this).attr("id") == "menu4")
                openMenu("diplomacy", false);
            else if ($(this).attr("id") == "menu5")
                openMenu("messages", false);
        }
        else {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close no-titlebar", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: false });
});