package org.converter;

import com.google.gson.Gson;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static org.converter.DBClearTable.DBClearData;
import static org.converter.DBCreate.createDB;

public class Main {
    private static String column = "";
    private static String search = "";
    private static String order = "";
    private static String direction = "";
    private static String orderChoice = "";
    private static String orderChoiceTwo = "";
    private static String searchChoice = "";
    private static String saveName = "";
    private static String fileName = "";
    private static String name = "";
    private static String date = "";
    private static String exchangeDate = "";
    private static double valueUSD = 0;
    private static double valuePLN = 0;
    private static double exchangeRate = 0;
    private static final String url03 = "/2022-01-03/";
    private static final String url10 = "/2022-01-10/";
    private static String urlSelection = "https://api.nbp.pl/api/exchangerates/rates/a/usd";
    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<Computer> computers = new ArrayList<Computer>();

    private static double roundNumber(double value, int places) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private static String currencyView(double value, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(value);
    }

    private static void extractAPIInfo() {
        try {
            URL url = new URL(urlSelection);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int respCode = conn.getResponseCode();
            if (respCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + respCode);
            }

            String jsonText = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                jsonText += scanner.nextLine();
            }
            scanner.close();

            Gson gson = new Gson();
            CurrencyAPI currencyAPI = gson.fromJson(jsonText, CurrencyAPI.class);

            exchangeDate = currencyAPI.rates.get(0).getEffectiveDate();
            exchangeRate = currencyAPI.rates.get(0).getMid();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Przelicznik USD - PLN");
        System.out.println();

        createDB("ComputersDataBase.db");

        while (!saveName.matches("[a-zA-Z0-9 ]+")) {
            System.out.println("Wprowadz nazwe pliku XML (bez znakow specjalnych)");
            System.out.println("Przykladowo: ");
            System.out.println("faktura");
            saveName = scanner.nextLine();

            if (!saveName.matches("[a-zA-Z0-9 ]+")) {
                System.out.println("Wprowadzono niepoprawna nazwe!");
            }
        }
        saveName += ".xml";

