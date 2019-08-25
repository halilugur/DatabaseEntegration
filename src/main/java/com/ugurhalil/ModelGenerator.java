package com.ugurhalil;

import com.ugurhalil.builder.ClassBuilder;
import com.ugurhalil.configuration.HibernateConfig;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.tools.ant.BuildException;

import java.io.IOException;

/**
 * @author Halil UĞUR
 * @since 25.08.2019
 */
public class ModelGenerator {

    private Database _database;
    private HibernateConfig hibernateConfig;

    private ModelGenerator() {
    }

    public static ModelGenerator getInstance(Database database, HibernateConfig hibernateConfig) {
        ModelGenerator modelGenerator = new ModelGenerator();
        modelGenerator._database = database;
        modelGenerator.hibernateConfig = hibernateConfig;
        return modelGenerator;
    }

    public void execute() throws BuildException {

        ClassBuilder classBuilder = new ClassBuilder();

        for (Table table : this._database.getTables()) {
            try {
                classBuilder.build(table,hibernateConfig);
            } catch (IOException e) {
                System.out.println("HATA");
                e.printStackTrace();
            }
        }
    }
}
