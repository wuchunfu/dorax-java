package org.dorax.io.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析Excel，支持2003、2007
 *
 * @author wuchunfu
 * @date 2020-02-05
 */
public class ExcelReadHelper {

    /**
     * 解析Excel 支持2003、2007
     * 利用反射技术完成propertis到obj对象的映射，并将相对应的值利用相对应setter方法设置到obj对象中最后add到list集合中
     * properties、obj需要符合如下规则：
     * 1、obj对象必须存在默认构造函数，且属性需存在setter方法
     * 2、properties中的值必须是在obj中存在的属性，且obj中必须存在这些属性的setter方法。
     * 3、properties中值得顺序要与Excel中列相相应，否则值会设置错：
     * excel: 编号 姓名 年龄 性别
     * properties: id name age sex
     *
     * @param file       待解析的Excel文件
     * @param properties 与Excel相对应的属性
     * @param obj        反射对象的Class
     * @return list 集合
     * @throws Exception Exception
     */
    public static List<Object> excelRead(File file, String[] properties, Class obj) throws Exception {
        Workbook book;
        try {
            // 解析2003
            book = new XSSFWorkbook(new FileInputStream(file));
        } catch (Exception e) {
            // 解析2007
            book = new HSSFWorkbook(new FileInputStream(file));
        }
        return getExcelContent(book, properties, obj);
    }

