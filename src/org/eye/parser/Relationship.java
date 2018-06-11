package org.eye.parser;

//refer to RFC6020 Substatement
public class Relationship {

	String statement;
	String cardinality;
	String section;

	public Relationship(String statement, String cardinality, String section) {
		super();
		this.statement = statement;
		this.cardinality = cardinality;
		this.section = section;
	}

	public String getStatement() {
		return statement;
	}

	public String getCardinality() {
		return cardinality;
	}

	public String getSection() {
		return section;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public void setSection(String section) {
		this.section = section;
	}

}
