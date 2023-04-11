package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DS;
import dao.JwtManager;
import dao.PizzaDAO;
import dto.Ingredients;
import dto.Pizzas;
import io.jsonwebtoken.Claims;


@WebServlet("/users/*")
public class UsersRestAPI extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PrintWriter out = resp.getWriter();
			ObjectMapper mapper = new ObjectMapper();
			String path = req.getPathInfo();
			if(path == null || path.equals("/")) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}else {
				String[] parts = path.split("/");
				if(parts[1].equals("token")) {
					StringBuilder data = new StringBuilder();
					BufferedReader reader = req.getReader();
					String line;
					while((line = reader.readLine()) != null) {
						data.append(line);
					}
					String stringData = data.toString();
					try(Connection con = new DS().getConnection()) {
						Statement statement = con.createStatement();
						String post = data.toString();
						String[] id = post.split(":");
						String name = id[0];
						String pwd = id[1];
						String query = "select * from pizza_users where name='"+name + "' AND password='" + pwd + "';";
						ResultSet rs = statement.executeQuery(query);
						if (rs.next()) {
							out.println(JwtManager.createJWT(name));
							return;
						}
						else {
							resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
							return;
						}
					}
					catch (Exception e){
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
					return;
				}else {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		String path = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		if(path == null || path.equals("/")) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String[] parts = path.split("/");
		if(parts[1].equals("token")) {
			// TODO : vérif les users mdp en BDD
			/*
			try(Connection con = new DS().getConnection()) {
				Statement statement = con.createStatement();
				String query = "select * from pizza_users where login=" + ;
				ResultSet rs = statement.executeQuery(query);
				while(rs.next()) {
					Ingredients ing = new Ingredients();
					ing.setId(rs.getInt("id"));
					ing.setName(rs.getString("name"));
					ing.setPrice(rs.getDouble("price"));
					list.add(ing);
				}
			}
			catch (Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			*/
			out.println(JwtManager.createJWT("loic"));
			return;
		}
		else {
			String authTokenHeader = req.getHeader("Authorization");
			String token = authTokenHeader.substring("Bearer".length()).trim();
			try {
				Claims claims = JwtManager.decodeJWT(token);
				System.out.println("CA MARCHE OMG");
			}catch (Exception e) {
				System.out.println("Token cassé");
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

}