        while (true) {
            System.out.println();
            System.out.println("Wyswietlenie danych obecnie znajdujacych sie w tabeli - 1");
            System.out.println("Wprowadzenie rekordu do bazy danych oraz zapisanie w pliku XML - 2");
            System.out.println("Wyszukanie rekordu w bazie danych - 3");
            System.out.println("Sortowanie oraz wyswietlenie rekordow znajdujacych sie w bazie danych - 4");
            System.out.println("Usuniecie wszystkich danych - 5");
            System.out.println("Zapisanie danych z bazy danych do pliku xml - 6");
            System.out.println("Opuszczenie programu - 0");

            String position = "";
            while (!position.matches("[0-6]")) {
                position = scanner.nextLine();
                if (!position.matches("[0-6]")) {
                    System.out.println("Wprowadz wartosc 0 - 6!");
                }
            }

            switch (position) {
                case "1" -> printDataBeingStored();
                case "2" -> saveToXmlAndDB(scanner);
                case "3" -> searchDataInDB();
                case "4" -> sortAndViewDataInDB();
                case "5" -> DBClearData();
                case "6" -> saveAllDBtoXml();
                case "0" -> System.exit(0);
                default -> {
                }
            }
        }
    }

    private static void saveAllDBtoXml() {
        while (!fileName.matches("[a-zA-Z0-9 ]+")) {
            System.out.println("Wprowadz nazwe pliku XML (bez znakow specjalnych)");
            System.out.println("Przykladowo: ");
            System.out.println("faktura");
            fileName = scanner.nextLine();

            if (!fileName.matches("[a-zA-Z0-9 ]+")) {
                System.out.println("Wprowadzono niepoprawna nazwe!");
            }
        }
        fileName += ".xml";

        XmlFromDB xml = new XmlFromDB();
        xml.xmlFromDB(fileName);
    }

    private static void saveToXmlAndDB(Scanner scanner) {
        name = "";
        while (!name.matches("[a-zA-Z0-9 ]+")) {
            System.out.println("Wprowadz nazwe urzadzenia (bez znakow specjalnych): ");
            name = scanner.nextLine();
            if (!name.matches("[a-zA-Z0-9 ]+")) {
                System.out.println("Wprowadzono niepoprawna nazwe!");
            }
        }

        System.out.println("Wybierz date przewalutowania: ");
        System.out.println("A - 03-01-2022");
        System.out.println("B - 10-01-2022");

        date = scanner.nextLine();
        date = date.toUpperCase();

        switch (date) {
            case "A" -> urlSelection += url03;
            case "B" -> urlSelection += url10;
            default -> {
                System.out.println("Niepoprwna wartosc! Wprowad A lub B");
                return;
            }
        }

        extractAPIInfo();

        boolean valueUSDValid = false;
        System.out.println("Wprowadz kwote w USD: ");
        while (!valueUSDValid) {
            try {
                valueUSD = scanner.nextDouble();
                scanner.nextLine();
                valueUSDValid = true;
            }
            catch (Exception e) {
                System.out.println("Niepoprawna wartosc!");
                scanner.nextLine();
            }
        }

        valuePLN = exchangeRate * valueUSD;
        valueUSD = roundNumber(valueUSD, 2);
        valuePLN = roundNumber(valuePLN, 2);

        System.out.println();
        System.out.println("Kwota w PLN przeliczona zgonie z kursem z dnia " + exchangeDate + ": ");
        System.out.println(currencyView(valuePLN, new Locale("pl", "PL")));
        System.out.println();

        DBInsertData insertData = new DBInsertData();
        insertData.DBInsertData(name, exchangeDate, valueUSD, valuePLN);

        Computer comp = new Computer();
        comp.setName(name);
        comp.setExchangeDate(exchangeDate);
        comp.setValue_USD(valueUSD);
        comp.setValue_PLN(valuePLN);

        computers.add(comp);

        Invoice invoice = new Invoice(computers);
        File file = new File(saveName);

        JAXBContext jaxbContext = null;

        try {
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{Invoice.class}, null);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            marshaller.marshal(invoice, file);

            System.out.println("Plik xml zapisano w lokalizacji: " + file.getAbsolutePath());
        }
        catch (JAXBException e) {
            System.out.println(e.getMessage());
            System.out.println("Niepowodzenie przy zapisywaniu pliku xml");
        }
    }

    private static void printDataBeingStored() {
        DBSelectData viewData = new DBSelectData();
        viewData.DBSelectAllData();
    }

    private static void searchDataInDB() {
        System.out.println("Wyszukiwanie po nazwie - N");
        System.out.println("Wyszuiwanie po dacie - D");
        searchChoice = scanner.nextLine();
        searchChoice = searchChoice.toUpperCase();

        switch (searchChoice) {
            case "N" -> {
                column = "nazwa";
                System.out.println("Wprowadz nazwe szukanego urzadzenia lub jej czesc: ");
                search = scanner.nextLine();
            }
            case "D" -> {
                column = "data_ksiegowania";
                System.out.println("Wprowadz date zaksiegowania (w formacie RRRR-MM-DD) szukanego urzadzenia lub jej czesc: ");
                search = scanner.nextLine();
            }
            default -> {
                System.out.println("Niepoprwna wartosc! Wprowad N lub D");
                return;
            }
        }

        DBSearchData searchData = new DBSearchData();
        searchData.DBSearch(column, search);
    }

    private static void sortOrder() {
        System.out.println("Sortowanie w kolejnosci rosnacej (R) czy malejacej (M)");
        orderChoiceTwo = scanner.nextLine();
        orderChoiceTwo = orderChoiceTwo.toUpperCase();

        switch (orderChoiceTwo) {
            case "R" -> direction = "ASC";
            case "M" -> direction = "DESC";
            default -> {
                System.out.println("Niepoprawna wartosc! Wprowadz R lub M");
            }
        }
    }

    private static void sortAndViewDataInDB() {
        System.out.println("Sortowanie po nazwie - N");
        System.out.println("Sortowanie po dacie - D");
        orderChoice = scanner.nextLine();
        orderChoice = orderChoice.toUpperCase();

        switch (orderChoice) {
            case "N" -> {
                order = "nazwa";
                sortOrder();
            }
            case "D" -> {
                order = "data_ksiegowania";
                sortOrder();
            }
            default -> {
                System.out.println("Niepoprawna wartosc! Wprowadz R lub M");
                return;
            }
        }

        DBOrderBy orderBy = new DBOrderBy();
        orderBy.DBOrder(order, direction);
    }
}