Crafty.c("Caravan", {
    _cid: "",
    _textEntity: {},

    _buildInfo: function () {
        this._textEntity = Crafty.e("2D, DOM, Text").attr({
            x: this._x,
            y: this._y - 40,
            w: 220,
            h: 35,
            z: 11
        }).textFont({ size: "24px", family: "Philosopher-Regular" }).unselectable().css({"text-shadow": "-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000"});

        this.attach(this._textEntity);
    },

    _updateInfo: function () {
        this._textEntity.text(Caravans[this._cid].name);
    },

    _updateDiplomacy: function () {
        if (Caravans[this._cid].diplomacy == 1) // Selbst
            this._textEntity.css("color", "#4096EE");
        else if (Caravans[this._cid].diplomacy == 2) // Verbündet
            this._textEntity.css("color", "#356AA0");
        else if (Caravans[this._cid].diplomacy == 3) // Verfeindet
            this._textEntity.css("color", "#D01F3C");
        else if (Caravans[this._cid].diplomacy == 4) // Neutral
            this._textEntity.css("color", "#EEEEEE");
    },

    showRoute: function () {
        hideRoute();

        tempRoute = Caravans[this._cid].route.roads;

        $.each(tempRoute, function (index, road) {
            RoadEntities[road].mark(true);
        });

        routeShown = true;
    },

    select: function () {
        deselect();
        Selected = this;

        if (Caravans[this._cid].route != undefined)
            this.showRoute();

        openCommand('caravan?caravan=' + this._cid, Caravans[this._cid].name);
    },

    deselect: function () {
        isCaravanSelected = false;
    },

    init: function () {
        this.requires("2D, Canvas, Image, Mouse");
    },

    caravan: function (id) {
        this._cid = id;
        this.z = 12;

        this.image("assets/Caravan.png");
        this.w = 60;
        this.h = 50;

        // Selektionsanbindung
        this.bind("Click", this.select);

        this._buildInfo();
        this._updateInfo();

        return this;
    },

    update: function () {
        if (Caravans[this._cid] == undefined) {
            this.destroy();
            delete CaravanEntities[this._cid];
        }
        else {
            this.x = Math.round(Caravans[this._cid].x - this._w / 2);
            this.y = Math.round(Caravans[this._cid].y - this._h / 2);

            this._updateDiplomacy();
            this._updateInfo();
        }

        return this;
    }
});