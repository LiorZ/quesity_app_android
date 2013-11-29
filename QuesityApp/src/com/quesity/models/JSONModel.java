package com.quesity.models;


public abstract class JSONModel {
	@Override
	public boolean equals(Object o) {
		if ( ! (o instanceof JSONModel) ) {
			return false;
		}
		JSONModel modelObject = (JSONModel) o;
		if ( modelObject.getId() == null || this.getId() == null )
			return false;
		
		return this._id.equals(modelObject.getId());
	}

	protected String _id;

	public String getId() {
		return _id;
	}

	
}
