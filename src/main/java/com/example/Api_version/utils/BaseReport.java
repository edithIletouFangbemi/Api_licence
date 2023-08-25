package com.example.Api_version.utils;

import lombok.Data;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.ParameterBuilder;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


public abstract class BaseReport {
    private JasperReportBuilder rapport = DynamicReports.report();
    private ParameterBuilder<?>[] parameterBuilder;

    protected FieldBuilder<?>[] getFields() {
        return null;
    }

    protected VariableBuilder<?>[] getVariables() {
        return null;
    }

    public void setParameterBuilder(ParameterBuilder<?>[] parameterBuilder) {
        this.parameterBuilder = parameterBuilder;
    }

    public JasperReportBuilder getRapport(String template, List list) {

        try {
            JasperDesign jasperDesign = null;

            try {
                URL resource = getClass().getClassLoader().getResource(template);
                if (resource != null) {
                    jasperDesign = JRXmlLoader.load(resource.openStream());
                }
                rapport.fields(getFields());
                if (getVariables() != null) {
                    rapport.variables(getVariables());
                }
                if (parameterBuilder != null) {
                    rapport.parameters(parameterBuilder);
                }

                rapport.setDataSource(new JRBeanCollectionDataSource(list));
            } catch (JRException | IOException ex) {
                Logger.getLogger(BaseReport.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (jasperDesign != null) {
                rapport.setTemplateDesign(jasperDesign);
            }
        } catch (DRException ex) {
            Logger.getLogger(BaseReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rapport;
    }
}
