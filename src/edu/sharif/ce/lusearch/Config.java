package edu.sharif.ce.lusearch;

import java.util.HashMap;

/**
 * Created by mohammad on 12/17/16.
 */
public class Config {
    public static final String META_DELIMITER="### ";
    public static final String TEXT_INDICATOR="متن";
    public static final String BOOK="کتاب";
    public static final String TITLE="عنوان";
    public static final String HIERARCHY="سلسله";
    public static final String POET="شاعر";
    public static final HashMap<String,String> CONFIG_MAPPER=new HashMap<String,String>(){{put("شاعر","POET");put("کتاب","BOOK"); put("عنوان", "TITLE"); put("سلسله", "HIERARCHY");}};
}
