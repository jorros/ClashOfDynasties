function loadTop() {
    $.getJSON("/game/menus/top", { timestamp: lastUpdate, editor: Editor }, function (data) {
        $("#globalCoins").text(data.coins);
        $("#globalBalance").text(data.balance);

        $("#globalBalance").removeClass();
        if (data.balance >= 0)
            $("#globalBalance").addClass("green");
        else
            $("#globalBalance").addClass("red");

        $("#globalPeople").text(data.people);
        $("#globalCities").text(data.cityNum);

        $.each(data.events, function(index, event) {
            $('<button class="event"><img src="assets/events/' + event.type + '.png" /></button>').appendTo("#events").mousedown(function(e) {
                if(e.which === 1) {
                    Crafty.viewport.centerOn(CityEntities[event.city], 100)
                }
                else if(e.which == 3) {
                    $(this).fadeOut();
                    $.delete("/game/menus/event", { timestamp: event.timestamp, type: event.type, city: event.city });
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