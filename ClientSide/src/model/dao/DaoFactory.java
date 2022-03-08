package model.dao;

import db.DB;
import model.dao.impl.CostumerDaoImplJDBC;
import model.dao.impl.CostumerXStandardMessageDaoJDBCimpl;
import model.dao.impl.NonExistentPhoneDaoJDBCimpl;
import model.dao.impl.StandardMessageDaoJDBCImpl;
import model.dao.impl.WaitingCostumerDaoJDBCImpl;

public class DaoFactory {
	
	//O método expõe o tipo da interface CostumerDao mas retorna intanciada o tipo da implementação CostumerDaoImplJDBC
	//macete para não precisar expor a implementação e deixar só a interface
	//pois no programa principal vamos chamar apenas a interface e vamos fazer ela receber o método do DaoFactory
	public static CostumerDao createCostumerDao() {
		return new CostumerDaoImplJDBC(DB.getConnection());
	}
	
	public static WaitingCostumerDao createWaitingCostumerDao() {
		return new WaitingCostumerDaoJDBCImpl(DB.getConnection());
	}
	
	public static StandardMessageDao createStandardMessageDao() {
		return new StandardMessageDaoJDBCImpl(DB.getConnection());
	}
	
	public static CostumerXStandardMessageDao createCostumerXStandardMessageDao() {
		return new CostumerXStandardMessageDaoJDBCimpl(DB.getConnection());
	}
	
	public static NonExistentPhoneDao createNonExistentPhoneDao() {
		return new NonExistentPhoneDaoJDBCimpl(DB.getConnection());
	}

}
