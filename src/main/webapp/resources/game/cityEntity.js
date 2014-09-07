Crafty.c("City", {
    _cid: "",
    _infoEntity: undefined,
    _formationsInfoEntity: {},
    _type: 0,

    _getMenu: function ($trigger, e) {
        var items = {};

        var id = $trigger.find("span").attr("id").slice(0, -11);

        $.each(Cities[id].formations, function (key, val) {
            items[val.id] = { name: val.name, icon: "edit" };
        });

        return {
            callback: function (key, options) {
                FormationEntities[key].select();
            },
            items: items
        }
    },

    _buildInfo: function () {
        this._infoEntity = Crafty.e("2D, DOM").attr({
            w: 270,
            h: 35,
            x: (Cities[this._cid].x - 270 / 2),
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

        if (Editor) {
            $(this._infoEntity._element).html('<span id="' + this._cid + '_name" onclick="openCommand(\'editcity?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\')" style="cursor:pointer; line-height: 18px">' + Cities[this._cid].name + '</span>');
            $(this._infoEntity._element).append('<div style="float:right; height:35px; width:40px;"><img id="' + this._cid + '_resource" src="assets/resources/' + Cities[this._cid].resource + '.png" style="margin-right:7px; margin-top:3px;" /></div>');
            $(this._infoEntity._element).append('<span id="' + this._cid + '_capacity" style="float:left; margin-left:10px; font-size:22px">' + Cities[this._cid].capacity + '</span>');
            this._formationsInfoEntity.css("display", "none");
        }
        else {
            $(this._infoEntity._element).html('<span id="' + this._cid + '_name" onclick="openCommand(\'city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\')" style="cursor:pointer; line-height: 18px">' + Cities[this._cid].name + '</span>');
            $(this._infoEntity._element).append('<div id="' + this._cid + '_build" onclick="openMenu(\'build?city=' + this._cid + '\'); openCommand(\'city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\');" style="cursor:pointer; float:right; border-left: 1px solid #000; border-top-right-radius: 20px; border-bottom-right-radius: 20px; height:35px; width:40px; background-color:#3F4C6B;"><img src="assets/build.png" style="margin-right:7px; margin-top:3px;" /></div>');
            $(this._infoEntity._element).append('<span id="' + this._cid + '_people" style="float:left; margin-left:10px; font-size:22px">?</span>');
            $(this._infoEntity._element).append('<img id="' + this._cid + '_satisfaction" style="float:left; margin-top:7px; margin-left:5px; height:20px; width:20px;" src="assets/satisfaction/Happy.png" />');
            $(this._infoEntity._element).append('<div id="' + this._cid + '_defence" style="float:right; margin-top:3px; margin-right:5px;"><img style="width:20px; height:20px;" src="assets/FormationsBlack.png" /><span id="' + this._cid + '_defencePoints" style="font-size:22px; vertical-align:top;">0</span></div>');

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

    _updateInfo: function () {
        this._infoEntity.h = 35;
        this._infoEntity.w = 270;
        this._infoEntity.x = Cities[this._cid].x - this._infoEntity._w / 2;
        this._infoEntity.y = this._y - 40;

        this._formationsInfoEntity.w = 50;
        this._formationsInfoEntity.h = 20;
        this._formationsInfoEntity.x = this._infoEntity.x + (this._infoEntity._w / 2 - 25);
        this._formationsInfoEntity.y = (this._y - 61);

        $("#" + this._cid + "_name").text(Cities[this._cid].name);
        $("#" + this._cid + "_defencePoints").text(Cities[this._cid].defence);

        if (Editor) {
            $("#" + this._cid + "_resource").attr("src", "assets/resources/" + Cities[this._cid].resource + ".png");
            $("#" + this._cid + "_capacity").text(Cities[this._cid].capacity);
        }
        else {
            var people = "?";
            if (Cities[this._cid].population != null)
                people = Cities[this._cid].population;

            $("#" + this._cid + "_people").text(people);

            if (Cities[this._cid].population > 0) {
                var smiley = "Happy";
                if (Cities[this._cid].satisfaction < 80 && Cities[this._cid].satisfaction >= 60)
                    smiley = "Satisfied";
                else if (Cities[this._cid].satisfaction < 60 && Cities[this._cid].satisfaction >= 30)
                    smiley = "Unhappy";
                else if (Cities[this._cid].satisfaction < 30)
                    smiley = "Angry";

                $("#" + this._cid + "_satisfaction").attr("src", "assets/satisfaction/" + smiley + ".png");
                $("#" + this._cid + "_satisfaction").show();
            }
            else
                $("#" + this._cid + "_satisfaction").hide();

            if (Cities[this._cid].build)
                $("#" + this._cid + "_build").show();
            else
                $("#" + this._cid + "_build").hide();

            if (Cities[this._cid].formations != null && Cities[this._cid].formations.length > 0) {
                $("#" + this._cid + "_formations").text(Cities[this._cid].formations.length);
                $(this._formationsInfoEntity._element).show();
            }
            else
                $(this._formationsInfoEntity._element).hide();
        }

        var infoWidth = 0;

        if(Editor) {
            infoWidth = $("#" + this._cid + "_name").width() + 50 + $("#" + this._cid + "_capacity").width() + $("#" + this._cid + "_resource").width();
        } else {
            infoWidth = $("#" + this._cid + "_name").width() + 50 + $("#" + this._cid + "_defence").width() + $("#" + this._cid + "_people").width();

            if ($("#" + this._cid + "_build").is(":visible"))
                infoWidth += $("#" + this._cid + "_build").width();

            if ($("#" + this._cid + "_satisfaction").is(":visible"))
                infoWidth += $("#" + this._cid + "_satisfaction").width();
        }

        this._infoEntity.css("background-color", getColor(Cities[this._cid].color));

        this._infoEntity.w = infoWidth;
        this._infoEntity.x = Cities[this._cid].x - this._infoEntity._w / 2;
    },

    _setType: function () {
        if (Cities[this._cid].type != this._type) {
            this.image("assets/cities/" + Cities[this._cid].type + ".png");

            var img = new Image();
            img.src = "assets/cities/" + Cities[this._cid].type + ".png";
            this.w = img.width;
            this.h = img.height;

            this._type = Cities[this._cid].type;
        }
    },

    select: function () {
        if (Editor) {
            openCommand('editcity?city=' + this._cid, Cities[this._cid].name);

            if (SelectionMode == 2) {
                if (SelectedWay == null)
                    SelectedWay = this;
                else if (SelectedWay != this) {
                    var _temp1 = SelectedWay._cid;
                    var _temp2 = this._cid;

                    $.get("/game/roads/getByPoints", { point1: _temp1, point2: _temp2 }, function (roadId) {
                        if (roadId != "") {
                            $.delete("/game/roads/" + roadId, function () {
                                forceUpdate();
                            });
                        }
                        else {
                            var weight = window.prompt("Wegbelastung festlegen (0-1)", "1");
                            if (weight != null) {
                                $.post("/game/roads", { point1: _temp1, point2: _temp2, "weight": weight }, function () {
                                    forceUpdate();
                                });
                            }
                        }
                    });
                    SelectedWay = null;
                }
            }
        }
        else if(Cities[this._cid].visible) {
            if (isFormationSelected) {
                $.get("game/formations/" + Selected._fid + "/move", { "target": this._cid }, function() {
                    forceUpdate();
                });

                return;
            }
            else if (isCaravanSelected) {
                if (Selected._cid != this._cid) {
                    openMenu("caravan?point1=" + Selected._cid + "&point2=" + this._cid);

                    isCaravanSelected = false;
                    $("#caravanText").hide();
                    $.powerTip.hide();
                }
            }
            else
                openCommand('city?city=' + this._cid, this._name);
        }

        deselect();
        Selected = this;
    },

    deselect: function () {
    },

    over: function () {
        if(Cities[this._cid].visible) {
            if (!Editor && isFormationSelected && !isCalculatedRoute) {
                Selected.showRoute(this._cid);
                isCalculatedRoute = true;
            }
            else if (!Editor && isCaravanSelected && Selected._cid != this._cid) {
                tempRoute = "";
                $.getJSON("game/caravans/route", { "point1": Selected._cid, "point2": this._cid }, function (data) {
                    if (data != undefined && !$.isEmptyObject(data)) {
                        isCalculatedRoute = true;
                        tempRoute = data.roads;
                        tempTime = data.time;

                        var totalSeconds = tempTime;
                        var hours = Math.floor(totalSeconds / 3600);
                        totalSeconds %= 3600;
                        var minutes = Math.floor(totalSeconds / 60);

                        var output = "";
                        if (hours > 0)
                            output += hours + " Stunden ";
                        if (minutes > 0)
                            output += minutes + " Minuten";

                        $(document).data('powertip', output);
                        $.powerTip.show($(document));

                        $.each(tempRoute, function (index, road) {
                            RoadEntities[road].mark(true);
                        });
                    }
                });
            }
        }
    },

    init: function () {
        this.requires("2D, Canvas, Image, Mouse");
    },

    city: function (id) {
        this._cid = id;
        this.z = 11;

        // Selektionsanbindung
        this.bind("Click", this.select);
        this.bind("MouseOver", this.over);

        if(Cities[this._cid].visible || Editor) {
            this._buildInfo();
            this._updateInfo();
        }

        return this;
    },

    update: function () {
        if (Cities[this._cid] == undefined) {
            this.destroy();
            delete CityEntities[this._cid];
        }
        else {
            this._setType();

            this.x = Math.round(Cities[this._cid].x - this._w / 2);
            this.y = Math.round(Cities[this._cid].y - this._h / 2);

            if(Cities[this._cid].visible || Editor) {
                if(this._infoEntity == undefined)
                    this._buildInfo();

                this._updateInfo();

                this.alpha = 1.0;
            } else if(this._infoEntity != undefined) {
                this._infoEntity.destroy();
                this.alpha = 0.6;
            }
            else
                this.alpha = 0.6;
        }

        return this;
    }
});