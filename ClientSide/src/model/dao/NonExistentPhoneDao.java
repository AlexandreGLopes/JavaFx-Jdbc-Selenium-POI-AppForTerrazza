package model.dao;

import java.util.List;

import model.entities.NonExistentPhone;

public interface NonExistentPhoneDao {
	
	public void insert(Integer fk_idcostumer);
	public List<NonExistentPhone> findAllOfCurrentDate();

}
