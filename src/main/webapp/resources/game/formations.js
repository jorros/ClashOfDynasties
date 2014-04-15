var Formations = {};
var FormationEntities = {};

function updateFormations() {
    $.getJSON("/game/formations", function(data) {
        Formations = data;
        $.each(data, function(id, formation) {
            if(FormationEntities[id] == undefined)
                FormationEntities[id] = Crafty.e("Formation").formation(id);
        });

        $.each(FormationEntities, function(id, entity) {
            entity.update();
        });
    });
}