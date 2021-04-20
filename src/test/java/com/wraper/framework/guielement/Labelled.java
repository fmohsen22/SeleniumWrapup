package com.wraper.framework.guielement;

/**
 * interface for all GuiElements with a label element (i.e. majority of UI data elements)
 * 
 * @author schiller
 *
 */
public interface Labelled {
  /**
   * return text of label element
   * 
   */
  public String getLabel();

  /**
   * validates the current label text with casing consideration
   * 
   * @param expected expected label text
   */
  public void assertLabel(String expected);

}
