package com.ssh.contextListener;

import com.mchange.v2.c3p0.DataSources;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * 手动关闭mysql——jdbc注册驱动信息
 * @author luyuhao
 * @date 2021/6/24 10:49
 */
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ContextListener-Init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ContextListener-End");
        //手动取消c3p0数据库连接池
        try {
            DataSources.destroy(DataSourceUtils.getDataSource());//getDataSource方法获取c3p0数据源
            System.out.println("关闭数据库连接池成功!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //手动取消驱动程序的注册：
        Enumeration drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = (Driver) drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("deregistering jdbc driver: "+driver);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error deregistering driver"+driver);
            }
        }
        //手动停止名为[mysql-cj-abandoned-connection-cleanup]的线程
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            System.out.println("------------------AbandonedConnectionCleanupThread.shutdown()报错--------------------");
            e.printStackTrace();
        }

    }
}
