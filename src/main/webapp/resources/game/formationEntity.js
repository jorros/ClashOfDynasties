function formationEntity()
{
    Crafty.c("Formation", {
        _fid: 0,
        _name: "Unbekannt",
        _textEntity: {},
        _diplomacy: 1,

        _updateDiplomacy: function() {
            if(this._diplomacy == 1) // Selbst
                this._infoEntity.css("color", "#4096EE");
            else if(this._diplomacy == 2) // Verbündet
                this._infoEntity.css("color", "#356AA0");
            else if(this._diplomacy == 3) // Verfeindet
                this._infoEntity.css("color", "#D01F3C");
            else if(this._diplomacy == 4) // Neutral
                this._infoEntity.css("color", "#EEEEEE");
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
            this._infoEntity = Crafty.e("2D, DOM, Text").attr({
                x: this._x,
                y: this._y - 40,
                w: 220,
                h: 35,
                z: 11
            }).text(this._name).textFont({ size: "24px", family: "Philosopher-Regular" }).unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

            this.attach(this._infoEntity);

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
                this._infoEntity.text(this._name);
            }

            return this;
        }
    });
}