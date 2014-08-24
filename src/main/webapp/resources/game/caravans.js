var Caravans = {};
var CaravanEntities = {};

function updateCaravans(loadOnly) {
    return $.getJSON("/game/caravans", { timestamp: lastUpdate, editor: Editor }, function (data) {
        var tempCaravans = {};

        $.each(data, function(id, caravan) {
            tempCaravans[id] = caravan;
            if (caravan.route == undefined)
                tempCaravans[id].route = Caravans[id].route;
        });

        Caravans = tempCaravans;

        if(loadOnly == undefined || !loadOnly)
            updateCaravanEntities();
    });
}

function updateCaravanEntities() {
    $.each(Caravans, function (id, caravan) {
        if (CaravanEntities[id] == undefined)
            CaravanEntities[id] = Crafty.e("Caravan").caravan(id);
    });

    $.each(CaravanEntities, function (id, entity) {
        entity.update();
    });
}