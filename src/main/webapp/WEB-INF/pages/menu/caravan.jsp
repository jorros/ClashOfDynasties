<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${caravan.name}</h1>
<div id="content" style="overflow:hidden;">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>${point1.name}</h4>
            <div style="height:310px; overflow-y:hidden; overflow-x:hidden;">
                <img id="point1ItemIcon" src="assets/items/<c:out value="${caravan.point1Item.id}" default="0" />.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><span id="point1ItemLoadText"><c:out value="${caravan.point1Load}" default="0" />t</span> <span id="point1ItemText">${caravan.point1Item.name} (${caravan.point1Item.type.name})</span></span><br><span>Wird nach ${point2.name} transportiert</span>
                <br><br><div id="slider1"></div>
                <hr>
                <div style="height:250px; overflow-y:auto; clear:both;">
                    <c:forEach items="${items}" var="item">
                    <img src="assets/items/${item.id}.png" style="float:left; margin-right:5px;" />
                    <span style="color:#FFF; font-weight:bold;"><c:out value="${point1.items[item]}" default="0" />t ${item.name} (${item.type.name})</span><br><span class="green">+4</span><c:if test="${point1.requiredItemTypes.contains(item.type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                    <a style="font-weight:bold; cursor:pointer;" onclick="selectItem(1, ${item.id}, '${item.type.name}', '${item.name}')">Auswählen</a>
                    <br><br>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>${point2.name}</h4>
            <div style="height:310px; overflow-y:hidden; overflow-x:hidden;">
                <img id="point2ItemIcon" src="assets/items/<c:out value="${caravan.point2Item.id}" default="0" />.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><span id="point2ItemLoadText"><c:out value="${caravan.point2Load}" default="0" />t</span> <span id="point2ItemText">${caravan.point2Item.name} (${caravan.point2Item.type.name})</span></span><br><span>Wird nach ${point1.name} transportiert</span>
                <br><br><div id="slider2"></div>
                <hr>
                <div style="height:250px; overflow-y:auto; clear:both;">
                    <c:forEach items="${items}" var="item">
                        <img src="assets/items/${item.id}.png" style="float:left; margin-right:5px;" />
                        <span style="color:#FFF; font-weight:bold;"><c:out value="${point2.items[item]}" default="0" />t ${item.name} (${item.type.name})</span><br><span class="green">+4</span><c:if test="${point2.requiredItemTypes.contains(item.type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                        <a style="font-weight:bold; cursor:pointer;" onclick="selectItem(2, ${item.id}, '${item.type.name}', '${item.name}')">Auswählen</a>
                        <br><br>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Karawanenlager</h4>
            <div style="height:310px; overflow-y:auto; overflow-x:hidden;">
                <c:if test="${caravan.point1StoreItem != null && caravan.point1Store > 0}">
                <img src="assets/items/<c:out value="${caravan.point1StoreItem.id}" default="0" />.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${caravan.point1Store}" default="0" />t ${caravan.point1StoreItem.name} (${caravan.point1StoreItem.type.name})</span><br><span>Wird nach ${point2.name} transportiert</span>
                <br><br><br>
                </c:if>
                <c:if test="${caravan.point2StoreItem != null && caravan.point2Store > 0}">
                    <img src="assets/items/<c:out value="${caravan.point2StoreItem.id}" default="0" />.png" style="float:left; margin-right:5px;" />
                    <span style="color:#FFF; font-weight:bold;"><c:out value="${caravan.point2Store}" default="0" />t ${caravan.point2StoreItem.name} (${caravan.point2StoreItem.type.name})</span><br><span>Wird nach ${point1.name} transportiert</span>
                    <br><br><br>
                </c:if>
            </div>
        </div>
    </div>
    <div style="clear:both; text-align:center;">
        <span style="color:white;"></span>
    </div>
    <div style="height:60px;">
        <label for="caravan_name">Name: </label>
        <input id="caravan_name" style="width:300px;" type="text" value="${caravan.name}" />
        <button onclick="save()" style="float:right;"><c:if test="${caravan.id == 0}">Erstellen</c:if><c:if test="${caravan.id != 0}">Ändern</c:if></button>
        <button onclick="closeMenu()" style="float:right;">Abbrechen</button>
    </div>
</div>
<script>
    var point1Item = <c:out value="${caravan.point1Item.id}" default="0" />;
    var point1Load = <c:out value="${caravan.point1Load}" default="0" />;
    var point2Item = <c:out value="${caravan.point2Item.id}" default="0" />;
    var point2Load = <c:out value="${caravan.point2Load}" default="0" />;

    function save() {
        var data = {
            name: $("#caravan_name").val(),
            point1Item: point1Item,
            point1Load: point1Load,
            point2Item: point2Item,
            point2Load: point2Load,
            point1: ${point1.id},
            point2: ${point2.id}
        };
        <c:if test="${caravan.id > 0}">
        $.put("/game/caravans/${caravan.id}", data, function() {
            closeMenu();
        });
        </c:if>
        <c:if test="${caravan.id == 0}">
        $.post("/game/caravans/", data, function() {
            closeMenu();
            updateGame();
        });
        </c:if>
    }

    function selectItem(point, id, type, name) {
        if(point == 1) {
            point1Item = id;
            point1Load = 0;

            $("#point1ItemText").text(name + " (" + type + ")");
            $("#point1ItemIcon").attr("src", "assets/items/"+id+".png");

            $("#point1ItemLoadText").text(point1Load + "t");
            $("#slider1").slider("value", point1Load);

            $("#slider1").slider("enable");
        } else {
            point2Item = id;
            point2Load = 0;

            $("#point2ItemText").text(name + " (" + type + ")");
            $("#point2ItemIcon").attr("src", "assets/items/"+id+".png");

            $("#point2ItemLoadText").text(point2Load + "t");
            $("#slider2").slider("value", point2Load);

            $("#slider2").slider("enable");
        }
    }

    $("#slider1").slider({
        max: 50,
        slide: function(event, ui) {
            point1Load = ui.value;
            $("#point1ItemLoadText").text(ui.value + "t");
        },
        value: point1Load,
        disabled: <c:if test="${caravan.point1Item != null}">false</c:if><c:if test="${caravan.point1Item == null}">true</c:if>
    });
    $("#slider2").slider({
        max: 50,
        slide: function(event, ui) {
            point2Load = ui.value;
            $("#point2ItemLoadText").text(ui.value + "t");
        },
        value: point2Load,
        disabled: <c:if test="${caravan.point2Item != null}">false</c:if><c:if test="${caravan.point2Item == null}">true</c:if>
    });
</script>