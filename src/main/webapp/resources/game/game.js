var Editor = false;
var Selected = null;
var isFormationSelected = false;

var tempRoute = null;
var tempRouteEntity = null;

window.onload = function() {
    Crafty.init();
    Crafty.canvas.init();

    console.log("Crafty " + Crafty.getVersion());

    Crafty.scene("loading", function() {
       Crafty.background = "#000";

        var txt = Crafty.e("2D, DOM, Text").attr({
            x: 0,
            y: 0
        }).text("Laden (" + 0 + "%)").textColor("#FFF", 1);

        Crafty.load(["assets/cities/3.png", "assets/Formation.png", "assets/map.jpg", "assets/cities/5.png", "assets/cities/2.png", "assets/cities/1.png", "assets/cities/4.png"], function() {
            Crafty.scene("main");
        },

            function(e) {
            txt.text("Laden (" + e.percent + "%)");
        }),

            function(e) {
                txt.text("Fehler beim Laden");
            }
    });

    Crafty.scene("main", function() {
        Crafty.background = "#000";

        Crafty.viewport.mouselook(true);

        var lastViewX = 0;
        var lastViewY = 0;

        Crafty.e("2D, Canvas, Image, Mouse").attr({
            x: 0,
            y: 0,
            w: 5100,
            h: 3600
        }).image("assets/map.jpg")
            .bind("MouseDown", function(e)
            {
                Crafty.viewport.mouselook('start', e);
                lastViewX = Crafty.viewport.x;
                lastViewY = Crafty.viewport.y;
            })
            .bind("MouseMove", function(e)
            {
                Crafty.viewport.mouselook('drag', e);

                if(tempRouteEntity != null) {
                    tempRouteEntity.destroy();
                    tempRouteEntity = null;
                }

                if(tempRoute != null)
                {
                    $.each(tempRoute, function(index, road) {
                        RoadEntities[road.id].mark(false);
                    });

                    tempRoute = null;
                }
            })
            .bind("MouseUp", function()
            {
                Crafty.viewport.mouselook('stop');

                if(lastViewX == Crafty.viewport.x && lastViewY == Crafty.viewport.y)
                {
                    if(Selected != null)
                        Selected.deselect();

                    closeMenu();
                    closeControl();
                }
            })

        // Initialisiere
        cityEntity();
        roadEntity();
        formationEntity();
        loadTop();
        updateCities();
        updateFormations();

        // Update Callback
        var updateCallback = function()
        {
            loadTop();
            updateCities();
            updateFormations();
            window.setTimeout(updateCallback, 1000);
        }
        window.setTimeout(updateCallback, 1000);
    });

    Crafty.scene("loading");
}