<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <script type="text/javascript" src="/lib/jquery-2.1.0.js"></script>
    <style>
        .colorChoose {
            width: 128px;
            height: 128px;
            cursor: pointer;
            margin-left: 12px;
        }

        .colorChooseOuter {
            overflow-y:hidden;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            justify-content: center;
            align-items: center;
            height: 100%;
            display:-webkit-flex;
            -webkit-flex-direction:row;
            -webkit-flex-wrap:wrap;
        }
    </style>
</head>
<body>
<div id="wrapper">
    <div id="body">
        <c:choose>
            <c:when test="${error}">
                <h1 style="color:#FF1A00;">Diese Farbe wurde bereits gew&auml;hlt</h1>
            </c:when>
            <c:otherwise>
                <h1>W&auml;hle eine Farbe</h1>
            </c:otherwise>
        </c:choose>
        <div id="content" class="colorChooseOuter">
            <c:if test='${!fn:contains(notAvailableColors, "1")}'>
                <div class="colorChoose" style="background-color:#4096EE;" onclick="window.location.href='/step3?key=${key}&color=1'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "2")}'>
                <div class="colorChoose" style="background-color:#D01F3C;" onclick="window.location.href='/step3?key=${key}&color=2'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "3")}'>
                <div class="colorChoose" style="background-color:#F5B800;" onclick="window.location.href='/step3?key=${key}&color=3'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "4")}'>
                <div class="colorChoose" style="background-color:#CCFF33;" onclick="window.location.href='/step3?key=${key}&color=4'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "5")}'>
                <div class="colorChoose" style="background-color:#33FF66;" onclick="window.location.href='/step3?key=${key}&color=5'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "6")}'>
                <div class="colorChoose" style="background-color:#FF794D;" onclick="window.location.href='/step3?key=${key}&color=6'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "7")}'>
                <div class="colorChoose" style="background-color:#FF4D79;" onclick="window.location.href='/step3?key=${key}&color=7'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "8")}'>
                <div class="colorChoose" style="background-color:#B88A00;" onclick="window.location.href='/step3?key=${key}&color=8'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "9")}'>
                <div class="colorChoose" style="background-color:#FE6F71;" onclick="window.location.href='/step3?key=${key}&color=9'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "10")}'>
                <div class="colorChoose" style="background-color:#CCCCCC;" onclick="window.location.href='/step3?key=${key}&color=10'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "11")}'>
                <div class="colorChoose" style="background-color:#E45EA7;" onclick="window.location.href='/step3?key=${key}&color=11'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "12")}'>
                <div class="colorChoose" style="background-color:#7DB2AB;" onclick="window.location.href='/step3?key=${key}&color=12'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "13")}'>
                <div class="colorChoose" style="background-color:#D8FBE2;" onclick="window.location.href='/step3?key=${key}&color=13'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "14")}'>
                <div class="colorChoose" style="background-color:#E8F5BB;" onclick="window.location.href='/step3?key=${key}&color=14'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "15")}'>
                <div class="colorChoose" style="background-color:#6CDFEA;" onclick="window.location.href='/step3?key=${key}&color=15'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "16")}'>
                <div class="colorChoose" style="background-color:#84C9FF;" onclick="window.location.href='/step3?key=${key}&color=16'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "17")}'>
                <div class="colorChoose" style="background-color:#7FAF1B;" onclick="window.location.href='/step3?key=${key}&color=17'"></div>
            </c:if>
            <c:if test='${!fn:contains(notAvailableColors, "18")}'>
                <div class="colorChoose" style="background-color:#D9FFA9;" onclick="window.location.href='/step3?key=${key}&color=18'"></div>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>