package model.entities;

import java.util.Date;
import java.util.Objects;

public class NonExistentPhone {
	
	private Integer id;
	private Integer idCostumer;
	private Date data;
	
	public NonExistentPhone() {
	}

	public NonExistentPhone(Integer id, Integer idCostumer, Date data) {
		super();
		this.id = id;
		this.idCostumer = idCostumer;
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdCostumer() {
		return idCostumer;
	}

	public void setIdCostumer(Integer idCostumer) {
		this.idCostumer = idCostumer;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idCostumer);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonExistentPhone other = (NonExistentPhone) obj;
		return Objects.equals(idCostumer, other.idCostumer);
	}

	@Override
	public String toString() {
		return "NonExistentPhones [id=" + id + ", idCostumer=" + idCostumer + ", data=" + data + "]";
	}
	
}
