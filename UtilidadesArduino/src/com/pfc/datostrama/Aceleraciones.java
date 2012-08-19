package com.pfc.datostrama;

public class Aceleraciones {
	private Double x;
	private Double y;
	private Double z;

	public Aceleraciones() {
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Aceleraciones [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
