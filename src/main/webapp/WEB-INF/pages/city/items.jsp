<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Lager (${city.name})</h1>
<div id="content">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Grundbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[0].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[0]]}" default="0" />t ${items[0].name} (${items[0].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[0].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[0].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[0].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[1].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[1]]}" default="0" />t ${items[1].name} (${items[3].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[1].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[1].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[1].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Dorfbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[2].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[2]]}" default="0" />t ${items[2].name} (${items[2].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[2].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[2].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[2].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[3].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[3]]}" default="0" />t ${items[3].name} (${items[3].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[3].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[3].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[3].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Stadtbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[6].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[6]]}" default="0" />t ${items[6].name} (${items[6].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[6].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[6].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[6].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[8].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[8]]}" default="0" />t ${items[8].name} (${items[8].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[8].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[8].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[8].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[10].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[10]]}" default="0" />t ${items[10].name} (${items[10].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[10].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[10].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[10].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[11].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[11]]}" default="0" />t ${items[11].name} (${items[11].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[11].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[11].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[11].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Großstadtbedürfnisse</h4>
            <div>
                <img src="assets/items/${items[4].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[4]]}" default="0" />t ${items[4].name} (${items[4].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[4].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[4].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[4].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[5].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[5]]}" default="0" />t ${items[5].name} (${items[5].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[5].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[5].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[5].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[7].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[7]]}" default="0" />t ${items[7].name} (${items[7].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[7].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[7].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[7].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
                <br><br><br>
                <img src="assets/items/${items[9].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${city.items[items[9]]}" default="0" />t ${items[9].name} (${items[9].type.name})</span><br><span class="green">+4</span><c:if test="${city.requiredItemTypes.contains(items[9].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <c:if test="${player == city.player}"><c:if test="${city.requiredItemTypes.contains(items[9].type)}"><a style="font-weight:bold; cursor:pointer;">Verzehr stoppen?</a></c:if><c:if test="${!city.requiredItemTypes.contains(items[9].type)}"><span style="font-weight:bold;">Exportgut</span></c:if></c:if>
            </div>
        </div>
    </div>
</div>