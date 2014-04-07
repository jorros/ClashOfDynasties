var Formations = {};
var FormationEntities = {};

function updateFormations()
{
    $.getJSON("/game/formations/all", function(data){
        Formations = data;
        $.each(data, function(id, formation) {
            if(FormationEntites[id] == null)
            {
                FormationEntities[id] = Crafty.e("Formation").formation(id);
            }

            FormationEntities[id].update();
        });
    })
}