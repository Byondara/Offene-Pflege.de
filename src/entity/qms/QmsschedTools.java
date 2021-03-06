package entity.qms;

import io.lamma.LammaConversion;
import io.lamma.Recurrence;
import op.OPDE;
import op.tools.SYSCalendar;
import op.tools.SYSConst;
import op.tools.SYSTools;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import java.text.DateFormat;

/**
 * Created by tloehr on 17.06.14.
 */
public class QmsschedTools {


    public static final byte STATE_ACTIVE = 0;
    public static final byte STATE_INACTIVE = 1;
    public static final byte STATE_ARCHIVE = 2;

    public static String getAsHTML(Qmssched qmssched) {
        String result = SYSConst.html_paragraph(SYSConst.html_bold(qmssched.getMeasure()));

        result += getRepeatPattern(qmssched);
//        result += SYSConst.html_paragraph(qmssched.hasTime() ? DateFormat.getTimeInstance(DateFormat.SHORT).format(qmssched.getTime()) + " " + SYSTools.xx("misc.msg.Time.short") + ", " + wdh : wdh);

        if (qmssched.getStation() != null) {
            result += SYSConst.html_paragraph(SYSTools.xx("misc.msg.station") + ": " + qmssched.getStation().getName() + ", " + qmssched.getStation().getHome().getName());
        } else if (qmssched.getHome() != null) {
            result += SYSConst.html_paragraph(SYSTools.xx("misc.msg.home") + ": " + qmssched.getHome().getName());
        }
        result += SYSTools.catchNull(qmssched.getText(), "<p><i>", "</i></p>");


        return result;
    }

    public static String getRepeatPattern(Qmssched qmssched) {
        String result = "";

        if (qmssched.isDaily()) {
            if (qmssched.getDaily() > 1) {
                result += SYSTools.xx("misc.msg.every") + " " + qmssched.getDaily() + " " + SYSTools.xx("misc.msg.Days2");
            } else {
                result += SYSTools.xx("misc.msg.everyDay");
            }
        } else if (qmssched.isWeekly()) {
            if (qmssched.getWeekly() == 1) {
                result += result += SYSTools.xx("misc.msg.everyWeek");
            } else {
                result += SYSTools.xx("misc.msg.every") + " " + qmssched.getWeekly() + " " + SYSTools.xx("misc.msg.weeks");
            }

            MutableDateTime mdt = new MutableDateTime();
            mdt.setDayOfWeek(qmssched.getWeekday());

            result += ", " + SYSTools.xx("misc.msg.every4") + " " + mdt.dayOfWeek().getAsText();

        } else if (qmssched.isMonthly()) {


            if (qmssched.getMonthly() == 1) {
                result += SYSTools.xx("misc.msg.everyMonth") + ", ";
            } else {
                result += SYSTools.xx("misc.msg.every") + " " + qmssched.getMonthly() + " " + SYSTools.xx("misc.msg.months") + ", ";
            }

            if (qmssched.getWeekday() > 0) { // with a nth weekday in that month
                MutableDateTime mdt = new MutableDateTime();
                mdt.setDayOfWeek(qmssched.getWeekday());

                result += SYSTools.xx("misc.msg.every4") + " " + qmssched.getDayinmonth() + ". " + mdt.dayOfWeek().getAsText();
            } else {
                result += SYSTools.xx("misc.msg.every4") + " " + qmssched.getDayinmonth() + ". " + SYSTools.xx("misc.msg.day");
            }


        } else if (qmssched.isYearly()) {

            if (qmssched.getYearly() == 1) {
                result += SYSTools.xx("misc.msg.everyYear") + ", ";
            } else {
                result += SYSTools.xx("misc.msg.every") + " " + qmssched.getYearly() + " " + SYSTools.xx("misc.msg.Years") + ", ";
            }


            MutableDateTime mdt = new MutableDateTime();
            mdt.setDayOfMonth(qmssched.getDayinmonth());
            mdt.setMonthOfYear(qmssched.getMonthinyear());
            result += SYSTools.xx("misc.msg.every4") + " " + qmssched.getDayinmonth() + ". " + mdt.monthOfYear().getAsText();


        } else {
            result = "";
        }

        LocalDate ldatum = new LocalDate(qmssched.getStartingOn());
        LocalDate today = new LocalDate();

        if (ldatum.compareTo(today) > 0) { // Die erste Ausführung liegt in der Zukunft
            result += "<br/>" + SYSTools.xx("opde.controlling.qms.dlgqmsplan.pnlschedule.startingon") + ": " + DateFormat.getDateInstance().format(qmssched.getStartingOn());
        }

        return result;
    }


