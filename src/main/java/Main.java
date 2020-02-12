import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static Connection conn;
    // private Connection conn = null;
//    private Statement stmt = null;
//    private final String jdbcDriver = "org.sqlite.Driver";
//    private final String dbUrl = "jdbc:sqlite:./main/Data/IterationDB";
    private static final String CSV_FILE_PATH = "src/main/Data/SEOExample.csv";
    private static final String CSV_FILE_PATH_2 = "src/main/Data/bookstore_report2.csv";

    public static void main(String[] args) throws IOException, CsvException, SQLException {
        Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> records = csvReader.readAll();
        for (String[] record : records) {
            System.out.print(record[0] + ", ");
            System.out.print(record[1] + ", ");
            System.out.print(record[2] + ", ");
            System.out.print(record[3] + ", ");
            System.out.print(record[4] + ", ");
            System.out.print(record[5] + ", ");
            System.out.print(record[6] + ", ");
            System.out.print(record[7] + ", ");
            System.out.print(record[8] + ", ");
            System.out.println(record[9]);
        }

        Gson gson = new Gson();
        JsonReader jread = new JsonReader(new FileReader("src/main/Data/authors.json"));
        AuthorParser[] authors = gson.fromJson(jread, AuthorParser[].class);
        final String dbUrl = "jdbc:sqlite:D:/OneDrive - Florida Gulf Coast University/COP3710/src/main/Data/IterationDB";
//        conn = DriverManager.getConnection("jdbc:sqlite:IterationDB.db");
        conn = DriverManager.getConnection(dbUrl);
        PreparedStatement pstmt;

        for (AuthorParser element : authors) {
            //System.out.println(element.getName());

            try {
                String sql = "INSERT INTO author (author_name, author_email , author_url) VALUES (?, ?, ?)";
                try {
                    String nEmail;
                    String nURL;
                    pstmt = conn.prepareStatement(sql);
                    if (element.getEmail().equals("")) {
                        nEmail = null;
                    } else {
                        nEmail = element.getEmail();
                    }
                    if (element.getUrl().equals("")) {
                        nURL = null;
                    } else {
                        nURL = element.getUrl();
                    }
                    pstmt.setString(1, element.getName());
                    pstmt.setString(2, nEmail);
                    pstmt.setString(3, nURL);
                    pstmt.executeUpdate();
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }

        PreparedStatement ps;
        Reader reader1 = Files.newBufferedReader(Paths.get(CSV_FILE_PATH_2));
        CSVReader csvReader1 = new CSVReader(reader1);
        List<String[]> reports = csvReader1.readAll();
        for (String[] report : reports) {
            try {
                String sql = "INSERT INTO book (isbn, publisher_name, author_name, book_title)" +
                        " VALUES (?,?,?,?)";
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, report[0]);
                    ps.setString(2, report[3]);
                    ps.setString(3, report[2]);
                    ps.setString(4, report[1]);
//                    ps.setString(5, report[4]);
//                    ps.setString(6, report[5]);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }


    }
}
