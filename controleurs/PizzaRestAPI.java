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
import dao.PizzaDAO;
import dto.Ingredients;
import dto.Pizzas;


@WebServlet("/pizzas/*")
public class PizzaRestAPI extends HttpServlet {
	
	PizzaDAO pizzadao = new PizzaDAO();
	
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
        	doPatch(req, res);
        } else {
        	super.service(req, res);
        }
    }
    
    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    	if(JwtManager.autorizedToken(req.getHeader("Authorization"))) {
			try {
				String path = req.getPathInfo();
				String[] parts = path.split("/");
				if(path == null || path.equals("/")) {
					res.sendError(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				Pizzas asked = PizzaDAO.find(Integer.parseInt(parts[1]));
				if(asked != null) {
					StringBuilder data = new StringBuilder();
					BufferedReader reader = req.getReader();
					String line;
					while((line = reader.readLine()) != null) {
						data.append(line);
					}
					String stringData = data.toString();
					PrintWriter out = res.getWriter();
					ObjectMapper mapper = new ObjectMapper();
					Pizzas pizz = mapper.readValue(stringData, Pizzas.class);
					if(!pizzadao.update(pizz, asked.getId())) {
						res.sendError(409);
						return;
					}
					out.println(mapper.writeValueAsString(PizzaDAO.find(asked.getId())));
				}
				else {
					res.sendError(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else{
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
    }
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		String path = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		if(path == null || path.equals("/")) {
			out.println(mapper.writeValueAsString(PizzaDAO.findAll()));
			return;
		}
		String[] parts = path.split("/");
		Pizzas asked = PizzaDAO.find(Integer.parseInt(parts[1]));
		if(asked == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if(parts.length == 3) {
			if(parts[2] != null && parts[2].equals("prixfinal")) {
				out.println(asked.getTotalPrice());
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
				PrintWriter out = resp.getWriter();
				ObjectMapper mapper = new ObjectMapper();
				String path = req.getPathInfo();
				if(path == null || path.equals("/")) {
					StringBuilder data = new StringBuilder();
					BufferedReader reader = req.getReader();
					String line;
					while((line = reader.readLine()) != null) {
						data.append(line);
					}
					String stringData = data.toString();
					Pizzas pizz = mapper.readValue(stringData, Pizzas.class);
					if(!pizzadao.save(pizz)) {
						resp.sendError(409);
						return;
					}
					out.println(mapper.writeValueAsString(pizz));
				}else {
					String[] parts = path.split("/");
					Pizzas asked = PizzaDAO.find(Integer.parseInt(parts[1]));
					if(asked == null) {
						resp.sendError(HttpServletResponse.SC_NOT_FOUND);
						return;
					}else {
						StringBuilder data = new StringBuilder();
						BufferedReader reader = req.getReader();
						String line;
						while((line = reader.readLine()) != null) {
							data.append(line);
						}
						String stringData = data.toString();
						Ingredients ing = mapper.readValue(stringData, Ingredients.class);
						pizzadao.addIngredients(asked.getId(), ing);
					}
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
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
				Pizzas asked = pizzadao.find(Integer.parseInt(parts[1]));
				if(asked == null) {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
				if(parts.length == 3) {
					if(parts[2] != null && checkInteger(parts[2])) {
						pizzadao.deleteIngredients(asked.getId(), Integer.parseInt(parts[2]));
						out.println(mapper.writeValueAsString(pizzadao.find(Integer.parseInt(parts[1]))));
					}
				}
				else {
					if(!pizzadao.delete(Integer.parseInt(parts[1]))) {
						resp.sendError(404);
						return;
					}else {
						out.println(mapper.writeValueAsString(asked));
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else{
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	private boolean checkInteger(String test) {
		try {
		    Integer.parseInt(test);
		    return true;
		} catch (NumberFormatException e) {
		    return false;
		}
	}
	

}