package model.services;

import java.util.List;

import model.dao.CostumerDao;
import model.dao.DaoFactory;
import model.entities.Costumer;

public class CostumerService {
	
	//Já fizemos a dependência da DB e injetamos a dependência usando o padrão Factory
	private CostumerDao dao = DaoFactory.createCostumerDao();
	
	public void insertIfExternalIdNotExists(Costumer costumer) {
		Costumer costumerSelected = dao.findByExternalId(costumer.getIdExterno());
		if (costumerSelected == null) {
			dao.insert(costumer);
		}
		else {
			if (!costumer.equals(costumerSelected)) {
				dao.update(costumer);
			}
		}
	}
	
	public List<Costumer> findAll() {
		return dao.findAll();
	}
	
	public List<Costumer> findAllofCurrentDate() {
		return dao.findTodayCostumers();
	}
	
	public List<Costumer> findAllofCurrentDateOrderByName() {
		return dao.findTodayCostumersByName();
	}
	
	public List<Costumer> findAllofCurrentDateOrderByNameExceptCancelled() {
		return dao.findTodayCostumersByNameExceptCancelled();
	}
	
	public List<Costumer> findAllofCurrentDateOrderByTelephoneExceptCancelled() {
		return dao.findTodayCostumersByTelephoneExceptCancelled();
	}
	
	public List<Costumer> findAllofCurrentDateOrderByEmailExceptCancelled() {
		return dao.findTodayCostumersByEmailExceptCancelled();
	}
}
