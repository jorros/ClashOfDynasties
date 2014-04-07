var Roads = {};
var RoadEntities = {};

function updateRoads() {
    $.getJSON("/game/roads/all", function(data){
        Roads = data;
        $.each(data, function(id, road) {
            if(RoadEntities[id] == null)
                RoadEntities[id] = Crafty.e("Road").road(id);

            RoadEntities[id].update();
        });
    })
}