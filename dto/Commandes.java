package dto;

import java.sql.Date;
import java.util.ArrayList;

public class Commandes {
	private int id;
	

	private int idUser;
	private Date date;
	private ArrayList<Pizzas> pizzas = new ArrayList<Pizzas>();
	
	public Commandes() {
		
	}
	
	public Commandes(int id,int idUser, Date date, ArrayList<Pizzas> pizzas) {
		this.id = id;
		this.idUser = idUser;
		this.date = date;
		this.pizzas = pizzas;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ArrayList<Pizzas> getPizzas() {
		return pizzas;
	}
	public void setPizzas(ArrayList<Pizzas> pizzas) {
		this.pizzas = pizzas;
	}
	
	public double getTotalPrice() {
		double res = 0.0;
		for(int i = 0; i<pizzas.size(); i++) {
			res = res + pizzas.get(i).getTotalPrice();
		}
		return res;
	}

	
}