    /**
     * takes the recurrence pattern inside a qmssched and creates a list of recurrences for a lamma sequence generator.
     *
     * @param qmssched
     * @return
     */
    public static Recurrence getRecurrence(Qmssched qmssched) {

        Recurrence recurrence = null;

        if (qmssched.isDaily()) {
            recurrence = LammaConversion.days(qmssched.getDaily());
        } else if (qmssched.isWeekly()) {
            recurrence = LammaConversion.weeks(
                    qmssched.getWeekly(),
                    SYSCalendar.weeksdays[qmssched.getWeekday()]);
        } else if (qmssched.isMonthly()) {
            if (qmssched.getWeekday() > 0) { // with a nth weekday in that month
                recurrence = LammaConversion.months(
                        qmssched.getMonthly(),
                        LammaConversion.nthWeekdayOfMonth(qmssched.getDayinmonth(), SYSCalendar.weeksdays[qmssched.getWeekday()]));
            } else { // with a specific day in that month
                recurrence = LammaConversion.months(
                        qmssched.getMonthly(),
                        LammaConversion.nthDayOfMonth(qmssched.getDayinmonth()));
            }
        } else if (qmssched.isYearly()) {
            recurrence = LammaConversion.years(
                    qmssched.getYearly(),
                    LammaConversion.nthMonthOfYear(SYSCalendar.months[qmssched.getMonthinyear()],
                            LammaConversion.nthDayOfMonth(qmssched.getDayinmonth())));
        }
        return recurrence;
    }

    public static boolean isUnused(Qmssched qmssched) {
        boolean unused = true;
        for (Qms qms : qmssched.getQmsList()) {
            unused = qms.isOpen();
            if (!unused) break;
        }
        return unused;
    }

    public static long getNumOpen(Qmssched qmssched) {
        long open = 0;

        EntityManager em = OPDE.createEM();

        try {

            String jpql = " SELECT count(q) " +
                    " FROM Qms q" +
                    " WHERE q.qmssched = :qmssched AND q.state = :state ";

            Query query = em.createQuery(jpql);
            query.setParameter("state", QmsTools.STATE_OPEN);
            query.setParameter("qmssched", qmssched);

            open = (long) query.getSingleResult();

        } catch (Exception se) {
            OPDE.fatal(se);
        } finally {
            em.close();
        }

//
//        for (Qms qms : qmssched.getQmsList()) {
//            if (qms.isOpen()) {
//                open++;
//            }
//
//        }
        return open;
    }


    public static Icon getIcon(Qmssched qmssched) {

        Icon icon = null;

        int pastdue = 2;
        int due = 1;

        int worstCase = 0;

        for (Qms qms : qmssched.getQmsList()) {
            if (qms.isOpen()) {
                int thisCase = 0;
                if (qms.isPastDue()) {
                    thisCase = pastdue;
                }
                if (qms.isDue()) {
                    thisCase = due;
                }
                worstCase = Math.max(worstCase, thisCase);
            }
        }

        if (worstCase == pastdue) {
            icon = SYSConst.icon22ledRedOn;
        } else if (worstCase == due) {
            icon = SYSConst.icon22ledYellowOn;
        }

        return icon;
    }

}
