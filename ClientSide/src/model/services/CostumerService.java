package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.CostumerDao;
import model.dao.DaoFactory;
import model.entities.Costumer;

public class CostumerService {
	
	//Já fizemos a dependência da DB e injetamos a dependência usando o padrão Factory
	private CostumerDao dao = DaoFactory.createCostumerDao();
	
	public void insertIfExteralIdNotExists(Costumer costumer) {
		Costumer costumerSelected = dao.findByExternalId(costumer.getIdExterno());
		if (costumerSelected == null) {
			dao.insert(costumer);
		}
		else {
			System.out.println("Costumer is already inserted");
		}
	}
	
	public List<Costumer> findAll() {
		return dao.findAll();
	}

}
