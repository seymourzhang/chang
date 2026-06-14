package com.chang.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chang.common.exception.Response;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;

public class MockUtil {
   public static void MockWebPostTest(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Object vo, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      if (null == params) {
         params = new HttpHeaders();
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params((MultiValueMap)params).header("Authorization", new Object[]{token}).content(JSON.toJSONString(vo)).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
   }

   public static <T> T MockWebPostTest(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Object vo, Class<T> clazz, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      if (null == params) {
         params = new HttpHeaders();
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params((MultiValueMap)params).header("Authorization", new Object[]{token}).content(JSON.toJSONString(vo)).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
      Response response = (Response)JSON.parseObject(mvcResult.getResponse().getContentAsString(), Response.class);
      if (response.getData().isPresent()) {
         JSONObject o = (JSONObject)response.getData().get();
         return JSON.parseObject(o.toJSONString(), clazz);
      } else {
         throw new RuntimeException("data is null");
      }
   }

   public static void MockWebGetTest(MockMvc mockMvc, String url, Object vo, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      MultiValueMap<String, String> params = new HttpHeaders();
      JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(vo, new SerializerFeature[]{SerializerFeature.WriteMapNullValue}));
      Map<String, Object> innerMap = jsonObject.getInnerMap();
      Map<String, String> paramsMap = new HashMap();
      Iterator var8 = innerMap.entrySet().iterator();

      while(var8.hasNext()) {
         Map.Entry<String, Object> entry = (Map.Entry)var8.next();
         if (null != entry.getValue()) {
            paramsMap.put(entry.getKey(), entry.getValue().toString());
         }
      }

      params.setAll(paramsMap);
      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params(params).header("Authorization", new Object[]{token}).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
   }

   public static <T> T MockWebGetTest(MockMvc mockMvc, String url, Object vo, Class<T> clazz, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      MultiValueMap<String, String> params = new HttpHeaders();
      JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(vo, new SerializerFeature[]{SerializerFeature.WriteMapNullValue}));
      Map<String, Object> innerMap = jsonObject.getInnerMap();
      Map<String, String> paramsMap = new HashMap();
      Iterator var9 = innerMap.entrySet().iterator();

      while(var9.hasNext()) {
         Map.Entry<String, Object> entry = (Map.Entry)var9.next();
         if (null != entry.getValue()) {
            paramsMap.put(entry.getKey(), entry.getValue().toString());
         }
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params(params).header("Authorization", new Object[]{token}).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
      Response response = (Response)JSON.parseObject(mvcResult.getResponse().getContentAsString(), Response.class);
      if (response.getData().isPresent()) {
         JSONObject o = (JSONObject)response.getData().get();
         return JSON.parseObject(o.toJSONString(), clazz);
      } else {
         throw new RuntimeException("data is null");
      }
   }

   public static void MockWebPutTest(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Object vo, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      if (null == params) {
         params = new HttpHeaders();
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params((MultiValueMap)params).header("Authorization", new Object[]{token}).content(JSON.toJSONString(vo)).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
   }

   public static <T> T MockWebPutTest(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Object vo, Class<T> clazz, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      if (null == params) {
         params = new HttpHeaders();
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params((MultiValueMap)params).header("Authorization", new Object[]{token}).content(JSON.toJSONString(vo)).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
      Response response = (Response)JSON.parseObject(mvcResult.getResponse().getContentAsString(), Response.class);
      if (response.getData().isPresent()) {
         JSONObject o = (JSONObject)response.getData().get();
         return JSON.parseObject(o.toJSONString(), clazz);
      } else {
         throw new RuntimeException("data is null");
      }
   }

   public static void MockWebDeleteTest(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Object vo, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      if (null == params) {
         params = new HttpHeaders();
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params((MultiValueMap)params).header("Authorization", new Object[]{token}).content(JSON.toJSONString(vo)).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
   }

   public static <T> T MockWebDeleteTest(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Object vo, Class<T> clazz, String token) throws Exception {
      if (null == vo) {
         vo = new Object();
      }

      if (null == params) {
         params = new HttpHeaders();
      }

      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url, new Object[0]).contentType(MediaType.APPLICATION_JSON).params((MultiValueMap)params).header("Authorization", new Object[]{token}).content(JSON.toJSONString(vo)).accept(new MediaType[]{MediaType.APPLICATION_JSON})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print()).andReturn();
      int status = mvcResult.getResponse().getStatus();
      Assert.assertEquals(200L, (long)status);
      Response response = (Response)JSON.parseObject(mvcResult.getResponse().getContentAsString(), Response.class);
      if (response.getData().isPresent()) {
         JSONObject o = (JSONObject)response.getData().get();
         return JSON.parseObject(o.toJSONString(), clazz);
      } else {
         throw new RuntimeException("data is null");
      }
   }
}
