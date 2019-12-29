package com.angelale;

import java.io.*;
import java.sql.*;

public class Main {
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/demo?serverTimezone=UTC";
    public static void main(String[] args) {
        PreparedStatement statement = null;

        ResultSet resultSet = null;

        FileReader fr = null;
        FileWriter fw = null;
        Reader r = null;

        try (Connection connection = DriverManager.getConnection(CONNECTION, "student", "studentstudent@12")) {
            String sql = "update employees set resume = ? where email ='john.doe@foo.com'";
            statement = connection.prepareStatement(sql);
            File file = new File("sample_resume.txt");
            fr = new FileReader(file);
            statement.setCharacterStream(1,fr);
            System.out.println("setting file "+file.getAbsolutePath());
            statement.executeUpdate();

            file = new File("resume2.txt");
            fw = new FileWriter(file);
            statement = connection.prepareStatement("select resume from employees where email = ?");
            statement.setString(1,"john.doe@foo.com");
            resultSet = statement.executeQuery();

            if (resultSet.next()){
                r = resultSet.getCharacterStream(1);
                System.out.println("reading from database");
                int c_char;
                while ((c_char = r.read())>0) fw.write(c_char);
                System.out.println("saved file"+file.getAbsolutePath());
            }
            System.out.println("done");
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
                if (fr!=null)fr.close();
                if (fw!=null)fw.close();
                if (fw!=null)r.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        

    }


}
