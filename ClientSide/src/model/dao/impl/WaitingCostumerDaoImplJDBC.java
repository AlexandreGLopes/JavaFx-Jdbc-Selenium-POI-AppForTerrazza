package model.dao.impl;

import java.sql.Connection;
import java.util.List;

import model.dao.WaitingCostumerDao;
import model.entities.WaitingCostumer;

public class WaitingCostumerDaoImplJDBC implements WaitingCostumerDao {
	
	private Connection conn;
	
	public WaitingCostumerDaoImplJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(WaitingCostumer waitingCostumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(WaitingCostumer waitingCostumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaitingCostumer findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WaitingCostumer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
