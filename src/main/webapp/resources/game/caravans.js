var Caravans = {};
var CaravanEntities = {};

function updateCaravans(loadOnly) {
    return $.getJSON("/game/caravans", { timestamp: lastUpdate, editor: Editor }, function (data) {
        Caravans = data;

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