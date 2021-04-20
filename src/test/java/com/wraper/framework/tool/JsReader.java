package com.wraper.framework.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class JsReader {

  private static JsReader        jsReader = null;
  private static Map<JS, String> jsCache  = new HashMap<JS, String>();
  private static String          RES_DIR  = "/js/";
  private Logger                 logger   = LoggerFactory.getLogger(this.getClass());

  public enum JS {
    //@formatter:off
    TABLE_CONTENTS("tableContents.js"), 
    TABLE_HEADER_NAMES("tableHeader.js"), 
    TBL_HEADER_TT("tableHeaderTT.js"),
    TBL_COMPLETE("tableComplete.js"), 
    HIS_ANG ("hisAng.js"),
    HIS_ANG_GREEN ("hisAngGreen.js"),
    IS_VISIBLE ("isVisible.js");

    private String path;

    private JS(String path) {
      this.path = path;
    }

    public String path() {
      return path;
    }
  }

  private JsReader() {
    //
  }

  public static JsReader of() {
    if (jsReader == null) {
      jsReader = new JsReader();
    }
    return jsReader;
  }


  public String getJs(JS js) {
    if (!jsCache.containsKey(js)) {
      load(js);
    }
    return jsCache.get(js);
  }

  private void load(JS js) {

    StringBuffer all = new StringBuffer();
    try (InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream(RES_DIR + js.path()));
        BufferedReader br = new BufferedReader(isr)) {
      String line;
      while ((line = br.readLine()) != null) {
        all.append(line).append("\n");
      }
      jsCache.put(js, all.toString());
    } catch (IOException e) {
      logger.error("Error loading " + js.path, e);
      throw new RuntimeException(e);
    }
  }
}
