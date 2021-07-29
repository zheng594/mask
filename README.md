#描述：
####一个文本内容脱敏的包
#使用：
####1、下载项目后执行：mvn clean package -Dmaven.test.skip=true
####2、将jar上传到本地：mvn install
####3、pom中引入：
``` 
 <dependency>
	 <groupId>com.data</groupId>
     <artifactId>mask</artifactId>
	 <version>1.0-SNAPSHOT</version>
 </dependency>
```
####3、代码示例
```
public class Test {
    public static void main(String[] args) {
        Masking masking = new Masking();

        String content = "15882183842 6221882600114166800 错误的银行卡：zheng_test@163.com 33062119921212004x zhda，浙A30A00 ";
        //Result:158****3842 622188******4166800 错误的银行卡：zheng_***t@163.c** 33062*******12004x zhda，浙A***00
        System.out.println(masking.getMaskValue(content));

        masking.setWithouts(new String[]{"邮箱", "手机号"});//不需要脱敏的数据类型
        //Result：15882183842 421b59d0708e60a450f7eca18928ba0a078641dc 错误的银行卡：zheng_test@163.com 22c5ce09c551817e0f928b0d356733b54573dd65 zhda，ae3f590f46029f513e897b0bfa40e04ad8a604d1
        System.out.println(masking.getMaskValue(1, content));

        //脱敏结果增加前缀,如果选择了hash类型脱敏，可能用户不知道数据本身如此还是脱敏后如此，增加前缀以区分
        masking.setPrefix("mask_");
        //Result：15882183842 mask_421b59d0708e60a450f7eca18928ba0a078641dc 错误的银行卡：zheng_test@163.com mask_22c5ce09c551817e0f928b0d356733b54573dd65 zhda，mask_ae3f590f46029f513e897b0bfa40e04ad8a604d1
        System.out.println(masking.getMaskValue(1, content));

        //自定义脱敏规则，如下则表示，识别长度为6位数的数字，中间3位打*显示
        Masking.ruleList.add(new MaskRule("类型名称", Pattern.compile("(^[0-9]{6}$)"),
                false, null, true, 3, false, null));
        //Result:mask_3***30
        System.out.println(masking.getMaskValue("302030")); //显示结果为：3***30

        //正则匹配可能产生回溯问题，默认无超时时间，可自定义设置，单位毫秒
        masking.setTimeout(1000);
    }
}
```
