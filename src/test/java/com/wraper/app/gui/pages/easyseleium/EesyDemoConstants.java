package com.wraper.app.gui.pages.easyseleium;

import com.wraper.framework.constants.EnumValueForm;

public class EesyDemoConstants {

    public static enum MainMenu implements EnumValueForm {
        INPUT_FORMS                 ("Input Forms"),
        DATE_PICKER                 ("Date pickers"),
        TABLE                       ("Table"),
        PROGRESS_BARS_SLIDERS       ("Progress Bars & Sliders"),
        ALERTS_MODALS               ("Alerts & Modals"),
        LIST_BOX                    ("List Box"),
        OTHERS                      ("Others");


        private final String value;

        MainMenu( String value) {

            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

}
