package de.clashofdynasties.jsp;

import de.clashofdynasties.models.Building;
import de.clashofdynasties.models.City;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class BuildItem extends SimpleTagSupport {
    private City city;
    private Building building;

    public void setCity(City city) {
        this.city = city;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
    }
}
