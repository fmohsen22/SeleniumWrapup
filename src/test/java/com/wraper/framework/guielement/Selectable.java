package com.wraper.framework.guielement;



/**
 * interface for all GuiElements with a select from a list functionality
 * 
 * @author schiller
 *
 */
public interface Selectable {
  /**
   * select an enum value from a list, where the concrete value is made available through toString()
   * 
   * @param pEnum enum based value to select
   */
  public void select(Enum value);

  /**
   * select an exact value from a list
   * 
   * @param pText
   */
  public void selectSlow(String pText);
}
