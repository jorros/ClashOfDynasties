function loadTop()
{
    $.getJSON("/game/menus/top", function(data){
        $("#globalCoins").text(data.coins);
        $("#globalBalance").text(data.balance);

        $("#globalBalance").removeClass();
        if(data.balance >= 0)
            $("#globalBalance").addClass("green");
        else
            $("#globalBalance").addClass("red");

        $("#globalPeople").text(data.people);
        $("#globalCities").text(data.cityNum);
    });
}