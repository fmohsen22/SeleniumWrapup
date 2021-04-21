package com.wraper.framework.tool;

import com.wraper.app.base.SWrapUtil;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


/**
 * For comparing lists in unit tests. Also allows for comparing List and Tables, and whether a table contains the list.
 * 
 * With one restriction, the tables may not contain identical rows. But because this might become a requirement, MultiValuedMaps are used. This
 * restriction is enforced with asserts.
 * 
 * 
 * @author cwitteveen
 *
 */

public class ListComparison {

  private static final Logger logger = LoggerFactory.getLogger(ListComparison.class);

  /****************** List Methoden ********************/

  public static void assertItemsInList(List<String> expected, List<String> actual) {
    boolean isInList = ListComparison.areItemsInList(expected, actual);
    if (!isInList) {
      logger.error("Geforderte Elemente nicht in Liste");
      logger.error("  Elemente: <{}>", ListComparison.printList(expected));
      logger.error("  Liste:    <{}>", ListComparison.printList(actual));
    }
    assertEquals(true, isInList);
  }

  /**
   * Prüft ob die in expected aufgelistete Items alle in der Liste (actual) vorhanden sind
   * 
   * @param expected
   * @param actual
   * @return
   */
  public static boolean areItemsInList(List<String> expected, List<String> actual) {
    HashSet<String> vSet = new HashSet<String>(actual);
    for (String s : expected) {
      if (!vSet.contains(s)) {
        return false;
      }
    }
    return true;
  }

