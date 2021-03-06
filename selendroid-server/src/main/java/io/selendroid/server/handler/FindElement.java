/*
 * Copyright 2012-2014 eBay Software Foundation and selendroid committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.selendroid.server.handler;

import io.selendroid.server.SafeRequestHandler;
import io.selendroid.server.Response;
import io.selendroid.server.SelendroidResponse;
import io.selendroid.server.http.HttpRequest;
import io.selendroid.server.model.AndroidElement;
import io.selendroid.server.model.By;
import io.selendroid.server.model.internal.NativeAndroidBySelector;
import io.selendroid.util.SelendroidLogger;
import org.json.JSONException;
import org.json.JSONObject;

public class FindElement extends SafeRequestHandler {

  public FindElement(String mappedUri) {
    super(mappedUri);
  }

  @Override
  public Response safeHandle(HttpRequest request) throws JSONException {
    JSONObject payload = getPayload(request);
    String method = payload.getString("using");
    String selector = payload.getString("value");
    SelendroidLogger.info(String.format("find element command using '%s' with selector '%s'.",
            method, selector));

    By by = new NativeAndroidBySelector().pickFrom(method, selector);
    AndroidElement element = getSelendroidDriver(request).findElement(by);
    JSONObject result = new JSONObject();

    String id = getIdOfKnownElement(request, element);
    result.put("ELEMENT", id);

    return new SelendroidResponse(getSessionId(request), result);
  }
}
