var Editor = false;
var Selected = null;
var isFormationSelected = false;
var isCalculatedRoute = false;

var tempRoute = null;
var tempRouteEntity = null;
var tempTime = 0;
var routeShown = false;

var lastUpdate = 0;

window.onload = function() {
    Crafty.init();
    Crafty.canvas.init();

    console.log("Crafty " + Crafty.getVersion());

    Crafty.scene("loading", function() {
        var logo = Crafty.e("2D, DOM, Image").image("/images/Logo.png", "no-repeat").attr({ x: Crafty.viewport.width / 2 - 200, y: Crafty.viewport.height / 2 - 100 });
        var progress = Crafty.e("2D, DOM, Color").color("#FFF").attr({ x: logo.x, y: logo.y + 200, w: 0, h: 15 });

        Crafty.load(Assets, function() {
                Crafty.scene("main");
            },

            function(e) {
                progress.w = e.percent * 4;
            }),

            function(e) {
                console.log("Fehler beim Laden");
            }
    });

    Crafty.scene("main", function() {
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
            .bind("MouseDown", function(e)
            {
                Crafty.viewport.mouselook('start', e);
                lastViewX = Crafty.viewport.x;
                lastViewY = Crafty.viewport.y;
            })
            .bind("MouseMove", function(e)
            {
                Crafty.viewport.mouselook('drag', e);

                if(Selected != null && Selected._fid != null) {
                    if(isCalculatedRoute && !Formations[Selected._fid].deployed) {
                        isCalculatedRoute = false;
                        $.powerTip.hide();
                        Selected.showRoute();
                    }
                    else if(isCalculatedRoute) {
                        isCalculatedRoute = false;
                        hideRoute();
                        $.powerTip.hide();
                    }
                }
            })
            .bind("MouseUp", function()
            {
                Crafty.viewport.mouselook('stop');

                if(lastViewX == Crafty.viewport.x && lastViewY == Crafty.viewport.y)
                {
                    deselect();
                }
            })

        // Initialisiere
        cityEntity();
        roadEntity();
        formationEntity();
        loadTop();
        updateCities();
        updateFormations();

        $(document).powerTip({smartPlacement: true, followMouse: true, manual: true});

        // Update Callback
        var updateCallback = function()
        {
            loadTop();
            updateCities();
            updateFormations();
            updateTimestamp();

            window.setTimeout(updateCallback, 10000);
        }
        window.setTimeout(updateCallback, 10000);
    });

    Crafty.scene("loading");
}