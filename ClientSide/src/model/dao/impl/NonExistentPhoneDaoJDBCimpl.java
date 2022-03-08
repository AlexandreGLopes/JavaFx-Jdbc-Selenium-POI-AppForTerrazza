package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.DB;
import db.DbException;
import model.dao.CostumerXStandardMessageDao;
import model.dao.NonExistentPhoneDao;
import model.entities.Costumer;
import model.entities.CostumerXStandardMessage;
import model.entities.NonExistentPhone;

public class NonExistentPhoneDaoJDBCimpl implements NonExistentPhoneDao {

	private Logger logger = LogManager.getLogger(NonExistentPhoneDaoJDBCimpl.class);

	private Connection conn;

	public NonExistentPhoneDaoJDBCimpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Integer fk_idcostumer) {
		PreparedStatement st = null;
		try {
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("INSERT INTO skycuritibacostumers.telefonesinexistentes "
					+ "(FK_idcostumer, Data) " + "VALUES (?, CURDATE())");
			st.setInt(1, fk_idcostumer);

			st.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<NonExistentPhone> findAllOfCurrentDate() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM skycuritibacostumers.telefonesinexistentes "
					+ "WHERE DATE(Data) = CURDATE()");
			
			rs = st.executeQuery();

			List<NonExistentPhone> list = new ArrayList<>();
			while (rs.next()) {
				NonExistentPhone nonExistentPhone = instantiateNonExistentPhone(rs);
				list.add(nonExistentPhone);
			}
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private NonExistentPhone instantiateNonExistentPhone(ResultSet rs) throws SQLException {
		NonExistentPhone nonExistentPhone = new NonExistentPhone();
		nonExistentPhone.setId(rs.getInt("Id"));
		nonExistentPhone.setIdCostumer(rs.getInt("FK_idcostumer"));
		nonExistentPhone.setData(rs.getDate("Data"));
		return nonExistentPhone;
	}

}
