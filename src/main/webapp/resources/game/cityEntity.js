function cityEntity()
{
    Crafty.c("City", {
        _cid: 0,
        _textEntity: {},

        _buildInfo: function() {
            this._textEntity = Crafty.e("2D, DOM").attr({
                w: 270,
                h: 35,
                x: (this.getX() - this._textEntity._w / 2),
                y: (this._y - 40),
                z: 11
            }); //.text(this._name).textFont("size", "24px").textFont("family", "Philosopher-Regular").unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

            this._textEntity.css({ "border-radius": "20px", "-moz-border-radius": "20px", "border": "1px #000 solid", "text-align": "center", "color": "#000", "font-family": "Philosopher-Regular", "font-size": "18px", "line-height": "35px", "vertical-align": "middle" });
            this.attach(this._textEntity);

            if(Editor)
            {
                $(this._textEntity._element).html('<span id="' + this._cid + '_name" onclick="openControl(\'editor/city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\')" style="cursor:pointer; line-height: 18px">' + Cities[this._cid].name + '</span>');
                $(this._textEntity._element).append('<div style="float:right; height:35px; width:40px;"><img id="' + this._cid + '_resource" src="assets/resources/' + Cities[this._cid].resource.id + '.png" style="margin-right:7px; margin-top:3px;" /></div>');
                $(this._textEntity._element).append('<span id="' + this._cid + '_capacity" style="float:left; margin-left:10px; font-size:22px">' + Cities[this._cid].capacity + '</span>');
            }
            else
            {
                $(this._textEntity._element).html('<span id="' + this._cid + '_name" onclick="openControl(\'game/controls/city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\')" style="cursor:pointer; line-height: 18px">' + Cities[this._cid].name + '</span>');
                $(this._textEntity._element).append('<div id="' + this._cid + '_build" onclick="openMenu(\'game/menu/build?city=' + this._cid + '\'); openControl(\'game/controls/city?city=' + this._cid + '\', \'' + Cities[this._cid].name + '\');" style="cursor:pointer; float:right; border-left: 1px solid #000; border-top-right-radius: 20px; border-bottom-right-radius: 20px; height:35px; width:40px; background-color:#3F4C6B;"><img src="assets/build.png" style="margin-right:7px; margin-top:3px;" /></div>');
                $(this._textEntity._element).append('<span id="' + this._cid + '_people" style="float:left; margin-left:10px; font-size:22px">?</span>');
                $(this._textEntity._element).append('<img id="' + this._cid + '_satisfaction" style="float:left; margin-top:7px; margin-left:5px; height:20px; width:20px;" src="assets/satisfaction/Happy.png" />');
                $(this._textEntity._element).append('<div id="' + this._cid + '_formations" style="float:right; margin-top:3px; margin-right:5px; cursor:pointer;"><img style="width:20px; height:20px;" src="assets/FormationsBlack.png" /><span id="' + this._cid + '_numFormations" style="font-size:22px; vertical-align:top;">0</span></div>');
            }

            this._textEntity.draw();
        },

        _updateInfo: function() {
            this._textEntity.h = 35;
            this._textEntity.w = 270;
            this._textEntity.x = this.getX() - this._textEntity._w / 2;
            this._textEntity.y = this._y - 40;

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
                    $("#" + this._cid + "_numFormations").text(Cities[this._cid].formations.length);
                    $("#" + this._cid + "_formations").show();
                }
                else
                    $("#" + this._cid + "_formations").hide();
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
                this._textEntity.css("background-color", "#4096EE");
            else if(Cities[this._cid].diplomacy == 2) // Verbündet
                this._textEntity.css("background-color", "#356AA0");
            else if(Cities[this._cid].diplomacy == 3) // Verfeindet
                this._textEntity.css("background-color", "#D01F3C");
            else if(Cities[this._cid].diplomacy == 4) // Neutral
                this._textEntity.css("background-color", "#EEEEEE");
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

        init: function() {
            this.requires("2D, Canvas, Image, Mouse");
        },

        city: function(id) {
            this._cid = id;
            this.z = 11;

            // Selektionsanbindung
            this.bind("Click", this.select);

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