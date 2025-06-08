package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.dto.UserDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/v1/signup")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("dataSource");

        try {

            ObjectMapper mapper = new ObjectMapper();
            UserDto userDto = mapper.readValue(req.getInputStream(), UserDto.class);

            Connection connection = basicDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user VALUE(?,?,?,?,?)");
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, userDto.getName());
            statement.setString(3, userDto.getAddress());
            statement.setString(4, userDto.getEmail());
            statement.setString(5, userDto.getPassword() );

            int i = statement.executeUpdate();
            PrintWriter out=resp.getWriter();
            resp.setContentType("application/json");

            if(i>0){
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                mapper.writeValue(out, Map.of(
                        "code","201",
                        "status","success",
                        "message","User signed up successfully"
                ));
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(out, Map.of(
                        "code", "400",
                        "status", "error",
                        "message", "Bad Request"
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}