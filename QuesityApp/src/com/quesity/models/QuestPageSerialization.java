package com.quesity.models;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class QuestPageSerialization implements
JsonDeserializer<QuestPage> , JsonSerializer<QuestPage>{

	private Map<String,Class<? extends QuestPage>> mapper;
	private Gson _gson;

	public QuestPageSerialization() {
		mapper = new HashMap<String, Class<? extends QuestPage>>();
		mapper.put("stall",QuestPageStall.class);
		_gson = new GsonBuilder().registerTypeAdapter(QuestPageLink.class,new QuestPageLinkSerialization()).create();
	}
	
	@Override
	public JsonElement serialize(QuestPage page, Type arg1,
			JsonSerializationContext arg2) {
		return _gson.toJsonTree(page);
	}
	
	@Override
	public QuestPage deserialize(JsonElement elem, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject json_object = elem.getAsJsonObject();
		JsonElement jsonElement = json_object.get("page_type");
		String page_type = jsonElement.getAsString();
		Class<? extends QuestPage> cl = mapper.get(page_type);
		QuestPage page;
		if ( cl == null ) {
			page = _gson.fromJson(elem, QuestPage.class);
		}else{
			page = _gson.fromJson(elem, cl);
		}
		return page;
	}
}