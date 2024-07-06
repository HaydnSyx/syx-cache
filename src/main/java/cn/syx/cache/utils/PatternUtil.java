package cn.syx.cache.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PatternUtil {

    /**
     * 使用自定义正则表达式匹配并过滤集合
     *
     * @param strings     字符串集合
     * @param customPattern 自定义正则表达式
     * @return 过滤后的集合
     */
    public static String[] filterByPattern(String[] strings, String customPattern) {
        String javaRegex = toJavaRegex(customPattern);
        Pattern pattern = Pattern.compile(javaRegex);

        return Arrays.stream(strings)
                .filter(pattern.asPredicate())
                .toArray(String[]::new);
    }

    /**
     * 将自定义正则表达式转换为标准的 Java 正则表达式
     *
     * @param customPattern 自定义正则表达式
     * @return 标准 Java 正则表达式
     */
    private static String toJavaRegex(String customPattern) {
        StringBuilder javaRegex = new StringBuilder();
        char[] chars = customPattern.toCharArray();

        for (char c : chars) {
            switch (c) {
                case '*':
                    javaRegex.append(".*");
                    break;
                case '?':
                    javaRegex.append('.');
                    break;
                case '[':
                case ']':
                    javaRegex.append(c);
                    break;
                default:
                    if ("\\.[]{}()^$|+".indexOf(c) >= 0) {
                        javaRegex.append('\\');
                    }
                    javaRegex.append(c);
                    break;
            }
        }
        return javaRegex.toString();
    }
}
