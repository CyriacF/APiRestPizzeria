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

import dao.CommandesDAO;
import dto.Commandes;



@WebServlet("/commandes/*")
public class CommandesRestAPI extends HttpServlet {
	
	CommandesDAO pizzadao = new CommandesDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		String path = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		if(path == null || path.equals("/")) {
			out.println(mapper.writeValueAsString(CommandesDAO.findAll()));
			return;
		}
		String[] parts = path.split("/");
		Commandes asked = CommandesDAO.find(Integer.parseInt(parts[1]));
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
				String path = req.getPathInfo();
				if(path == null || path.equals("/")) {
					StringBuilder data = new StringBuilder();
					BufferedReader reader = req.getReader();
					String line;
					while((line = reader.readLine()) != null) {
						data.append(line);
					}
					String stringData = data.toString();
					PrintWriter out = resp.getWriter();
					ObjectMapper mapper = new ObjectMapper();
					Commandes commandes = mapper.readValue(stringData, Commandes.class);
					if(!pizzadao.save(commandes)) {
						resp.sendError(409);
						return;
					}
					out.println(mapper.writeValueAsString(commandes));
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
}