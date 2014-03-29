var Formations = {};
var FormationEntites = {};

function loadAllFormations()
{
    $.getJSON("/game/formations/all", function(data){
        Formations = data;
        $.each(data, function(id, formation) {
            console.log(formation.name);
            FormationEntites[id] = Crafty.e("Formation").formation(id);
            FormationEntites[id].update();
        });
    })
}

function updateFormations()
{
    $.getJSON("/game/formations/all", function(data){
        Formations = data;
        $.each(data, function(id, formation) {
            if(FormationEntites[id] == null)
            {
                console.log("Formation");
                FormationEntites[id] = Crafty.e("Formation").formation(id);
            }
            else {
                FormationEntites[id].update();
            }
        });
    })
}