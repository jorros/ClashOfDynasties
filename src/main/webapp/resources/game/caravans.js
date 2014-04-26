var Caravans = {};
var CaravanEntities = {};

function updateCaravans() {
    $.getJSON("/game/caravans", { timestamp: lastUpdate, editor: Editor }, function(data) {
        Caravans = data;

        $.each(data, function(id, caravan) {
            if(CaravanEntities[id] == undefined)
                CaravanEntities[id] = Crafty.e("Caravan").caravan(id);
        });

        $.each(CaravanEntities, function(id, entity) {
            entity.update();
        });
    });
}