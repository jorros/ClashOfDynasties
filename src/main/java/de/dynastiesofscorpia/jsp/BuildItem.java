package de.dynastiesofscorpia.jsp;

import de.dynastiesofscorpia.models.BuildingBlueprint;
import de.dynastiesofscorpia.models.City;
import de.dynastiesofscorpia.models.IBlueprint;
import de.dynastiesofscorpia.models.UnitBlueprint;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class BuildItem extends SimpleTagSupport {
    private City city;
    private IBlueprint blueprint;

    public void setCity(City city) {
        this.city = city;
    }

    public void setBlueprint(IBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        out.println("<div style=\"position:relative; width:70px; height:80px; margin-left:10px;\">");

        if(blueprint instanceof UnitBlueprint) {
            UnitBlueprint unit = (UnitBlueprint)blueprint;

            boolean allowed = city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 7 || b.getBlueprint().getId() == 15).count() > 0;

            out.print("<button data-blueprint=\"" + unit.getId() + "\" data-type=\"1\" class=\"build" + (allowed ? "" : " disabled") + "\" style=\"position:absolute; top:15px;\"><img style=\"width:32px;height:32px;\" src=\"assets/units/" + unit.getId() + ".png\" /></button>");

            out.print("<span style=\"color:#FFF; position:absolute; left:0px; top:64px; text-align:center; width:49px;\">");
            long count = city.countUnits(unit);
            out.print(count);
            out.print("</span>");

            out.print("<button data-blueprint=\"" + unit.getId() + "\" data-type=\"1\" " + (count == 0 ? "disabled" : "") + " style=\"position:absolute; top:0; left:30px;\" class=\"remove\"></button>");
        } else {
            BuildingBlueprint building = (BuildingBlueprint)blueprint;

            boolean allowed = building.getRequiredBiomes().contains(city.getBiome()) && (building.getRequiredResource() == null || building.getRequiredResource().equals(city.getResource())) && city.getBuildings().size() < city.getCapacity() && (building.getMaxCount() == 0 || building.getMaxCount() > city.countBuildings(building) && (blueprint.getNation() == null || blueprint.getNation().equals(city.getPlayer().getNation()))) && building.getRequiredCityType().getId() <= city.getType().getId() && city.getType().getId() != 4;

            out.println("<button data-blueprint=\"" + building.getId() + "\" data-type=\"0\" class=\"build " + (allowed ? "" : "disabled") + "\" style=\"position:absolute; top:15px;\"><img style=\"width:32px;height:32px;\" src=\"assets/buildings/" + building.getId() + ".png\" /></button>");

            out.print("<span style=\"color:#FFF; position:absolute; left:0px; top:64px; text-align:center; width:49px;\">");
            long count = city.countBuildings(building);
            out.print(count);
            out.print("</span>");

            out.print("<button data-blueprint=\"" + building.getId() + "\" data-type=\"0\" " + (count == 0 ? "disabled" : "") + " style=\"position:absolute; top:0; left: 30px;\" class=\"remove\"></button>");
        }
        out.print("</div>");
    }
}
