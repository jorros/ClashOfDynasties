var Editor = true;
var SelectionMode = 0;
var Selected = null;
var SelectedWay = null;
var timeoutID;

window.onload = function () {
    Crafty.init();
    Crafty.canvas.init(5100, 3600);

    console.log("Crafty " + Crafty.getVersion());

    Crafty.scene("loading", function () {
        var logo = Crafty.e("2D, DOM, Image").image("/images/Logo.png", "no-repeat").attr({ x: Crafty.viewport.width / 2 - 200, y: Crafty.viewport.height / 2 - 100 });
        var progress = Crafty.e("2D, DOM, Color").color("#FFFFFF").attr({ x: logo.x, y: logo.y + 200, w: 0, h: 15 });

        Crafty.load(Assets, function () {
                loadGame(function() {
                    Crafty.scene("main");
                });
            },
            function (e) {
                progress.w = e.percent * 4;
            },
            function (e) {
                console.log("Fehler beim Laden");
            });
    });

    Crafty.scene("main", function () {
        Crafty.background = "#000";

        Crafty.viewport.mouselook(true);

        var lastViewX = 0;
        var lastViewY = 0;

        $("#cr-stage").mousedown(function(e) {
            Crafty.viewport.mouselook('start', e);
            lastViewX = Crafty.viewport.x;
            lastViewY = Crafty.viewport.y;
        });

        $("#cr-stage").mousemove(function(e) {
            Crafty.viewport.mouselook('drag', e);
        });

        $("#cr-stage").mouseup(function() {
            Crafty.viewport.mouselook('stop');

            if (lastViewX != Crafty.viewport.x || lastViewY != Crafty.viewport.y) {
                $.put("/game/core/scroll", { x: Math.round(Crafty.viewport.x), y: Math.round(Crafty.viewport.y) });
            }
        });

        Crafty.e("2D, Canvas, Image, Mouse").attr({
            x: 0,
            y: 0,
            w: 5100,
            h: 3600
        }).image("assets/map.jpg")
            .bind("MouseUp", function (e) {
                if (lastViewX == Crafty.viewport.x && lastViewY == Crafty.viewport.y) {
                    if (Selected != null)
                        Selected.deselect();

                    closeMenu();

                    if (SelectionMode == 1) {
                        $.post("/game/cities", { x: e.realX + 30, y: e.realY + 30}, function() {
                            forceUpdate();
                        });
                    }
                }
            });

        $("#cr-stage").on("mousewheel", function(event) {
            var calcScale = Crafty.viewport._scale + (event.deltaY / 10);
            if(calcScale < 1 && calcScale > 0.5)
                Crafty.viewport.scale(calcScale);
            else if(calcScale > 1)
                Crafty.viewport.scale(1);
            else if(calcScale < 0.5)
                Crafty.viewport.scale(0.5);
        });

        updateGameEntities();

        Crafty.viewport.x = tempScrollX;
        Crafty.viewport.y = tempScrollY;

        timeoutID = window.setInterval(updateGame, 5000);
    });

    Crafty.scene("loading");
}