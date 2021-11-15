package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Costumer;

public interface CostumerDao {
	
	void insert(Costumer costumer);
	void update(Costumer costumer);
	void deleteById(Integer id);
	void deleteAll(Date date);
	Costumer findById(Integer id);
	Costumer findByExternalId(String idExterno);
	List<Costumer> findAll();

}
