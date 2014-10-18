package com.superphil0.runtasticapi;

import java.util.List;

public class Test {


	public static void main(String[] args) {
		List<Activity> activities = new RequestHandler().getActivitiesOfUser("EMAILADRESS", "PASSWORD");
		for(Activity a : activities)
		{
			System.out.println(a.getDate().getDate().toString()+ 
					" Time: "+ Integer.parseInt(a.getDuration())/60000 + 
					" km:" + Integer.parseInt(a.getDistance())/1000);
		}
	}
}
