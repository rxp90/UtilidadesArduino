package com.pfc.datostrama;

public class DatosXBee {
	private Integer direccion;
	private Integer idRed;
	private Integer canal;

	public DatosXBee() {
		// TODO Auto-generated constructor stub
	}

	public Integer getDireccion() {
		return direccion;
	}

	public void setDireccion(Integer direccion) {
		this.direccion = direccion;
	}

	public Integer getIdRed() {
		return idRed;
	}

	public void setIdRed(Integer idRed) {
		this.idRed = idRed;
	}

	public Integer getCanal() {
		return canal;
	}

	public void setCanal(Integer canal) {
		this.canal = canal;
	}

	@Override
	public String toString() {
		return "DatosXBee [direccion=" + direccion + ", idRed=" + idRed
				+ ", canal=" + canal + "]";
	}
	
	
}
