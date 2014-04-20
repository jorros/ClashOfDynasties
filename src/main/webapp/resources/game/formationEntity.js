function formationEntity()
{
    Crafty.c("Formation", {
        _fid: 0,
        _textEntity: {},

        _buildInfo: function() {
            this._textEntity = Crafty.e("2D, DOM, Text").attr({
                x: this._x,
                y: this._y - 40,
                w: 220,
                h: 35,
                z: 11
            }).textFont({ size: "24px", family: "Philosopher-Regular" }).unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

            this.attach(this._textEntity);
        },

        _updateInfo: function() {
            this._textEntity.text(Formations[this._fid].name);
        },

        _updateDiplomacy: function() {
            if(Formations[this._fid].diplomacy == 1) // Selbst
                this._textEntity.css("color", "#4096EE");
            else if(Formations[this._fid].diplomacy == 2) // Verbündet
                this._textEntity.css("color", "#356AA0");
            else if(Formations[this._fid].diplomacy == 3) // Verfeindet
                this._textEntity.css("color", "#D01F3C");
            else if(Formations[this._fid].diplomacy == 4) // Neutral
                this._textEntity.css("color", "#EEEEEE");
        },

        showRoute: function(to) {
            hideRoute();

            if(to == undefined) {
                tempRouteEntity = Crafty.e("Road").temp(Selected._fid, Formations[Selected._fid].route.next);
                tempRoute = Formations[Selected._fid].route.roads;
                tempTime = Formations[Selected._fid].route.time;

                $.each(tempRoute, function (index, road) {
                    RoadEntities[road].mark(true);
                });
            }
            else {
                tempRouteEntity = "";
                tempRoute = "";
                $.getJSON("game/formations/" + Selected._fid + "/route", { "target": to }, function(data)
                {
                    isCalculatedRoute = true;
                    tempRouteEntity = Crafty.e("Road").temp(Selected._fid, data.next);
                    tempRoute = data.roads;
                    tempTime = data.time;

                    var totalSeconds = tempTime;
                    var hours = Math.floor(totalSeconds / 3600);
                    totalSeconds %= 3600;
                    var minutes = Math.floor(totalSeconds / 60);

                    var output = "";
                    if(hours > 0)
                        output += hours + " Stunden ";
                    if(minutes > 0)
                        output += minutes + " Minuten";

                    $(document).data('powertip' , output);
                    $.powerTip.show($(document));

                    $.each(tempRoute, function(index, road) {
                        RoadEntities[road].mark(true);
                    });
                });
            }
            routeShown = true;
        },

        select: function() {
            deselect();
            Selected = this;

            if(Formations[this._fid].diplomacy == 1)
                isFormationSelected = true;

            if(Formations[this._fid].route != undefined)
                this.showRoute();

            openCommand('formation?formation=' + this._fid, Formations[this._fid].name);
        },

        deselect: function() {
            isFormationSelected = false;
        },

        init: function() {
            this.requires("2D, Canvas, Image, Mouse");
        },

        formation: function(id) {
            this._fid = id;
            this.z = 12;

            this.image("assets/Formation.png");
            this.w = 60;
            this.h = 52;

            // Selektionsanbindung
            this.bind("Click", this.select);

            this._buildInfo();
            this._updateInfo();

            return this;
        },

        update: function() {
            if(Formations[this._fid] == undefined) {
                this.destroy();
                delete FormationEntities[this._fid];
            }
            else
            {
                this.x = Math.round(Formations[this._fid].x - this._w / 2);
                this.y = Math.round(Formations[this._fid].y - this._h / 2);

                if(Formations[this._fid].deployed) {
                    this.visible = false;
                    this._textEntity.visible = false;
                }
                else {
                    this.visible = true;
                    this._textEntity.visible = true;
                }

                this._updateDiplomacy();
                this._updateInfo();
            }

            return this;
        }
    });
}