
  <%@ page import = "java.sql.*" %>
  <%
      Connection conn = DriverManager.getConnection(
    		  
          "jdbc:postgresql://127.0.0.1:5432/elezioni", "postgres", "password"); 
      // Connection conn =
      //    DriverManager.getConnection("jdbc:odbc:eshopODBC");  // Access
      Statement stmt = conn.createStatement();
 
      String sqlStr = "SELECT * FROM carica ";  
 
      // for debugging
      System.out.println("Query statement is " + sqlStr);
      ResultSet rset = stmt.executeQuery(sqlStr);
  %>
      <hr>
      <form method="get" action="order.jsp">
        <table border=1 cellpadding=5>
          <tr>
            <th>id_carica</th>
            <th>descr</th>
            <th>durata</th>
          </tr>
  <%
      while (rset.next()) {
        int id = rset.getInt("id_carica");
  %>
          <tr>
            <td><input type="checkbox" name="id" value="<%= id %>"></td>
            <td><%= rset.getString("descr") %></td>
            <td><%= rset.getString("durata") %></td>
          </tr>
  <%
      }
  %>
        </table>
        <br>
        <input type="submit" value="submit">
        <input type="reset" value="Clear">
      </form>
  <%
      rset.close();
      stmt.close();
      conn.close();
   
  %>