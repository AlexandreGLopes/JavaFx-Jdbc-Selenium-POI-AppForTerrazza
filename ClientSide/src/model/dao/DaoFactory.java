package model.dao;

import db.DB;
import model.dao.impl.CostumerDaoImplJDBC;

public class DaoFactory {
	
	//O método expõe o tipo da interface CostumerDao mas retorna intanciada o tipo da implementação CostumerDaoImplJDBC
	//macete para não precisar expor a implementação e deixar só a interface
	//pois no programa principal vamos chamar apenas a interface e vamos fazer ela receber o método do DaoFactory
	public static CostumerDao createCostumerDao() {
		return new CostumerDaoImplJDBC(DB.getConnection());
	}

}
