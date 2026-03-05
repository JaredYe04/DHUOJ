/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author tange
 */
public class EscapeXml {
            // 手动转义特殊字符为 XML 实体
    public static String escapeXml(String input) {
        if(input.length()>1000)
            input=input.substring(0,1000);
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;

                default:
                    builder.append(c);
                    break;
            }
        }
        return builder.toString();
    }
}
