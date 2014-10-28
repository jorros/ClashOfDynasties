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
        int amount = city.getItems().containsKey(item.getId()) ? city.getItems().get(item.getId()).intValue() : 0;

        JspWriter out = getJspContext().getOut();
        out.print("<img src=\"assets/items/" + item.getId() + ".png\" style=\"float:left; margin-right:5px;\" />");
        out.print("<span style=\"color:#FFF; font-weight:bold;\">" + amount + "t " + item.getName() + " (" + item.getType().getName() + ")</span><br>");

        if(city.getPlayer().equals(player)) {
            int production = (int)(Math.floor(city.getBuildings().stream().filter(b -> b.getBlueprint().getProduceItem() != null).filter(b -> b.getBlueprint().getProduceItem().equals(item)).mapToDouble(b -> b.getBlueprint().getProducePerStep() * 3600).sum()));

            int consumption = 0;
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

            if((city.getStopConsumption() == null || !city.getStopConsumption().contains(item)) && city.getRequiredItemTypes().contains(item.getType())) {
                consumption = (int)Math.ceil(city.getPopulation() * rate * 3600);
            }

            if(production > 0 || amount > 0) {
                int balance = production - consumption;

                if (balance > 0)
                    out.print("<span class=\"green\">+" + balance + "</span>");
                else if (balance < 0)
                    out.print("<span class=\"red\">" + balance + "</span>");
                else
                    out.print("<span>" + balance + "</span>");

                if (production > 0 || consumption > 0) {
                    out.print(" (<span class=\"green\">" + production + "</span>/<span class=\"red\">" + consumption + "</span>)");
                }
            }
            else
                out.print("<span>0</span>");
        } else
            out.print("<span>Unbekannt</span>");

        out.print("<br>");
    }
}
