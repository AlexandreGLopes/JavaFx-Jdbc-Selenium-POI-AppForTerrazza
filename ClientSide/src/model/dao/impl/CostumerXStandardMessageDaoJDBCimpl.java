package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.DB;
import db.DbException;
import model.dao.CostumerXStandardMessageDao;
import model.entities.CostumerXStandardMessage;

public class CostumerXStandardMessageDaoJDBCimpl implements CostumerXStandardMessageDao {
	
	private Logger logger = LogManager.getLogger(CostumerXStandardMessageDaoJDBCimpl.class);

	private Connection conn;

	public CostumerXStandardMessageDaoJDBCimpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(CostumerXStandardMessage costumerXmessage) {
		PreparedStatement st = null;
		try {
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("INSERT INTO costumer_x_standardmessage "
					+ "(FK_idcostumer, FK_idstandardmessage) " + "VALUES (?, ?)");
			st.setInt(1, costumerXmessage.getIdCostumer());
			st.setInt(2, costumerXmessage.getIdStandardMessage());

			st.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public CostumerXStandardMessage findByBothFK(Integer idCostumer, Integer idMessage) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM costumer_x_standardmessage "
					+ "WHERE FK_idcostumer = ? AND FK_idstandardmessage = ?");
			st.setInt(1, idCostumer);
			st.setInt(2, idMessage);
			rs = st.executeQuery();
			if (rs.next()) {
				CostumerXStandardMessage costumerXmessage = new CostumerXStandardMessage();
				costumerXmessage.setIdCostumer(rs.getInt("FK_idcostumer"));
				costumerXmessage.setIdStandardMessage(rs.getInt("FK_idstandardmessage"));
				return costumerXmessage;
			}
			return null;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
