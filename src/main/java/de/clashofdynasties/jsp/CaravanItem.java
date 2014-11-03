package de.clashofdynasties.jsp;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Item;
import de.clashofdynasties.models.Player;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class CaravanItem extends SimpleTagSupport {
    private Player player;
    private Item item;
    private City city;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void doTag() throws JspException, IOException {
        if(item != null) {
            int amount = (int) Math.round(city.getStoredItem(item.getId()));

            JspWriter out = getJspContext().getOut();
            out.print("<img src=\"assets/items/" + item.getId() + ".png\" style=\"float:left; margin-right:5px;\" />");
            out.print("<span style=\"color:#FFF; font-weight:bold;\">" + amount + "t " + item.getName() + " (" + item.getType().getName() + ")</span><br>");

            if (city.getPlayer().equals(player)) {
                double production = city.getBuildings().stream().filter(b -> b.getBlueprint().getProduceItem() != null).filter(b -> b.getBlueprint().getProduceItem().equals(item)).mapToDouble(b -> b.getBlueprint().getProducePerStep() * 3600).sum();

                double consumption = 0;
                double rate = 0;

                switch (item.getType().getType()) {
                    case 1:
                        rate = city.getType().getConsumeBasic();
                        break;

                    case 2:
                        rate = city.getType().getConsumeLuxury1();
                        break;

                    case 3:
                        rate = city.getType().getConsumeLuxury2();
                        break;

                    case 4:
                        rate = city.getType().getConsumeLuxury3();
                        break;
                }

                if ((city.getStopConsumption() == null || !city.getStopConsumption().contains(item)) && city.getRequiredItemTypes().contains(item.getType())) {
                    consumption = city.getPopulation() * rate * 3600;
                }

                if (production > 0 || amount > 0) {
                    double balance = production - consumption;

                    if (balance > 0)
                        out.print("<span class=\"green\">+" + Math.floor(balance) + "</span>");
                    else if (balance < 0)
                        out.print("<span class=\"red\">" + Math.floor(balance) + "</span>");
                    else
                        out.print("<span>" + Math.floor(balance) + "</span>");

                    if (production > 0 || consumption > 0) {
                        out.print(" (<span class=\"green\">" + Math.floor(production) + "</span>/<span class=\"red\">" + Math.floor(consumption) + "</span>)");
                    }
                } else
                    out.print("<span>0</span>");
            } else
                out.print("<span>Unbekannt</span>");

            out.print("<br>");
        }
    }
}
