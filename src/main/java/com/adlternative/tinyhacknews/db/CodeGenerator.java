package com.adlternative.tinyhacknews.db;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import java.nio.file.Paths;
import java.util.Collections;

public class CodeGenerator {
  public static void main(String[] args) {
    String projectPath = System.getProperty("user.dir");

    FastAutoGenerator.create("jdbc:mysql://localhost:3306/tinyhacknews", "tiny-hacknews", "123456")
        .globalConfig(
            builder ->
                builder
                    .author("adlternative")
                    .outputDir(Paths.get(projectPath) + "/src/main/java")
                    .disableOpenDir()
                    .commentDate("yyyy-MM-dd"))
        .packageConfig(
            builder ->
                builder
                    .parent("com.adlternative.tinyhacknews")
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .xml("mapper")
                    .pathInfo(
                        Collections.singletonMap(
                            OutputFile.xml, Paths.get(projectPath) + "src/main/resources")))
        .strategyConfig(
            builder ->
                builder
                    .addInclude("users")
                    .entityBuilder()
                    .enableLombok()
                    .enableTableFieldAnnotation()
                    .enableFileOverride()
                    .enableRemoveIsPrefix()
                    .logicDeleteColumnName("is_deleted")
                    .mapperBuilder()
                    .enableFileOverride())
        .templateConfig(builder -> builder.service("").serviceImpl(""))
        .execute();
  }
}
