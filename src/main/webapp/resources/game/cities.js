var Cities = {};
var CityEntities = {};

function updateCities(loadOnly) {
    return $.getJSON("/game/cities", { timestamp: lastUpdate, editor: Editor }, function (data) {
        var tempCities = {};

        $.each(data, function (id, city) {
            if (city.nn)
                tempCities[id] = Cities[id];
            else
                tempCities[id] = city;
        });

        Cities = tempCities;

        if(loadOnly == undefined || !loadOnly)
            updateCityEntities();
    });
}

function updateCityEntities() {
    $.each(Cities, function (id, city) {
        if (CityEntities[id] == undefined)
            CityEntities[id] = Crafty.e("City").city(id);
    });

    $.each(CityEntities, function (id, entity) {
        entity.update();
    });

    if(Editor || $.isEmptyObject(Roads))
        updateRoads();
}