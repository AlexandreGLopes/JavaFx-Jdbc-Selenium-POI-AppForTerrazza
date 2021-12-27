package model.services;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.WaitingCostumerDao;
import model.entities.WaitingCostumer;

public class WaitingCostumerService {
	
	//Já fizemos a dependência da DB e injetamos a dependência usando o padrão Factory
	private WaitingCostumerDao dao = DaoFactory.createWaitingCostumerDao();
	
	public List<WaitingCostumer> findAllofDatePickerDate(Date date) {
		return dao.findAllofSpecificDate(date);
	}
	
	public void saveOrUpdate(WaitingCostumer obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else if (obj.getSituacao() != "Sentado") {
			dao.updateWithoutSitting(obj);
		}
		else {
			dao.updateWhenSitting(obj);
		}
	}
}