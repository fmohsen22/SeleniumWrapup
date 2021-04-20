package com.wraper.framework.pageobject;

import com.wraper.framework.dto.E2EDtoBase;


/**
 * basic interface for a page object with data input capability via DTO elements
 * 
 * @author schiller
 *
 */
public interface DataInputForm {

  /**
   * create a page object specific DTO object
   * 
   * @return <T extends E2EDtoBase> T page specific DTO based on E2EDtoBase
   */
  public abstract <T extends E2EDtoBase> T getPageDto();



}
