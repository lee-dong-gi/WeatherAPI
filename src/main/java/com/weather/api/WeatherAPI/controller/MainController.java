package com.weather.api.WeatherAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.api.WeatherAPI.model.*;
import com.weather.api.WeatherAPI.repository.*;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unchecked")  //무점검 형변환 경고설정 끄기(제네릭 세팅 관련)
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private PopRepository popRepository;

    @Autowired
    private PtyRepository ptyRepository;

    @Autowired
    private RehRepository rehRepository;

    @Autowired
    private TmnRepository tmnRepository;

    @Autowired
    private TmxRepository tmxRepository;


    // 데이터 조회
    @GetMapping("/selectWeather")
    Object select(   @RequestParam(required = false, defaultValue = "") String baseDate,
                     @RequestParam(required = false, defaultValue = "") String baseTime,
                     @RequestParam(required = false, defaultValue = "") String dataType) {
        String result = "";
        dataType = dataType.toUpperCase();
        result = jsonData(baseDate,baseTime,dataType);
        if (dataType.equals("XML")){
            //JSON to XML 변환
            JSONObject json = new JSONObject(result);
            result = XML.toString(json);
        }
        return result;
    }

    //조회 - JSON 반환
    private String jsonData(String baseDate, String baseTime,String dataType) {
        Weather weather = new Weather();
        header Header = new header();
        Map response = new HashMap();
        Map responseLevel = new HashMap();
        Map bodyLevel = new HashMap();
        String json = "";

        Header.setResultCode("00");
        Header.setResultMsg("NORMAL_SERVICE");

        try {
            weather.setPOP(popRepository.findByBaseDateAndBaseTime(baseDate,baseTime));
            weather.setPTY(ptyRepository.findByBaseDateAndBaseTime(baseDate,baseTime));
            weather.setREH(rehRepository.findByBaseDateAndBaseTime(baseDate,baseTime));
            weather.setTMN(tmnRepository.findByBaseDateAndBaseTime(baseDate,baseTime));
            weather.setTMX(tmxRepository.findByBaseDateAndBaseTime(baseDate,baseTime));

            bodyLevel.put("dataType",dataType);
            bodyLevel.put("items",weather);
            responseLevel.put("header",Header);
            responseLevel.put("body",bodyLevel);
            response.put("response",responseLevel);
        }catch (Exception e){
            //데이터 조회관련 에러
            Header.setResultCode("01");
            Header.setResultMsg("DB_ERROR");
            responseLevel.put("header",Header);
            response.put("response",responseLevel);
        }

        // MAP to JSON 변환
        try {
            json = new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            // MAP to JSON 변환에러
            return "{\"response\":{\"header\":{\"resultCode\":\"02\",\"resultMsg\":\"JSON_ERROR\"}}}";
        }
        return json;
    }

    // 삽입
    @PutMapping("/insertWeather")
    void newWeather(@RequestBody  String params, String dataType) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Map map;
        dataType = dataType.toUpperCase();
        if (dataType.equals("XML")){
            params = XMLtoJSON(params);
        }

        map = mapper.readValue(params, Map.class);
        map = (Map)map.get("response");
        map = (Map)map.get("body");
        map = (Map)map.get("items");

        String json = mapper.writeValueAsString(map.get("item"));
        List<Map<String, String>> list = mapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});

        if(json.contains("[") & json.contains("]")) {
            List<Map<String, String>> listData = mapper.readValue(json, new TypeReference<List<Map<String, String>>>() {});
            for (Map mapData : listData){
                dataInsert(mapData);
            }
        }else{
            Map<String, String> mapData = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            dataInsert(mapData);
        }
    }

    private void dataInsert(Map mapData){
        if (mapData.get("category").equals("POP")){
            pop POP = new pop();
            POP = (pop)convertMapToObject(mapData,POP);
            popRepository.save(POP);
        }
        else if(mapData.get("category").equals("PTY")){
            pty PTY = new pty();
            PTY = (pty)convertMapToObject(mapData,PTY);
            ptyRepository.save(PTY);
        }
        else if(mapData.get("category").equals("REH")){
            reh REH = new reh();
            REH = (reh)convertMapToObject(mapData,REH);
            rehRepository.save(REH);
        }
        else if(mapData.get("category").equals("TMN")){
            tmn TMN = new tmn();
            TMN = (tmn)convertMapToObject(mapData,TMN);
            tmnRepository.save(TMN);
        }
        else if(mapData.get("category").equals("TMX")){
            tmx TMX = new tmx();
            TMX = (tmx)convertMapToObject(mapData,TMX);
            tmxRepository.save(TMX);
        }
    }

    // convert XML to JSON
    private String XMLtoJSON(String xml){
        try {
            JSONObject jObject = XML.toJSONObject(xml);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object json = mapper.readValue(jObject.toString(), Object.class);
            String output = mapper.writeValueAsString(json);
            return output;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 수정
    @PostMapping("/updateWeather")
    void updateWeather(@RequestBody  String params, String dataType) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Map map;
        dataType = dataType.toUpperCase();
        if (dataType.equals("XML")){
            params = XMLtoJSON(params);
        }

        map = mapper.readValue(params, Map.class);
        map = (Map)map.get("response");
        map = (Map)map.get("body");
        map = (Map)map.get("items");

        String json = mapper.writeValueAsString(map.get("item"));
        if(json.contains("[") & json.contains("]")) {   //json데이터에 리스트 형식이라면
            List<Map<String, String>> listData = mapper.readValue(json, new TypeReference<List<Map<String, String>>>() {});
            for (Map mapData : listData){
                dataUpdate(mapData);
            }
        }else{
            Map<String, String> mapData = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            dataUpdate(mapData);
        }

    }

    private void dataUpdate(Map mapData){
        if (mapData.get("category").equals("POP")){    // 강수확률 %
            pop POP = new pop();
            Long id = Long.parseLong((String)mapData.get("id"));
            pop existPOP = popRepository.findById(id).orElse(null); //기존 데이터

            ObjectMapper objectMapper = new ObjectMapper ();
            Map<String,String> exist = objectMapper.convertValue (existPOP, Map.class); //기존 데이터 Object to Map

            POP = (pop)convertMapToObjectUpdate(mapData, POP, exist); // 빈값을 기존 데이터로 교체
            POP.setId(id);
            popRepository.save(POP);
        }
        else if(mapData.get("category").equals("PTY")){    // 강수형태(세부적인 코드값은 pty model class참고)
            pty PTY = new pty();
            Long id = Long.parseLong((String)mapData.get("id"));
            pty existPTY = ptyRepository.findById(id).orElse(null); //기존 데이터

            ObjectMapper objectMapper = new ObjectMapper ();
            Map<String,String> exist = objectMapper.convertValue (existPTY, Map.class); //기존 데이터 Object to Map

            PTY = (pty)convertMapToObjectUpdate(mapData, PTY, exist); // 빈값을 기존 데이터로 교체
            PTY.setId(id);
            ptyRepository.save(PTY);
        }
        else if(mapData.get("category").equals("REH")){    // 습도 %
            reh REH = new reh();
            Long id = Long.parseLong((String)mapData.get("id"));
            reh existREH = rehRepository.findById(id).orElse(null); //기존 데이터

            ObjectMapper objectMapper = new ObjectMapper ();
            Map<String,String> exist = objectMapper.convertValue (existREH, Map.class); //기존 데이터 Object to Map

            REH = (reh)convertMapToObjectUpdate(mapData, REH, exist); // 빈값을 기존 데이터로 교체
            REH.setId(id);
            rehRepository.save(REH);
        }
        else if(mapData.get("category").equals("TMN")){    // 아침최저기온 ℃
            tmn TMN = new tmn();
            Long id = Long.parseLong((String)mapData.get("id"));
            tmn existTMN = tmnRepository.findById(id).orElse(null); //기존 데이터

            ObjectMapper objectMapper = new ObjectMapper ();
            Map<String,String> exist = objectMapper.convertValue (existTMN, Map.class); //기존 데이터 Object to Map

            TMN = (tmn)convertMapToObjectUpdate(mapData, TMN, exist); // 빈값을 기존 데이터로 교체
            TMN.setId(id);
            tmnRepository.save(TMN);
        }
        else if(mapData.get("category").equals("TMX")){    // 낮 최고기온 ℃
            tmx TMX = new tmx();
            Long id = Long.parseLong((String)mapData.get("id"));
            tmx existTMX = tmxRepository.findById(id).orElse(null); //기존 데이터

            ObjectMapper objectMapper = new ObjectMapper ();
            Map<String,String> exist = objectMapper.convertValue (existTMX, Map.class); //기존 데이터 Object to Map

            TMX = (tmx)convertMapToObjectUpdate(mapData, TMX, exist); // 빈값을 기존 데이터로 교체
            TMX.setId(id);
            tmxRepository.save(TMX);
        }
    }

    // 삭제
    @DeleteMapping("/deletePop/{id}")
    void deletePop(@PathVariable Long id) {
        popRepository.deleteById(id);
    }

    @DeleteMapping("/deletePty/{id}")
    void deletePty(@PathVariable Long id) {
        ptyRepository.deleteById(id);
    }

    @DeleteMapping("/deleteReh/{id}")
    void deleteReh(@PathVariable Long id) {
        rehRepository.deleteById(id);
    }

    @DeleteMapping("/deleteTmn/{id}")
    void deletetmn(@PathVariable Long id) {
        tmnRepository.deleteById(id);
    }

    @DeleteMapping("/deleteTmx/{id}")
    void deleteTmx(@PathVariable Long id) {
        tmxRepository.deleteById(id);
    }

    //convert Map to Object
    private static Object convertMapToObject(Map<String,Object> map,Object obj){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                if(methodString.equals(methods[i].getName())&(!methods[i].getName().equals("setId"))){
                    try{
                        methods[i].invoke(obj, map.get(keyAttribute));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

    //convert Map to Object Update 전용(기존 데이터를 받아 빈값을 기존데이터로 교체하는 로직추가)
    private static Object convertMapToObjectUpdate(Map<String,Object> map,Object obj, Map<String,String> existMap){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();
        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                if(methodString.equals(methods[i].getName())&(!methods[i].getName().equals("setId"))){
                    try{
                        if (map.get(keyAttribute)==null || map.get(keyAttribute).equals("")){
                            String key =  methodString.substring(3);
                            key = key.substring(0,1).toLowerCase() + key.substring(1);
                            methods[i].invoke(obj, existMap.get(key));
                        }else{
                            methods[i].invoke(obj, map.get(keyAttribute));
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }
}
