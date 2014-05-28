$(document).ready(function () {
    $(".menu").click(function () {
        var selected = !$(this).hasClass("selected");
        $(".menu").removeClass("selected");

        if (selected) {
            $(this).addClass("selected");

            if ($(this).attr("id") == "menu1")
                openMenu("editcity");
            else if ($(this).attr("id") == "menu2")
                openMenu("editplayers");
            else if ($(this).attr("id") == "menu3")
                openMenu("editbuildings");
            else if ($(this).attr("id") == "menu4")
                openMenu("editunits");
            else if ($(this).attr("id") == "menu5")
                window.location = "/logout";
        }
        else {
            closeMenu();
        }
    });

    $("#controls").dialog({ width: 400, dialogClass: "no-close", position: { my: "left top", at: "left bottom" }, draggable: false, autoOpen: true });
});