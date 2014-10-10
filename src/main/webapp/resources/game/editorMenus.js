$(document).ready(function () {
    $(".menu").click(function () {
        var selected = !$(this).hasClass("selected");
        $(".menu").removeClass("selected");

        if (selected) {
            $(this).addClass("selected");

            if ($(this).attr("id") == "menu1")
                openMenu("editcity", false);
            else if ($(this).attr("id") == "menu2")
                openMenu("editplayers", false);
            else if ($(this).attr("id") == "menu3")
                openMenu("editbuildings", false);
            else if ($(this).attr("id") == "menu4")
                openMenu("editunits", false);
            else if ($(this).attr("id") == "menu5")
                window.location = "/logout";
        }
        else {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: true });
});