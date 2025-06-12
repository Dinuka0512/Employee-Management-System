package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.dto.EmployeeDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@WebServlet("/employee")
@MultipartConfig
public class EmployeeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource dataSource = (BasicDataSource) servletContext.getAttribute("dataSource");
        ArrayList<EmployeeDto> employeeDtos;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Employee");
            ResultSet resultSet = statement.executeQuery();

            employeeDtos = new ArrayList<>();

            while (resultSet.next()) {
                EmployeeDto employeeDto = new EmployeeDto(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("email"),
                        resultSet.getString("contact"),
                        resultSet.getBytes("image")
                );

                employeeDtos.add(employeeDto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), employeeDtos);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource dataSource = (BasicDataSource) servletContext.getAttribute("dataSource");
        try {
            ObjectMapper mapper = new ObjectMapper();

            Part employee = req.getPart("employee");
            InputStream employeeInputStream = employee.getInputStream();
            EmployeeDto dto = mapper.readValue(employeeInputStream, EmployeeDto.class);

            Part image = req.getPart("image");
            InputStream imageInputStream = image.getInputStream();
            byte[] bytes = imageInputStream.readAllBytes();


            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO employee VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getAddress());
            statement.setString(4, dto.getEmail());
            statement.setString(5, dto.getContact());
            statement.setBytes(6, bytes);

            int i = statement.executeUpdate();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            if(i > 0){
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(out, Map.of(
                        "code","200",
                        "status","Success",
                        "message","Employee Saved Successfully"
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
