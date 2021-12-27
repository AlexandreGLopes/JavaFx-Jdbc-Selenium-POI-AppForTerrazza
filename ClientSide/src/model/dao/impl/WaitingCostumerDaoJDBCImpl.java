package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.WaitingCostumerDao;
import model.entities.WaitingCostumer;

public class WaitingCostumerDaoJDBCImpl implements WaitingCostumerDao {
	
	private Connection conn;
	
	public WaitingCostumerDaoJDBCImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(WaitingCostumer waitingCostumer) {
		PreparedStatement st = null;
		try {
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("INSERT INTO waitingcostumers (Nome, Sobrenome, Telefone, Salao, Pessoas, Data, HoraChegada, HoraSentada, Mesa, Situacao, Observacao) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, null, null, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, waitingCostumer.getNome());
			st.setString(2, waitingCostumer.getSobrenome());
			st.setString(3, waitingCostumer.getTelefone());
			st.setString(4, waitingCostumer.getSalao());
			st.setInt(5, waitingCostumer.getPessoas());
			st.setDate(6, new java.sql.Date(waitingCostumer.getData().getTime()));
			st.setTime(7, new java.sql.Time(waitingCostumer.getHoraChegada().getTime()));
			st.setString(8, waitingCostumer.getSituacao());
			st.setString(9, waitingCostumer.getObservacao());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				// Se o rs receber uma generatedKey
				if (rs.next()) {
					int id = rs.getInt(1);
					waitingCostumer.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro inesperado: nenhuma linha foi alterada");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public void updateWithoutSitting(WaitingCostumer waitingCostumer) {
		PreparedStatement st = null;
		try {
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("UPDATE waitingcostumers "
					+ "SET Nome = ?, Sobrenome = ?, Telefone = ?, Salao = ?, Pessoas = ?, Data = ?, HoraChegada = ?, Situacao = ?, Observacao = ?, Aguardando = ? "
					+ "WHERE Id = ?");

			st.setString(1, waitingCostumer.getNome());
			st.setString(2, waitingCostumer.getSobrenome());
			st.setString(3, waitingCostumer.getTelefone());
			st.setString(4, waitingCostumer.getSalao());
			st.setInt(5, waitingCostumer.getPessoas());
			st.setDate(6, new java.sql.Date(waitingCostumer.getData().getTime()));
			st.setTime(7, new java.sql.Time(waitingCostumer.getHoraChegada().getTime()));
			st.setString(8, waitingCostumer.getSituacao());
			st.setString(9, waitingCostumer.getObservacao());
			st.setBoolean(10, waitingCostumer.isAguardando());
			st.setInt(11, waitingCostumer.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}		
	}
	
	@Override
	public void updateWhenSitting(WaitingCostumer waitingCostumer) {
		PreparedStatement st = null;
		try {
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("UPDATE waitingcostumers "
					+ "SET Nome = ?, Sobrenome = ?, Telefone = ?, Salao = ?, Pessoas = ?, Data = ?, HoraChegada = ?, HoraSentada = ?, Mesa = ?, Situacao = ?, Observacao = ?, Aguardando = ? "
					+ "WHERE Id = ?");

			st.setString(1, waitingCostumer.getNome());
			st.setString(2, waitingCostumer.getSobrenome());
			st.setString(3, waitingCostumer.getTelefone());
			st.setString(4, waitingCostumer.getSalao());
			st.setInt(5, waitingCostumer.getPessoas());
			st.setDate(6, new java.sql.Date(waitingCostumer.getData().getTime()));
			st.setTime(7, new java.sql.Time(waitingCostumer.getHoraChegada().getTime()));
			st.setTime(8, new java.sql.Time(waitingCostumer.getHoraSentada().getTime()));
			st.setString(9, waitingCostumer.getMesa());
			st.setString(10, waitingCostumer.getSituacao());
			st.setString(11, waitingCostumer.getObservacao());
			st.setBoolean(12, waitingCostumer.isAguardando());
			st.setInt(13, waitingCostumer.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}		
	}
	
	// Método a ser utilizado por outros métodos para instanciar um WaitingCostumer
	// Vamos economizar linha pois vamos usar esse código várias vezes
	private WaitingCostumer instantiateWaitingCostumer(ResultSet rs) throws SQLException {
		WaitingCostumer waitingCostumer = new WaitingCostumer();
		waitingCostumer.setId(rs.getInt("Id"));
		waitingCostumer.setNome(rs.getString("Nome"));
		waitingCostumer.setSobrenome(rs.getString("Sobrenome"));
		waitingCostumer.setTelefone(rs.getString("Telefone"));
		waitingCostumer.setSalao(rs.getString("Salao"));
		waitingCostumer.setPessoas(rs.getInt("Pessoas"));
		waitingCostumer.setData(rs.getDate("Data"));
		waitingCostumer.setHoraChegada(rs.getTime("HoraChegada"));
		waitingCostumer.setHoraSentada(rs.getTime("HoraSentada"));
		waitingCostumer.setMesa(rs.getString("Mesa"));
		waitingCostumer.setSituacao(rs.getString("Situacao"));
		waitingCostumer.setObservacao(rs.getString("Observacao"));
		waitingCostumer.setAguardando(rs.getBoolean("Aguardando"));
		return waitingCostumer;
	}

	@Override
	public WaitingCostumer findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WaitingCostumer> findAllofSpecificDate(Date date) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM skycuritibacostumers.waitingcostumers "
										+ "WHERE DATE(Data) = ?");
			
			st.setDate(1, new java.sql.Date(date.getTime()));
			rs = st.executeQuery();

			List<WaitingCostumer> list = new ArrayList<>();
			while (rs.next()) {
				WaitingCostumer waitingCostumer = instantiateWaitingCostumer(rs);
				list.add(waitingCostumer);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
