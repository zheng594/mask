package cn.mask.model;

/**
 * Created by zheng on 2019-10-10.
 */
public enum DataTypeEnum {
    EMAIL("邮箱", "(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)"),
    TEL("座机号", "^0[0-9]{2,3}-[0-9]{8}$|^0[0-9]{2,3}[0-9]{8}$"),
    IP("IP", "(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|\\b(?:(?:(?:[A-F0-9]{1,4}:){6}|(?=(?:[A-F0-9]{0,4}:){0,6}(?:[0-9]{1,3}\\.){3}[0-9]{1,3}(?![:.\\w]))(([0-9A-F]{1,4}:){0,5}|:)((:[0-9A-F]{1,4}){1,5}:|:)|::(?:[A-F0-9]{1,4}:){5})(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[A-F0-9]{1,4}:){7}[A-F0-9]{1,4}|(?=(?:[A-F0-9]{0,4}:){0,7}[A-F0-9]{0,4}(?![:.\\w]))(([0-9A-F]{1,4}:){1,7}|:)((:[0-9A-F]{1,4}){1,7}|:)|(?:[A-F0-9]{1,4}:){7}:|:(:[A-F0-9]{1,4}){7})(?![:.\\w])\\b"),
    IMEI("IMEI", "(^[0-9]{15}$)"),
    MAC("MAC地址", "(([a-fA-F0-9]{2}:)|([a-fA-F0-9]{2}-)){5}[a-fA-F0-9]{2}"),
    MOBILE("手机号", "^(?<!\\d)(?:(?:1[3456789]\\d{9})$|^(?:861[3456789]\\d{9}))(?!\\d)$"),
    IDENTITY_CARD("身份证", "([1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"),
    LICENSE_NUMBER("车牌号", "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})"),
    CARD_NO("银行卡", "(^[0-9]{19}$)|(^[0-9]{16}$)");

    private String name;
    private String regex;

    DataTypeEnum(String name, String regex) {
        this.name = name;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public static String getByName(String name) {
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            if (dataTypeEnum.name.equals(name)) {
                return dataTypeEnum.regex;
            }
        }
        return "";
    }
}
