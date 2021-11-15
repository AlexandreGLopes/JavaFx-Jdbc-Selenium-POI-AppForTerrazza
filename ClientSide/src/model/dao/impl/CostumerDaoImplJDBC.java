package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CostumerDao;
import model.entities.Costumer;

public class CostumerDaoImplJDBC implements CostumerDao {

	private Connection conn;

	public CostumerDaoImplJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Costumer costumer) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO terrazzacostumers "
					+ "(Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, costumer.getNome());
			st.setString(2, costumer.getSobrenome());
			st.setString(3, costumer.getTelefone());
			st.setString(4, costumer.getEmail());
			st.setString(5, costumer.getSalao());
			st.setInt(6, costumer.getPessoas());
			st.setDate(7, new java.sql.Date(costumer.getData().getTime()));
			st.setDate(8, new java.sql.Date(costumer.getHora().getTime()));
			st.setString(9, costumer.getMesa());
			st.setString(10, costumer.getSituacao());
			st.setDouble(11, costumer.getPagamento());
			st.setString(12, costumer.getIdExterno());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				//Se o rs receber uma generatedKey
				if (rs.next()) {
					int id = rs.getInt(1);
					costumer.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado: nenhuma linha foi alterada");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Costumer costumer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Date date) {
		// TODO Auto-generated method stub

	}

	@Override
	public Costumer findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM skycuritibacostumers.terrazzacostumers " + "WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Costumer costumer = instantiateCostumer(rs);
				return costumer;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Método a ser utilizado por outros métodos para instanciar um Costumer
	// Vamos economizar linha pois vamos usar esse código várias vezes
	private Costumer instantiateCostumer(ResultSet rs) throws SQLException {
		Costumer costumer = new Costumer();
		costumer.setId(rs.getInt("Id"));
		costumer.setNome(rs.getString("Nome"));
		costumer.setSobrenome(rs.getString("Sobrenome"));
		costumer.setTelefone(rs.getString("Telefone"));
		costumer.setEmail(rs.getString("Email"));
		costumer.setSalao(rs.getString("Salao"));
		costumer.setPessoas(rs.getInt("Pessoas"));
		costumer.setData(rs.getDate("Data"));
		costumer.setHora(rs.getTime("Hora"));
		costumer.setMesa(rs.getString("Mesa"));
		costumer.setSituacao(rs.getString("Situacao"));
		costumer.setPagamento(rs.getDouble("Pagamento"));
		costumer.setIdExterno(rs.getString("IdExterno"));
		return costumer;
	}

	@Override
	public List<Costumer> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM skycuritibacostumers.terrazzacostumers");

			rs = st.executeQuery();

			List<Costumer> list = new ArrayList<>();
			while (rs.next()) {
				Costumer costumer = instantiateCostumer(rs);
				list.add(costumer);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Costumer findByExternalId(String idExterno) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
							"SELECT * FROM skycuritibacostumers.terrazzacostumers "
							+ "WHERE Id = ?");
			st.setString(1, idExterno);
			rs = st.executeQuery();
			if (rs.next()) {
				Costumer costumer = instantiateCostumer(rs);
				return costumer;
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
