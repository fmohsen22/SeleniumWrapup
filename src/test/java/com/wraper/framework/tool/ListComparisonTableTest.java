package com.wraper.framework.tool;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ListComparisonTableTest {
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
  public void testInTable() {
    ListComparison.assertTableContainsList(oneTable, oneList);
    ListComparison.assertTableContainsList(oneTable, oneListLess);
    ListComparison.assertTableContainsList(oneTable, oneListMore);
    ListComparison.assertTableContainsList(oneTable, oneListBlank);
  }

  @Test
  public void testNotInTable() {
    ListComparison.assertTableContainsList(oneTable, oneListBlank2);
  }

  @Test
  public void testTableIdent() {
    ListComparison.assertTablesIdent(oneTable, oneTableIdent);
  }

  @Test
  public void testTableNotIdent() {
    ListComparison.assertTablesIdent(oneTable, oneTableNoOrder);
  }

  @Test
  public void testTableNoOrder() {
    ListComparison.assertTablesIdentNoOrder(oneTable, oneTableNoOrder);
    ListComparison.assertTablesIdentNoOrder(oneTable, oneTableIdent);
  }

  @Test
  public void testIgnoreListIn() {
    ListComparison.assertTableContainsList(oneDiff, twoList, Arrays.asList(2));
  }

  @Test
  public void testIgnoreListInWithError() {
    ListComparison.assertTableContainsList(oneDiff, twoList, Arrays.asList(3));
  }

  @Test
  public void testIgnoreListIn2() {
    ListComparison.assertTableContainsList(oneDiff, twoList2Diff, Arrays.asList(2, 3));
  }

  @Test
  public void testIgnoreListIn2WithError() {
    ListComparison.assertTableContainsList(oneDiff, twoList2Diff, Arrays.asList(3));
  }
}
