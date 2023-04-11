package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAO;
import dao.JwtManager;
import dto.Ingredients;


@WebServlet("/ingredients/*")
public class IngredientRestAPI extends HttpServlet {
	
	IngredientDAO ingredientDAO = new IngredientDAO();
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		String path = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		if(path == null || path.equals("/")) {
			out.println(mapper.writeValueAsString(IngredientDAO.findAll()));
			return;
		}
		String[] parts = path.split("/");
		System.out.println(parts.length);
		Ingredients asked = IngredientDAO.find(Integer.parseInt(parts[1]));
		if(asked == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if(parts.length == 3) {
			if(parts[2] != null && parts[2].equals("name")) {
				out.println(asked.getName());
				return;
			}
		}
		out.println(mapper.writeValueAsString(asked));
		return;
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(JwtManager.autorizedToken(req.getHeader("Authorization"))) {
			try {
				StringBuilder data = new StringBuilder();
				BufferedReader reader = req.getReader();
				String line;
				while((line = reader.readLine()) != null) {
					data.append(line);
				}
				String stringData = data.toString();
				PrintWriter out = resp.getWriter();
				ObjectMapper mapper = new ObjectMapper();
				Ingredients ingredient = mapper.readValue(stringData, Ingredients.class);
				if(!ingredientDAO.save(ingredient)) {
					resp.sendError(409);
					return;
				}
				out.println(mapper.writeValueAsString(ingredient));
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(JwtManager.autorizedToken(req.getHeader("Authorization"))) {
			try {
				String path = req.getPathInfo();
				PrintWriter out = resp.getWriter();
				ObjectMapper mapper = new ObjectMapper();
				String[] parts = path.split("/");
				Ingredients asked = IngredientDAO.find(Integer.parseInt(parts[1]));
				if(asked == null) {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
				if(!ingredientDAO.delete(Integer.parseInt(parts[1]))) {
					resp.sendError(404);
					return;
				}
				out.println(mapper.writeValueAsString(asked));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(new HashMap<Integer, Object>().get(24));
	}

}