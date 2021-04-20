package com.wraper.app.base;

import com.wraper.framework.logger.MyLogger;
import org.apache.commons.lang.NullArgumentException;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Sammlung div. statischer Hilfsroutinen
 *
 */
public class MvblUtil {


  private static final Pattern PATT_DATUM    = Pattern.compile(".*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d).*");
  private static final Pattern PATT_DATUM_DB = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");

  private static enum ListType {
    STANDARD, ID, VALUE
  };

  // Datumsformate
  public static final String            DATE_FORMAT_DEFAULT             = "dd.MM.yyyy";
  public static final String            DATE_FORMAT_DEFAULT_WITH_TIME   = "dd.MM.yyyy kk:mm:ss";
  public static final String            DATE_FORMAT_DEFAULT_WITH_MILLIS = "dd.MM.yyyy kk:mm:ss,SSS";
  public static final String            DATE_FORMAT_NO_SEP              = "ddMMyyyy";
  public static final String            DATE_FORMAT_YEAR_MONTH          = "yyyyMM";
  public static final String            DATE_FORMAT_MONTH_PNKT_YEAR     = "MM.yyyy";
  public static final String            DATE_FORMAT_DAY_NUMBER          = "dd.MM.uuuu";
  public static final String            DATE_FORMAT_DB                  = "yyyy-MM-dd";
  public static final String            DATE_FORMAT_DB_DAY_FIRST        = "dd-MM-yyyy";
  public static final String            DATE_FORMAT_MONTH_TEXT          = "MMMM";
  public static final String            DATE_FORMAT_YEAR_MONTH_TEXT     = "yyyy-MM";
  public static final String            DECIMAL_FORMAT_GERMAN           = "#,##0.00";


  // Formatierungselemente für Datumswerte
  // @formatter:off
  public static final DateTimeFormatter DATE_FORMATTER_WITH_MILLIS     = DateTimeFormatter.ofPattern(DATE_FORMAT_DEFAULT_WITH_MILLIS);
  public static final DateTimeFormatter DATE_FORMATTER_WITH_TIME       = DateTimeFormatter.ofPattern(DATE_FORMAT_DEFAULT_WITH_TIME);
  public static final DateTimeFormatter DATE_FORMATTER_DEFAULT         = DateTimeFormatter.ofPattern(DATE_FORMAT_DEFAULT);
  public static final DateTimeFormatter DATE_FORMATTER_NO_SEP          = DateTimeFormatter.ofPattern(DATE_FORMAT_NO_SEP);
  public static final DateTimeFormatter DATE_FORMATTER_YEAR_MONTH      = DateTimeFormatter.ofPattern(DATE_FORMAT_YEAR_MONTH);
  public static final DateTimeFormatter DATE_FORMATTER_MONTH_PNKT_YEAR = DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_PNKT_YEAR);
  public static final DateTimeFormatter DATE_FORMATTER_DAY_NO          = DateTimeFormatter.ofPattern(DATE_FORMAT_DAY_NUMBER);
  public static final DateTimeFormatter DATE_FORMATTER_DB              = DateTimeFormatter.ofPattern(DATE_FORMAT_DB);
  public static final DateTimeFormatter DATE_FORMATTER_DB_DAY_FIRST    = DateTimeFormatter.ofPattern(DATE_FORMAT_DB_DAY_FIRST);
  public static final DateTimeFormatter DATE_FORMATTER_MONTH_TEXT      = DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_TEXT).withLocale(Locale.GERMAN);
  public static final DateTimeFormatter DATE_FORMATTER_YEAR_MONTH_TEXT = DateTimeFormatter.ofPattern(DATE_FORMAT_YEAR_MONTH_TEXT);
  public static final DecimalFormat     DECIMAL_FORMATTER_GERMAN       = new DecimalFormat(DECIMAL_FORMAT_GERMAN, new DecimalFormatSymbols(Locale.GERMAN));
  // @formatter:on

  /**
   * Wartet threadsafe x Millisekunden
   * 
   * @param millis Wartezeit in Millisekunden
   */
  public static void wait(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
    }
  }

  /**
   * Formatiert eine Zahl als normierte BKNR
   * 
   * @param bknr zu formatierende Zahl
   * @return Zahl als normierte BKNR
   */
