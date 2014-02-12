function formationEntity()
{
    Crafty.c("City", {
        _fid: 0,
        _name: "Unbekannt",
        _textEntity: {},
        _diplomacy: 1,

        _updateDiplomacy: function() {
            if(this._diplomacy == 1) // Selbst
                this._textEntity.css("color", "#4096EE");
            else if(this._diplomacy == 2) // Verbündet
                this._textEntity.css("color", "#356AA0");
            else if(this._diplomacy == 3) // Verfeindet
                this._textEntity.css("color", "#D01F3C");
            else if(this._diplomacy == 4) // Neutral
                this._textEntity.css("color", "#EEEEEE");
        },

        select: function() {
            if(Selected != null)
                Selected.deselect();

            Selected = this;

            openControl('game/controls/formation?formation=' + this._cid, this._name);
        },

        deselect: function() {
        },

        init: function() {
            this.requires("2D, Canvas, Image, Mouse");
            this.image("assets/Formation.png");
            this.w = 105;
            this.h = 90;
        },

        formation: function(formation) {
            this._fid = formation.id;
            this.x = formation.x;
            this.y = formation.y;
            this._name = formation.name;
            this.z = 11;

            // Selektionsanbindung
            this.bind("Click", this.select);

            // Erstellen der Textentity
            this._textEntity = Crafty.e("2D, DOM").attr({
                x: this._x,
                y: this._y - 40,
                w: 220,
                h: 35,
                z: 11
            }).text(this._name).textFont("size", "24px").textFont("family", "Philosopher-Regular").unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

            //this._textEntity.css({ "border-radius": "20px", "-moz-border-radius": "20px", "border": "1px #000 solid", "text-align": "center", "color": "#000", "font-family": "Philosopher-Regular", "font-size": "18px", "line-height": "35px", "vertical-align": "middle" });
            this.attach(this._textEntity);

            this._updateDiplomacy();

            return this;
        },

        getX: function() {
            return this._x + this._w / 2;
        },

        getY: function() {
            return this._y + this._h / 2;
        },

        update: function(formation) {
            if(formation.id != this._fid)
                this._fid = formation.id;

            if(this._x != formation.x)
            {
                this.x = formation.x;
            }

            if(this._y != formation.y)
            {
                this.y = formation.y;
            }

            if(formation.name != this._name)
            {
                this._name = formation.name;
                this._textEntity.text(this._name);
            }

            return this;
        }
    });
}