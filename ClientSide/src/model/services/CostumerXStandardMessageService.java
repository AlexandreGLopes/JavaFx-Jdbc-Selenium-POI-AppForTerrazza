package model.services;

import model.dao.CostumerXStandardMessageDao;
import model.dao.DaoFactory;
import model.entities.CostumerXStandardMessage;

public class CostumerXStandardMessageService {
	
	private CostumerXStandardMessageDao dao = DaoFactory.createCostumerXStandardMessageDao();
	
	public void createRelationship(CostumerXStandardMessage costumerXmessage) {
		dao.insert(costumerXmessage);
	}
	
	public CostumerXStandardMessage findIfRelationshipExists(Integer idCostumer, Integer idMessage) {
		return dao.findByBothFK(idCostumer, idMessage);
	}

}
