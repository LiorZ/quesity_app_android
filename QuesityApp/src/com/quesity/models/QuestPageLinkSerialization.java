package com.quesity.models;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class QuestPageLinkSerialization implements
		JsonDeserializer<QuestPageLink> , JsonSerializer<QuestPageLink>{
	private Map<String,Class> mapper;
	private Gson gson;
	public QuestPageLinkSerialization() {
		mapper = new HashMap<String, Class>();
		mapper.put("location", QuestPageLocationLink.class);
		mapper.put("answer", QuestPageQuestionLink.class);
		gson = new Gson();
	}
	@Override
	public QuestPageLink deserialize(JsonElement elem, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject json_object = elem.getAsJsonObject();
		JsonElement jsonElement = json_object.get("type");
		String link_type = jsonElement.getAsString();
		QuestPageLink link;
		if ( mapper.containsKey(link_type) ){
			link = gson.fromJson(elem, mapper.get(link_type));
		}else {
			
			link = gson.fromJson(elem, QuestPageLink.class);
		}
		
		return link;
	}
	@Override
	public JsonElement serialize(QuestPageLink arg0, Type arg1,
			JsonSerializationContext arg2) {
		return gson.toJsonTree(arg0);
	}
	
}
