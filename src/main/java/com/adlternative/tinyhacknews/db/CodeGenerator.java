package com.adlternative.tinyhacknews.db;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import java.nio.file.Paths;

public class CodeGenerator {
  public static void main(String[] args) {
    FastAutoGenerator.create("jdbc:mysql://localhost:3306/tinyhacknews", "tiny-hacknews", "123456")
        .globalConfig(
            builder ->
                builder
                    .author("Baomidou")
                    .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                    .commentDate("yyyy-MM-dd"))
        .packageConfig(
            builder ->
                builder
                    .parent("com.adlternative.tinyhacknews")
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .xml("mapper.xml"))
        .strategyConfig(
            builder ->
                builder
                    .entityBuilder()
                    .enableLombok()
                    .enableFileOverride()
                    .mapperBuilder()
                    .enableFileOverride())
        .templateConfig(builder -> builder.service("").serviceImpl(""))
        .execute();
  }
}
