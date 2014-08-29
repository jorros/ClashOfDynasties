package de.clashofdynasties.jsp;

import de.clashofdynasties.models.*;

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

        if(blueprint instanceof UnitBlueprint) {
            UnitBlueprint unit = (UnitBlueprint)blueprint;

            boolean allowed = city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 7 || b.getBlueprint().getId() == 15).count() > 0;

            out.println("<button data-blueprint=\"" + unit.getId() + "\" data-type=\"1\" class=\"build " + (allowed ? "" : "disabled") + "\" style=\"margin-left:10px;\"><img style=\"width:32px;height:32px;\" src=\"assets/units/" + unit.getId() + ".png\" /></button>");

            out.print("<span style=\"color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;\">");
            int count = city.countUnits(unit.getId());
            out.print(count);
            out.print("</span>");

            out.print("<button data-blueprint=\"" + unit.getId() + "\" data-type=\"1\" " + (count == 0 ? "disabled" : "") + " style=\"margin-left:0px; vertical-align:25px\" class=\"remove\"></button>");
        } else {
            BuildingBlueprint building = (BuildingBlueprint)blueprint;

            boolean allowed = building.getRequiredBiomes().contains(city.getBiome()) && (building.getRequiredResource() == null || building.getRequiredResource().equals(city.getResource())) && city.getBuildings().size() < city.getCapacity() && (building.getMaxCount() == 0 || building.getMaxCount() > city.countBuildings(building.getId()));

            out.println("<button data-blueprint=\"" + building.getId() + "\" data-type=\"0\" class=\"build " + (allowed ? "" : "disabled") + "\" style=\"margin-left:10px;\"><img style=\"width:32px;height:32px;\" src=\"assets/buildings/" + building.getId() + ".png\" /></button>");

            out.print("<span style=\"color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;\">");
            int count = city.countBuildings(building.getId());
            out.print(count);
            out.print("</span>");

            out.print("<button data-blueprint=\"" + building.getId() + "\" data-type=\"0\" " + (count == 0 ? "disabled" : "") + " style=\"margin-left:0px; vertical-align:25px\" class=\"remove\"></button>");
        }
    }
}
