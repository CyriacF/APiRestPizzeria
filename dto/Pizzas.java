package dto;

import java.util.ArrayList;

public class Pizzas {
	private int id;
	private String name;
	private String typePate;
	private double price;
	private ArrayList<Ingredients> ingredients = new ArrayList<Ingredients>();
	private double totalPrice;
	
	public Pizzas() {
		
	}

	public Pizzas(int id, String name, String typePate, double price, ArrayList<Ingredients> ingredients) {
		this.name = name;
		this.typePate = typePate;
		this.price = price;
		this.ingredients = ingredients;
		this.totalPrice = getTotalPrice();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypePate() {
		return typePate;
	}

	public void setTypePate(String typePate) {
		this.typePate = typePate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ArrayList<Ingredients> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredients> ingredients) {
		this.ingredients = ingredients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public double getTotalPrice() {
		double res = 0.0;
		for(int i = 0; i<ingredients.size(); i++) {
			res = res + ingredients.get(i).getPrice();
		}
		res = res + getPrice();
		return res;
	}
	
}
