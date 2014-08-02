<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cod" uri="/WEB-INF/clashofdynasties.tld" %>

<h1>Lager (${city.name})</h1>
<div id="content">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">

            <h4>Grundbedürfnisse</h4>
            <div>
                <cod:StoreItem item="${items[0]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[1]}" player="${player}" city="${city}" />
            </div>
        </div>
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Dorfbedürfnisse</h4>
            <div>
                <cod:StoreItem item="${items[2]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[3]}" player="${player}" city="${city}" />
                <br><br>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Stadtbedürfnisse</h4>
            <div>
                <cod:StoreItem item="${items[6]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[8]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[10]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[11]}" player="${player}" city="${city}" />
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Großstadtbedürfnisse</h4>
            <div>
                <cod:StoreItem item="${items[4]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[5]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[7]}" player="${player}" city="${city}" />
                <br><br><br>
                <cod:StoreItem item="${items[9]}" player="${player}" city="${city}" />
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