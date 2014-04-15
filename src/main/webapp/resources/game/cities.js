var Cities = {};
var CityEntities = {};

function updateCities() {
    $.getJSON("/game/cities", function(data) {
        Cities = data;
        $.each(data, function(id, city) {
            if(CityEntities[id] == undefined)
                CityEntities[id] = Crafty.e("City").city(id);
        });

        $.each(CityEntities, function(id, entity) {
            entity.update();
        });

        updateRoads();
    });
}