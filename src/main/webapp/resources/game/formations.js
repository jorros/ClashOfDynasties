function loadAllFormations()
{
    $.getJSON("/game/formations/load", function(data){
        $.each(data, function(key, formation) {
            Formations[formation.id] = Crafty.e("Formation").formation(formation);
        })
    })
}

function updateFormations()
{
    $.getJSON("/game/formations/update", function(data){
        $.each(data, function(key, formation) {
            Formations[formation.id].update(formation);
        })
    })
}