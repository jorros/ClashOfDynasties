var Editor = true;
var SelectionMode = 0;
var Selected = null;
var SelectedWay = null;

window.onload = function () {
    Crafty.init();
    Crafty.canvas.init();

    Crafty.scene("loading", function () {
        Crafty.background = "#000";

        var txt = Crafty.e("2D, DOM, Text").attr({
            x: 0,
            y: 0
        }).text("Laden (" + 0 + "%)").textColor("#FFF", 1);

        Crafty.load(Assets, function () {
                Crafty.scene("main");
            },

            function (e) {
                txt.text("Laden (" + e.percent + "%)");
            }),

            function (e) {
                txt.text("Fehler beim Laden");
            }
    });

    Crafty.scene("main", function () {
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
            .bind("MouseDown", function (e) {
                Crafty.viewport.mouselook('start', e);
                lastViewX = Crafty.viewport.x;
                lastViewY = Crafty.viewport.y;
            })
            .bind("MouseMove", function (e) {
                Crafty.viewport.mouselook('drag', e);
            })
            .bind("MouseUp", function (e) {
                Crafty.viewport.mouselook('stop');

                if (lastViewX == Crafty.viewport.x && lastViewY == Crafty.viewport.y) {
                    if (Selected != null)
                        Selected.deselect();

                    closeMenu();

                    if (SelectionMode == 1) {
                        $.post("/game/cities", { x: e.realX, y: e.realY});
                    }
                }
            })

        // Initialisiere
        cityEntity();
        roadEntity();
        updateCities();

        $("#cr-stage").on("mousewheel", function(event) {
            var calcScale = Crafty.viewport._scale + (event.deltaY / 10);
            if(calcScale < 1 && calcScale > 0.5)
                Crafty.viewport.scale(calcScale);
            else if(calcScale > 1)
                Crafty.viewport.scale(1);
            else if(calcScale < 0.5)
                Crafty.viewport.scale(0.5);
        });

        // Update Callback
        var updateCallback = function () {
            updateCities();
            updateTimestamp();
            window.setTimeout(updateCallback, 10000);
        }
        window.setTimeout(updateCallback, 10000);
    });

    Crafty.scene("loading");
}