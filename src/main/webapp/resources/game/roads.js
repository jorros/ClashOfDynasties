var Roads = {};
var RoadEntities = {};

function updateRoads() {
    $.getJSON("/game/roads", function(data) {
        Roads = data;
        $.each(data, function(id, road) {
            if(RoadEntities[id] == undefined)
                RoadEntities[id] = Crafty.e("Road").road(id);
        });

        $.each(RoadEntities, function(id, entity) {
            entity.update();
        });
    });
}