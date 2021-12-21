package model.services;

import model.dao.DaoFactory;
import model.dao.WaitingCostumerDao;

public class WaitingCostumerService {
	
	//Já fizemos a dependência da DB e injetamos a dependência usando o padrão Factory
	private WaitingCostumerDao dao = DaoFactory.createWaitingCostumerDao();

}
