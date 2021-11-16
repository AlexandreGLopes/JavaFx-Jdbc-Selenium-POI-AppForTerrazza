package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Costumer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String sobrenome;
	private String telefone;
	private String email;
	private String salao;
	private Integer pessoas;
	private Date data;
	private Date hora;
	private String mesa;
	private String situacao;
	private Double pagamento;
	private String idExterno;
	
	public Costumer() {
	}

	public Costumer(Integer id, String nome, String sobrenome, String telefone, String email, String salao,
			Integer pessoas, Date data, Date hora, String mesa, String situacao, Double pagamento, String idExterno) {
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefone = telefone;
		this.email = email;
		this.salao = salao;
		this.pessoas = pessoas;
		this.data = data;
		this.hora = hora;
		this.mesa = mesa;
		this.situacao = situacao;
		this.pagamento = pagamento;
		this.idExterno = idExterno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalao() {
		return salao;
	}

	public void setSalao(String salao) {
		this.salao = salao;
	}

	public Integer getPessoas() {
		return pessoas;
	}

	public void setPessoas(Integer pessoas) {
		this.pessoas = pessoas;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getMesa() {
		return mesa;
	}

	public void setMesa(String mesa) {
		this.mesa = mesa;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Double getPagamento() {
		return pagamento;
	}

	public void setPagamento(Double pagamento) {
		this.pagamento = pagamento;
	}
	
	public String getIdExterno() {
		return idExterno;
	}

	public void setIdExterno(String idExterno) {
		this.idExterno = idExterno;
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, email, hora, idExterno, mesa, nome, pagamento, pessoas, salao, situacao, sobrenome,
				telefone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Costumer other = (Costumer) obj;
		return Objects.equals(data, other.data) && Objects.equals(email, other.email)
				&& Objects.equals(hora, other.hora) && Objects.equals(idExterno, other.idExterno)
				&& Objects.equals(mesa, other.mesa) && Objects.equals(nome, other.nome)
				&& Objects.equals(pagamento, other.pagamento) && Objects.equals(pessoas, other.pessoas)
				&& Objects.equals(salao, other.salao) && Objects.equals(situacao, other.situacao)
				&& Objects.equals(sobrenome, other.sobrenome) && Objects.equals(telefone, other.telefone);
	}
	
	@Override
	public String toString() {
		return "Costumer [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", telefone=" + telefone
				+ ", email=" + email + ", salao=" + salao + ", pessoas=" + pessoas + ", data=" + data + ", hora=" + hora
				+ ", mesa=" + mesa + ", situacao=" + situacao + ", pagamento=" + pagamento + ", idExterno=" + idExterno
				+ "]";
	}
	
}
