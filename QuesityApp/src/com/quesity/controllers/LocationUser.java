package com.quesity.controllers;

import android.location.Location;

public interface LocationUser extends ProgressableProcess{
	public void useLocation(Location location);
}
