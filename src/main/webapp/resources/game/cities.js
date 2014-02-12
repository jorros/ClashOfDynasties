function loadAllCities()
{
    $.getJSON("/game/cities/load", function(data){
        $.each(data, function(key, city) {
            Cities[city.id] = Crafty.e("City").city(city);
            Crafty.viewport.centerOn(Cities[city.id], 60);
        })
        loadAllRoads();
    })
}

function updateCities()
{
    $.getJSON("/game/cities/update", function(data){
        $.each(data, function(key, city) {
            Cities[city.id].update(city);
        })
    })
}