package com.angelale;

import java.io.*;
import java.sql.*;

public class Main {
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/demo?serverTimezone=UTC";
    public static void main(String[] args) {
        PreparedStatement statement = null;
        Statement st = null;
        ResultSet resultSet = null;

        InputStream input = null;
        FileOutputStream out = null;

        try (Connection connection = DriverManager.getConnection(CONNECTION, "student", "studentstudent@12")) {
            //reading file and writing it to the database
            String sql = "update employees set resume=? where email= 'john.doe@foo.com'";
            statement = connection.prepareStatement(sql);

            File pdf = new File("sample_resume.pdf");
            input = new FileInputStream(pdf);

            statement.setBinaryStream(1,input);
            System.out.println("saving files "+pdf.getAbsolutePath());
            System.out.println("storing "+pdf);
            statement.executeUpdate();

            st = connection.createStatement();
            resultSet = st.executeQuery("select resume from employees where email = 'john.doe@foo.com'");

            //reading file from database and writing it to a byte array
            File file = new File("resume_db.pdf");
            out = new FileOutputStream(file);

            if(resultSet.next()){
                input = resultSet.getBinaryStream(1);

                System.out.println("Reading from database...");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0){
                    out.write(buffer);
                }

                System.out.println("saved to file "+file.getAbsolutePath());
            }

            System.out.println("Done");

        } catch (SQLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (input != null) {
                    input.close();
                }
                if(out != null){
                    out.close();
                }
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        

    }


}
