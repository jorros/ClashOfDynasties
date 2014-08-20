function roadEntity() {
    Crafty.c("Road", {
        _rid: 0,
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 0,
        marked: false,

        init: function () {
            this.requires("2D, Canvas");
            this.bind("Draw", this._draw);
        },

        road: function (id) {
            this._rid = id;
            this.z = 10;

            this.ready = true;

            return this;
        },

        update: function () {
            if (Roads[this._rid] == undefined) {
                this.destroy();
                delete RoadEntities[this._rid];
            }
            else {
                var city1 = Roads[this._rid].point1;
                var city2 = Roads[this._rid].point2;

                this.x1 = Cities[city1].x;
                this.y1 = Cities[city1].y;
                this.x2 = Cities[city2].x;
                this.y2 = Cities[city2].y;

                this.x = Math.min(this.x1, this.x2);
                this.y = Math.min(this.y1, this.y2);
                this.w = Math.max(this.x1, this.x2) - Math.min(this.x1, this.x2);
                this.h = Math.max(this.y1, this.y2) - Math.min(this.y1, this.y2);
            }
        },

        temp: function (formation, city) {
            if(city != undefined) {
                this.x1 = Formations[formation].x;
                this.y1 = Formations[formation].y;
                this.x2 = Cities[city].x;
                this.y2 = Cities[city].y;

                this.x = Math.min(this.x1, this.x2);
                this.y = Math.min(this.y1, this.y2);
                this.w = Math.max(this.x1, this.x2) - Math.min(this.x1, this.x2);
                this.h = Math.max(this.y1, this.y2) - Math.min(this.y1, this.y2);
                this.z = 10;

                this.marked = true;
                this.ready = true;
            }

            return this;
        },

        mark: function (val) {
            this.marked = val;
            Crafty.DrawManager.drawAll();
        },

        _draw: function (e) {
            var ctx = Crafty.canvas.context;

            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(this.x1, this.y1);
            ctx.lineTo(this.x2, this.y2);

            if (this.marked)
                ctx.strokeStyle = "#FF0000";
            else
                ctx.strokeStyle = "#000000";
            ctx.stroke();
        }
    });
}