function roadEntity()
{
    Crafty.c("Road", {
        _rid: 0,
        _city1: 0,
        _city2: 0,
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 0,
        marked: false,

        init: function() {
            this.requires("2D, Canvas");
            this.bind("Draw", this._draw);
        },

        road: function(road) {
            this._rid = road.id;
            this._city1 = road.point1.id;
            this._city2 = road.point2.id;

            this.x1 = CityEntities[this._city1].getX();
            this.y1 = CityEntities[this._city1].getY();
            this.x2 = CityEntities[this._city2].getX();
            this.y2 = CityEntities[this._city2].getY();

            this.x = Math.min(this.x1, this.x2);
            this.y = Math.min(this.y1, this.y2);
            this.w = Math.max(this.x1, this.x2) - Math.min(this.x1, this.x2);
            this.h = Math.max(this.y1, this.y2) - Math.min(this.y1, this.y2);
            this.z = 10;

            this.ready = true;

            return this;
        },

        temp: function(formation, city) {
            this.x1 = FormationEntites[formation].getX();
            this.y1 = FormationEntites[formation].getY();
            this.x2 = CityEntities[city].getX();
            this.y2 = CityEntities[city].getY();

            this.x = Math.min(this.x1, this.x2);
            this.y = Math.min(this.y1, this.y2);
            this.w = Math.max(this.x1, this.x2) - Math.min(this.x1, this.x2);
            this.h = Math.max(this.y1, this.y2) - Math.min(this.y1, this.y2);
            this.z = 11;

            this.marked = true;
            this.ready = true;

            return this;
        },

        mark: function(val) {
            this.marked = val;
            Crafty.DrawManager.drawAll();
        },

        _draw: function(e) {
            var ctx = Crafty.canvas.context;

            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(this.x1, this.y1);
            ctx.lineTo(this.x2, this.y2);

            if(this.marked)
                ctx.strokeStyle = "#FF0000";
            else
                ctx.strokeStyle = "#000000";
            ctx.stroke();
        }
    });
}