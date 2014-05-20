<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Lager (${city.name})</h1>
<div id="content">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Grundbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[0].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[1]}" default="0" />t ${items[0].name} (${items[0].type.name})</span><br><span class="<c:if test="${balance[0] >= 0}">green</c:if><c:if test="${balance[0] < 0}">red</c:if>"><c:if test="${balance[0] > 0}">+</c:if><c:if test="${balance[0] < 0}">-</c:if><c:out value="${balance[0]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[0].type)}"> (<span class="green"><c:out value="${production[0]}" default="0" /></span>/<span class="red"><c:out value="${consumption[0]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[0].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(1);"><c:if test="${city.stopConsumption.contains(items[0])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[0])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[0].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[1].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[2]}" default="0" />t ${items[1].name} (${items[1].type.name})</span><br><span class="<c:if test="${balance[1] >= 0}">green</c:if><c:if test="${balance[1] < 0}">red</c:if>"><c:if test="${balance[1] > 0}">+</c:if><c:if test="${balance[1] < 0}">-</c:if><c:out value="${balance[1]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[1].type)}"> (<span class="green"><c:out value="${production[1]}" default="0" /></span>/<span class="red"><c:out value="${consumption[1]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[1].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(2);"><c:if test="${city.stopConsumption.contains(items[1])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[1])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[1].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Dorfbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[2].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[3]}" default="0" />t ${items[2].name} (${items[2].type.name})</span><br><span class="<c:if test="${balance[2] >= 0}">green</c:if><c:if test="${balance[2] < 0}">red</c:if>"><c:if test="${balance[2] > 0}">+</c:if><c:if test="${balance[2] < 0}">-</c:if><c:out value="${balance[2]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[2].type)}"> (<span class="green"><c:out value="${production[2]}" default="0" /></span>/<span class="red"><c:out value="${consumption[2]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[2].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(3);"><c:if test="${city.stopConsumption.contains(items[2])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[2])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[2].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[3].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[4]}" default="0" />t ${items[3].name} (${items[3].type.name})</span><br><span class="<c:if test="${balance[3] >= 0}">green</c:if><c:if test="${balance[3] < 0}">red</c:if>"><c:if test="${balance[3] > 0}">+</c:if><c:if test="${balance[3] < 0}">-</c:if><c:out value="${balance[3]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[3].type)}"> (<span class="green"><c:out value="${production[3]}" default="0" /></span>/<span class="red"><c:out value="${consumption[3]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[3].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(4);"><c:if test="${city.stopConsumption.contains(items[3])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[3])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[3].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Stadtbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[6].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[7]}" default="0" />t ${items[6].name} (${items[6].type.name})</span><br><span class="<c:if test="${balance[6] >= 0}">green</c:if><c:if test="${balance[6] < 0}">red</c:if>"><c:if test="${balance[6] > 0}">+</c:if><c:if test="${balance[6] < 0}">-</c:if><c:out value="${balance[6]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[6].type)}"> (<span class="green"><c:out value="${production[6]}" default="0" /></span>/<span class="red"><c:out value="${consumption[6]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[6].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(7);"><c:if test="${city.stopConsumption.contains(items[6])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[6])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[6].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[8].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[9]}" default="0" />t ${items[8].name} (${items[8].type.name})</span><br><span class="<c:if test="${balance[8] >= 0}">green</c:if><c:if test="${balance[8] < 0}">red</c:if>"><c:if test="${balance[8] > 0}">+</c:if><c:if test="${balance[8] < 0}">-</c:if><c:out value="${balance[8]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[8].type)}"> (<span class="green"><c:out value="${production[8]}" default="0" /></span>/<span class="red"><c:out value="${consumption[8]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[8].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(9);"><c:if test="${city.stopConsumption.contains(items[8])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[8])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[8].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[10].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[11]}" default="0" />t ${items[10].name} (${items[10].type.name})</span><br><span class="<c:if test="${balance[10] >= 0}">green</c:if><c:if test="${balance[10] < 0}">red</c:if>"><c:if test="${balance[10] > 0}">+</c:if><c:if test="${balance[10] < 0}">-</c:if><c:out value="${balance[10]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[10].type)}"> (<span class="green"><c:out value="${production[10]}" default="0" /></span>/<span class="red"><c:out value="${consumption[10]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[10].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(11);"><c:if test="${city.stopConsumption.contains(items[10])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[10])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[10].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[11].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[12]}" default="0" />t ${items[11].name} (${items[11].type.name})</span><br><span class="<c:if test="${balance[11] >= 0}">green</c:if><c:if test="${balance[11] < 0}">red</c:if>"><c:if test="${balance[11] > 0}">+</c:if><c:if test="${balance[11] < 0}">-</c:if><c:out value="${balance[11]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[11].type)}"> (<span class="green"><c:out value="${production[11]}" default="0" /></span>/<span class="red"><c:out value="${consumption[11]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[11].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(12);"><c:if test="${city.stopConsumption.contains(items[11])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[11])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[11].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Großstadtbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[4].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[5]}" default="0" />t ${items[4].name} (${items[4].type.name})</span><br><span class="<c:if test="${balance[4] >= 0}">green</c:if><c:if test="${balance[4] < 0}">red</c:if>"><c:if test="${balance[4] > 0}">+</c:if><c:if test="${balance[4] < 0}">-</c:if><c:out value="${balance[4]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[4].type)}"> (<span class="green"><c:out value="${production[4]}" default="0" /></span>/<span class="red"><c:out value="${consumption[4]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[4].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(5);"><c:if test="${city.stopConsumption.contains(items[4])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[4])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[4].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[5].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[6]}" default="0" />t ${items[5].name} (${items[5].type.name})</span><br><span class="<c:if test="${balance[5] >= 0}">green</c:if><c:if test="${balance[5] < 0}">red</c:if>"><c:if test="${balance[5] > 0}">+</c:if><c:if test="${balance[5] < 0}">-</c:if><c:out value="${balance[5]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[5].type)}"> (<span class="green"><c:out value="${production[5]}" default="0" /></span>/<span class="red"><c:out value="${consumption[5]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[5].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(6);"><c:if test="${city.stopConsumption.contains(items[5])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[5])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[5].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[7].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[8]}" default="0" />t ${items[7].name} (${items[7].type.name})</span><br><span class="<c:if test="${balance[7] >= 0}">green</c:if><c:if test="${balance[7] < 0}">red</c:if>"><c:if test="${balance[7] > 0}">+</c:if><c:if test="${balance[7] < 0}">-</c:if><c:out value="${balance[7]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[7].type)}"> (<span class="green"><c:out value="${production[7]}" default="0" /></span>/<span class="red"><c:out value="${consumption[7]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[7].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(8);"><c:if test="${city.stopConsumption.contains(items[7])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[7])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[7].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[9].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[10]}" default="0" />t ${items[9].name} (${items[9].type.name})</span><br><span class="<c:if test="${balance[9] >= 0}">green</c:if><c:if test="${balance[9] < 0}">red</c:if>"><c:if test="${balance[9] > 0}">+</c:if><c:if test="${balance[9] < 0}">-</c:if><c:out value="${balance[9]}" default="0" /></span><c:if test="${city.requiredItemTypes.contains(items[9].type)}"> (<span class="green"><c:out value="${production[9]}" default="0" /></span>/<span class="red"><c:out value="${consumption[9]}" default="0" /></span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[9].type)}"><a style="font-weight:bold; cursor:pointer;" onclick="toggleConsumption(10);"><c:if test="${city.stopConsumption.contains(items[9])}">Verzehr erlauben?</c:if><c:if test="${!city.stopConsumption.contains(items[9])}">Verzehr verbieten?</c:if></a></c:if><c:if test="${!city.requiredItemTypes.contains(items[9].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
    </div>
    <div>
        <button onclick="closeMenu()" style="position:absolute; right:25px; bottom: 120px;">Schlie&szlig;en</button>
    </div>
</div>
<script>
    function toggleConsumption(item) {
        $.put("/game/cities/${city.id}/consumption", { item: item }, function() {
            openMenu("store?city=${city.id}");
        });
    }
</script>