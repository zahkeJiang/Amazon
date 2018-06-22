package amazon.jy.com.amazon.utils;

import java.util.Random;

public class CodeUtil {

	public static String getRandom(int number){
	    int max = ((Double) Math.pow(10, (double) number)).intValue() -1;
	    int min = ((Double) Math.pow(10, (double) number-1)).intValue() ;
        Random r = new Random();
        int s = r.nextInt(max-min)+min;
	    return s+"";
    }

}
