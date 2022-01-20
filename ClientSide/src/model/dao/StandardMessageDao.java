package model.dao;

import java.util.List;

import model.entities.StandardMessage;

public interface StandardMessageDao {
	
	void insert(StandardMessage standardMessage);
	void update(StandardMessage standardMessage);
	void updateToDefault(StandardMessage standardMessage);
	StandardMessage findById(Integer id);
	StandardMessage findByTitle(String title);
	List<StandardMessage> findAll();

}
