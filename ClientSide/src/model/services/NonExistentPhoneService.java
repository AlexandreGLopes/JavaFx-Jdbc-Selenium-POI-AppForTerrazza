package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.NonExistentPhoneDao;
import model.entities.NonExistentPhone;

public class NonExistentPhoneService {
	
	private NonExistentPhoneDao dao = DaoFactory.createNonExistentPhoneDao();
	
	public void insertNonExistentPhone(Integer fk_idcostumer) {
		dao.insert(fk_idcostumer);
	}
	
	public List<NonExistentPhone> findAllTodayNonExistentPhones() {
		return dao.findAllOfCurrentDate();
	}

}
