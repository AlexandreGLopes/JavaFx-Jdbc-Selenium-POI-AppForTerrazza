package model.services;

import model.dao.DaoFactory;
import model.dao.StandardMessageDao;
import model.entities.StandardMessage;

public class StandardMessageService {
	
	private StandardMessageDao dao = DaoFactory.createStandardMessageDao();
	
	public StandardMessage findByTitle(String title) {
		return dao.findByTitle(title);
	}

}
