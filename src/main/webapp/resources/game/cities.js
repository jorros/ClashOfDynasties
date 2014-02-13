var Cities = {};
var CityEntities = {};

function loadAllCities()
{
    $.getJSON("/game/cities/all", function(data){
        Cities = data;
        $.each(data, function(id, city) {
            CityEntities[id] = Crafty.e("City").city(id);
            CityEntities[id].update();
        });
        loadAllRoads();
    })
}

function updateCities()
{
    $.getJSON("/game/cities/all", function(data){
        Cities = data;
        $.each(data, function(id, city) {
            if(CityEntities[id] == null)
                CityEntities[id] = Crafty.e("City").city(id);
            else
                CityEntities[id].update();
        });
    })
}