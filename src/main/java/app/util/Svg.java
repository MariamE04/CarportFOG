package app.util;

public class Svg {
    private static final String SVG_TEMPLATE = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\"  width=\"%s\" \n" +
            "     preserveAspectRatio=\"xMinYMin\">";



    private static final String SVG_ARROW_DEFS ="  <defs>\n" +
            "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "    </defs>";

    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%.2f\" y=\"%.2f\" height=\"%f\" width=\"%f\" style=\"%s\" />";

    private static final String SVG_LINE_TEMPLATE = "<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"%s\" />";

    private static final String SVG_TEXT_TEMPLATE = "<text x=\"%d\" y=\"%d\" transform=\"rotate(%d %d %d)\" style=\"%s\">%s</text>";


    private StringBuilder svg = new StringBuilder();

    //Det er her, man definerer hvor SVG-billede starter, hvor stort det er, osv.
    public Svg(int x, int y, String viewBox, String width){
        svg.append(String.format(SVG_TEMPLATE,x,y,viewBox, width));
        svg.append(SVG_ARROW_DEFS);
    }

    //Tilføjer en rektangel til SVG’en med de givne mål og stil.
    public void addRectangle(double x, double y, double height, double width, String style){
        svg.append(String.format(SVG_RECT_TEMPLATE, x,y,height,width,style));
    }

    //Tilføjer en almindelig linje til SVG’en.
    public void addLine(int x1, int y1, int x2, int y2, String style){
        svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2, style));
    }

    //Tilføjer en linje med en pil i enden (bruger marker-end).
    public void addArrow(int x1, int y1, int x2, int y2, String style){
        // Pile i slutningen af linjen
        String arrowStyle = style + "; marker-end: url(#endArrow)";
        addLine(x1, y1, x2, y2, arrowStyle);
    }

    //Tilføjer tekst med placering og rotation (f.eks. for at vise "300 cm" lodret).
    public void addText(int x, int y, int rotation, String text){
        String textStyle = "font-family: Arial; font-size: 16px; fill: black"; // stilen kan ændres her
        svg.append(String.format(SVG_TEXT_TEMPLATE, x, y, rotation, x, y, textStyle, text));
    }

    //Lægger en anden SVG ind i den eksisterende. Bruges, hvis man bygger flere dele separat.
    public void addSvg(Svg innerSvg){
        svg.append(innerSvg.toString());
    }

    //Når objektet udskrives som tekst, returnerer det hele SVG-koden, afsluttet med </svg>.
    @Override
    public String toString() {
        return svg.append("</svg>").toString();
    }
}
