import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchHamana")
public class SearchHamana extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SearchHamana() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }

   void search(String keyword, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionHamana.getDBConnectionHamana();
         connection = DBConnectionHamana.connection;

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM MyTableHamanaT3";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else {
            String selectSQL = "SELECT * FROM MyTableHamanaT3 WHERE phone LIKE ?";
            String thePhone = "%" + "%" + keyword + "%" + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, thePhone);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("firstName").trim();
            String lastName = rs.getString("lastName").trim();
            String phone = rs.getString("phone").trim();
            String date = rs.getString("date").trim();
            String time = rs.getString("time").trim();

            if (keyword.isEmpty() || phone.contains(keyword)) {
               out.println("ID: " + id + ", ");
               out.println("First Name: " + firstName + ", ");
               out.println("Last Name: " + lastName + ", ");
               out.println("Phone: " + phone + ", ");
               out.println("Date: " + date + ", ");
               out.println("Time: " + time + "<br>");
            }
         }
         out.println("<a href=/webproject-T3-Hamana/search_hamana.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
