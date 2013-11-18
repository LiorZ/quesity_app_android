package com.b2msolutions.reyna;

import android.content.Context;

import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.network.NetworkInterface;
import com.quesity.network.exceptions.Status401Exception;
import com.quesity.network.exceptions.Status500Exception;

public class Dispatcher {

	private static final String TAG = "Dispatcher";

    public enum Result {
		OK, PERMANENT_ERROR, TEMPORARY_ERROR
	}

    private JSONPostRequestTypeGetter _jsonGetter;
    
    public Dispatcher() {
    	_jsonGetter = new JSONPostRequestTypeGetter();
    }
    
	public Result sendMessage(Context context, Message message) {
		Logger.v(TAG, "sendMessage");

        try {
		    return this.sendMessage(message);
        } finally {
//            httpClient.close();
        }
	}

	protected Result sendMessage(Message message) {
        Logger.v(TAG, "sendMessage: injected");
		
		Result parseHttpPostResult = this.parseHttpPost(message);
		if(parseHttpPostResult != Result.OK) return parseHttpPostResult;
		
		return this.tryToExecute(message);
	}
	
	private Result parseHttpPost(Message message) {
        Logger.v(TAG, "parseHttpPost");
		
		try {
			for (Header header : message.getHeaders()) {
				_jsonGetter.getRequestObj().setHeader(header.getKey(), header.getValue());
			}
			
			_jsonGetter.setJSON(message.getBody());
			return Result.OK;
		} catch (Exception e) {
			Logger.e(TAG, "parseHttpPost", e);
			return Result.PERMANENT_ERROR;
		}
	}

	private Result tryToExecute(Message msg) {
        Logger.v(TAG, "tryToExecute");
		
		try {
			NetworkInterface.getInstance().getStringContent(msg.getUrl(), _jsonGetter);
			return Result.OK;
//			return Dispatcher.getResult(response.getStatusLine().getStatusCode());
		} 
		catch(Status401Exception s401) {
            Logger.d(TAG, "tryToExecute", s401);
			Logger.i(TAG, "tryToExecute: Server returned 401 error");
			return Result.TEMPORARY_ERROR;
		}catch(Status500Exception s500) {
            Logger.d(TAG, "tryToExecute", s500);
			Logger.i(TAG, "tryToExecute: Server returned 500 error");
			return Result.PERMANENT_ERROR;
		}
		catch (Exception e) {
            Logger.d(TAG, "tryToExecute", e);
			Logger.i(TAG, "tryToExecute: General error");
			return Result.TEMPORARY_ERROR;
		}
	}

}
