package sjsu.xuy87.cmpe277adproj.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Yu Xu
 */

public class BigDecimalUtil {

    public static String getValue(BigDecimal value){
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        return String.valueOf(df.format(value));
    }
    /*
        For rating bar (actual rating)
     */
    public static float getFloat(BigDecimal value){
        return value.floatValue();
    }


}
