package com.wraper.framework.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JsonUtils {

  public static List<List<String>> transform2DArray(String json) /* throws JsonParseException, JsonMappingException, IOException */ {
    List<List<String>> output = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      Object asArray = mapper.readValue(json, String[][].class);
      String[][] arr2d = (String[][]) asArray;
      output = new LinkedList<List<String>>();
      for (String[] sarr : arr2d) {
        output.add(Arrays.asList(sarr));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

  public static List<String> transform1DArray(String json) /* throws JsonParseException, JsonMappingException, IOException */ {
    ObjectMapper mapper = new ObjectMapper();
    try {
      Object asArray = mapper.readValue(json, String[].class);
      String[] arrd = (String[]) asArray;
      return Arrays.asList(arrd);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
