package com.wraper.framework.tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


/**
 * Calculates austrian holidays. The holidays are partly static and partly dynamic. All dynamic holidays are relative to easter
 * monday. Calculation of easter monday is adapted from:
 * http://www.arstechnica.de/index.html?name=http://www.arstechnica.de/computer/JavaScript/JS11_07.html
 *
 * @author cwitteveen
 */
public class AustrianHoliday {

  private static DateTimeFormatter MMDD = DateTimeFormatter.ofPattern("MMdd");
  // private static DateTimeFormatter YYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

  public static String getHolidayText(int holidayNr) {
    switch (holidayNr) {
      case 0:
        return "Kein Feiertag";
      case 1:
        return "Neujahr";
      case 2:
        return "Heilige 3 Knige";
      case 3:
        return "Ostermontag";
      case 4:
        return "Pfingstmontag";
      case 5:
        return "Staatsfeiertag";
      case 6:
        return "Christi Himmelfahrt";
      case 7:
        return "Fronleichnam";
      case 8:
        return "Maria Himmelfahrt";
      case 9:
        return "Nationalfeiertag";
      case 10:
        return "Allerheiligen";
      case 11:
        return "Maria Empfngnis";
      case 12:
        return "Christtag";
      case 13:
        return "Stephanitag";
      case 14:
        return "Karfreitag";
      case 15:
        return "Ostern";
      case 16:
        return "Fehler";
      case 17:
        return "Weihnachten";
      case 18:
        return "Silvesterausgleich";
      default:
        return "Illegale FeiertagsNummer";

    }
  }

  /**
   * calculates Easter Sunday for the year adapted after: http://www.arstechnica.de/index.html?name
   * =http://www.arstechnica.de/computer/JavaScript/JS11_07.html
   *
   * @return
   */
  public static LocalDate easterSunday(int pYear) {

    if (pYear < 1970 || pYear > 2099) {
      return null;
    }
    int a = pYear % 19;
    int d = (19 * a + 24) % 30;
    int day = d + (2 * (pYear % 4) + 4 * (pYear % 7) + 6 * d + 5) % 7;
    if ((day == 35) || ((day == 34) && (d == 28) && (a > 10))) {
      day -= 7;
    }

    return LocalDate.of(pYear, 3, 22).plusDays(day);
  }


  /**
   * Returns 0, when the day is not a holiday, or a number 1..15 (see getHolidayText()) for a holiday.
   *
   * @param pDate
   * @return
   */
  public static int isHoliday(LocalDate pDate) {


    String hday = pDate.format(MMDD);
    int year = pDate.getYear();

    int holiday = 0;

    // first the static holidays
    if ("0101".equals(hday)) {
      holiday = 1;
    } else if ("0106".equals(hday)) {
      holiday = 2;
    } else if ("0501".equals(hday)) {
      if (year > 1918) {
        holiday = 5;
      } ;
    } else if ("0815".equals(hday)) {
      holiday = 8;
    } else if ("1026".equals(hday)) {
      if (year > 1955) {
        holiday = 9;
      } ;
    } else if ("1101".equals(hday)) {
      holiday = 10;
    } else if ("1208".equals(hday)) {
      holiday = 11;
    } else if ("1224".equals(hday)) {
      holiday = 17;
    } else if ("1225".equals(hday)) {
      holiday = 12;
    } else if ("1226".equals(hday)) {
      holiday = 13;
    } else if ("1231".equals(hday)) {
      holiday = 18;
    } else {
      holiday = 0;
    }

    if (holiday > 0) {
      return holiday;
    }

    // in case the date is outside the possible easter period, return
    int ihday = Integer.parseInt(hday);
    if ((ihday < 320) || (ihday > 625)) {
      return 0;
    }

    LocalDate easter = easterSunday(pDate.getYear());
    // karfreitag
    if (pDate.equals(easter.minusDays(2))) {
      return 14;
    }

    int diff = (int) ChronoUnit.DAYS.between(easter, pDate);

    switch (diff) {
      case 0:
        return 15;
      case 1:
        return 3;
      case 39:
        return 6;
      case 50:
        return 4;
      case 60:
        return 7;
      default:
        return 0;
    }
  }

  public static boolean istFeiertag(LocalDate pLocalDate) {
    return isHoliday(pLocalDate) > 0;
  }

  public static boolean isWorkday(LocalDate pLocalDate) {
    if (isHoliday(pLocalDate) > 0) {
      return false;
    }
    switch (pLocalDate.getDayOfWeek()) {
      case SATURDAY:
        return false;
      case SUNDAY:
        return false;
      default:
        return true;
    }
  }

  public static LocalDate minusWorkdays(LocalDate pDate, int pWorkdays) {
    LocalDate vDate = pDate;
    for (int i = 0; i < pWorkdays; i++) {
      vDate = minusOneWorkday(vDate);
    }
    return vDate;
  }

  public static LocalDate minusOneWorkday(LocalDate pLocalDate) {
    LocalDate vDate = pLocalDate.minusDays(1);
    while (!isWorkday(vDate)) {
      vDate = vDate.minusDays(1);
    }
    return vDate;
  }

  public static void main(String[] args) {
    System.out.println(AustrianHoliday.minusWorkdays(LocalDate.now().withDayOfMonth(15), 2));
    System.out.println(AustrianHoliday.isHoliday(LocalDate.of(2020, 4, 13)));
    System.out.println(AustrianHoliday.easterSunday(2018));
    System.out.println(AustrianHoliday.easterSunday(2019));
    System.out.println(AustrianHoliday.easterSunday(2020));
    System.out.println(AustrianHoliday.getHolidayText(isHoliday(LocalDate.of(2019, 6, 11))));
    System.out.println(AustrianHoliday.istFeiertag(LocalDate.now().plusDays(1)));
    System.out.println(AustrianHoliday.minusWorkdays(LocalDate.of(2019, 8, 12), 2));
    System.out.println(AustrianHoliday.minusWorkdays(LocalDate.of(2019, 8, 14), 2));
  }


}
