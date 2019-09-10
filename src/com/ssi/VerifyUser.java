package com.ssi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VerifyUser
 */
@WebServlet("/VerifyUser")
public class VerifyUser extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		
		String utype = request.getParameter("utype");
		try {
			if (utype.equals("owner")) {
				if (userid.equals("admin") && password.equals("indore")) {
					response.sendRedirect("adminpage.jsp");
				} else {
					out.println("INVALID CREDENTIALS FOR ADMIN");
				}

			} else {
				
				
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("Driver Loaded");

				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "rishabh","abcd1234");
				String sql = "SELECT userid FROM users where userid=? AND password=?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, userid);
				ps.setString(2, password);
				System.out.println(userid);
				System.out.println(password+"abc");
				ResultSet rs = ps.executeQuery();
				
				if (rs.next()) {

					// whether user want to save the password
					
					String choice = request.getParameter("save");
					if (choice != null) {

						Cookie c1 = new Cookie("id", userid);
						Cookie c2 = new Cookie("pw", password);

						c1.setMaxAge(60 * 60 * 24 * 7);
						c2.setMaxAge(60 * 60 * 24 * 7);

						response.addCookie(c1);
						response.addCookie(c2);

					}
//					System.out.println("here1");
					Cookie[] ck = request.getCookies();
					boolean cookiePresent = false;
					if (ck != null) {

						for (Cookie c : ck) {
							if (c.getName().equals("offer")) {
								cookiePresent = true;

							}

						}

					}
	//				System.out.println("here");
					System.out.println(cookiePresent);
					if (!cookiePresent) {
						String offer = "All";
						Cookie c3 = new Cookie("offer", offer);
						//Cookie c4 = new Cookie("increasePrice", "0");
						c3.setMaxAge(60 * 60 * 24 * 7);
						//c4.setMaxAge(60 * 60 * 24 * 7);
						response.addCookie(c3);
						//response.addCookie(c4);
						System.out.println("inserted cookie once");

					}

					// response.sendRedirect("buyerpage.jsp");
					RequestDispatcher rd = request.getRequestDispatcher("buyerpage.jsp");
					rd.forward(request, response);

				} else {
					out.println("INVALID BUYER CREDENTIALS");
				}
				con.close();
			}
		} catch (Exception e) {
			out.println(e);
		}
	}

}
