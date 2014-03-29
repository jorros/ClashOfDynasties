function cityEntity()
{
    Crafty.c("City", {
        _cid: 0,
        _infoEntity: {},
        _formationsInfoEntity: {},

        _getMenu: function($trigger, e) {
            var items = {};

            var id = $trigger.find("span").attr("id").slice(0, -11);

            $.each(Cities[id].formations, function(key, val) {
                items[val.id] = { name: val.name, icon: "edit" };
            });

            return {
                callback: function(key, options) {
                    FormationEntites[key].select();
                },
                items: items
            }
        },

        _buildInfo: function() {
            this._infoEntity = Crafty.e("2D, DOM").attr({
                w: 270,
                h: 35,
                x: (this.getX() - this._infoEntity._w / 2),
                y: (this._y - 40),
                z: 11
            }); //.text(this._name).textFont("size", "24px").textFont("family", "Philosopher-Regular").unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

            this._infoEntity.css({ "border-radius": "20px", "-moz-border-radius": "20px", "border": "1px #000 solid", "text-align": "center", "color": "#000", "font-family": "Philosopher-Regular", "font-size": "18px", "line-height": "35px", "vertical-align": "middle" });
            this.attach(this._infoEntity);

            this._formationsInfoEntity = Crafty.e("2D, DOM").attr({
                w: 50,
                h: 20,
                x: this._infoEntity.x + 25,
                y: (this._y - 75),
                z: 11
            });

            this._formationsInfoEntity.css({
                "-webkit-border-top-left-radius": "20px",
                "-webkit-border-top-right-radius": "20px",
                "-moz-border-radius-topleft": "20px",
                "-moz-border-radius-topright": "20px",
                "border-top-left-radius": "20px",
                "border-top-right-radius": "20px",
                "border": "1px #000 solid",
                "text-align": "center",
                "color": "#fff",
                "font-family": "Philosopher-Regular",
                "font-size": "18px",
                "background-color": "#36393D",
                "cursor": "pointer"
            });
            this.attach(this._formationsInfoEntity);

            if(Editor)
            {
                $(this._infoEntity._element).html('<span id="' + this._cid + '_name" onclick="openControl(\'editor/city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\')" style="cursor:pointer; line-height: 18px">' + Cities[this._cid].name + '</span>');
                $(this._infoEntity._element).append('<div style="float:right; height:35px; width:40px;"><img id="' + this._cid + '_resource" src="assets/resources/' + Cities[this._cid].resource.id + '.png" style="margin-right:7px; margin-top:3px;" /></div>');
                $(this._infoEntity._element).append('<span id="' + this._cid + '_capacity" style="float:left; margin-left:10px; font-size:22px">' + Cities[this._cid].capacity + '</span>');
            }
            else
            {
                $(this._infoEntity._element).html('<span id="' + this._cid + '_name" onclick="openControl(\'game/controls/city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\')" style="cursor:pointer; line-height: 18px">' + Cities[this._cid].name + '</span>');
                $(this._infoEntity._element).append('<div id="' + this._cid + '_build" onclick="openMenu(\'game/menu/build?city=' + this._cid + '\'); openControl(\'game/controls/city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\');" style="cursor:pointer; float:right; border-left: 1px solid #000; border-top-right-radius: 20px; border-bottom-right-radius: 20px; height:35px; width:40px; background-color:#3F4C6B;"><img src="assets/build.png" style="margin-right:7px; margin-top:3px;" /></div>');
                $(this._infoEntity._element).append('<span id="' + this._cid + '_people" style="float:left; margin-left:10px; font-size:22px">?</span>');
                $(this._infoEntity._element).append('<img id="' + this._cid + '_satisfaction" style="float:left; margin-top:7px; margin-left:5px; height:20px; width:20px;" src="assets/satisfaction/Happy.png" />');
                $(this._infoEntity._element).append('<div id="' + this._cid + '_defense" style="float:right; margin-top:3px; margin-right:5px;"><img style="width:20px; height:20px;" src="assets/FormationsBlack.png" /><span id="' + this._cid + '_defensePoints" style="font-size:22px; vertical-align:top;">0</span></div>');

                $(this._formationsInfoEntity._element).html('<span style="line-height: 20px;" id="' + this._cid + '_formations">0</span>');

                $.contextMenu({
                    selector: "#" + this._formationsInfoEntity.getDomId(),
                    trigger: 'left',
                    zIndex: 20,
                    build: this._getMenu
                });
            }

            this._infoEntity.draw();
            this._formationsInfoEntity.draw();
        },

        _updateInfo: function() {
            this._infoEntity.h = 35;
            this._infoEntity.w = 270;
            this._infoEntity.x = this.getX() - this._infoEntity._w / 2;
            this._infoEntity.y = this._y - 40;

            this._formationsInfoEntity.w = 50;
            this._formationsInfoEntity.h = 20;
            this._formationsInfoEntity.x = this._infoEntity.x + (this._infoEntity._w / 2 - 25);
            this._formationsInfoEntity.y = (this._y - 61);

            $("#" + this._cid + "_name").text(Cities[this._cid].name);

            if(Editor)
            {
                $("#" + this._cid + "_resource").attr("src", "assets/resources/" + Cities[this._cid].resource + ".png");
                $("#" + this._cid + "_capacity").text(Cities[this._cid].capacity);
            }
            else
            {
                var people = "?";
                if(Cities[this._cid].population != null)
                    people = Cities[this._cid].population;

                $("#" + this._cid + "_people").text(people);

                if(Cities[this._cid].satisfaction >= 0)
                {
                    $("#" + this._cid + "_satisfaction").show();
                    var smiley = "Happy";
                    if(Cities[this._cid].satisfaction < 80 && Cities[this._cid].satisfaction >= 60)
                        smiley = "Satisfied";
                    else if(Cities[this._cid].satisfaction < 60 && Cities[this._cid].satisfaction >= 30)
                        smiley = "Unhappy";
                    else if(Cities[this._cid].satisfaction < 30)
                        smiley = "Angry";

                    $("#" + this._cid + "_satisfaction").attr("src", "assets/satisfaction/" + smiley + ".png");
                    $("#" + this._cid + "_satisfaction").show();
                }
                else
                    $("#" + this._cid + "_satisfaction").hide();

                if(Cities[this._cid].diplomacy == 1)
                    $("#" + this._cid + "_build").show();
                else
                    $("#" + this._cid + "_build").hide();

                if(Cities[this._cid].formations != null && Cities[this._cid].formations.length > 0)
                {
                    $("#" + this._cid + "_formations").text(Cities[this._cid].formations.length);
                    $(this._formationsInfoEntity._element).show();
                }
                else
                    $(this._formationsInfoEntity._element).hide();
            }
        },

        _setType: function() {
            this.image("assets/cities/" + Cities[this._cid].type.id + ".png");

            var img = new Image();
            img.src = "assets/cities/" + Cities[this._cid].type.id + ".png";
            this.w = img.width;
            this.h = img.height;
        },

        _updateDiplomacy: function() {
            if(Cities[this._cid].diplomacy == 1) // Selbst
                this._infoEntity.css("background-color", "#4096EE");
            else if(Cities[this._cid].diplomacy == 2) // Verbündet
                this._infoEntity.css("background-color", "#356AA0");
            else if(Cities[this._cid].diplomacy == 3) // Verfeindet
                this._infoEntity.css("background-color", "#D01F3C");
            else if(Cities[this._cid].diplomacy == 4) // Neutral
                this._infoEntity.css("background-color", "#EEEEEE");
        },

        select: function() {
            if(Selected != null)
                Selected.deselect();

            Selected = this;

            if(Editor)
            {
                openControl('editor/city?city=' + this._cid, Cities[this._cid].name);

                if(SelectionMode == 2)
                {
                    if(SelectedWay == null)
                        SelectedWay = this;
                    else if(SelectedWay != this)
                    {
                        var _temp1 = SelectedWay._cid;
                        var _temp2 = this._cid;
                        $.get("/editor/addWay", { point1: _temp1, point2: _temp2 }, function(data){
                            loadAllRoads();
                            if(data == "ok")
                            {
                                var weight = prompt("Geschwindigkeitsfaktor angeben (zwischen 0 und 1)");
                                if(weight != null)
                                {
                                    $.get("/editor/setWay", { 'point1': _temp1, 'point2': _temp2, 'weight': weight });
                                }
                            }
                        });
                        SelectedWay = null;
                    }
                }
            }
            else
                openControl('game/controls/city?city=' + this._cid, this._name);
        },

        deselect: function() {
        },

        over: function() {
            if(isFormationSelected)
            {
                $.getJSON("game/formation/way", { "formation": Selected._fid, "target": this._cid }, function(data)
                {

                });
            }
        },

        init: function() {
            this.requires("2D, Canvas, Image, Mouse");
        },

        city: function(id) {
            this._cid = id;
            this.z = 11;

            // Selektionsanbindung
            this.bind("Click", this.select);
            this.bind("MouseOver", this.over);

            this._buildInfo();
            this._updateInfo();

            return this;
        },

        getX: function() {
            return Math.round(this._x + this._w / 2);
        },

        getY: function() {
            return Math.round(this._y + this._h / 2);
        },

        update: function() {
            if(Cities[this._cid] == null)
                this.destroy();
            else
            {
                this.x = Cities[this._cid].x;
                this.y = Cities[this._cid].y;

                if(Cities[this._cid].type.id != this._type)
                {
                    this._setType();
                }

                this._updateDiplomacy();
                this._updateInfo();
            }

            return this;
        }
    });
}