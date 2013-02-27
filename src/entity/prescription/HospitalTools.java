package entity.prescription;

import op.OPDE;
import op.tools.SYSTools;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: tloehr
 * Date: 14.12.11
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class HospitalTools {

    public static ListCellRenderer getKHRenderer() {
        return new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList jList, Object o, int i, boolean isSelected, boolean cellHasFocus) {
                String text;
                if (o == null) {
                    text = OPDE.lang.getString("misc.commands.>>noselection<<");
                } else if (o instanceof Hospital) {
                    text = ((Hospital) o).getName() + ", " + ((Hospital) o).getCity();
                } else {
                    text = o.toString();
                }
                return new DefaultListCellRenderer().getListCellRendererComponent(jList, text, i, isSelected, cellHasFocus);
            }
        };
    }

    public static String getFullName(Hospital kh) {
        if (kh == null)
            return "--";

        String string = kh.getName() + SYSTools.catchNull(kh.getCity(), ", ", "");
//        string += SYSTools.catchNull(kh.getTel(), OPDE.lang.getString("misc.msg.phone") + ": ", " ") + SYSTools.catchNull(kh.getFax(), OPDE.lang.getString("misc.msg.fax") + ": ", " ");
//        String string = kh.getName() + ", " + SYSTools.catchNull(kh.getStreet(), "", ", ") + SYSTools.catchNull(kh.getZIP(), "", " ") + SYSTools.catchNull(kh.getCity(), "", ", ");
//        string += SYSTools.catchNull(kh.getTel(), OPDE.lang.getString("misc.msg.phone") + ": ", " ") + SYSTools.catchNull(kh.getFax(), OPDE.lang.getString("misc.msg.fax") + ": ", " ");
        return string;
    }
}
