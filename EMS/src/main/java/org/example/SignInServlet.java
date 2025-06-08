package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/v1/signIn")
public class SignInServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource dataSource = (BasicDataSource) servletContext.getAttribute("dataSource");
        try {
            //GET RESPONSE FROM FRONT END
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> user = mapper.readValue(req.getInputStream(), Map.class);

            String email=user.get("email");
            String password = user.get("password");

            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM user where email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            //CREATE OBJ TO GIVE OUTPUT
            PrintWriter out = resp.getWriter();

            if(result.next()){
                //USER FOUND
                resp.setContentType("application/json");
                String dbPw = result.getString("password");

                if(dbPw.equals(password)){
                    //PASSWORD IS CORRECT
                    mapper.writeValue(out, Map.of(
                            "code", "200",
                            "status", "Success",
                            "message", "Log In Successfully"
                    ));
                }else{
                    //PASSWORD IS CORRECT
                    mapper.writeValue(out, Map.of(
                            "code", "401",
                            "status", "Failed",
                            "message", "User Password is Wrong !!"
                    ));
                }

            }else{
                //USER NOT FOUND --> Wrong Email
                //EMAIL ACCOUNT NOT FOUND
                mapper.writeValue(out, Map.of(
                        "code", "400",
                        "status", "UnAuthorized",
                        "message", "Email Account is Not Found"
                ));
            }

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
