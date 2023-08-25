package com.example.Api_version.report;

import com.example.Api_version.utils.BaseReport;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;

public class FicheProduitEtat extends BaseReport {
    public static String getTemplate(){
        return "template/ficheProduits.jrxml";
    }

    @Override
    public FieldBuilder<?>[] getFields(){
        FieldBuilder<?> mesField[] = new FieldBuilder<?>[1];
        mesField[0] = DynamicReports.field("libelle", DataTypes.stringType());
        /*
        mesField[1] = DynamicReports.field("prix", DataTypes.doubleType());
        mesField[2] = DynamicReports.field("quantite", DataTypes.doubleType());
        mesField[3] = DynamicReports.field("montant", DataTypes.doubleType());*/
        return mesField;
    }
}
