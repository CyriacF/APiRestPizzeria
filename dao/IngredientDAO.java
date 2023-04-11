package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredients;

public class IngredientDAO {
	
	public static Ingredients find(int id) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "select * from ingredients where id="+id;
			ResultSet rs = statement.executeQuery(query);
			Ingredients ing = new Ingredients();
			rs.next();
			ing.setId(rs.getInt("id"));
			ing.setName(rs.getString("name"));
			ing.setPrice(rs.getDouble("price"));
			return ing;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return new Ingredients();
	}
	
	public static List<Ingredients> findAll() {
		ArrayList<Ingredients> list = new ArrayList<>();
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "select * from ingredients";
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
		return list;
	}
	
	public boolean save(Ingredients ing) {
		try(Connection con = new DS().getConnection()) {
			Statement statement = con.createStatement();
			String query = "insert into ingredients values (" + ing.getId() + ", '" + ing.getName() + "', " + ing.getPrice() + ")";
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
			if(find(id).getName() == null){
				return false;
			}
			String query = "delete from ingredients where id =" + id;
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

}
