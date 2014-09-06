var Caravans = {};
var Cities = {};
var Formations = {};
var Roads = {};

var CaravanEntities = {};
var CityEntities = {};
var FormationEntities = {};
var RoadEntities = {};

var lastUpdate = 0;

function updateGameContent() {
    return $.getJSON("/game/core/update", { timestamp: lastUpdate, editor: Editor }, function (data) {
        var tempCities = {};
        var tempCaravans = {};
        var tempFormations = {};
        var tempRoads = {};

        $.each(data.cities, function (id, city) {
            if (city.nn)
                tempCities[id] = Cities[id];
            else
                tempCities[id] = city;
        });

        $.each(data.roads, function (id, road) {
            if (road.nn)
                tempRoads[id] = Roads[id];
            else
                tempRoads[id] = road;
        });


        $.each(data.formations, function (id, formation) {
            if (formation.nn)
                tempFormations[id] = Formations[id];
            else
                tempFormations[id] = formation;
        });


        $.each(data.caravans, function(id, caravan) {
            tempCaravans[id] = caravan;
            if (caravan.route == undefined)
                tempCaravans[id].route = Caravans[id].route;
        });

        Cities = tempCities;
        Roads = tempRoads;
        Formations = tempFormations;
        Caravans = tempCaravans;

        lastUpdate = data.timestamp;

        $("#globalCoins").text(data.top.coins);
        $("#globalBalance").text(data.top.balance);

        $("#globalBalance").removeClass();
        if (data.top.balance >= 0)
            $("#globalBalance").addClass("green");
        else
            $("#globalBalance").addClass("red");

        $("#globalPeople").text(data.top.people);
        $("#globalCities").text(data.top.cityNum);
        $("#globalFormations").text(data.top.formationNum);
        $("#globalCaravans").text(data.top.caravanNum);
        $("#globalRanking").text(data.top.ranking);

        $.each(data.events, function(index, event) {
            $('<button class="event"><img src="assets/events/' + event.type + '.png" /></button>').appendTo("#events").mousedown(function(e) {
                if(e.which === 1) {
                    if(event.city != undefined)
                        Crafty.viewport.centerOn(CityEntities[event.city], 100)
                    else if(event.action != undefined)
                        openMenu(event.action);
                }
                else if(e.which == 3) {
                    $(this).fadeOut();
                    $.delete("/game/core/event", { id: event.id });
                }
            }).contextmenu(function() {
                return false;
            }).tooltip({
                position: {
                    my: "left",
                    at: "right+10 center",
                    collision: "flipfit"
                },
                content: "<span style=\"font-family:'Philosopher-Bold'; font-size:18px;\">" + event.title + "</span><br><br>" + event.description + "<br><br><span class='red'>Mit Rechtsklick ausblenden</span>",
                items: "button"
            });
        });
    });
}

function updateGameEntities() {
    $.each(Cities, function (id, city) {
        if (CityEntities[id] == undefined)
            CityEntities[id] = Crafty.e("City").city(id);
    });

    $.each(CityEntities, function (id, entity) {
        entity.update();
    });

    $.each(Roads, function (id, road) {
        if (RoadEntities[id] == undefined)
            RoadEntities[id] = Crafty.e("Road").road(id);
    });

    $.each(RoadEntities, function (id, entity) {
        entity.update();
    });


    $.each(Formations, function (id, formation) {
        if (FormationEntities[id] == undefined)
            FormationEntities[id] = Crafty.e("Formation").formation(id);
    });

    $.each(FormationEntities, function (id, entity) {
        entity.update();
    });


    $.each(Caravans, function (id, caravan) {
        if (CaravanEntities[id] == undefined)
            CaravanEntities[id] = Crafty.e("Caravan").caravan(id);
    });

    $.each(CaravanEntities, function (id, entity) {
        entity.update();
    });
}