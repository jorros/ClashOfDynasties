var Formations = {};
var FormationEntities = {};

function updateFormations()
{
    $.getJSON("/game/formation", function(data){
        Formations = data;
        $.each(data, function(id, formation) {
            if(FormationEntities[id] == null)
            {
                FormationEntities[id] = Crafty.e("Formation").formation(id);
            }

            FormationEntities[id].update();
        });
    })
}