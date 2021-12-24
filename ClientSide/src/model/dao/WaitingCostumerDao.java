package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.WaitingCostumer;

public interface WaitingCostumerDao {
	
	void insert(WaitingCostumer waitingCostumer);
	void update(WaitingCostumer waitingCostumer);
	WaitingCostumer findById(Integer id);
	List<WaitingCostumer> findAllofSpecificDate(Date date);

}
