<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${caravan.name}</h1>
<div id="content" style="overflow:hidden;">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>${point1.name}</h4>
            <div style="height:270px; overflow-y:hidden; overflow-x:hidden;">
                <img src="assets/items/${items[0].id}.png" style="float:left; margin-right:5px;" />
                <span style="color:#FFF; font-weight:bold;"><c:out value="${point1.items[items[0]]}" default="0" />t ${items[0].name} (${items[0].type.name})</span><br><span class="green">+4</span><c:if test="${point1.requiredItemTypes.contains(items[0].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                <div style="height:200px; overflow-y:auto; clear:both;">
                    <img src="assets/items/${items[0].id}.png" style="float:left; margin-right:5px;" />
                    <span style="color:#FFF; font-weight:bold;"><c:out value="${point1.items[items[0]]}" default="0" />t ${items[0].name} (${items[0].type.name})</span><br><span class="green">+4</span><c:if test="${point1.requiredItemTypes.contains(items[0].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                    <a style="font-weight:bold; cursor:pointer;">Auswählen</a>
                    <br><br>
                    <img src="assets/items/${items[1].id}.png" style="float:left; margin-right:5px;" />
                    <span style="color:#FFF; font-weight:bold;"><c:out value="${point1.items[items[1]]}" default="0" />t ${items[1].name} (${items[3].type.name})</span><br><span class="green">+4</span><c:if test="${point1.requiredItemTypes.contains(items[1].type)}"> (<span class="green">10</span>/<span class="red">6</span>)</c:if><br>
                </div>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>${point2.name}</h4>
            <div style="height:270px; overflow-y:auto; overflow-x:hidden;">
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Karawanenlager</h4>
            <div style="height:270px; overflow-y:auto; overflow-x:hidden;">
            </div>
        </div>
    </div>
    <div style="clear:both; text-align:center;">
        <span style="color:white;"></span>
    </div>
    <div style="height:60px;">
        <label for="caravan_name">Name: </label>
        <input id="caravan_name" style="width:300px;" type="text" value="${caravan.name}" />
        <button onclick="save()" style="float:right;">Speichern</button>
        <button onclick="closeMenu()" style="float:right;">Abbrechen</button>
    </div>
</div>