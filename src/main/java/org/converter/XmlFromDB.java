package org.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class XmlFromDB {
    private static ArrayList<Computer> computers = new ArrayList<Computer>();

    public void xmlFromDB(String fileName) {
        Connection conn = DBConnection.connect();
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        String sql = "SELECT * FROM computers";
        File file = new File(fileName);

        try {
            prepStmt = conn.prepareStatement(sql);
            resSet = prepStmt.executeQuery();

            while (resSet.next()) {
                Computer comp = new Computer();
                comp.setName(resSet.getString("nazwa"));
                comp.setExchangeDate(resSet.getString("data_ksiegowania"));
                comp.setValue_USD(resSet.getDouble("koszt_USD"));
                comp.setValue_PLN(resSet.getDouble("koszt_PLN"));

                computers.add(comp);

                Invoice invoice = new Invoice(computers);

                JAXBContext jaxbContext = null;

                try {
                    jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                            .createContext(new Class[]{Invoice.class}, null);
                    Marshaller marshaller = jaxbContext.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

                    marshaller.marshal(invoice, file);
                }
                catch (
                        JAXBException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Niepowodzenie przy zapisywaniu pliku xml");
                }
            }
            System.out.println("Plik xml zapisano w lokalizacji: " + file.getAbsolutePath());

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                prepStmt.close();
                resSet.close();
                conn.close();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
