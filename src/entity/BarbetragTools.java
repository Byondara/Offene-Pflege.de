/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.info.Resident;
import entity.info.ResidentTools;
import op.OPDE;
import op.tools.HTMLTools;
import op.tools.SYSCalendar;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author tloehr
 */
public class BarbetragTools {

    // TGID, BelegDatum, Belegtext, Betrag, _creator, _editor, _cdate, _edate, _cancel
    public static String getEinzelnAsHTML(List<Barbetrag> listTG, BigDecimal vortrag, Resident bewohner) {

        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
        DecimalFormat currency = new DecimalFormat("######.00");
        BigDecimal saldo = vortrag;

        int BARBEGTRAG_PAGEBREAK_AFTER_ELEMENT_NO = Integer.parseInt(OPDE.getProps().getProperty("barbetrag_pagebreak_after_element_no"));

        int elementNumber = 1;
        boolean pagebreak = false;

        String header = "Barbetragsübersicht für " + ResidentTools.getLabelText(bewohner);

        String html = "<html>\n"
                + "<head>\n"
                + "<title>" + header + "</title>\n"
                + OPDE.getCSS()
//                + "<style type=\"text/css\" media=\"all\">\n"
//                + "body { padding:10px; }\n"
//                + "#fontsmall { font-size:10px; font-weight:bold; font-family:Arial,sans-serif;}\n"
//                + "#fonth1 { font-size:24px; font-family:Arial,sans-serif;}\n"
//                + "#fonth2 { font-size:16px; font-weight:bold; font-family:Arial,sans-serif;}\n"
//                + "#fonttext { font-size:12px; font-family:Arial,sans-serif;}\n"
//                + "#fonttextgrau { font-size:12px; background-color:#CCCCCC; font-family:Arial,sans-serif;}\n"
//                + "</style>\n"
                + HTMLTools.JSCRIPT_PRINT
                + "</head>\n"
                + "<body>\n";


        html += "<h1 align=\"center\" id=\"fonth1\">" + header + "</h1>";

        int monat = -1;

        for (Barbetrag tg : listTG){

            GregorianCalendar belegDatum = SYSCalendar.toGC(tg.getBelegDatum());
            boolean monatsWechsel = monat != belegDatum.get(GregorianCalendar.MONTH);


            if (pagebreak || monatsWechsel) {
                // Falls zufällig ein weiterer Header (der 3 Elemente hoch ist) einen Pagebreak auslösen WÜRDE
                // müssen wir hier schonmal vorsorglich den Seitenumbruch machen.
                // 2 Zeilen rechne ich nochdrauf, damit die Tabelle mindestens 2 Zeilen hat, bevor der Seitenumbruch kommt.
                // Das kann dann passieren, wenn dieser if Konstrukt aufgrund eines Monats-Wechsels durchlaufen wird.
                pagebreak = (elementNumber + 3 + 2) > BARBEGTRAG_PAGEBREAK_AFTER_ELEMENT_NO;

                // Außer beim ersten mal und beim Pagebreak, muss dabei die vorherige Tabelle abgeschlossen werden.


                if (monat != -1) { // beim ersten mal nicht
                    html += "<tr>";
                    html += "<td width=\"90\"  align=\"center\" >&nbsp;</td>";
                    html += "<td width=\"400\">" + (monatsWechsel ? "Saldo zum Monatsende" : "Zwischensumme") + "</td>";
                    html += "<td>&nbsp;</td>";
                    html += "<td width=\"70\" align=\"right\"" + (monatsWechsel ? " id=\"fonth2\">" : ">") + "&euro; " + currency.format(saldo) + "</td>";
                    html += "</tr>\n";
                    html += "</table>\n";
                }

                monat = belegDatum.get(GregorianCalendar.MONTH);

                html += "<h2 id=\"fonth2\" " + (pagebreak ? "style=\"page-break-before:always\">" : ">") + ((pagebreak && !monatsWechsel) ? "<i>(fortgesetzt)</i> " : "") + df.format(new Date(belegDatum.getTimeInMillis())) + "</h2>\n";
                html += "<table id=\"fonttext\" border=\"1\" cellspacing=\"0\">\n<tr>"
                        + "<th>Belegdatum</th><th>Belegtext</th><th>Betrag</th><th>Saldo</th></tr>\n";

                // Vortragszeile
                html += "<tr id=\"fonttextgrau\">";
                html += "<td width=\"90\"  align=\"center\" >&nbsp;</td>";
                html += "<td width=\"400\">" + (pagebreak && !monatsWechsel ? "Übertrag von vorheriger Seite" : "Übertrag aus Vormonat") + "</td>";
                html += "<td>&nbsp;</td>";
                html += "<td width=\"70\" align=\"right\">&euro; " + currency.format(saldo) + "</td>";
                html += "</tr>\n";

                elementNumber += 3;

                if (pagebreak) {
                    elementNumber = 1;
                    pagebreak = false;
                }
            }

            saldo = saldo.add(tg.getBetrag());

            html += "<tr>";
            html += "<td width=\"90\"  align=\"center\" >" + DateFormat.getDateInstance().format(new Date(belegDatum.getTimeInMillis())) + "</td>";
            html += "<td width=\"400\">" + tg.getBelegtext() + "</td>";
            html += "<td width=\"70\" align=\"right\">&euro; " + currency.format(tg.getBetrag()) + "</td>";
            html += "<td width=\"70\" align=\"right\">&euro; " + currency.format(saldo) + "</td>";
            html += "</tr>\n";
            elementNumber += 1;

            pagebreak = elementNumber > BARBEGTRAG_PAGEBREAK_AFTER_ELEMENT_NO;
        }

        html += "<tr>";
        html += "<td width=\"90\"  align=\"center\" >&nbsp;</td>";
        html += "<td width=\"400\">Saldo zum Monatsende</td>";
        html += "<td>&nbsp;</td>";
        html += "<td width=\"70\" align=\"right\" id=\"fonth2\">&euro; " + currency.format(saldo) + "</td>";
        html += "</tr>\n";

        html += "</table>\n"
                + "</body>";


        return html;
    }
}
