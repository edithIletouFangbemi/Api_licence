package com.example.Api_version.utils;

import com.example.Api_version.controller.LicenceServeurController;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jasperprint  {

    public void print(JasperReportBuilder jasperReportBuilder, HttpServletResponse response)  {
        try {
            final JasperPrint jasperPrint = jasperReportBuilder.toJasperPrint();
            response.setContentType("application/x-pdf");
            response.setHeader("Content-disposition", "inline; filename=fiche_demande.pdf");
            final OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (DRException ex) {
            Logger.getLogger(LicenceServeurController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

}
