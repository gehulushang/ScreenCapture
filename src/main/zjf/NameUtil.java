



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Date;

public class NameUtil {

    char[] str = {'A','B','C','D','E','F','G','H','I','J','K'};
    public String getName(){

        LocalDateTime ldt= LocalDateTime.now();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("YYYY年MM月dd日  HH时mm分ss秒");

        String tempName = ldt.format(dtf);
        String prefixName = "";

        for(int i = 0;i<tempName.length();i++){
            if(Character.isDigit(tempName.charAt(i))){
                prefixName = prefixName + tempName.charAt(i);
            }
        }

        Date date = new Date();
        String imgName = "";
        String dateName = date.toString();
        for(int i = 12;i<19;i++){
            if(dateName.charAt(i)!=':'){
                imgName = imgName + str[Integer.valueOf(dateName.charAt(i))-48];
            }else{
                imgName = imgName+'N';
            }
        }

        imgName = prefixName +  imgName ;

        return imgName;
    }



}
