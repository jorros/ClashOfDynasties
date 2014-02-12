function cityEntity()
{
    Crafty.c("City", {
        _cid: 0,
        _type: 1,
        _name: "Unbekannt",
        _textEntity: {},
        _diplomacy: 1,
        _satisfaction: 0,
        _population: 0,
        _capacity: 0,
        _resource: 0,

        _buildInfo: function() {
            this._textEntity.x = this.getX() - this._textEntity._w / 2;
            this._textEntity.y = this._y - 40;
            this._textEntity.h = 35;

            if(Editor)
                $(this._textEntity._element).html('<span id="' + this._cid + '_name" onclick="openControl(\'editor/city?city=' + this._cid + '\', \'' + this._name + '\')" style="cursor:pointer; line-height: 18px">' + this._name + '</span>');
            else
                $(this._textEntity._element).html('<span id="' + this._cid + '_name" onclick="openControl(\'game/controls/city?city=' + this._cid + '\', \'' + this._name + '\')" style="cursor:pointer; line-height: 18px">' + this._name + '</span>');

            if(this._diplomacy == 1 && !Editor)
                $(this._textEntity._element).append('<div onclick="openMenu(\'game/menu/build?city=' + this._cid + '\'); openControl(\'game/controls/city?city=' + this._cid + '\', \'' + this._name + '\');" style="cursor:pointer; float:right; border-left: 1px solid #000; border-top-right-radius: 20px; border-bottom-right-radius: 20px; height:35px; width:40px; background-color:#3F4C6B;"><img src="assets/build.png" style="margin-right:7px; margin-top:3px;" /></div>');
            else if(Editor)
                $(this._textEntity._element).append('<div style="float:right; height:35px; width:40px;"><img id="' + this._cid + '_resource" src="assets/resources/' + this._resource + '.png" style="margin-right:7px; margin-top:3px;" /></div>');

            var people = this._population;
            if(this._population < 0)
                people = "?";

            if(Editor)
                $(this._textEntity._element).append('<span id="' + this._cid + '_capacity" style="float:left; margin-left:10px; font-size:22px">' + this._capacity + '</span>');
            else
                $(this._textEntity._element).append('<span id="' + this._cid + '_people" style="float:left; margin-left:10px; font-size:22px">' + people + '</span>');

            if((this._diplomacy == 1 || this._diplomacy == 2) && !Editor && this._population > 0)
            {
                var smiley = "happy";
                if(this._satisfaction < 80 && this._satisfaction >= 60)
                    smiley = "satisfied";
                else if(this._satisfaction < 60 && this._satisfaction >= 30)
                    smiley = "unhappy";
                else if(this._satisfaction < 30)
                    smiley = "angry";

                $(this._textEntity._element).append('<img id="' + this._cid + '_satisfaction" style="float:left; margin-top:2px; margin-left:5px;" src="assets/' + smiley + '32.png" />');
            }

            this._textEntity.draw();
        },

        _updateInfo: function() {
            this._textEntity.x = this.getX() - this._textEntity._w / 2;
            this._textEntity.y = this._y - 40;
            this._textEntity.h = 35;

            $("#" + this._cid + "_name").text(this._name);

            if(Editor)
            {
                $("#" + this._cid + "_resource").attr("src", "assets/resources/" + this._resource + ".png");
                $("#" + this._cid + "_capacity").text(this._capacity);
            }
            else
            {
                var people = this._population;
                if(this._population < 0)
                    people = "?";

                $("#" + this._cid + "_people").text(people);

                if(this._diplomacy == 1 || this._diplomacy == 2)
                {
                    var smiley = "happy";
                    if(this._satisfaction < 80 && this._satisfaction >= 60)
                        smiley = "satisfied";
                    else if(this._satisfaction < 60 && this._satisfaction >= 30)
                        smiley = "unhappy";
                    else if(this._satisfaction < 30)
                        smiley = "angry";

                    $("#" + this._cid + "_satisfaction").attr("src", "assets/" + smiley + "32.png");
                }
            }


        },

        _setType: function(type) {
            this._type = type;
            this.image("assets/cities/" + this._type + ".png");

            var img = new Image();
            img.src = "assets/cities/" + this._type + ".png";
            this.w = img.width;
            this.h = img.height;
        },

        _updateDiplomacy: function() {
            if(this._diplomacy == 1) // Selbst
                this._textEntity.css("background-color", "#4096EE");
            else if(this._diplomacy == 2) // Verbündet
                this._textEntity.css("background-color", "#356AA0");
            else if(this._diplomacy == 3) // Verfeindet
                this._textEntity.css("background-color", "#D01F3C");
            else if(this._diplomacy == 4) // Neutral
                this._textEntity.css("background-color", "#EEEEEE");
        },

        select: function() {
            if(Selected != null)
                Selected.deselect();

            Selected = this;

            if(Editor)
            {
                openControl('editor/city?city=' + this._cid, this._name);

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

            //this.tint("#FF0000", 0.4);
        },

        deselect: function() {
            //this.tint("#FFF", 0);
        },

        init: function() {
            this.requires("2D, Canvas, Image, Mouse");
        },

        city: function(city) {
            this._cid = city.id;
            this.x = city.x;
            this.y = city.y;
            this._setType(city.type.id);
            this._name = city.name;
            this._population = city.population;
            this._satisfaction = city.satisfaction;
            this._capacity = city.capacity;
            this._resource = city.resource.id;
            this.z = 11;

            // Selektionsanbindung
            this.bind("Click", this.select);

            // Erstellen der Textentity
            this._textEntity = Crafty.e("2D, DOM").attr({
                x: this._x,
                y: this._y - 40,
                w: 250,
                h: 35,
                z: 11
            }); //.text(this._name).textFont("size", "24px").textFont("family", "Philosopher-Regular").unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

            this._textEntity.css({ "border-radius": "20px", "-moz-border-radius": "20px", "border": "1px #000 solid", "text-align": "center", "color": "#000", "font-family": "Philosopher-Regular", "font-size": "18px", "line-height": "35px", "vertical-align": "middle" });
            this.attach(this._textEntity);

            this._updateDiplomacy();

            this._buildInfo();

            return this;
        },

        getX: function() {
            return Math.round(this._x + this._w / 2);
        },

        getY: function() {
            return Math.round(this._y + this._h / 2);
        },

        update: function(city) {
            if(city.id != this._cid)
                this._cid = city.id;

            if(this._x != city.x)
            {
                this.x = city.x;
            }

            if(this._y != city.y)
            {
                this.y = city.y;
            }

            if(city.type.id != this._type)
            {
                this._setType(city.type.id);
            }

            this._population = city.population;
            this._satisfaction = city.satisfaction;
            this._name = city.name;
            this._capacity = city.capacity;
            this._resource = city.resource.id;

            this._updateInfo();

            return this;
        }
    });
}