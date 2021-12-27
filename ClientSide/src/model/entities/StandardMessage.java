package model.entities;

import java.util.Objects;

public class StandardMessage {
	
	private Integer id;
	private String titulo;
	private String mensagem;
	private String mensagemPadrao;
	
	public StandardMessage() {
	}
	
	public StandardMessage(Integer id, String titulo, String mensagem, String mensagemPadrao) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.mensagem = mensagem;
		this.mensagemPadrao = mensagemPadrao;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getMensagemPadrao() {
		return mensagemPadrao;
	}
	public void setMensagemPadrao(String mensagemPadrao) {
		this.mensagemPadrao = mensagemPadrao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mensagem, mensagemPadrao, titulo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StandardMessage other = (StandardMessage) obj;
		return Objects.equals(mensagem, other.mensagem) && Objects.equals(mensagemPadrao, other.mensagemPadrao)
				&& Objects.equals(titulo, other.titulo);
	}

	@Override
	public String toString() {
		return "StandardMessage [id=" + id + ", titulo=" + titulo + ", mensagem=" + mensagem + ", mensagemPadrao="
				+ mensagemPadrao + "]";
	}
	
}
