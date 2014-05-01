var Formations = {};
var FormationEntities = {};

function updateFormations() {
    $.getJSON("/game/formations", { timestamp: lastUpdate, editor: Editor }, function (data) {
        var tempFormations = {};

        $.each(data, function (id, formation) {
            if (formation.nn)
                tempFormations[id] = Formations[id];
            else
                tempFormations[id] = formation;
        });

        Formations = tempFormations;

        $.each(data, function (id, formation) {
            if (FormationEntities[id] == undefined)
                FormationEntities[id] = Crafty.e("Formation").formation(id);
        });

        $.each(FormationEntities, function (id, entity) {
            entity.update();
        });
    });
}