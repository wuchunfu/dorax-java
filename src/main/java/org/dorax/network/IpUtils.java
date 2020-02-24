package org.dorax.network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip 获取工具类
 *
 * @author wuchunfu
 * @date 2019-12-07
 */
public class IpUtils {
    /**
     * 是否是本地地址
     *
     * @param ip
     * @return
     */
    public static boolean isLocalAddr(String ip) {
        return inString(ip, "127.0.0.1", "0:0:0:0:0:0:0:1");
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 去除左右空格（包含中文空格）
     *
     * @param str
     */
    public static String trim(final String str) {
        return str == null ? null : str.replaceAll("^[\\s|　| ]*|[\\s|　| ]*$", "");
    }

    /**
     * 判断IP地址为内网IP还是公网IP
     * <p>
     * tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下：
     * 10.0.0.0/8：10.0.0.0～10.255.255.255
     * 172.16.0.0/12：172.16.0.0～172.31.255.255
     * 192.168.0.0/16：192.168.0.0～192.168.255.255
     *
     * @param ip
     * @return
     */
    public static boolean isInternalAddr(String ip) {
        if (isLocalAddr(ip)) {
            return true;
        }
        byte[] addr = textToNumericFormatV4(ip);
        byte b0 = 0;
        if (addr != null) {
            b0 = addr[0];
        }
        byte b1 = 0;
        if (addr != null) {
            b1 = addr[1];
        }
        //10.x.x.x/8
        final byte section1 = 0x0A;
        //172.16.x.x/12
        final byte section2 = (byte) 0xAC;
        final byte section3 = (byte) 0x10;
        final byte section4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte section5 = (byte) 0xC0;
        final byte section6 = (byte) 0xA8;
        switch (b0) {
            case section1:
                return true;
            case section2:
                if (b1 >= section3 && b1 <= section4) {
                    return true;
                }
            case section5:
                return b1 == section6;
            default:
                return false;
        }
    }

    public static byte[] textToNumericFormatV4(String paramString) {
        if (paramString.length() == 0) {
            return null;
        }
        byte[] arrayOfByte = new byte[4];
        String[] arrayOfString = paramString.split("\\.", -1);
        try {
            long l;
            int i;
            switch (arrayOfString.length) {
                case 1:
                    l = Long.parseLong(arrayOfString[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    arrayOfByte[0] = ((byte) (int) (l >> 24 & 0xFF));
                    arrayOfByte[1] = ((byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF));
                    arrayOfByte[2] = ((byte) (int) ((l & 0xFFFF) >> 8 & 0xFF));
                    arrayOfByte[3] = ((byte) (int) (l & 0xFF));
                    break;
                case 2:
                    l = Integer.parseInt(arrayOfString[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    arrayOfByte[0] = ((byte) (int) (l & 0xFF));
                    l = Integer.parseInt(arrayOfString[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    arrayOfByte[1] = ((byte) (int) (l >> 16 & 0xFF));
                    arrayOfByte[2] = ((byte) (int) ((l & 0xFFFF) >> 8 & 0xFF));
                    arrayOfByte[3] = ((byte) (int) (l & 0xFF));
                    break;
                case 3:
                    for (i = 0; i < 2; i++) {
                        l = Integer.parseInt(arrayOfString[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        arrayOfByte[i] = ((byte) (int) (l & 0xFF));
                    }
                    l = Integer.parseInt(arrayOfString[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    arrayOfByte[2] = ((byte) (int) (l >> 8 & 0xFF));
                    arrayOfByte[3] = ((byte) (int) (l & 0xFF));
                    break;
                case 4:
                    for (i = 0; i < 4; i++) {
                        l = Integer.parseInt(arrayOfString[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        arrayOfByte[i] = ((byte) (int) (l & 0xFF));
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException localNumberFormatException) {
            return null;
        }
        return arrayOfByte;
    }

    public static byte[] textToNumericFormatV6(String paramString) {
        if (paramString.length() < 2) {
            return null;
        }
        char[] arrayOfChar = paramString.toCharArray();
        byte[] arrayOfByte1 = new byte[16];

        int m = arrayOfChar.length;
        int n = paramString.indexOf("%");
        if (n == m - 1) {
            return null;
        }
        if (n != -1) {
            m = n;
        }
        int i = -1;
        int i1 = 0;
        int i2 = 0;
        if ((arrayOfChar[i1] == ':') && (arrayOfChar[(++i1)] != ':')) {
            return null;
        }
        int i3 = i1;
        int j = 0;
        int k = 0;
        int i4;
        while (i1 < m) {
            char c = arrayOfChar[(i1++)];
            i4 = Character.digit(c, 16);
            if (i4 != -1) {
                k <<= 4;
                k |= i4;
                if (k > 65535) {
                    return null;
                }
                j = 1;
            } else if (c == ':') {
                i3 = i1;
                if (j == 0) {
                    if (i != -1) {
                        return null;
                    }
                    i = i2;
                } else {
                    if (i1 == m) {
                        return null;
                    }
                    if (i2 + 2 > 16) {
                        return null;
                    }
                    arrayOfByte1[(i2++)] = ((byte) (k >> 8 & 0xFF));
                    arrayOfByte1[(i2++)] = ((byte) (k & 0xFF));
                    j = 0;
                    k = 0;
                }
            } else if ((c == '.') && (i2 + 4 <= 16)) {
                String str = paramString.substring(i3, m);

                int i5 = 0;
                int i6 = 0;
                while ((i6 = str.indexOf('.', i6)) != -1) {
                    i5++;
                    i6++;
                }
                if (i5 != 3) {
                    return null;
                }
                byte[] arrayOfByte3 = textToNumericFormatV4(str);
                if (arrayOfByte3 == null) {
                    return null;
                }
                for (int i7 = 0; i7 < 4; i7++) {
                    arrayOfByte1[(i2++)] = arrayOfByte3[i7];
                }
                j = 0;
            } else {
                return null;
            }
        }
        if (j != 0) {
            if (i2 + 2 > 16) {
                return null;
            }
            arrayOfByte1[(i2++)] = ((byte) (k >> 8 & 0xFF));
            arrayOfByte1[(i2++)] = ((byte) (k & 0xFF));
        }
        if (i != -1) {
            i4 = i2 - i;
            if (i2 == 16) {
                return null;
            }
            for (i1 = 1; i1 <= i4; i1++) {
                arrayOfByte1[(16 - i1)] = arrayOfByte1[(i + i4 - i1)];
                arrayOfByte1[(i + i4 - i1)] = 0;
            }
            i2 = 16;
        }
        if (i2 != 16) {
            return null;
        }
        byte[] arrayOfByte2 = convertFromIpv4MappedAddress(arrayOfByte1);
        if (arrayOfByte2 != null) {
            return arrayOfByte2;
        }
        return arrayOfByte1;
    }

    public static boolean isIpv4LiteralAddress(String paramString) {
        return textToNumericFormatV4(paramString) != null;
    }

    public static boolean isIpv6LiteralAddress(String paramString) {
        return textToNumericFormatV6(paramString) != null;
    }

    public static byte[] convertFromIpv4MappedAddress(byte[] paramArrayOfByte) {
        if (isIpv4MappedAddress(paramArrayOfByte)) {
            byte[] arrayOfByte = new byte[4];
            System.arraycopy(paramArrayOfByte, 12, arrayOfByte, 0, 4);
            return arrayOfByte;
        }
        return null;
    }

    private static boolean isIpv4MappedAddress(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length < 16) {
            return false;
        }
        return (paramArrayOfByte[0] == 0) && (paramArrayOfByte[1] == 0) && (paramArrayOfByte[2] == 0) && (paramArrayOfByte[3] == 0)
                && (paramArrayOfByte[4] == 0) && (paramArrayOfByte[5] == 0) && (paramArrayOfByte[6] == 0) && (paramArrayOfByte[7] == 0)
                && (paramArrayOfByte[8] == 0) && (paramArrayOfByte[9] == 0) && (paramArrayOfByte[10] == -1) && (paramArrayOfByte[11] == -1);
    }

    private static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    private static final Pattern IPV6_PATTERN = Pattern.compile("^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$");

    public static boolean isIPV4(String addr) {
        return isMatch(addr, IPV4_PATTERN);
    }

    public static boolean isIPV6(String addr) {
        return isMatch(addr, IPV6_PATTERN);
    }

    private static boolean isMatch(String data, Pattern pattern) {
        if (isBlank(data)) {
            return false;
        }
        Matcher mat = pattern.matcher(data);
        return mat.find();
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
