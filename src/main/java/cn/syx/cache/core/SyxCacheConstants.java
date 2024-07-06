package cn.syx.cache.core;

import io.netty.util.AttributeKey;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SyxCacheConstants {

    public static final AttributeKey<Integer> DB_KEY = AttributeKey.valueOf("connectDb");

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final Charset ASCII = StandardCharsets.US_ASCII;
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static final Long ZERO = 0L;
    public static final Long NEGATIVE_ONE = -1L;

    public static final String ZERO_STRING = "0";
    public static final String NEGATIVE_ONE_STRING = "-1";

    public static final String EMPTY_STRING = "";
    public static final String DOLLAR_STRING = "$";
    public static final String ASTERISK_STRING = "*";
    public static final String PLUS_STRING = "+";
    public static final String MINUS_STRING = "-";
    public static final String COLON_STRING = ":";
    public static final String CRLF_STRING = "\r\n";

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';
    public static final byte PLUS_BYTE = '+';
    public static final byte MINUS_BYTE = '-';
    public static final byte COLON_BYTE = ':';

    public static final byte CR = (byte) '\r';
    public static final byte LF = (byte) '\n';
    public static final byte[] CRLF = "\r\n".getBytes(ASCII);

    public enum ReplyType {

        SIMPLE_STRING,

        ERROR,

        NUMBER,

        BULK_STRING,

        ARRAY
    }
}
