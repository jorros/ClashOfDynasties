var Editor = false;
var Selected = null;
var isFormationSelected = false;
var isCaravanSelected = false;
var isCalculatedRoute = false;
var timeoutID;

var tempRoute = null;
var tempRouteEntity = null;
var tempTime = 0;
var routeShown = false;
var stopMenuUpdate = false;

window.onload = function () {
    Crafty.init();
    Crafty.canvas.init();

    console.log("Crafty " + Crafty.getVersion());

    Crafty.scene("loading", function () {
        var logo = Crafty.e("2D, DOM, Image").image("/images/Logo.png", "no-repeat").attr({ x: Crafty.viewport.width / 2 - 200, y: Crafty.viewport.height / 2 - 100 });
        var progress = Crafty.e("2D, DOM, Color").color("#FFF").attr({ x: logo.x, y: logo.y + 200, w: 0, h: 15 });

        Crafty.load(Assets, function () {
                loadGame(function() {
                    Crafty.scene("main");
                });
            },

            function (e) {
                progress.w = e.percent * 4;
            }),

            function (e) {
                console.log("Fehler beim Laden");
            }
    });

    Crafty.scene("main", function () {
        Crafty.background("#000");

        Crafty.viewport.mouselook(true);

        var lastViewX = 0;
        var lastViewY = 0;

        Crafty.e("2D, Canvas, Image, Mouse").attr({
            x: 0,
            y: 0,
            w: 5100,
            h: 3600,
            z: 0
        }).image("assets/map.jpg")
            .bind("MouseDown", function (e) {
                Crafty.viewport.mouselook('start', e);
                lastViewX = Crafty.viewport.x;
                lastViewY = Crafty.viewport.y;
            })
            .bind("MouseMove", function (e) {
                Crafty.viewport.mouselook('drag', e);

                if (Selected != null && Selected._fid != null) {
                    if (isCalculatedRoute && !Formations[Selected._fid].deployed) {
                        isCalculatedRoute = false;
                        $.powerTip.hide();
                        Selected.showRoute();
                    }
                    else if (isCalculatedRoute) {
                        isCalculatedRoute = false;
                        hideRoute();
                        $.powerTip.hide();
                    }
                }
                else if (Selected != null && Selected.has("City") && isCaravanSelected) {
                    hideRoute();
                    $.powerTip.hide();
                }
            })
            .bind("MouseUp", function () {
                Crafty.viewport.mouselook('stop');

                if (lastViewX == Crafty.viewport.x && lastViewY == Crafty.viewport.y) {
                    if (isCaravanSelected) {
                        isCaravanSelected = false;
                        $("#caravanText").hide();
                    }
                    else
                        deselect();
                } else {
                    $.put("/game/core/scroll", { x: Math.round(Crafty.viewport.x), y: Math.round(Crafty.viewport.y) });
                }
            })

        $(document).powerTip({smartPlacement: true, followMouse: true, manual: true});

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

        // Update Callback
        timeoutID = window.setInterval(updateGame, 5000);
    });

    Crafty.scene("loading");
}