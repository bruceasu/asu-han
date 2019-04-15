
package me.asu.han;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证号码验证
 *
 */

public class IdentificationCodeUtils {
    public static final String ID_CARD_REGEX = "\\d{15,17}([\\dxX]{1})?";
    public static final int IDENTITYCODE_OLD = 15; // 老身份证15位
    public static final int IDENTITYCODE_NEW = 18; // 新身份证18位
    public static int[] Wi = new int[17];
    public static boolean isEmpty(String src) {
        return src == null || "".equals(src.trim());
    }
    /**
     * 判断身份证号码是否正确。
     *
     * @param code
     *            身份证号码。
     * @return 如果身份证号码正确，则返回true，否则返回false。
     */
    public static boolean isIdentityCode(String code) {

        if (isEmpty(code)) {
            return false;
        }

        String birthDay = "";
        code = code.trim();

        // 长度只有15和18两种情况
        if ((code.length() != IDENTITYCODE_OLD)
                && (code.length() != IDENTITYCODE_NEW)) {
            return false;
        }

        // 身份证号码必须为数字(18位的新身份证最后一位可以是x)
        Pattern pt = Pattern.compile(ID_CARD_REGEX);
        Matcher mt = pt.matcher(code);
        if (!mt.find()) {
            return false;
        }


        // 最后一位校验码验证
        if (code.length() == IDENTITYCODE_NEW) {
            String lastNum = getCheckFlag(code.substring(0,
                    IDENTITYCODE_NEW - 1));
            // check last digit
            if (!("" + code.charAt(IDENTITYCODE_NEW - 1)).toUpperCase().equals(
                    lastNum)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取新身份证的最后一位:检验位
     *
     * @param code
     *            18位身份证的前17位
     * @return 新身份证的最后一位
     */
    private static String getCheckFlag(String code) {

        int[] varArray = new int[code.length()];
        String lastNum = "";
        int numSum = 0;
        // 初始化位权值
        setWiBuffer();
        for (int i = 0; i < code.length(); i++) {
            varArray[i] =Integer.parseInt("" + code.charAt(i));
            varArray[i] = varArray[i] * Wi[i];
            numSum = numSum + varArray[i];
        }
        int checkDigit = 12 - numSum % 11;
        switch (checkDigit) {
            case 10:
                lastNum = "X";
                break;
            case 11:
                lastNum = "0";
                break;
            case 12:
                lastNum = "1";
                break;
            default:
                lastNum = String.valueOf(checkDigit);
        }
        return lastNum;
    }

    /**
     * 初始化位权值
     */
    private static void setWiBuffer() {
        for (int i = 0; i < Wi.length; i++) {
            int k = (int) Math.pow(2, (Wi.length - i));
            Wi[i] = k % 11;
        }
    }

     /**
     * 将15位身份证号码升级为18位身份证号码
     *
     * @param code
     *            15位身份证号码
     * @return 18位身份证号码
     */
    public static String update2eighteen(String code) {

        if (isEmpty(code)) {
            return "";
        }

        code =code.trim();

        if (code.length() != IDENTITYCODE_OLD || !isIdentityCode(code)) {
            return "";
        }

        code = code.substring(0, 6) + "19" + code.substring(6);
        //
        code = code + getCheckFlag(code);

        return code;
    }

    public static String degrade(String code) {
        if (isEmpty(code)) {
            return "";
        }

        code =code.trim();

        if (code.length() != IDENTITYCODE_NEW || !isIdentityCode(code)) {
            return "";
        }

        return code.substring(0, 6) +  code.substring(8, 17);

    }

    public static String replaceIDCardWithStar(String idCard) {
        String result = "";
        if (isEmpty(idCard)) {
            return result;
        }
        if (isIdentityCode(idCard)) {
            result = idCard.substring(0, 10) + "****" + idCard.substring(14, 18);
        }
        else {
            return result;
        }
        return result;
    }

    public static void main(String[] args){
        String[] codeArray = new String[]{"330521197411044030","53010119810602007x","53010119810602001x"};
        for(int i= 0;i<codeArray.length;i++){
            if(isIdentityCode(codeArray[i])){
                System.out.println(codeArray[i]+"：为正确的身份证号码！");
            }else{
                System.out.println(codeArray[i]+"：为错误的身份证号码！");
            }
        }

        System.out.println("转换后的身份证号码为："+update2eighteen("330521820721052"));

        System.out.println("转换后的身份证号码为："+degrade("330521197411044030"));


    }
}

