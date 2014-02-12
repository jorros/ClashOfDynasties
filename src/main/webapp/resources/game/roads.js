function loadAllRoads() {
    $.getJSON("/game/roads/load", function(data){
        $.each(data, function(key, road) {
            Roads[road.id] = Crafty.e("Road").road(road);
        })
    })
}