//  public static String formatBknr(long bknr) {
//    return formatBknr(String.valueOf(bknr));
//  }

  /**
   * Formatiert Text als 10-stellig normierte BKNR mit führenden Nullen
   * 
   * @param bknr zu formatierende Rumpf-BKNR
   * @return 10-stellige Norm-BKNR
   */
//  public static String formatBknr(String bknr) {
//    if (bknr == null)
//      throw new NullArgumentException("bknr");
//    else if (bknr.length() > 10)
//      throw new ParameterException("bknr length greater 10");
//
//    while (bknr.length() < 10) {
//      bknr = "0" + bknr;
//    }
//    return bknr;
//  }

  /**
   * Formatiert Fließkommawert als Festkomma-Textwert mit 2 Nachkommastellen und Tausendertrennzeichen
   * 
   * @param betrag zu formatierender Wert
   * @return formatierter Wert
   */
  public static String formatBetrag(double betrag) {
    return DECIMAL_FORMATTER_GERMAN.format(betrag);
  }

  /**
   * Entfernt führende Nullen z.B. aus BKNR
   * 
   * @param bknr (ev. normierte) BKNR mit führenden Nullen
   * @return Wert ohne führende Nullen
   */
  public static String removeLeadingZeros(String bknr) {
    if (bknr == null) {
      throw new NullArgumentException("bknr");
    }
    while (bknr.startsWith("0")) {
      bknr = bknr.substring(1);
    }
    return bknr;
  }

  /**
   * Formatiert VSNR in der Form <SozVersNr><Leerzeichen><Geburtsdatum> eventuell enthaltene Leerzeichen an anderen Stellen werden entfernt Anmerkung:
   * führt keinerlei Prüfungen der VSNR durch
   * 
   * @param vsnr VSNR mit ev. Leerzeichen
   * @return korrekt strukturierte VSNR
   */
  public static String formatVsnr(String vsnr) {
    vsnr = plainVsnr(vsnr);
    return vsnr.substring(0, 4) + " " + vsnr.substring(4);
  }

  public static String plainVsnr(String vsnr) {
    return vsnr.replaceAll(" ", "");
  }

  // /**
  // * Wandelt Array oder Liste von Objekten in Liste von Strings
  // *
  // * @param objectList Liste oder Array von Objekten
  // * @return Liste mit Objektwerten als Strings
  // */
  // public static List<String> convertToStringList(Object[] objectList) {
  // List<String> vStList = new LinkedList<String>();
  // for (Object o : objectList) {
  // if (o == null) {
  // vStList.add("");
  // } else if (o instanceof java.sql.Date) {
  // LocalDate ld = ((java.sql.Date) o).toLocalDate();
  // vStList.add(ld.format(DATE_FORMATTER_DB));
  // } else {
  // vStList.add(o.toString());
  // }
  // }
  // return vStList;
  // }
  //
  // /**
  // * Wandelt 2-dimensionale Tabelle von Objekten in Tabelle von Strings
  // *
  // * @param objectList Tabelle von Objekten
  // * @return 2-dimensionale Tabelle mit Objektwerten als Strings
  // */
  // public static List<List<String>> convertToStringTable(List<Object[]> objectTable) {
  // List<List<String>> vTable = new LinkedList<List<String>>();
  // if (objectTable != null) {
  // for (Object[] objArray : objectTable) {
  // vTable.add(convertToStringList(objArray));
  // }
  // }
  // return vTable;
  // }

  /**
   * When retrieving data from the database, the date format ist yyyy-mm-dd. This method converts all dates to the default mvbl format dd.mm.yyyy
   *
   * @param table
   * @return
   */
