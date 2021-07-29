package cn.mask.model;

import java.util.regex.Pattern;

/**
 * Created by zheng on 2021-07-01
 */
public class MaskRule {
    private String dataName; //数据类型名称
    private Integer valid; //是否支持脱敏
    private Pattern pattern; //内容识别正则
    private Integer header; //支持开头掩盖
    private boolean headerMask; //开头掩盖位数
    private Integer middle; //支持中间掩盖
    private boolean middleMask; //中间掩盖位数
    private Integer last; //支持末尾掩盖
    private boolean lastMask; //末尾掩盖位数

    //掩盖脱敏
    public MaskRule(String dataName, Pattern pattern, boolean headerMask, Integer header, boolean middleMask, Integer middle, boolean lastMask, Integer last) {
        this.dataName = dataName;
        this.pattern = pattern;
        this.header = header;
        this.headerMask = headerMask;
        this.middle = middle;
        this.middleMask = middleMask;
        this.last = last;
        this.lastMask = lastMask;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Integer getHeader() {
        return header;
    }

    public void setHeader(Integer header) {
        this.header = header;
    }

    public boolean isHeaderMask() {
        return headerMask;
    }

    public void setHeaderMask(boolean headerMask) {
        this.headerMask = headerMask;
    }

    public Integer getMiddle() {
        return middle;
    }

    public void setMiddle(Integer middle) {
        this.middle = middle;
    }

    public boolean isMiddleMask() {
        return middleMask;
    }

    public void setMiddleMask(boolean middleMask) {
        this.middleMask = middleMask;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public boolean isLastMask() {
        return lastMask;
    }

    public void setLastMask(boolean lastMask) {
        this.lastMask = lastMask;
    }

}
