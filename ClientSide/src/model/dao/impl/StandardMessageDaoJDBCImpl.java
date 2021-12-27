package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.StandardMessageDao;
import model.entities.StandardMessage;

public class StandardMessageDaoJDBCImpl implements StandardMessageDao {
	
	private Connection conn;
	
	public StandardMessageDaoJDBCImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(StandardMessage standardMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(StandardMessage standardMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateToDefault(StandardMessage standardMessage) {
		// TODO Auto-generated method stub
		
	}
	
	private StandardMessage instantiateStandardMessage(ResultSet rs) throws SQLException {
		StandardMessage standardMessage = new StandardMessage();
		standardMessage.setId(rs.getInt("Id"));
		standardMessage.setTitulo(rs.getString("Titulo"));
		standardMessage.setMensagem(rs.getString("Mensagem"));
		standardMessage.setMensagemPadrao(rs.getString("MensagemPadrao"));
		return standardMessage;
	}

	@Override
	public StandardMessage findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StandardMessage findByTitle(String title) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM skycuritibacostumers.standardmessages "
					+ "WHERE Titulo = ?");
			st.setString(1, title);
			rs = st.executeQuery();
			if (rs.next()) {
				StandardMessage standardMessage = instantiateStandardMessage(rs);
				return standardMessage;
			}
		return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
