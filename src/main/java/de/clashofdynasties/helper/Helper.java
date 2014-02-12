package de.clashofdynasties.helper;

import org.codehaus.jackson.map.ObjectMapper;

public class Helper
{
	private static ObjectMapper mapper = null;

	public static ObjectMapper getMapper()
	{
		if(mapper == null)
			mapper = new ObjectMapper();

		return mapper;
	}
}
