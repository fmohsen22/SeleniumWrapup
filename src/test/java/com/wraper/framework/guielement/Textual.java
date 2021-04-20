package com.wraper.framework.guielement;



/**
 * interface for all GuiElements with a readable text value (e.g. all input data elements)
 * 
 * @author schiller
 *
 */
public interface Textual {
  /**
   * return the currently selected value of the element
   * 
   */
  public String getText();

  /**
   * return text of label element from value attribute
   * 
   */
  public String getValue();

  /**
   * validates the current textual value of the element with casing consideration
   * 
   * @param expected
   */
  public void assertText(String expected);

  /**
   * validates the current textual value of the element against the value of an enum based type
   * 
   * @param expected
   */
//  public void assertText(MvblValuedEnum expected);

  /**
   * validates the current textual value of the element without casing consideration
   * 
   * @param expected
   */
  public void assertTextCaseInsensitive(String expected);

}
