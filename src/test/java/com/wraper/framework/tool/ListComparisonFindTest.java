package com.wraper.framework.tool;

import com.wraper.framework.tool.ListComparison.ColumnValue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListComparisonFindTest {

  Logger             logger            = LoggerFactory.getLogger(getClass());

  char[]             nbspText          = new char[] {0x00a0};


  List<String>       oneList           = Arrays.asList("1", "2", "3", "4", "5", "6");
  List<String>       oneListIdent      = Arrays.asList("1", "2", "3", "4", "5", "6");
  List<String>       oneListLess       = Arrays.asList("1", "2", "3", "5", "6");
  List<String>       oneListMore       = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
  List<String>       oneListBlank      = Arrays.asList("1", "2", "", "4", "5", "6");
  List<String>       oneListBlank2     = Arrays.asList("1", "2", "3 ", "4", "5", "6");
  List<String>       oneListMoreBlank  = Arrays.asList("1", "2", "3", "", "4", "5", "6");
  List<String>       oneListSpaceBlank = Arrays.asList("1", "2", "3", " ", "4", "5", "6");
  List<String>       oneListNbspBlank  = Arrays.asList("1", "2", "3", "\u00A0", "4", "5", "6");
  List<String>       oneListNull       = Arrays.asList("1", "2", "3", null, "4", "5", "6");
  List<String>       twoList           = Arrays.asList("1", "2", "2", "4", "5", "6");
  List<String>       twoList2Diff      = Arrays.asList("1", "2", "2", "3", "5", "6");
  List<String>       differentList     = Arrays.asList("a", "b", "c", "d", "e", "f");

  List<List<String>> oneTable          = Arrays.asList(oneList, oneListLess, oneListMore, oneListBlank);
  List<List<String>> oneTableIdent     = Arrays.asList(oneList, oneListLess, oneListMore, oneListBlank);
  List<List<String>> oneTableNoOrder   = Arrays.asList(oneList, oneListMore, oneListLess, oneListBlank);
  List<List<String>> oneTableNull      = Arrays.asList(oneList, oneListMore, oneListLess, oneListBlank, oneListNull);
  List<List<String>> oneDiff           = Arrays.asList(oneList, differentList);

  @Test
  public void findNull() {
    List<Integer> vResult = ListComparison.findRows(oneTableNull, new ColumnValue(3, null));
    assertEquals(1, vResult.size());
    assertEquals(new Integer(4), vResult.get(0));

    // blank darf nicht gefunden werden
    vResult = ListComparison.findRows(oneTable, new ColumnValue(3, null));
    assertEquals(0, vResult.size());

  }

  @Test
  public void findNotNull() {
    List<Integer> vResult = ListComparison.findRows(oneTableNull, new ColumnValue(1, "2"));
    assertEquals(5, vResult.size());
    assertEquals(new Integer(0), vResult.get(0));
  }

  @Test
  public void findBlank() {
    List<Integer> vResult = ListComparison.findRows(oneTable, new ColumnValue(2, ""));
    assertEquals(1, vResult.size());
    assertEquals(new Integer(3), vResult.get(0));
  }

  @Test
  public void findMultipe() {
    List<Integer> vResult = ListComparison.findRows(oneTable, new ColumnValue(2, "3"), new ColumnValue(0, "1"));
    assertEquals(3, vResult.size());
    assertEquals(new Integer(0), vResult.get(0));

  }



}
