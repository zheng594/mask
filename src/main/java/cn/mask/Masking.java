package cn.mask;

import cn.mask.model.*;
import cn.mask.util.BankCardUtils;
import cn.mask.util.IdentityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zheng on 2021-07-01
 */
public class Masking implements Serializable {
    public static List<MaskRule> ruleList = new ArrayList<>();
    private String[] withouts; //不需要脱敏的类型
    private String prefix; //脱敏后内容前缀,诸如数据hash脱敏后，用户可能不知道数据本来就是这样还是脱敏后如此
    private Integer timeout; //正则匹配产生回溯问题时的超时时间

    public Masking() {
    }

    public Masking(String[] withouts) {
        this.withouts = withouts;
    }

    public void init() {
        ruleList.add(new MaskRule("邮箱", Pattern.compile(DataTypeEnum.getByName("邮箱")),
                true, 4, false, null, true, 2));
        ruleList.add(new MaskRule("座机号", Pattern.compile(DataTypeEnum.getByName("座机号")),
                false, null, true, 4, false, null));
        ruleList.add(new MaskRule("IP", Pattern.compile(DataTypeEnum.getByName("IP")),
                false, null, true, 4, false, null));
        ruleList.add(new MaskRule("IMEI", Pattern.compile(DataTypeEnum.getByName("IMEI")),
                false, null, true, 4, false, null));
        ruleList.add(new MaskRule("MAC地址", Pattern.compile(DataTypeEnum.getByName("MAC地址")),
                false, null, true, 4, false, null));
        ruleList.add(new MaskRule("手机号", Pattern.compile(DataTypeEnum.getByName("手机号")),
                false, null, true, 4, false, null));
        ruleList.add(new MaskRule("身份证", Pattern.compile(DataTypeEnum.getByName("身份证")),
                false, null, true, 7, false, null));
        ruleList.add(new MaskRule("车牌号", Pattern.compile(DataTypeEnum.getByName("车牌号")),
                false, null, true, 3, false, null));
        ruleList.add(new MaskRule("银行卡", Pattern.compile(DataTypeEnum.getByName("银行卡")),
                false, null, true, 6, false, null));
    }

    private CopyOnWriteArraySet<ReplaceObj> getReplaced(int type, String content) {
        CopyOnWriteArraySet<ReplaceObj> contentList = new CopyOnWriteArraySet();

        if (StringUtils.isEmpty(content)) {
            return contentList;
        }

        String[] contentArr = content.split("-|_|#|;|,|；|，| |。|：|'|\"|“|”|’|‘|\\{|\\}|\\[|\\]|\\(|\\)|（|）|<|>|\\||=");
        List<String> withoutList = withouts == null ? new ArrayList<>() : Arrays.asList(withouts);

        for (MaskRule rule : ruleList) {
            if (CollectionUtils.isNotEmpty(withoutList) && withoutList.contains(rule.getDataName())) {
                continue;
            }
            Arrays.stream(contentArr).forEach(item -> {
                Pattern pattern = rule.getPattern();
                if (item.length() < 6 || pattern == null) {
                    return;
                }

                Matcher matcher = null;
                if (this.timeout == null) {
                    matcher = pattern.matcher(item);
                } else {
                    matcher = createMatcherWithTimeout(item, pattern, this.timeout);
                }

                try {
                    while (matcher.find()) {
                        String matched = matcher.group();
                        boolean bol = true;
                        if (DataTypeEnum.IDENTITY_CARD.getName().equals(rule.getDataName())) {
                            if (!IdentityUtils.isValid(matched)) {
                                bol = false;
                            }
                        } else if (DataTypeEnum.CARD_NO.getName().endsWith(rule.getDataName())) {
                            if (!BankCardUtils.isBankCard(matched)) {
                                bol = false;
                            }
                        }

                        if (bol) {
                            ReplaceObj replaceObj = new ReplaceObj();
                            replaceObj.setDataName(rule.getDataName());
                            replaceObj.setContent(matched);
                            replaceObj.setReplace(getReplaced(type, rule, matched));
                            contentList.add(replaceObj);
                        }
                    }
                } catch (Exception e) {
                    throw new MaskException("识别" + content + "失败");
                }
            });

        }

        return contentList;
    }

    private String getContent(int type, String content) {
        CopyOnWriteArraySet<ReplaceObj> set = getReplaced(type, content);
        if (set.size() == 0) {
            return content;
        }

        for (ReplaceObj rule : set) {
            String val = rule.getContent();
            if (DataTypeEnum.CARD_NO.getName().equals(rule.getDataName()) && BankCardUtils.isBankCard(val)) {
                content = content.replaceAll(val, rule.getReplace());

            } else if (DataTypeEnum.IDENTITY_CARD.getName().equals(rule.getDataName()) && IdentityUtils.isValid(val)) {
                content = content.replaceAll(val, rule.getReplace());

            } else {
                content = content.replaceAll(val, rule.getReplace());
            }
        }

        return content;
    }

    /**
     * 获取要替换的值
     *
     * @param rule
     * @param value
     * @return
     */
    private String getReplaced(int type, MaskRule rule, String value) {
        String prefix = this.prefix == null ? "" : this.prefix;

        if (type == 1) { //hash
            return prefix + getHash(value);
        } else { //掩盖
            int valLen = value.length();
            StringBuffer buffer = new StringBuffer(value);

            if (rule.isHeaderMask()) {
                int header = rule.getHeader() > valLen ? valLen : rule.getHeader() - 1;
                buffer = buffer.replace(0, header, getStar(header));
            }
            if (rule.isMiddleMask()) {
                int middle = rule.getMiddle() > valLen ? valLen : rule.getMiddle();
                int start = (valLen - middle) / 2;
                buffer = buffer.replace(start, start + middle, getStar(middle));
            }
            if (rule.isLastMask()) {
                int last = rule.getLast() > valLen ? valLen : rule.getLast();
                buffer = buffer.replace(valLen - last, valLen, getStar(last));
            }
            return prefix + buffer;
        }
    }

    private static String getStar(int num) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < num; i++) {
            str.append("*");
        }
        return str.toString();
    }

    private static String getHash(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            return value;
        }
        md.update(value.getBytes());
        StringBuffer buf = new StringBuffer();
        byte[] bits = md.digest();
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) {
                a += 256;
            }
            if (a < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(a));
        }
        return buf.toString();
    }

    private Matcher createMatcherWithTimeout(String stringToMatch, Pattern pattern, int timeoutMillis) {
        CharSequence charSequence = new TimeoutRegexCharSequence(stringToMatch, timeoutMillis, stringToMatch,
                pattern.pattern());
        return pattern.matcher(charSequence);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setWithouts(String[] withouts) {
        this.withouts = withouts;
    }

    /**
     * 文本内容脱敏
     *
     * @param type    1hash方式加密，0掩盖
     * @param content 需要脱敏的内容
     * @return
     */
    public String getMaskValue(int type, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        this.init();
        return this.getContent(type, content);
    }

    /**
     * 文本内容脱敏
     *
     * @param content 需要脱敏的内容,掩盖方式
     * @return
     */
    public String getMaskValue(String content) {
        return this.getMaskValue(0, content);
    }

}
