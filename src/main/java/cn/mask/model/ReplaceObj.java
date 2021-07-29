package cn.mask.model;

import java.io.Serializable;

/**
 * Created by zheng on 2021-07-01
 */
public class ReplaceObj implements Serializable {
    private String dataName; //数据类型名称
    private String content;
    private String replace;

    public ReplaceObj() {
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}
