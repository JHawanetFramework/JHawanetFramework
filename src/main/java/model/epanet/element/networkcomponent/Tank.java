package model.epanet.element.networkcomponent;

import model.epanet.element.systemoperation.Curve;

public final class Tank extends Node {
	private double elev;
	private double initLvl;
	private double minLvl;
	private double maxLvl;
	private double diameter;
	private double minVol;
	private Curve volCurve;

	public Tank() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a new tank copy the values of tank received. This is a shallow copy,
	 * i.e., If the field value is a reference to an object (e.g., a memory address)
	 * it copies the reference. If it is necessary for the object to be completely
	 * independent of the original you must ensure that you replace the reference to
	 * the contained objects.
	 * 
	 * @param tank the tank to copy
	 */
	public Tank(Tank tank) {
		super(tank);
		this.elev = tank.elev;
		this.initLvl = tank.initLvl;
		this.minLvl = tank.minLvl;
		this.maxLvl = tank.maxLvl;
		this.diameter = tank.diameter;
		this.minVol = tank.minVol;
		this.volCurve = tank.volCurve;
	}

	/**
	 * @return the elev
	 */
	public double getElev() {
		return elev;
	}

	/**
	 * @param elev the elev to set
	 */
	public void setElev(double elev) {
		this.elev = elev;
	}

	/**
	 * @return the initLvl
	 */
	public double getInitLvl() {
		return initLvl;
	}

	/**
	 * @param initLvl the initLvl to set
	 */
	public void setInitLvl(double initLvl) {
		this.initLvl = initLvl;
	}

	/**
	 * @return the minLvl
	 */
	public double getMinLvl() {
		return minLvl;
	}

	/**
	 * @param minLvl the minLvl to set
	 */
	public void setMinLvl(double minLvl) {
		this.minLvl = minLvl;
	}

	/**
	 * @return the maxLvl
	 */
	public double getMaxLvl() {
		return maxLvl;
	}

	/**
	 * @param maxLvl the maxLvl to set
	 */
	public void setMaxLvl(double maxLvl) {
		this.maxLvl = maxLvl;
	}

	/**
	 * @return the diam
	 */
	public double getDiameter() {
		return diameter;
	}

	/**
	 * @param diam the diam to set
	 */
	public void setDiameter(double diam) {
		this.diameter = diam;
	}

	/**
	 * @return the minVol
	 */
	public double getMinVol() {
		return minVol;
	}

	/**
	 * @param minVol the minVol to set
	 */
	public void setMinVol(double minVol) {
		this.minVol = minVol;
	}

	/**
	 * @return the volCurve
	 */
	public Curve getVolCurve() {
		return volCurve;
	}

	/**
	 * @param volCurve the volCurve to set
	 */
	public void setVolCurve(Curve volCurve) {
		this.volCurve = volCurve;
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		txt.append(String.format("%-10s\t", getId()));
		txt.append(String.format("%-10f\t", getElev()));
		txt.append(String.format("%-10f\t", getInitLvl()));
		txt.append(String.format("%-10f\t", getMinLvl()));
		txt.append(String.format("%-10f\t", getMaxLvl()));
		txt.append(String.format("%-10f\t", getDiameter()));
		txt.append(String.format("%-10f\t", getMinVol()));
		if (getVolCurve() != null) {
			txt.append(String.format("%-10s", getVolCurve().getId()));
		}
		String description = getDescription();
		if (description != null) {
			txt.append(String.format(";%s", description));
		}
		return txt.toString();
	}

	/**
	 * Copy this object realizing a shallow copy.
	 */
	@Override
	public Tank copy() {
		return new Tank(this);
	}
}
