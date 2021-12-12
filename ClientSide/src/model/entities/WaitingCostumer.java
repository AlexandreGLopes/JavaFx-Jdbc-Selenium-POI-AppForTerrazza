package model.entities;

import java.util.Date;
import java.util.Objects;

public class WaitingCostumer {
	
	private Integer id;
	private String nome;
	private String sobrenome;
	private String telefone;
	private String salao;
	private Integer pessoas;
	private Date data;
	private Date horaChegada;
	private Date horaSentada;
	private String mesa;
	private String situacao;
	private String observacao;
	
	public WaitingCostumer() {
	}

	public WaitingCostumer(Integer id, String nome, String sobrenome, String telefone, String salao, Integer pessoas,
			Date data, Date horaChegada, Date horaSentada, String mesa, String situacao, String observacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefone = telefone;
		this.salao = salao;
		this.pessoas = pessoas;
		this.data = data;
		this.horaChegada = horaChegada;
		this.horaSentada = horaSentada;
		this.mesa = mesa;
		this.situacao = situacao;
		this.observacao = observacao;
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

	public Date getHoraChegada() {
		return horaChegada;
	}

	public void setHoraChegada(Date horaChegada) {
		this.horaChegada = horaChegada;
	}

	public Date getHoraSentada() {
		return horaSentada;
	}

	public void setHoraSentada(Date horaSentada) {
		this.horaSentada = horaSentada;
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public String toString() {
		return "WaitingCostumer [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", telefone=" + telefone
				+ ", salao=" + salao + ", pessoas=" + pessoas + ", data=" + data + ", horaChegada=" + horaChegada
				+ ", horaSentada=" + horaSentada + ", mesa=" + mesa + ", situacao=" + situacao + ", observacao="
				+ observacao + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, horaChegada, horaSentada, mesa, nome, observacao, pessoas, salao, situacao, sobrenome,
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
		WaitingCostumer other = (WaitingCostumer) obj;
		return Objects.equals(data, other.data) && Objects.equals(horaChegada, other.horaChegada)
				&& Objects.equals(horaSentada, other.horaSentada) && Objects.equals(mesa, other.mesa)
				&& Objects.equals(nome, other.nome) && Objects.equals(observacao, other.observacao)
				&& Objects.equals(pessoas, other.pessoas) && Objects.equals(salao, other.salao)
				&& Objects.equals(situacao, other.situacao) && Objects.equals(sobrenome, other.sobrenome)
				&& Objects.equals(telefone, other.telefone);
	}

}
