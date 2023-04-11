package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredients;
import dto.Pizzas;

public class PizzaDAO {
	
	public static Pizzas find(int id) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "select * from pizzas where id="+id;
			ResultSet rs = statement.executeQuery(query);
			Pizzas pizz = new Pizzas();
			rs.next();
			pizz.setId(rs.getInt("id"));
			pizz.setName(rs.getString("name"));
			pizz.setTypePate(rs.getString("typePate"));
			pizz.setPrice(rs.getDouble("price"));
			pizz.setIngredients(stringToIngredients(rs.getString("ingredients")));
			return pizz;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Pizzas> findAll() {
		ArrayList<Pizzas> list = new ArrayList<>();
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "select * from pizzas";
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()) {
				Pizzas pizz = new Pizzas();
				pizz.setId(rs.getInt("id"));
				pizz.setName(rs.getString("name"));
				pizz.setTypePate(rs.getString("typePate"));
				pizz.setPrice(rs.getDouble("price"));
				pizz.setIngredients(stringToIngredients(rs.getString("ingredients")));
				list.add(pizz);
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean save(Pizzas pizz) {
		try(Connection con = new DS().getConnection()) {
			
			Statement statement = con.createStatement();
			String query = "insert into pizzas values (" + pizz.getId() + ", '" + pizz.getName() + "', '" + pizz.getTypePate() + "', " + pizz.getPrice() + ", '" + ingredientsToString(pizz.getIngredients()) + "')";
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
	
	public boolean update(Pizzas pizz, int id) {
		try(Connection con = new DS().getConnection()) {
			
			Statement statement = con.createStatement();
			StringBuilder str = new StringBuilder();
			str.append("UPDATE pizzas SET ");
			if(pizz.getName() != null) {
				str.append("name = '" + pizz.getName() + "', ");
			}
			if(pizz.getTypePate() != null) {
				str.append("typePate = '" + pizz.getTypePate() + "', ");
			}
			if(pizz.getPrice() != 0.0) {
				str.append("price = " + pizz.getPrice() + ", ");
			}
			if(pizz.getIngredients().size() != 0) {
				str.append("ingredients = '" + ingredientsToString(pizz.getIngredients()) + "', ");
			}
			str.replace(str.length() - 2, str.length(), "");
			str.append(" WHERE id = " + id + ";");
			System.out.println(str);
			statement.executeUpdate(str.toString());
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
			if(find(id).getName() == null){
				return false;
			}
			String query = "delete from pizzas where id =" + id;
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
	
	public boolean deleteIngredients(int idPizz, int idIngre) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			if(find(idPizz).getName() == null){
				return false;
			}
			String query = "select * from pizzas where id="+idPizz;
			ResultSet rs = statement.executeQuery(query);
			System.out.println(query);
			rs.next();
			ArrayList<Ingredients> listIng = stringToIngredients(rs.getString("ingredients"));
			for(int i = 0; i<listIng.size(); i++) {
				if(idIngre == listIng.get(i).getId()) {
					listIng.remove(i);
					String query2 = "update pizzas set ingredients = '" + ingredientsToString(listIng) + "' WHERE id = " + idPizz;
					System.out.println(query2);
					statement.executeUpdate(query2);
					return true;
				}
			}
			
			return false;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addIngredients(int idPizz, Ingredients idIngre) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			if(find(idPizz).getName() == null){
				return false;
			}
			String query = "select * from pizzas where id="+idPizz;
			ResultSet rs = statement.executeQuery(query);
			System.out.println(query);
			rs.next();
			ArrayList<Ingredients> listIng = stringToIngredients(rs.getString("ingredients"));
			listIng.add(idIngre);
			String query2 = "update pizzas set ingredients = '" + ingredientsToString(listIng) + "' WHERE id = " + idPizz;
			System.out.println(query2);
			statement.executeUpdate(query2);
			return true;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Ingredients> stringToIngredients(String ingredientsString){
		String[] listId = ingredientsString.split(",");
		ArrayList<Ingredients> ingredients = new ArrayList<Ingredients>();
		for(int i = 0; i<listId.length; i++) {
			new IngredientDAO();
			ingredients.add(IngredientDAO.find(Integer.parseInt(listId[i])));
		}
		return ingredients;
	}
	
	public static String ingredientsToString(ArrayList<Ingredients> ing) {
		String stringInt = "";
		for (int i = 0; i<ing.size(); i++) {
			stringInt = stringInt + ing.get(i).getId() + ",";
		}
		return stringInt;
	}

}
