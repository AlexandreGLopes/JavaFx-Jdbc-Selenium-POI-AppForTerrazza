package model.dao;

import model.entities.CostumerXStandardMessage;

public interface CostumerXStandardMessageDao {
	
	public void insert(CostumerXStandardMessage costumerXmessage);
	public CostumerXStandardMessage findByBothFK(Integer idCostumer, Integer idMessage);

}
