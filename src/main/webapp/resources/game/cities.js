var Cities = {};
var CityEntities = {};

function updateCities() {
    $.getJSON("/game/cities", { timestamp: lastUpdate, editor: Editor }, function(data) {
        var tempCities = {};

        $.each(data, function(id, city) {
            if(city.nn)
                tempCities[id] = Cities[id];
            else
                tempCities[id] = city;
        });

        Cities = tempCities;

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