  /** assumes that the list does not contain identical items. **/
  public static boolean areItemsInListInCorrectOrder(List<String> expected, List<String> actual) {
    int index = -1;
    for (String item : actual) {
      index++;
      if (item.equals(expected.get(0))) {
        for (int i = 0; i < expected.size(); i++) {
          if (!expected.get(i).equals(actual.get(index + i))) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  public static void assertNotInList(List<String> unexpected, List<String> actual) {
    for (String vNeg : unexpected) {
      String vNegItem = (vNeg == null ? "" : vNeg);
      for (String vListItem : actual) {
        String vCheckListItem = (vListItem == null ? "" : vListItem);
        assertFalse(vNegItem.equals(vCheckListItem));
      }
    }
  }

  /** for testing whether both lists contains the same elements, but possibly in a different order */
  public static void assertListsIdenticalButOrderIrrelevant(List<String> expected, List<String> actual) {
    if (areListsIdenticalButOrderIrrelevant(expected, actual)) {
      return;
    }
    outputLists(expected, actual);
    // list size must be identical
    if (expected.size() != actual.size()) {
      logger.error("The lists do not have the same number of items ({} and {})", expected.size(), actual.size());
      assertEquals(expected.size(), actual.size());
    }

    // check the items one by one
    List<String> comp = new LinkedList<String>(expected);
    for (String s : actual) {
      for (String exp : comp) {
        if (s.equals(exp)) {
          comp.remove(exp);// removes first instance of exp, so also works when there is more than one
          break;
        }
      }
    }
    if (comp.size() != 0) {
      logger.error("Following items in expected List are not found in the found list:");
      for (String s : comp)
        logger.error("     " + s);
      logger.error("Found List:");
      for (String s : actual) {
        logger.error("     " + s);
      }
      assertEquals(0, comp.size());
    }
    throw new RuntimeException("Code should never come her, check method");
  }

  public static boolean areListsIdenticalButOrderIrrelevant(List<String> expected, List<String> actual) {
    if (SWrapUtil.md5(concatenateList(expected)).equals(SWrapUtil.md5(concatenateList(actual)))) {
      return true;
    }
    // list size must be identical
    if (expected.size() != actual.size()) {
      logger.warn("The lists do not have the same number of items ({} and {})", expected.size(), actual.size());
      return false;
    }

    // check the items one by one
    List<String> comp = new LinkedList<String>(expected);
    for (String s : actual) {
      for (String exp : comp) {
        if (s.equals(exp)) {
          comp.remove(exp);// removes first instance of exp, so also works when there is more than one
          break;
        }
      }
    }
    if (comp.size() != 0) {
      return false;
    }
    return true;
  }

  public static void assertListsIdentical(List<String> expected, List<String> actual) {
    if (areListsIdentical(expected, actual)) {
      return;
    }
    outputLists(expected, actual);
    logger.error("Lists are not identical");
    assertTrue(false);
  }

  public static void assertListsIdentical(List<String> expected, List<String> actual, List<Integer> excluded) {
    if (areListsIdentical(expected, actual, excluded)) {
      return;
    }
    logger.error("Lists are not identical (compare with excluded columns)");
    outputLists(expected, actual);
    printListExcluded(excluded);
    assertFalse(true);
  }

  public static boolean areListsIdentical(List<String> expected, List<String> actual) {
    return areListsIdentical(expected, actual, new LinkedList<Integer>());
  }

  /** comparison, but no junit asserts **/
  public static boolean areListsIdentical(List<String> expected, List<String> actual, List<Integer> excluded) {
    if (SWrapUtil.md5(concatenateList(expected, excluded)).equals(SWrapUtil.md5(concatenateList(actual, excluded)))) {
      return true;
    }
    // otherwise find the (first) difference
    if (expected.size() != actual.size()) {
      logger.trace("Listenlängen sind nicht gleich: {} - {}", expected.size(), actual.size());
      logger.trace("  Erwartet:  <{}>", ListComparison.printList(expected));
      logger.trace("  Vorhanden: <{}>", ListComparison.printList(actual));
      return false;
    }

    int count = -1;
    for (String s : expected) {
      count++;
      if (excluded.contains(Integer.valueOf(count))) {
        continue;
      }
      String sNotNullE = (s == null ? "" : s);
      String sNotNullF = actual.get(count);
      sNotNullF = (sNotNullF == null ? "" : sNotNullF);

      // remove supplementary unicode characters
      if (!SWrapUtil.basicString(sNotNullE).equals(SWrapUtil.basicString(sNotNullF))) {
        logger.trace("Erwarteter Wert [" + sNotNullE + "]");
        logger.trace("            Hex [" + SWrapUtil.hex(sNotNullE) + "]");
        logger.trace("Vorhandener Wert[" + sNotNullF);
        logger.trace("            Hex [" + SWrapUtil.hex(sNotNullF) + "]");
        logger.trace("Listen sind nicht identisch");
        logger.trace("  Erwartet:  <{}>", ListComparison.printList(expected));
        logger.trace("  Vorhanden: <{}>", ListComparison.printList(actual));
        return false;
      }
    }
    return true;
  }

  /************** Ende List Methods *******************/

  /************** Table Methods ************************/

  /**
   * checks whether all entries of pSubtable are also present in table
   * 
   * @param table
   * @param pSubTable
   * @throws NoSuchAlgorithmException
   */
  public static void assertTableContainsTable(List<List<String>> table, List<List<String>> subTable) {

    logTables(subTable, table);

    MultiValuedMap<String, List<String>> vFullMap = convertToMultiMap(table);
    MultiValuedMap<String, List<String>> vSubsetMap = convertToMultiMap(subTable);

    // initial implementation: only check the keys, the hash should guarantee that the content is identical
    // however, a more precise implementation could check, that there are multiple entries, when one
    // key occurs more than once.
    for (String key : vSubsetMap.keySet()) {
      assertEquals(true, vFullMap.containsKey(key));
      if (vSubsetMap.get(key).size() != vFullMap.get(key).size())
        logger.info("Data element {} contained with different mulitplicity in tables", vFullMap.get(key));
      assertEquals(vSubsetMap.get(key).size(), vFullMap.get(key).size());
    }
  }

  public static void assertTableContainsTable(List<List<String>> table, List<List<String>> subTable, List<Integer> ignoreColumns) {
    logTables(subTable, table);

    MultiValuedMap<String, List<String>> vFullMap = convertToMultiMap(table, ignoreColumns);
    MultiValuedMap<String, List<String>> vSubsetMap = convertToMultiMap(subTable, ignoreColumns);

    // initial implementation: only check the keys, the hash should guarantee that the content is identical
    // however, a more precise implementation could check, that there are multiple entries, when one
    // key occurs more than once.
    for (String key : vSubsetMap.keySet()) {
      assertEquals(true, vFullMap.containsKey(key));
      assertEquals(1, vFullMap.get(key).size());
    }
  }

  public static void assertTableContainsList(List<List<String>> table, List<String> list) {
    assertTableContainsList(table, list, new LinkedList<Integer>());
  }

  /**
   * Prüft eine Datenzeile auf Vorhandensein in einer Tabelle
   * 
   * @param table Tabelle, gegen die geprüft werden soll
   * @param list Datenzeile
   * @param ignoreColumns Liste mit 0-basierten Spalten, die nicht geprüft werden sollen
   */
  public static void assertTableContainsList(List<List<String>> table, List<String> list, List<Integer> ignoreColumns) {
    String hash = getHashFromList(list, ignoreColumns);
    MultiValuedMap<String, List<String>> vFullMap = convertToMultiMap(table, ignoreColumns);
    boolean success = vFullMap.containsKey(hash);
    if (!success) {
      logger.error("Table does not contain the List");
      logger.error("Expected = " + concatenateList(list));
      logger.error("Table:     ");
      for (List<String> vL : table) {
        logger.error("           " + concatenateList(vL));
      }
    }
    assertTrue(success);
    // check only one item
    assertEquals(1, vFullMap.get(hash).size());
  }

  public static void assertTableNotContainsList(List<List<String>> table, List<String> list) {
    String hash = getHashFromList(list);
    MultiValuedMap<String, List<String>> vFullMap = convertToMultiMap(table);
    assertEquals(false, vFullMap.containsKey(hash));
  }

  // also the order of the elements must be identical
  public static void assertTablesIdent(List<List<String>> expected, List<List<String>> actual) {
    if (!tablesAreIdentical(expected, actual)) {
      logTables(expected, actual);
    }
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertListsIdentical(expected.get(i), actual.get(i));
    }
  }

  public static void assertTablesIdent(List<List<String>> expected, List<List<String>> actual, List<Integer> excludedColumns) {
    if (!tablesAreIdentical(expected, actual, excludedColumns)) {
      logTables(expected, actual);
    }
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertListsIdentical(expected.get(i), actual.get(i), excludedColumns);
    }
  }

  /** version that returns true or false, no junit tests executed **/
  public static boolean tablesAreIdentical(List<List<String>> expected, List<List<String>> actual) {
    if (expected.size() != actual.size()) {
      return false;
    }
    for (int i = 0; i < expected.size(); i++) {
      if (!areListsIdentical(expected.get(i), actual.get(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean tablesAreIdentical(List<List<String>> expected, List<List<String>> actual, List<Integer> excludedColumns) {
    if (expected.size() != actual.size()) {
      return false;
    }

    for (int i = 0; i < expected.size(); i++) {
      if (!areListsIdentical(expected.get(i), actual.get(i), excludedColumns)) {
        return false;
      }
    }
    return true;
  }

  // if there are identical rows, multiple rows will be under one key
  public static void assertTablesIdentNoOrder(List<List<String>> expected, List<List<String>> actual) {
    logTables(expected, actual);
    MultiValuedMap<String, List<String>> vExpMap = convertToMultiMap(expected);
    MultiValuedMap<String, List<String>> vMap = convertToMultiMap(actual);
    assertEquals(vExpMap.size(), vMap.size());
    for (String key : vExpMap.keySet()) {
      assertTrue(vMap.containsKey(key));
      assertEquals(1, vExpMap.get(key).size()); // the method is not tested for multiple rows under one key
      assertEquals(1, vMap.get(key).size());
    }
  }

  /**
   * This method asserts that the table has a row that has pValue1 in column pColumn1 and pValue2 in column pColumn2
   * 
   * pValue1 and pValue2 may not be null. Wenn no value is available, this must be indicated as an empty String
   */
  public static boolean tableHasRow(List<List<String>> table, int column1, String value1, int column2, String value2) {
    for (int i = 0; i < table.size(); i++) {
      if (value1.equals(table.get(i).get(column1)) && value2.equals(table.get(i).get(column2))) {
        return true;
      }
    }
    return false;
  }


  /************** End Table Methods ************************/


  /************** Helper Methods ***************************/
  private static MultiValuedMap<String, List<String>> convertToMultiMap(List<List<String>> table) {
    return convertToMultiMap(table, new LinkedList<Integer>());
  }

  private static MultiValuedMap<String, List<String>> convertToMultiMap(List<List<String>> table, List<Integer> ignoreColumns) {
    MultiValuedMap<String, List<String>> vMap = new ArrayListValuedHashMap<String, List<String>>();
    for (List<String> vList : table) {
      vMap.put(getHashFromList(vList, ignoreColumns), vList);
    }

    return vMap;
  }

  private static String getHashFromList(List<String> list) {
    return SWrapUtil.md5(concatenateList(list));
  }

  private static String getHashFromList(List<String> list, List<Integer> ignoreColumns) {
    return SWrapUtil.md5(concatenateList(list, ignoreColumns));
  }

  public static String getHashForTable(List<List<String>> table) {
    StringBuilder sb = new StringBuilder();
    for (List<String> row : table) {
      sb.append(concatenateList(row));
    }
    return SWrapUtil.md5(sb.toString());
  }

  private static String concatenateList(List<String> list) {
    StringBuilder concatenated = new StringBuilder();
    for (String s : list) {
      concatenated.append('|').append(s == null ? "" : SWrapUtil.basicString(s));
    }
    return concatenated.toString();
  }

  private static String concatenateList(List<String> list, List<Integer> excludeColumns) {
    StringBuilder concatenated = new StringBuilder();
    int index = 0;
    for (String listItem : list) {
      if (excludeColumns.contains(index)) {
        concatenated.append("|");
      } else {
        concatenated.append('|').append(listItem == null ? "" : SWrapUtil.basicString(listItem));
      }
      index++;
    }
    return concatenated.toString();

  }

  /***************** End helper methods ****************/

  /***************** Logging methods *******************/

  // private static String dump(String dumpTxt) {
  // StringBuilder dumpInfo = new StringBuilder();
  // for (int i = 0; i < dumpTxt.length(); i++)
  // dumpInfo.append(String.format("0x%04x ", (int) dumpTxt.charAt(i)));
  //
  // return "[" + dumpInfo.toString() + "]";
  // }

  public static String printList(List<String> list) {
    StringBuilder sb = new StringBuilder();
    for (String s : list) {
      sb.append(s).append("|");
    }
    return sb.toString();
  }

  public static String printListExcluded(List<Integer> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("excluded: {");
    int index = 0;
    for (Integer i : list) {
      if (index > 0)
        sb.append(",");
      sb.append(i);
    }
    sb.append("}");
    return sb.toString();
  }

  private static void outputLists(List<String> expected, List<String> actual) {
    logger.error("Lists compared:");
    logger.error("  Expected: <{}>", concatenateList(expected));
    logger.error("  Actual  : <{}>", concatenateList(actual));
  }

  private static void logTables(List<List<String>> expected, List<List<String>> actual) {
    logger.info("Tables compared:");
    logger.info("  Expected:");
    for (List<String> row : expected) {
      logger.info("      " + concatenateList(row));
    }
    logger.info("  Actual : ");
    for (List<String> row : actual) {
      logger.info("      " + concatenateList(row));
    }
  }

  /**************** End logging methods *********************/


  /******************** finder methods ****************************/

  /**
   * Variation of findRows. values are provided, not the column in which these are supposed to be
   * 
   * The method requires that exactly one row is found. Wenn more or no rows are found, a RuntimeException is thrown. If this is not the required
   * behaviour, use findRows
   * 
   * @param values
   * @return the index (0-based) of the row that contains the values.
   */
  public static int findRowByValues(List<List<String>> table, List<String> values) {
    int index = findRow(table, values);
    if (index < 0) {
      throw new RuntimeException("Row could not be found with indicated values: " + values.toString());
    }
    return index;
  }

  /** zero based. Returns -1 when not found, but throws an error when more than one row is found **/
  public static int findRow(List<List<String>> table, List<String> values) {
    List<Integer> vFoundRows = findRows(table, values);

    if (vFoundRows.size() == 0) {
      return -1;
    } else if (vFoundRows.size() > 1) {
      throw new RuntimeException("Multiple Rows found using values: " + values);
    }
    return vFoundRows.get(0);
  }

//  public static int findRow(List<List<String>> table, MvblValuedEnum... values) {
//    List<Integer> vFoundRows = findRows(table, values);
//
//    if (vFoundRows.size() == 0) {
//      return -1;
//    } else if (vFoundRows.size() > 1) {
//      throw new RuntimeException("Multiple Rows found using values: " + Arrays.toString(values));
//    }
//    return vFoundRows.get(0);
//  }

  /**
   * Searches for rows containing all the values in the row, and returns a list of the found indexes. 0-based
   * 
   * A special case: the value searched for occurs twice in the row. In the initial version of this method, it was required that the value was added
   * twice, which is cumbersome if you know that the value does not exist in other rows. This was adapted. It is still possible to indicate the same
   * value twice. This allows you to distinct between a row that has the value twice versus a row that has the value once. Now a special situation is
   * intercepted: only the value is present only once in "values", but it still finds a row that has the value at two positions
   * 
   * Summary: the values must be found in a row, for the row to classify. But each value *may* occur more often.
   * 
   **/
  public static List<Integer> findRows(List<List<String>> table, List<String> values) {
    int rowIndex = -1;
    List<Integer> foundRows = new LinkedList<Integer>();
    for (List<String> row : table) {
      rowIndex++;
      List<Integer> foundIndexes = new LinkedList<Integer>();
      List<String> foundValues = new LinkedList<String>();
      for (String v : values) {
        int currentIndex = -1;
        // int found = 0; // how often is value found in one row
        for (String c : row) {
          // prevent finding more than one value at the same position
          if (foundIndexes.contains(++currentIndex)) {
            continue;
          }
          if (c.equals(v)) {
            foundIndexes.add(currentIndex);
            foundValues.add(v);
            continue;
          }
        }
      }

      // result validation
      if (foundIndexes.size() >= values.size()) {
        // double check, to cover the double value situation
        // might very well be, that these checks can be coded more efficiently...
        boolean found = true;
        // first check whether all values were found (and not when values X and Y are indicated, X is not found, and Y twice
        for (String v : values) {
          if (!foundValues.contains(v)) {
            found = false;
          }
        }
        if (!found)
          continue;
        // check when a value occurs twice it is also found twice
        boolean countOk = true;
        for (String v : values) {
          int count = 0;
          for (String s : values) {
            if (s.equals(v))
              count++;
          }
          if (count > 1) {
            int foundCount = 0;
            for (String f : foundValues) {
              if (f.equals(v))
                foundCount++;
            }
            if (foundCount < count)
              countOk = false;
          }
        }
        if (countOk)
          foundRows.add(rowIndex);
      }

    }

    return foundRows;
  }

  /**
   * searches for rows containing all the values in the row, and returns a list of the found indexes. 0-based
   **/
  public static List<Integer> findRows(List<List<String>> table, String... values) {
    return findRows(table, Arrays.asList(values));
  }

//  public static List<Integer> findRows(List<List<String>> table, MvblValuedEnum... values) {
//    return findRows(table, SWrapUtil.listOf((Object[]) values));
//  }

  /** same as findRowByValues but returns -1 when no row is found and -2 when >1 is found **/
  public static int findRowByValuesNoExc(List<List<String>> table, String... pColValues) {
    int rowIndex = -1;
    List<Integer> vFoundRows = new LinkedList<Integer>();
    for (List<String> vRow : table) {
      rowIndex++;
      List<Integer> vFoundIndexes = new LinkedList<Integer>();
      for (String v : pColValues) {
        int currentIndex = -1;
        for (String c : vRow) {
          // prevent finding more than one value at the same position
          if (vFoundIndexes.contains(++currentIndex)) {
            continue;
          }
          if (c.equals(v)) {
            vFoundIndexes.add(currentIndex);
            continue;
          }
        }
      }

      if (vFoundIndexes.size() == pColValues.length) {
        vFoundRows.add(rowIndex);
      }
    }

    if (vFoundRows.size() == 0) {
      return -1;
    } else if (vFoundRows.size() > 1) {
      return -2;
    }

    return vFoundRows.get(0);
  }

  /**
   * Sucht in Tabelle nach 1 oder mehr Werte (definiert in ColumnValue) Wenn mehrere ColumnValue Parameter verwendet werden, müssen sie alle vorhanden
   * sein.
   * 
   * @param table
   * @param pColValues definiert Spalte (zero based) und Wert der darin gefunden werden muss
   * @return
   */
  public static List<Integer> findRows(List<List<String>> table, ColumnValue... values) {
    List<Integer> vResults = new LinkedList<Integer>();

    // leere Tabellen anfangen
    if (table.size() == 0 || (table.size() == 1 && table.get(0).size() == 1))
      return vResults;

    for (int i = 0; i < table.size(); i++) {
      List<String> vRow = table.get(i);
      // pro ColumnValue prüfen ob vorhanden, sonst nächste row
      boolean found = true;  // umgekehrt arbeiten
      for (ColumnValue cv : values) {
        if (cv.getValue() == null) {
          if (vRow.get(cv.getColumn()) != null) {
            found = false;
          }
        } else {
          if (!cv.getValue().equals(vRow.get(cv.getColumn()))) {
            found = false;
          }
        }
      }

      if (found) {
        vResults.add(i);
      }
    }

    return vResults;
  }

  public static List<String> getColumnValues(List<List<String>> table, int columnIndex) {
    assertTrue(columnIndex >= 0);
    return table.stream().map(list -> {
      return list.get(columnIndex);
    }).collect(Collectors.toList());
  }

  /** sum of values in a column specified by 0-based columnIndex */
  public static long sumColumn(List<List<String>> table, int columnIndex) {
    return getColumnValues(table, columnIndex).stream().map(val -> StringUtils.isEmpty(val) ? 0 : Long.parseLong(val))
        .collect(Collectors.summingLong(Long::longValue));
  }

  /******************** end finder methods ****************************/


  public static class ColumnValue {
    private int    column;
    private String value;

    protected ColumnValue(int column, String value) {
      this.column = column;
      this.value = value;
    }

    /**
     * creates a ColumnValue object
     * 
     * @param colValAttributes set of 1 or more (1-based) column number-column value pairs
     * @return
     */
    public static ColumnValue of(Object... colValAttributes) {
      if (!((colValAttributes[0] instanceof Integer) /* || (colValAttributes[0] instanceof MvblDataTableDescription) */)
          && !(colValAttributes[1] instanceof String))
        throw new InvalidArgumentException("Ungültige Parameterwerte für Methode columnValueOf (colValAttributes)");

      return new ColumnValue((Integer) colValAttributes[0], (String) colValAttributes[1]);
    }

    public int getColumn() {
      return column;
    }

    public String getValue() {
      return value;
    }
  }

  /******************* List Converter Methods ****************************/

  public static List<String> removeValuesFromList(List<String> inputList, String... values) {
    HashSet<String> ignored = new HashSet<String>(Arrays.asList(values));
    List<String> newList = new LinkedList<String>();
    for (String s : inputList) {
      if (!ignored.contains(s)) {
        newList.add(s == null ? "" : s);
      }
    }
    return newList;
  }

  /*************** end list converter methods **************************/

  // public static void main(String[] args) {
  // String s = "some text";
  // System.out.println(SWrapUtil.hex(s));
  // String s2 = s + "\u00A0" + s;
  // System.out.println(SWrapUtil.hex(s2));
  //
  // System.out.println(SWrapUtil.codepoint(s));
  // System.out.println(SWrapUtil.codepoint(s2));
  //
  // }

}
