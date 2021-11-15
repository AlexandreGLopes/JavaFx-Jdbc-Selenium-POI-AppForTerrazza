package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Costumer;

public class CostumerService {
	
	public List<Costumer> findAll() {
		List<Costumer> list = new ArrayList<>();
		list.add(new Costumer(1, "Alexandre", "Lopes", "41998642881", "alexandre_lopess@hotmail.com", "Terrazza 40", 2, null, null, "1", "Confirmado", 300.00, "suhduahda"));
		list.add(new Costumer(2, "Alexandre", "Lopes", "41998642881", "alexandre_lopess@hotmail.com", "Terrazza 40", 2, null, null, "1", "Confirmado", 300.00, "suhduahda"));
		list.add(new Costumer(3, "Alexandre", "Lopes", "41998642881", "alexandre_lopess@hotmail.com", "Terrazza 40", 2, null, null, "1", "Confirmado", 300.00, "suhduahda"));
		list.add(new Costumer(4, "Alexandre", "Lopes", "41998642881", "alexandre_lopess@hotmail.com", "Terrazza 40", 2, null, null, "1", "Confirmado", 300.00, "suhduahda"));
		
		return list;
	}

}
