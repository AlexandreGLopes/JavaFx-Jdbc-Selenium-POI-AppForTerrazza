package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Costumer;

public interface CostumerDao {
	
	void insert(Costumer obj);
	void update(Costumer obj);
	void deleteById(Integer id);
	void deleteAll(Date date);
	Costumer findById(Integer id);
	List<Costumer> findAll();

}
