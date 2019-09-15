package com.ugurhalil.builder;

import com.ugurhalil.configuration.HibernateConfig;
import com.ugurhalil.enums.DatabaseFieldType;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Halil UĞUR
 * @since 25.08.2019
 */
public class ClassBuilder {

    public void build(Table table, HibernateConfig hibernateConfig) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("package ").append(hibernateConfig.get_hibernatePackageName()).append(";");
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        stringBuilder.append("import java.io.Serializable;\n");

        List<Column> columns = new ArrayList<>(Arrays.asList(table.getColumns())).stream().filter(column -> !column.isForeignKey()).collect(Collectors.toList());
        List<ForeignKey> foreignKeys = new ArrayList<>(Arrays.asList(table.getForeignKeys()));
        List<ForeignKey> exportForeignKeys = new ArrayList<>(Arrays.asList(table.getExportForeignKeys()));

        if (hibernateConfig.is_activeHibernate()) {
            stringBuilder.append("import javax.persistence.*;").append("\n");
        }
        columns.stream().filter(column -> DatabaseFieldType.valueOf(column.getType()).fieldType().contains(".")).collect(Collectors.toSet()).forEach(column -> {
            if (!stringBuilder.toString().contains("import " + DatabaseFieldType.valueOf(column.getType()).fieldType())) {
                stringBuilder.append("import ").append(DatabaseFieldType.valueOf(column.getType()).fieldType()).append(";").append("\n");
            }
        });

        if (hibernateConfig.isExportTableActive()){
            if (!exportForeignKeys.isEmpty()){
                stringBuilder.append("import ").append("java.util.List").append(";").append("\n");
            }
        }


        stringBuilder.append("\n");
        stringBuilder.append("/**\n");
        stringBuilder.append(" *\n");
        stringBuilder.append(" * This file auto generate by UGURSOFT.\n");
        stringBuilder.append(" *\n");
        stringBuilder.append(" */\n");
        if (hibernateConfig.is_activeHibernate()) {
            stringBuilder.append("@Entity(name = \"").append(table.getName()).append("\")\n");
        }
        stringBuilder.append("public class ").append(table.getJavaName()).append(" implements Serializable").append(" {");
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        columns.forEach(column -> {
            if (column.isPrimaryKey() && hibernateConfig.is_activeHibernate()) {
                stringBuilder.append("    @Id\n");
                stringBuilder.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            stringBuilder.append("    private ").append(getShortTypeName(DatabaseFieldType.valueOf(column.getType()).fieldType())).append(" ").append(getNameForColumn(column)).append(";").append("\n\n");
        });

        foreignKeys.forEach(foreignKey -> {
            if (hibernateConfig.is_activeHibernate()) {
                stringBuilder.append("    @ManyToOne(fetch = FetchType.").append(hibernateConfig.get_hibernateFetchType()).append(", cascade = CascadeType.").append(hibernateConfig.get_hibernateCascadeType()).append(")\n");
                stringBuilder.append("    @JoinColumn(name = \"").append(foreignKey.getForeignTable().getName()).append("\")\n");
            }
            stringBuilder.append("    private ").append(foreignKey.getForeignTable().getJavaName()).append(" ").append(getNameForColumn(foreignKey.getFirstReference().getLocalColumn())).append(";").append("\n\n");
        });

        if (hibernateConfig.isExportTableActive()) {
            exportForeignKeys.forEach(foreignKey -> {
                stringBuilder.append("    private ").append("List<").append(foreignKey.getForeignTable().getJavaName()).append("> ").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append("List").append(";").append("\n\n");
            });
        }

        columns.forEach(column -> {
            stringBuilder.append("\n");
            stringBuilder.append("    public void").append(" set").append(getterAndSetterName(column)).append(" (").append(getShortTypeName(DatabaseFieldType.valueOf(column.getType()).fieldType())).append(" ").append(getNameForColumn(column)).append(") {").append("\n");
            stringBuilder.append("        this.").append(getNameForColumn(column)).append(" = ").append(getNameForColumn(column)).append(";");
            stringBuilder.append("\n");
            stringBuilder.append("    }");
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append("    public ").append(getShortTypeName(DatabaseFieldType.valueOf(column.getType()).fieldType())).append(" get").append(getterAndSetterName(column)).append(" () {").append("\n");
            stringBuilder.append("        return this.").append(getNameForColumn(column)).append(";");
            stringBuilder.append("\n");
            stringBuilder.append("    }");
            stringBuilder.append("\n");
        });

        foreignKeys.forEach(foreignKey -> {
            stringBuilder.append("\n");
            stringBuilder.append("    public void").append(" set").append(foreignKey.getForeignTable().getJavaName()).append(" (").append(foreignKey.getForeignTable().getJavaName()).append(" ").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append(") {").append("\n");
            stringBuilder.append("        this.").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append(" = ").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append(";");
            stringBuilder.append("\n");
            stringBuilder.append("    }");
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append("    public ").append(foreignKey.getForeignTable().getJavaName()).append(" get").append(foreignKey.getForeignTable().getJavaName()).append(" () {").append("\n");
            stringBuilder.append("        return this.").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append(";");
            stringBuilder.append("\n");
            stringBuilder.append("    }");
            stringBuilder.append("\n");
        });

        if (hibernateConfig.isExportTableActive()) {
            exportForeignKeys.forEach(foreignKey -> {
                stringBuilder.append("\n");
                stringBuilder.append("    public void").append(" set").append(foreignKey.getForeignTable().getJavaName()).append(" (List<").append(foreignKey.getForeignTable().getJavaName()).append("> ").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append("List) {").append("\n");
                stringBuilder.append("        this.").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append("List = ").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append("List;");
                stringBuilder.append("\n");
                stringBuilder.append("    }");
                stringBuilder.append("\n");
                stringBuilder.append("\n");
                stringBuilder.append("    public List<").append(foreignKey.getForeignTable().getJavaName()).append("> get").append(foreignKey.getForeignTable().getJavaName()).append("List () {").append("\n");
                stringBuilder.append("        return this.").append(toCamelCase(foreignKey.getForeignTable().getJavaName())).append("List;");
                stringBuilder.append("\n");
                stringBuilder.append("    }");
                stringBuilder.append("\n");
            });
        }
        stringBuilder.append("\n");
        stringBuilder.append("}");

        File createFolder = new File(hibernateConfig.get_hibernateModelsGeneratedPath());
        boolean isCreated = createFolder.mkdirs();

        if (isCreated) {
            System.out.println("--------------------------");
            System.out.println("Create folder for model files: " + hibernateConfig.get_hibernateModelsGeneratedPath());
            System.out.println("--------------------------");
        } else {
            System.out.println(table.getJavaName());
        }

        Files.write(Paths.get(hibernateConfig.get_hibernateModelsGeneratedPath() + "/" + table.getJavaName() + ".java"), stringBuilder.toString().getBytes());
    }

    private String getShortTypeName(String longTypeName) {
        if (longTypeName.contains(".")) {
            String[] split = longTypeName.split("\\.");
            return split[split.length - 1];
        }
        return longTypeName;
    }

    private String toCamelCase(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toLowerCase(Locale.ENGLISH) + original.substring(1);
    }

    private String getterAndSetterName(Column column){
        String name = getNameForColumn(column);
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    private String getNameForColumn(Column column){
        return StringUtils.isNotBlank(column.getJavaName()) ? toCamelCase(column.getJavaName()) : toCamelCase(column.getName());
    }
}
