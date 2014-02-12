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

        init: function() {
            this.requires("2D, Canvas");
            this.bind("Draw", this._draw);
        },

        road: function(road) {
            this._rid = road.id;
            this._city1 = road.point1.id;
            this._city2 = road.point2.id;

            this.x1 = Cities[this._city1].getX();
            this.y1 = Cities[this._city1].getY();
            this.x2 = Cities[this._city2].getX();
            this.y2 = Cities[this._city2].getY();

            this.x = Math.min(this.x1, this.x2);
            this.y = Math.min(this.y1, this.y2);
            this.w = Math.max(this.x1, this.x2) - Math.min(this.x1, this.x2);
            this.h = Math.max(this.y1, this.y2) - Math.min(this.y1, this.y2);
            this.z = 10;

            this.ready = true;

            return this;
        },

        _draw: function(e) {
            var ctx = Crafty.canvas.context;

            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(this.x1, this.y1);
            ctx.lineTo(this.x2, this.y2);
            ctx.stroke();
        }
    });
}