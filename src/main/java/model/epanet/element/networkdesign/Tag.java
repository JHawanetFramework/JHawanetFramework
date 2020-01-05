package model.epanet.element.networkdesign;

public final class Tag {
	public static enum TagType {
		NODE("NODE"), LINK("LINK");

		private String name;

		private TagType(String name) {
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}

	private TagType type;
	private String id;
	private String label;

	public Tag() {
		// TODO Auto-generated constructor stub
	}
	
	public Tag(Tag tag) {
		this.type = tag.type;
		this.id = tag.id;
		this.label = tag.label;
	}
	
	
	/**
	 * @return the type
	 */
	public TagType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TagType type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		txt.append(String.format("%-10s\t", getType().getName()));
		txt.append(String.format("%-10s\t", getId()));
		txt.append(String.format("%-10s", getLabel()));
		return txt.toString();
	}
	
	/**
	 * Copy this object.
	 * @return the copy.
	 */
	public Tag copy() {
		return new Tag(this);
	}
}
