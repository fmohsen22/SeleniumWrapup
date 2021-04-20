package com.wraper.framework.dto;

public abstract class E2EDtoBase {

  public enum FinalAction {
    // @formatter:off
    SPEICHERN,
    ABBRECHEN,
    SCHLIESSEN,
//    ZUWEISEN,
    KEINE_AKTION,
    STANDARD_AKTION
    // @formatter:on
  }

  private Enum<?> finalAction;
  private boolean doClearForm;

  /**
   * block object creation from outside
   */
  protected E2EDtoBase() {}

  // @formatter:off
  /**
   * reset all data fields to their "empty" value 
   * these values mean for input operations "clear respective field"
   * 
   * @return reseted object
   */
  // @formatter:on
  @SuppressWarnings("unchecked")
  public <T extends E2EDtoBase> T emptyDto() {
    return (T) initFields(false);
  };

  // @formatter:off
  /**
   * check DTO for emptiness (=no data in any one of the fields)
   * @return true = no data in a field, 
   *         false = data in at least one field 
  
   */
  // @formatter:on
  abstract public boolean isEmpty();

  // @formatter:off
  /**
   * initialize all data fields with their respective "null" value 
   * these values mean for input operations "do not touch respective field"
   * 
   * @return cleared object
   */
  // @formatter:on
  @SuppressWarnings("unchecked")
  public <T extends E2EDtoBase> T clear() {
    return (T) initFields(true);
  };

  // @formatter:off
  /**
   * same as {@link #clearDto}, enhanced with preset final action indicator 
   * 
   * @return cleared object with preset final action {@link #FinalAction.KEINE_AKTION} 
   */
  // @formatter:on
  @SuppressWarnings("unchecked")
  public <T extends E2EDtoBase> T clearNoAction() {
    initFields(true);
    withFinalAction(FinalAction.KEINE_AKTION);
    return (T) this;
  };

  // @formatter:off
  /**
   * internal clearing/emptying routine for enhanced maintainability
   * 
   * @param doClear true = perform clear operation on all fields
   *                false = perform empty operation on all fields
   *                
   * @return initialized object
   */
  // @formatter:on
  @SuppressWarnings("unchecked")
  protected <T extends E2EDtoBase> T initFields(boolean doClear) {
    withClearForm(false);
    withFinalAction(FinalAction.STANDARD_AKTION);
    return (T) this;
  };

  /**
   * return the currently configured final action for the overlay, typically of type {@link #FinalAction}
   * 
   * @return currently set final action indicator
   */
  public Enum<?> getFinalAction() {
    return finalAction;
  }

  @SuppressWarnings("unchecked")
  public final <T extends E2EDtoBase> T withFinalAction(Enum<?> finalAction) {
    this.finalAction = finalAction;
    return (T) this;
  };

  /**
   * activity to be performed when close icon is activated
   */
  protected abstract void clickOnClose();

  /**
   * activity to be performed when "Abbrechen" is activated
   */
  protected abstract void clickOnAbbrechen();

  /**
   * activity to be performed when "Speichern" or any similar button is activated
   */
  protected abstract void clickOnSpeichern();

  /**
   * activity to be performed when the overlay's default button is activated
   */
  protected abstract void performStandardAction();

  /**
   * fill a form with the data elements of the DTO including a eventually configured final action
   */
  @SuppressWarnings("unchecked")
  public <T extends E2EDtoBase> T fillForm() {
    if (getFinalAction() == FinalAction.ABBRECHEN)
      clickOnAbbrechen();
    else if (getFinalAction() == FinalAction.SPEICHERN)
      clickOnSpeichern();
    else if (getFinalAction() == FinalAction.STANDARD_AKTION)
      performStandardAction();
    else if (getFinalAction() == FinalAction.SCHLIESSEN)
      clickOnClose();
    return (T) this;
  }

  /**
   * fill a form with the data elements of the DTO without final action
   */
  public <T extends E2EDtoBase> T fillFormNoFinish() {
    return withFinalAction(FinalAction.KEINE_AKTION).fillForm();
  }

  /**
   * set form clearing indicator
   */
  @SuppressWarnings("unchecked")
  public <T extends E2EDtoBase> T withClearForm(boolean doClearForm) {
    this.doClearForm = doClearForm;
    return (T) this;
  }

  /**
   * get form clearing indicator
   */
  public boolean isClearForm() {
    return doClearForm;
  }

}

