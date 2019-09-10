package com.ssi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BookDetailsServlet
 */
@WebServlet("/BookDetailsServlet")
public class BookDetailsServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Cookie ck[] = request.getCookies();
		int val = 0;
		boolean cookieFound = false;
		for (Cookie c : ck) {
			if (c.getName().equals(request.getParameter("code"))) {
				cookieFound = true;
				c.setValue((Integer.parseInt(c.getValue()) + 1) + "");
				c.setMaxAge(60*60*24*7);
				response.addCookie(c);
				val = Integer.parseInt(c.getValue());
                out.println(val);
			}

		}
		if (!cookieFound) {
			Cookie cookie = new Cookie(request.getParameter("code"), "0");
			cookie.setMaxAge(60*60*24*7);
			response.addCookie(cookie);
		}
		
		String code = request.getParameter("code");
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver Loaded");

			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "rishabh", "abcd1234");
			String sql = "SELECT * from books where bcode=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(code));
			ResultSet rs = ps.executeQuery();
			out.println("<html>");
			out.println("<html><body>");
			out.println("<h3>Book-Details</h3>");
			out.println("<hr>");
			while (rs.next()) {
				String bcode = rs.getString(1);
				String title = rs.getString(2);
				String author = rs.getString(3);
				String subject = rs.getString(4);
				String price = rs.getString(5);
				out.println("<table border=2>");
				out.println("<tr>");
				out.println("<td>BCode</td>");
				out.println("<td>" + bcode + "</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td>Title</td>");
				out.println("<td>" + title + "</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td>Author</td>");
				out.println("<td>" + author + "</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td>Subject</td>");
				out.println("<td>" + subject + "</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td>Price</td>");
				if (val > 5)
					out.println("<td>" + Integer.parseInt(price) * 10 + "</td>");
				else if (val > 10)
					out.println("<td>" + Integer.parseInt(price) * 20 + "</td>");
				else
					out.println("<td>" + Integer.parseInt(price) + "</td>");
				out.println("</tr>");
				out.println("</table>");
			}
			out.println("<hr>");
			out.println("<a href=CartManager?code=" + code + ">Add-To-Cart</a><br>");
			out.println("<a href=SubjectPageServlet>Subject-Page</a><br>");
			out.println("<a href=buyerpage.jsp>Buyer-Page</a><br>");
			out.println("</body></html>");

		} catch (Exception e) {
			out.println(e);
		}
	}
}
