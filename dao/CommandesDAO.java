package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Commandes;
import dto.Ingredients;
import dto.Pizzas;

public class CommandesDAO {
	
	public static Commandes find(int id) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "select * from commandes where id="+id;
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()) {
				Commandes commande = new Commandes();
				commande.setIdUser(rs.getInt("id"));
				commande.setDate(rs.getDate("date"));
				commande.setPizzas(stringToPizzas(rs.getString("pizzas")));
				commande.setIdUser(rs.getInt("iduser"));
				return commande;
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Commandes> findAll() {
		ArrayList<Commandes> list = new ArrayList<>();
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "select * from commandes";
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()) {
				Commandes commande = new Commandes();
				commande.setIdUser(rs.getInt("id"));
				commande.setDate(rs.getDate("date"));
				commande.setPizzas(stringToPizzas(rs.getString("pizzas")));
				commande.setIdUser(rs.getInt("iduser"));
				list.add(commande);
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean save(Commandes commande) {
		try(Connection con = new DS().getConnection()) {
			
			Statement statement = con.createStatement();
			String query = "insert into commandes values (" + commande.getId() + ", '" + commande.getDate() + "', '" + 
			pizzasToString(commande.getPizzas()) + "', " + commande.getIdUser()+ ")";
			
			System.out.println(query);
			statement.executeUpdate(query);
			return true;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(int id) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			if(find(id).getIdUser() == -1){
				return false;
			}
			String query = "delete from commandes where id =" + id;
			System.out.println(query);
			statement.executeUpdate(query);
			return true;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Pizzas> stringToPizzas(String pizzasString){
		String[] listId = pizzasString.split(",");
		if(listId.length == 0) {
			return null;
		}
		ArrayList<Pizzas> pizz = new ArrayList<Pizzas>();
		for(int i =0;i < listId.length; i++) {
			new PizzaDAO();
			pizz.add(PizzaDAO.find(Integer.parseInt(listId[i])));
		}
		return pizz;
	}
	
	public static String pizzasToString(ArrayList<Pizzas> pizz) {
		String stringInt = "";
		for (int i = 0; i<pizz.size(); i++) {
			stringInt = stringInt + pizz.get(i).getId() + ",";
		}
		return stringInt;
	}

}
