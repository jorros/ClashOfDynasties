<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Nachrichten</h1>
<div id="content" style="overflow:hidden;">
    <div style="width:250px; overflow-y: auto; height: 100%; float:left;">
        <c:forEach items="${players}" var="player" varStatus="pstatus">
            <div style="cursor:pointer;" onclick="openMenu('messages?pid=${player.id}', false);">
                <span style="font-size:20px; font-weight: bold;">${player.name}</span><br>
                <span class="<c:if test='${lastMessages[player.id] == "Neue Nachricht"}'>green</c:if>">${lastMessages[player.id]}</span>
            </div>
            <c:if test="${!pstatus.last}">
                <hr>
            </c:if>
        </c:forEach>
    </div>
    <div style="float:right; width:650px;">
        <c:if test="${other != null}">
            <h1>${other.name}</h1><br>
            <div id="messageContent" style="width:630px; height:340px; margin-left:20px; margin-top:20px; overflow-y:auto; overflow-x:hidden; padding-right:10px;">
                <c:forEach items="${messages}" var="message" varStatus="mstatus">
                    <div style="<c:choose><c:when test="${player != message.from}">float:left;</c:when><c:otherwise>float:right; text-align:right;</c:otherwise></c:choose>">
                        <div>${message.from.name} (${message.getDate()}):</div>
                        <div style="font-family:'Philosopher-Bold'; font-size:18px; color:#a8a8a8;">${message.message}</div>
                    </div>
                    <c:if test="${!mstatus.last}">
                        <div style="clear:both; height:40px;"></div>
                    </c:if>
                </c:forEach>
            </div>
            <div style="margin-top:10px;">
            <textarea id="message" style="margin-left:80px; width:450px; height:75px; resize:none;"></textarea>
            <div style="float:right; margin-top:-20px;"><button id="send" style="margin-top:40px;">Schicken</button></div>
            </div>
        </c:if>
    </div>
</div>
<c:if test="${other != null}">
<script>
    var objDiv = document.getElementById("messageContent");
    objDiv.scrollTop = objDiv.scrollHeight;

    $("#send").click(function() {
        if($("#message").val() != "") {
            $.post("/game/players/${other.id}/message", { content: $("#message").val().replace(/(?:\r\n|\r|\n)/g, '<br />') }, function() {
                openMenu('messages?pid=${other.id}', false);
            });
        }
    });

    $("#message").keyup(function(event) {
        if(event.keyCode == 13 && !event.shiftKey) {
            $("#send").click();
            event.preventDefault();
            $("#message").focus();

            var objDiv = document.getElementById("messageContent");
            objDiv.scrollTop = objDiv.scrollHeight;

            return false;
        }
    });
</script>
</c:if>