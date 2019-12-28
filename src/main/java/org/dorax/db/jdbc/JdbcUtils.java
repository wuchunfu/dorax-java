package org.dorax.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC工具类
 *
 * @author wuchunfu
 * @date 2019-12-27
 */
public class JdbcUtils {
    /**
     * 定义数据库的链接
     */
    private Connection conn;
    /**
     * 定义sql语句的执行对象
     */
    private PreparedStatement pstmt;
    /**
     * 定义查询返回的结果集合
     */
    private ResultSet rs;

    /**
     * 初始化
     *
     * @param driver   驱动类
     * @param url      url
     * @param username 用户名
     * @param password 密码
     */
    public JdbcUtils(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据
     *
     * @param sql    sql 字符串
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 异常对象
     */
    public boolean updateByParams(String sql, List<String> params) throws SQLException {
        // 影响行数
        int result;
        pstmt = conn.prepareStatement(sql);
        int index = 1;
        // 填充sql语句中的占位符
        if (null != params && !params.isEmpty()) {
            for (Object param : params) {
                pstmt.setObject(index++, param);
            }
        }
        result = pstmt.executeUpdate();
        return result > 0;
    }

    /**
     * 查询多条记录
     *
     * @param sql    sql 字符串
     * @param params 参数列表
     * @return 查询结果集合
     * @throws SQLException 异常对象
     */
    public List<Map<String, Object>> selectByParams(String sql, List<String> params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        int index = 1;
        pstmt = conn.prepareStatement(sql);
        if (null != params && !params.isEmpty()) {
            for (Object param : params) {
                pstmt.setObject(index++, param);
            }
        }
        rs = pstmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int colsLen = metaData.getColumnCount();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>(colsLen);
            for (int i = 0; i < colsLen; i++) {
                String columnName = metaData.getColumnName(i + 1);
                Object columnValue = rs.getObject(columnName);
                if (null == columnValue) {
                    columnValue = "";
                }
                map.put(columnName, columnValue);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 释放连接
     */
    public void release() {
        try {
            if (null != rs) {
                rs.close();
            }
            if (null != pstmt) {
                pstmt.close();
            }
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
