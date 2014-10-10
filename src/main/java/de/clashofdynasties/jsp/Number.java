package de.clashofdynasties.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Number extends SimpleTagSupport {
    private double value;

    public void setValue(double value) {
        this.value = value;
    }

    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("#.#", dfs);
        df.setMaximumFractionDigits(8);
        out.print(df.format(value));
    }
}