    /**
     * 解析Excel 支持2003、2007
     * 利用反射技术完成propertis到obj对象的映射，并将相对应的值利用相对应setter方法设置到obj对象中最后add到list集合中
     * properties、obj需要符合如下规则：
     * 1、obj对象必须存在默认构造函数，且属性需存在setter方法
     * 2、properties中的值必须是在obj中存在的属性，且obj中必须存在这些属性的setter方法。
     * 3、properties中值得顺序要与Excel中列相相应，否则值会设置错：
     * excel：编号 姓名 年龄 性别
     * properties：id name age sex
     *
     * @param filePath   待解析的Excel文件的路径
     * @param properties 与Excel相对应的属性
     * @param obj        反射对象的Class
     * @return list 集合
     * @throws Exception Exception
     */
    public static List<Object> excelRead(String filePath, String[] properties, Class obj) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("指定的文件不存在");
        }
        return excelRead(file, properties, obj);
    }

    /**
     * 根据params、object解析Excel，并且构建list集合
     *
     * @param book       WorkBook对象，他代表了待将解析的Excel文件
     * @param properties 需要参考Object的属性
     * @param obj        构建的Object对象，每一个row都相当于一个object对象
     * @return list 集合
     * @throws Exception Exception
     */
    private static List<Object> getExcelContent(Workbook book, String[] properties, Class obj) throws Exception {
        // 初始化结果解
        List<Object> resultList = new ArrayList<>();
        Map<String, Method> methodMap = getObjectSetterMethod(obj);
        Map<String, Field> fieldMap = getObjectField(obj);
        for (int numSheet = 0; numSheet < book.getNumberOfSheets(); numSheet++) {
            Sheet sheet = book.getSheetAt(numSheet);
            // 谨防中间空一行
            if (sheet == null) {
                continue;
            }
            // 一个row就相当于一个Object
            for (int numRow = 1; numRow < sheet.getLastRowNum(); numRow++) {
                Row row = sheet.getRow(numRow);
                if (row == null) {
                    continue;
                }
                resultList.add(getObject(row, properties, methodMap, fieldMap, obj));
            }
        }
        return resultList;
    }

    /**
     * 获取row的数据，利用反射机制构建Object对象
     *
     * @param row        row对象
     * @param properties Object参考的属性
     * @param methodMap  object对象的setter方法映射
     * @param fieldMap   object对象的属性映射
     * @param obj        反射对象的Class
     * @return object 对象
     * @throws Exception Exception
     */
    private static Object getObject(Row row, String[] properties, Map<String, Method> methodMap, Map<String, Field> fieldMap, Class obj) throws Exception {
        Object object = obj.newInstance();
        for (int numCell = 0; numCell < row.getLastCellNum(); numCell++) {
            Cell cell = row.getCell(numCell);
            if (cell == null) {
                continue;
            }
            String cellValue = getValue(cell);
            String property = properties[numCell].toLowerCase();
            // 该property在object对象中对应的属性
            Field field = fieldMap.get(property);
            // 该property在object对象中对应的setter方法
            Method method = methodMap.get(property);
            setObjectPropertyValue(object, field, method, cellValue);
        }
        return object;
    }

    /**
     * 根据指定属性的的setter方法给object对象设置值
     *
     * @param obj    object对象
     * @param field  object对象的属性
     * @param method object对象属性的相对应的方法
     * @param value  需要设置的值
     * @throws Exception Exception
     */
    private static void setObjectPropertyValue(Object obj, Field field, Method method, String value) throws Exception {
        Object[] oo = new Object[1];
        String type = field.getType().getName();
        if ("java.lang.String".equals(type) || "String".equals(type)) {
            oo[0] = value;
        } else if ("java.lang.Integer".equals(type) || "java.lang.int".equals(type) || "Integer".equals(type) || "int".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Integer.valueOf(value);
            }
        } else if ("java.lang.Float".equals(type) || "java.lang.float".equals(type) || "Float".equals(type) || "float".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Float.valueOf(value);
            }
        } else if ("java.lang.Double".equals(type) || "java.lang.double".equals(type) || "Double".equals(type) || "double".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Double.valueOf(value);
            }
        } else if ("java.math.BigDecimal".equals(type) || "BigDecimal".equals(type)) {
            if (value.length() > 0) {
                oo[0] = new BigDecimal(value);
            }
        } else if ("java.util.Date".equals(type) || "Date".equals(type)) {
            if (value.length() > 0) {
                // 当长度为19(yyyy-MM-dd HH24:mm:ss)或者为14(yyyyMMddHH24mmss)时Date格式转换为yyyyMMddHH24mmss
                if (value.length() == 19 || value.length() == 14) {
                    oo[0] = string2Date(value, "yyyyMMddHH24mmss");
                } else {     //其余全部转换为yyyyMMdd格式
                    oo[0] = string2Date(value, "yyyyMMdd");
                }
            }
        } else if ("java.sql.Timestamp".equals(type)) {
            if (value.length() > 0) {
                oo[0] = formatDate(value, "yyyyMMddHH24mmss");
            }
        } else if ("java.lang.Boolean".equals(type) || "Boolean".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Boolean.valueOf(value);
            }
        } else if ("java.lang.Long".equals(type) || "java.lang.long".equals(type) || "Long".equals(type) || "long".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Long.valueOf(value);
            }
        }
        try {
            method.invoke(obj, oo);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return NumberToTextConverter.toText(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 获取object对象所有属性的Setter方法，并构建map对象，结构为Map<'field','method'>
     *
     * @param object object对象
     * @return
     */
    private static Map<String, Method> getObjectSetterMethod(Class object) {
        // 获取object对象的所有属性
        Field[] fields = object.getDeclaredFields();
        // 获取object对象的所有方法
        Method[] methods = object.getDeclaredMethods();
        Map<String, Method> methodMap = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            for (Method method : methods) {
                String meth = method.getName();
                //匹配set方法
                if (meth != null && "set".equals(meth.substring(0, 3)) && Modifier.isPublic(method.getModifiers()) && ("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1)).equals(meth)) {
                    // 将匹配的setter方法加入map对象中
                    methodMap.put(fieldName.toLowerCase(), method);
                    break;
                }
            }
        }
        return methodMap;
    }

    /**
     * 获取object对象的所有属性，并构建map对象，对象结果为Map<'field','field'>
     *
     * @param object object对象
     * @return map 映射
     */
    private static Map<String, Field> getObjectField(Class object) {
        // 获取object对象的所有属性
        Field[] fields = object.getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            fieldMap.put(fieldName.toLowerCase(), field);
        }
        return fieldMap;
    }

    /**
     * 获取日期显示格式，为空默认为yyyy-mm-dd HH:mm:ss
     *
     * @param format 日期格式
     * @return SimpleDateFormat
     */
    protected static SimpleDateFormat getFormat(String format) {
        if (format == null || "".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(format);
    }

    /**
     * 格式化日期
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 格式化后的日期字符串
     */
    public static String formatDate(String date, String format) {
        if (date == null || "".equals(date)) {
            return "";
        }
        Date dt;
        SimpleDateFormat inFmt;
        SimpleDateFormat outFmt;
        ParsePosition pos = new ParsePosition(0);
        date = date.replace("-", "").replace(":", "");
        if ("".equals(date.trim())) {
            return "";
        }
        try {
            if (Long.parseLong(date) == 0L) {
                return "";
            }
        } catch (Exception nume) {
            return date;
        }
        try {
            switch (date.trim().length()) {
                case 14:
                    inFmt = new SimpleDateFormat("yyyyMMddHHmmss");
                    break;
                case 12:
                    inFmt = new SimpleDateFormat("yyyyMMddHHmm");
                    break;
                case 10:
                    inFmt = new SimpleDateFormat("yyyyMMddHH");
                    break;
                case 8:
                    inFmt = new SimpleDateFormat("yyyyMMdd");
                    break;
                case 6:
                    inFmt = new SimpleDateFormat("yyyyMM");
                    break;
                case 7:
                case 9:
                case 11:
                case 13:
                default:
                    return date;
            }
            if ((dt = inFmt.parse(date, pos)) == null) {
                return date;
            }
            if ((format == null) || ("".equals(format.trim()))) {
                outFmt = new SimpleDateFormat("yyyy年MM月dd日");
            } else {
                outFmt = new SimpleDateFormat(format);
            }
            return outFmt.format(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串(格式符合规范)转换成Date
     *
     * @param value  待转换的字符串
     * @param format 日期格式
     * @return date 类型的日期
     */
    public static Date string2Date(String value, String format) {
        if (value == null || "".equals(value)) {
            return null;
        }
        SimpleDateFormat sdf = getFormat(format);
        Date date = null;
        try {
            value = formatDate(value, format);
            date = sdf.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
