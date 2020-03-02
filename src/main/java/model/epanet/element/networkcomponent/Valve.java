package model.epanet.element.networkcomponent;

import java.util.Objects;

public final class Valve extends Link {
	/**
	 * A enumerator that to define the status of Valve.
	 * 
	 * 
	 */
	public static enum ValveStatus {
		OPEN("OPEN"), CLOSED("CLOSED"), NONE("NONE");

		private String name;

		private ValveStatus(String name) {
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Parse the name to a object of the enum class if exist. if name no exist in enum class so return null;
		 * @param name the name of object
		 * @return the object of enum class or null if no exist
		 */
		public static ValveStatus parse(String name) {
			for (ValveStatus object : ValveStatus.values()) {
				if (object.getName().equalsIgnoreCase(name)) {
					return object;
				}
			}
			return null;
		}

	}

	public static enum ValveType {
		PRV("PRV"), PSV("PSV"), PBV("PSV"), FCV("FCV"), TCV("TCV"), GPV("GPV");

		private String name;

		private ValveType(String name) {
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		public static ValveType parse(String name) {
			for (ValveType object : ValveType.values()) {
				if (object.getName().equalsIgnoreCase(name)) {
					return object;
				}
			}
			throw new IllegalArgumentException("There are not a valid element with the name " + name);
		}
	}

	public static final ValveStatus DEFAULT_STATUS = ValveStatus.NONE;
	public static final ValveType DEFAULT_TYPE = ValveType.PRV;
	public static final double DEFAULT_DIAMETER = 12;
	public static final double DEFAULT_LOSS_COEFFICIENT = 0;

	private double diameter;
	private ValveType type;
	private String setting;
	private double lossCoefficient;
	/*
	 * This variable is saved in [Status] section of inp file only when is OPEN or
	 * CLOSE.
	 */
	private ValveStatus fixedStatus; // the default value

	public Valve() {
		this.diameter = DEFAULT_DIAMETER;
		this.type = DEFAULT_TYPE;
		this.setting = "";
		this.lossCoefficient = DEFAULT_LOSS_COEFFICIENT;
		this.fixedStatus = DEFAULT_STATUS;
	}

	/**
	 * Copy constructor. This is a shallow copy, i.e., If the field value is a
	 * reference to an object (e.g., a memory address) it copies the reference. If
	 * it is necessary for the object to be completely independent of the original
	 * you must ensure that you replace the reference to the contained objects.
	 * 
	 * You must replace node1 and node2 to do the copy independent of the original
	 * 
	 * @param valve
	 */
	public Valve(Valve valve) {
		super(valve);
		this.diameter = valve.diameter;
		this.type = valve.type;
		this.setting = valve.setting;
		this.lossCoefficient = valve.lossCoefficient;
	}

	/**
	 * @return the diameter
	 */
	public double getDiameter() {
		return diameter;
	}

	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	/**
	 * Get the type
	 * 
	 * @return the type
	 */
	public ValveType getType() {
		return type;
	}

	/**
	 * Set the type
	 * 
	 * @param type the type to set
	 * @throws if type is null
	 */
	public void setType(ValveType type) {
		Objects.requireNonNull(type);
		this.type = type;
	}

	/**
	 * Get the setting
	 * 
	 * @return the setting or a empty string if it does not exist
	 */
	public String getSetting() {
		return setting;
	}

	/**
	 * Set the setting
	 * 
	 * @param setting the setting to set or a empty string if it does not exist
	 * @throws NullPointerException if setting is null
	 */
	public void setSetting(String setting) {
		Objects.requireNonNull(setting);
		this.setting = setting;
	}

	/**
	 * @return the minorLoss
	 */
	public double getLossCoefficient() {
		return lossCoefficient;
	}

	/**
	 * @param minorLoss the minorLoss to set
	 */
	public void setLossCoefficient(double minorLoss) {
		this.lossCoefficient = minorLoss;
	}

	/**
	 * Get the fixed status
	 * 
	 * @return the fixedStatus
	 */
	public ValveStatus getFixedStatus() {
		return fixedStatus;
	}

	/**
	 * Set the fixed status
	 * 
	 * @param fixedStatus the fixedStatus to set
	 * @throws NullPointerException if fixedStatus is null
	 */
	public void setFixedStatus(ValveStatus fixedStatus) {
		Objects.requireNonNull(fixedStatus);
		this.fixedStatus = fixedStatus;
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		txt.append(String.format("%-10s\t", getId()));
		txt.append(String.format("%-10s\t", getNode1().getId()));
		txt.append(String.format("%-10s\t", getNode2().getId()));
		txt.append(String.format("%-10f\t", getDiameter()));
		txt.append(String.format("%-10s\t", getType()));
		txt.append(String.format("%-10s\t", getSetting()));
		txt.append(String.format("%-10f", getLossCoefficient()));
		String description = getDescription();
		if (description != null) {
			txt.append(String.format(";%s", description));
		}
		return txt.toString();
	}

	/**
	 * Copy the object realizing a shallow copy of this.
	 */
	@Override
	public Valve copy() {
		// TODO Auto-generated method stub
		return new Valve(this);
	}

}
