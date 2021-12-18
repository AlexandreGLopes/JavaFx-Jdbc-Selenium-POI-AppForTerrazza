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
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("INSERT INTO terrazzacostumers "
					+ "(Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno) "
					+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, costumer.getNome());
			st.setString(2, costumer.getSobrenome());
			st.setString(3, costumer.getTelefone());
			st.setString(4, costumer.getEmail());
			st.setString(5, costumer.getSalao());
			st.setInt(6, costumer.getPessoas());
			st.setDate(7, new java.sql.Date(costumer.getData().getTime()));
			st.setTime(8, new java.sql.Time(costumer.getHora().getTime()));
			st.setString(9, costumer.getMesa());
			st.setString(10, costumer.getSituacao());
			st.setString(11, costumer.getObservacao());
			st.setDouble(12, costumer.getPagamento());
			st.setString(13, costumer.getIdExterno());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				// Se o rs receber uma generatedKey
				if (rs.next()) {
					int id = rs.getInt(1);
					costumer.setId(id);
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
	public void update(Costumer costumer) {
		PreparedStatement st = null;
		try {
			// SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			st = conn.prepareStatement("UPDATE terrazzacostumers "
					+ "SET Nome = ?, Sobrenome = ?, Telefone = ?, Email = ?, Salao = ?, Pessoas = ?, Data = ?, Hora = ?, Mesa = ?, Situacao = ?, Observacao = ?, Pagamento = ? "
					+ "WHERE IdExterno = ?");

			st.setString(1, costumer.getNome());
			st.setString(2, costumer.getSobrenome());
			st.setString(3, costumer.getTelefone());
			st.setString(4, costumer.getEmail());
			st.setString(5, costumer.getSalao());
			st.setInt(6, costumer.getPessoas());
			st.setDate(7, new java.sql.Date(costumer.getData().getTime()));
			st.setTime(8, new java.sql.Time(costumer.getHora().getTime()));
			st.setString(9, costumer.getMesa());
			st.setString(10, costumer.getSituacao());
			st.setString(11, costumer.getObservacao());
			st.setDouble(12, costumer.getPagamento());
			st.setString(13, costumer.getIdExterno());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM skycuritibacostumers.terrazzacostumers WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	// Este método é muito perigoso. Vamos utilizar ele só até fazer uma outra
	// implementação com um loop mais seguro pela escolha do usuário
	// Este programa não depende que o banco de dados fqie salvo na memória. Por
	// enquanto não vamos utilizar nenhuma ferramenta de análise de dados mais
	// profunda
	// Se isso passar a ser necessário será também necessário fazer uma
	// implementação diferente disso ou retirá-lo completamente
	@Override
	public void deleteOlderThan30Days() {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"DELETE FROM skycuritibacostumers.terrazzacostumers WHERE Data < now() - interval 30 DAY;");

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
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
		costumer.setObservacao(rs.getString("Observacao"));
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
			st = conn.prepareStatement("SELECT * FROM skycuritibacostumers.terrazzacostumers " + "WHERE IdExterno = ?");
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

	@Override
	public List<Costumer> findTodayCostumers() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno "
							+ "FROM skycuritibacostumers.terrazzacostumers " + "WHERE DATE(Data) = CURDATE() ORDER BY Hora");

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
	public List<Costumer> findTodayCostumersByName() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno "
							+ "FROM skycuritibacostumers.terrazzacostumers " + "WHERE DATE(Data) = CURDATE() ORDER BY Nome");

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
	public List<Costumer> findTodayCostumersByNameExceptCancelled() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno "
							+ "FROM skycuritibacostumers.terrazzacostumers "
							+ "WHERE DATE(Data) = CURDATE() AND Situacao != \"Cancelado pelo cliente\" AND Situacao != \"Cancelado por solicitação do cliente\" "
							+ "AND Situacao != \"Cancelado por no-show do cliente\" AND Situacao != \"Cancelado por erro de cadastro\" "
							+ "ORDER BY Nome");

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
	public List<Costumer> findTodayCostumersByTelephoneExceptCancelled() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno "
							+ "FROM skycuritibacostumers.terrazzacostumers "
							+ "WHERE DATE(Data) = CURDATE() AND Situacao != \"Cancelado pelo cliente\" AND Situacao != \"Cancelado por solicitação do cliente\" "
							+ "AND Situacao != \"Cancelado por no-show do cliente\" AND Situacao != \"Cancelado por erro de cadastro\" "
							+ "ORDER BY Telefone");

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
	public List<Costumer> findTodayCostumersByEmailExceptCancelled() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno "
							+ "FROM skycuritibacostumers.terrazzacostumers "
							+ "WHERE DATE(Data) = CURDATE() AND Situacao != \"Cancelado pelo cliente\" AND Situacao != \"Cancelado por solicitação do cliente\" "
							+ "AND Situacao != \"Cancelado por no-show do cliente\" AND Situacao != \"Cancelado por erro de cadastro\" "
							+ "ORDER BY Email");

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

}
