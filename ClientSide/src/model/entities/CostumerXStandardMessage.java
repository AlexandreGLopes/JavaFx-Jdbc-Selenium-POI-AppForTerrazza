package model.entities;

import java.util.Objects;

public class CostumerXStandardMessage {
	
	private Integer idCostumer;
	private Integer idStandardMessage;
	
	public CostumerXStandardMessage() {
	}

	public CostumerXStandardMessage(Integer idCostumer, Integer idStandardMessage) {
		super();
		this.idCostumer = idCostumer;
		this.idStandardMessage = idStandardMessage;
	}

	public Integer getIdCostumer() {
		return idCostumer;
	}

	public void setIdCostumer(Integer idCostumer) {
		this.idCostumer = idCostumer;
	}

	public Integer getIdStandardMessage() {
		return idStandardMessage;
	}

	public void setIdStandardMessage(Integer idStandardMessage) {
		this.idStandardMessage = idStandardMessage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idCostumer, idStandardMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CostumerXStandardMessage other = (CostumerXStandardMessage) obj;
		return Objects.equals(idCostumer, other.idCostumer)
				&& Objects.equals(idStandardMessage, other.idStandardMessage);
	}

	@Override
	public String toString() {
		return "CostumerXStandardMessage [idCostumer=" + idCostumer + ", idStandardMessage=" + idStandardMessage + "]";
	}
	
}
