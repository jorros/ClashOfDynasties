var Cities = {};
var CityEntities = {};

function updateCities()
{
    $.getJSON("/game/cities", function(data){
        Cities = data;
        $.each(data, function(id, city) {
            if(CityEntities[id] == null)
                CityEntities[id] = Crafty.e("City").city(id);

            CityEntities[id].update();
            updateRoads();
        });
    })
}