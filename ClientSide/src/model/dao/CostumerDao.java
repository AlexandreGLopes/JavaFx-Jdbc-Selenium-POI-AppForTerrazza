package model.dao;

import java.util.List;

import model.entities.Costumer;

public interface CostumerDao {
	
	void insert(Costumer costumer);
	void updateByExternalIdExceptNoshowAndSited(Costumer costumer);
	void deleteById(Integer id);
	void deleteOlderThan30Days();
	Costumer findById(Integer id);
	Costumer findByExternalId(String idExterno);
	List<Costumer> findAll();
	List<Costumer> findTodayCostumers();
	List<Costumer> findTodayCostumersByName();
	List<Costumer> findTodayCostumersByNameExceptCancelled();
	List<Costumer> findTodayCostumersByTelephoneExceptCancelled();
	List<Costumer> findTodayCostumersByEmailExceptCancelled();
	void updateTableStatusObsAndWaiting(Costumer costumer);

}
