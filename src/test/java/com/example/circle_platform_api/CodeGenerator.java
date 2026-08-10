package com.example.circle_platform_api;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {

    // 数据库配置（建议加上字符集）
    private static final String URL = "JDBC:mysql://localhost:3306/circle_platform?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static final String AUTHOR = "GAOMING";
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)

                // ==================== 全局配置 ====================
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .outputDir(PROJECT_PATH + "/src/main/java")
                        .dateType(DateType.TIME_PACK)        // 使用 java.time 包
                        .commentDate("yyyy-MM-dd")           // 注释日期格式
                        .disableOpenDir()                    // 生成后不自动打开文件夹
                        .enableSpringdoc()          // ← 关键修改：使用 Springdoc / OpenAPI 3 注解
                )

                // ==================== 包配置 ====================
                .packageConfig(builder -> builder
                        .parent("com.example.circle_platform_api")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_PATH + "/src/main/resources/mapper/"))
                )

                // ==================== 策略配置 ====================
                .strategyConfig(builder -> {
                    // 自动获取所有表并过滤
                    List<String> tables = getAllTables(URL, USERNAME, PASSWORD);
                    builder.addInclude(tables);

                    // Entity 配置（最常用优化）
                    builder.entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .enableChainModel()           // 链式调用
                            .enableFileOverride()         // 允许覆盖
                            // 自动填充常见字段（推荐）
                            .addTableFills(new Column("created_at", FieldFill.INSERT))
                            .addTableFills(new Column("updated_at", FieldFill.INSERT_UPDATE))
                            .logicDeleteColumnName("delete_at")  // 逻辑删除
                    ;

                    // Mapper 配置
                    builder.mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .enableFileOverride()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");

                    // Service 配置
                    builder.serviceBuilder()
                            .enableFileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");

                    // Controller 配置
                    builder.controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .enableFileOverride()
                            .formatFileName("%sController");
                })

                // ==================== 模板引擎 ====================
                .templateEngine(new FreemarkerTemplateEngine())

                .execute();
    }
    // 自动读取表
    public static List<String> getAllTables(String url, String username, String password) {
        List<String> tables = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String table = rs.getString("table_name");
                // 过滤系统表或不需要的表
                if (table.startsWith("flyway") ||
                        table.startsWith("sys_") ||
                        table.startsWith("t_") ||
                        table.equalsIgnoreCase("flyway_schema_history")) {
                    continue;
                }
                tables.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }
}