//  public static List<List<String>> convertDbDatesToMvblDates(List<List<String>> table) {
//    for (int i = 0; i < table.size(); i++) {
//      List<String> row = table.get(i);
//      for (int j = 0; j < row.size(); j++) {
//        String value = row.get(j);
//        if (value != null && PATT_DATUM_DB.matcher(value).matches()) {
//          LocalDate date = LocalDate.parse(value, DATE_FORMATTER_DB);
//          if (date.equals(UiConstants.NO_DATE_VALUE))
//            table.get(i).set(j, UiConstants.NO_DATE);
//          else
//            table.get(i).set(j, date.format(DATE_FORMATTER_DEFAULT));
//        }
//      }
//    }
//    return table;
//  }

  public static String toBytes(String value) {
    byte[] bytes = value.getBytes();
    StringBuffer sb = new StringBuffer();
    for (byte b : bytes) {
      sb.append(b).append("|");
    }
    return sb.toString();
  }

  // to filter out irrelevant supplementary unicode characters
  public static String basicString(String s) {
    StringBuilder sb = new StringBuilder();
    int charType;
    for (char ch : s.toCharArray()) {
      // special care for nbsp
      if (/* ch == 0xfffd || */ ch == 0x00a0) {
        ch = ' ';
      }
      charType = Character.getType(ch);
      if (Character.isLetterOrDigit(ch)) {
        sb.append(ch);
      } else if (Character.isSpaceChar(ch) || Character.isWhitespace(ch)) {
        sb.append(' ');
      } else if (charType == Character.CURRENCY_SYMBOL) {
        sb.append(ch);
      } else if (charType >= Character.DASH_PUNCTUATION && charType <= Character.OTHER_PUNCTUATION) {
        sb.append(ch);
      } else if (charType == Character.FORMAT) {
        ;
      } else {
        if (Character.MATH_SYMBOL != charType) // to prevent a huge amount of logged data
          MyLogger.WarnLog(MvblUtil.class,String.format("'%s'/'%d/'0x%04x' (Typ %d) potenziell negativ bei Vergleichen", ch, (int) ch, (int) ch, charType));
        sb.append(ch);
      }
    }

    return sb.toString();
  }

  // error is converted to a runtimeException, otherwise the NoAlgorithmException must be handled
  public static String md5(String pInput) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(pInput.getBytes());
      byte[] digest = md.digest();
      return DatatypeConverter.printHexBinary(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String hex(String text) {
    if (text == null || text.length() == 0) {
      return "";
    }
    return text.chars().mapToObj(i -> Integer.toHexString(i)).collect(Collectors.joining(" "));
  }

  public static String codepoint(String text) {
    if (text == null || text.length() == 0) {
      return "";
    }
    return text.codePoints().mapToObj(i -> Integer.toHexString(i)).collect(Collectors.joining(" "));
  }

  /**
   * Extracts a Date from a text. Date should bin in format dd.mm.yyyy The first date found is returned.
   * 
   * @param text
   * @return
   */
  public static LocalDate extractDate(String text) {
    if (text == null)
      return null;
    Matcher matcher = PATT_DATUM.matcher(text);
    if (!matcher.find()) {
      return null;
    }
    return LocalDate.parse(matcher.group(1), DATE_FORMATTER_DEFAULT);
  }


  /**
   * create a string list from all passed parameters in their given order
   * 
   * @param elements of type String, Integer, Long, MvblValuedEnum, MvblMultiValuedEnum
   * @return ArrayList containing the strings in the same order as passed in <code>elements</code>
   */
  public static List<String> listOf(Object... elements) {
    return listOf(ListType.STANDARD, elements);
  }

  /**
   * create a string list from the id part of passed parameters
   * 
   * @param elements of any type, id relevant for MvblMultiValuedEnum
   * @return ArrayList containing the strings in the same order as passed in <code>elements</code>
   */
  public static List<String> idList(Object... elements) {
    return listOf(ListType.ID, elements);
  }

  /**
   * create a string list from the textual or value-part of passed parameters
   * 
   * @param elements of any type, value relevant for MvblMultiValuedEnum
   * @return ArrayList containing string representation from parameters in <code>elements</code>
   */
  public static List<String> valueList(Object... elements) {
    return listOf(ListType.VALUE, elements);
  }

  /**
   * create a string list from the textual, id-part or value-part of passed parameters handles LocalDate, YearMonth and double
   * 
   * @param elements of any type, value relevant for MvblMultiValuedEnum
   * @return ArrayList containing string representation from parameters in <code>elements</code>
   */
//  private static List<String> listOf(ListType type, Object... elements) {
//    ArrayList<String> liste = new ArrayList<String>();
//    for (Object element : elements) {
//      if (element == null)
//        liste.add("");
//      else if (element instanceof LocalDate)
//        liste.add(((LocalDate) element).format(MvblUtil.DATE_FORMATTER_DEFAULT));
//      else if (element instanceof YearMonth)
//        liste.add(((YearMonth) element).format(MvblUtil.DATE_FORMATTER_YEAR_MONTH));
//      else if (element instanceof MvblMultiValuedEnum && type == ListType.ID)
//        liste.add(((MvblMultiValuedEnum) element).id());
//      else if (element instanceof MvblValuedEnum) {
//        if (type == ListType.STANDARD)
//          liste.add(((MvblValuedEnum) element).toString());
//        else // default
//          liste.add(((MvblValuedEnum) element).value());
//      } else if (element instanceof Double) {
//        liste.add(DECIMAL_FORMATTER_GERMAN.format(element));
//        // String txt = "";
//        // txt = String.valueOf(DECIMAL_FORMATTER.format(element)).replace(".", ",");
//        // if (txt.length() > 6) {
//        // txt = txt.substring(0, txt.length() - 6) + "." + txt.substring(txt.length() - 6);
//        // }
//        // liste.add(txt);
//      } else
//        liste.add(element.toString());
//    }
//    return liste;
//  }

//  public static String join(MvblValuedEnum... args) {
//    return join(false, args);
//  }
//
//  public static String joinUpper(MvblValuedEnum... args) {
//    return join(true, args);
//  }

  public static String join(String... args) {
    StringBuilder sb = new StringBuilder();
    for (String e : args) {
      sb.append("[");
      sb.append(e.toString());
      sb.append("]");
    }
    return sb.toString();
  }
//
//  protected static String join(boolean withUpperCase, MvblValuedEnum... args) {
//    StringBuilder sb = new StringBuilder();
//    for (MvblValuedEnum e : args) {
//      sb.append("[");
//      if (withUpperCase)
//        sb.append(e.toString().toUpperCase());
//      else
//        sb.append(e.toString());
//      sb.append("]");
//    }
//    return sb.toString();
//  }

  /**
   * Datum und Kontoauszugsnummer zu Standarddarstellung formatieren
   * 
   * @param date
   * @param kaNummer
   * @return zusammengefügter Wert <date[yyyy-MM-dd]>-<kaNummer>
   */
  public static String formatKa(LocalDate date, String kaNummer) {
    return String.format("%s-%s", date.format(DATE_FORMATTER_DB), kaNummer);
  }

  /**
   * Datum und Kontoauszugsnummer zu erweiterter Standarddarstellung formatieren
   * 
   * @param date
   * @param kaNummer
   * @return zusammengefügter Wert <KA: ><date[yyyy-MM-dd]>-<kaNummer>
   */
  public static String formatKaFull(LocalDate date, String kaNummer) {
    return "KA: " + formatKa(date, kaNummer);
  }

  /** it is a complete method to find any difference in both Strings or just one of them */
  public static Pair<String> diff(String a, String b) {
    return diffHelper(a, b, new HashMap<>());
  }

  public static String diffFirst(String a, String b) {
    return diffHelper(a, b, new HashMap<>()).first;
  }

  public static String diffSecond(String a, String b) {
    return diffHelper(a, b, new HashMap<>()).second;
  }


  private static Pair<String> diffHelper(String a, String b, Map<Long, Pair<String>> lookup) {
    long key = ((long) a.length()) << 32 | b.length();
    if (!lookup.containsKey(key)) {
      Pair<String> value;
      if (a.isEmpty() || b.isEmpty()) {
        value = new Pair<>(a, b);
      } else if (a.charAt(0) == b.charAt(0)) {
        value = diffHelper(a.substring(1), b.substring(1), lookup);
      } else {
        Pair<String> aa = diffHelper(a.substring(1), b, lookup);
        Pair<String> bb = diffHelper(a, b.substring(1), lookup);
        if (aa.first.length() + aa.second.length() < bb.first.length() + bb.second.length()) {
          value = new Pair<>(a.charAt(0) + aa.first, aa.second);
        } else {
          value = new Pair<>(bb.first, b.charAt(0) + bb.second);
        }
      }
      lookup.put(key, value);
    }
    return lookup.get(key);
  }

  public static class Pair<T> {
    public Pair(T first, T second) {
      this.first = first;
      this.second = second;
    }

    public final T first, second;

    @Override
    public String toString() {
      return "(" + first + "," + second + ")";
    }
  }

  public static String getStringFromClipboard() throws Exception {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String clipboardData = (String) clipboard.getData(DataFlavor.stringFlavor);
    return clipboardData;
  }


  public static Date convertToDateViaInstant(LocalDate dateToConvert) {
    return Date.from(dateToConvert.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant());
  }

  public static boolean isValidDateFormat(String date) {

    try {
      DateFormat df = new SimpleDateFormat(MvblUtil.DATE_FORMAT_DEFAULT);
      df.setLenient(false);
      df.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

}

