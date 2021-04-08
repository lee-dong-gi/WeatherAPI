package com.weather.api.WeatherAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.api.WeatherAPI.model.*;
import com.weather.api.WeatherAPI.repository.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.lang.reflect.InvocationTargetException;
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


    // 조회
    @GetMapping("/selectWeather")
    String select(   @RequestParam(required = false, defaultValue = "") String baseDate,
                      @RequestParam(required = false, defaultValue = "") String baseTime) {
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

            bodyLevel.put("dataType","JSON");
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
    void newWeather(@RequestBody  Map params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        params = (Map) params.get("response");
        params = (Map) params.get("body");
        params = (Map) params.get("items");

        String json = mapper.writeValueAsString(params.get("item"));
        List<Map<String, String>> list = mapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});

        for (Map data : list){
            if (data.get("category").equals("POP")){
                pop POP = new pop();
                POP = (pop)convertMapToObject(data,POP);
                popRepository.save(POP);
            }
            else if(data.get("category").equals("PTY")){
                pty PTY = new pty();
                PTY = (pty)convertMapToObject(data,PTY);
                ptyRepository.save(PTY);
            }
            else if(data.get("category").equals("REH")){
                reh REH = new reh();
                REH = (reh)convertMapToObject(data,REH);
                rehRepository.save(REH);
            }
            else if(data.get("category").equals("TMN")){
                tmn TMN = new tmn();
                TMN = (tmn)convertMapToObject(data,TMN);
                tmnRepository.save(TMN);
            }
            else if(data.get("category").equals("TMX")){
                tmx TMX = new tmx();
                TMX = (tmx)convertMapToObject(data,TMX);
                tmxRepository.save(TMX);
            }
        }
    }

    // 수정
    @PostMapping("/updateWeather")
    void updateWeather(@RequestBody  Map params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(params.get("params"));
        List<Map<String, String>> list = mapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});

        for (Map data : list){
            if (data.get("category").equals("POP")){    // 강수확률 %
                pop POP = new pop();
                POP = (pop)convertMapToObject(data,POP);
                POP.setId(Long.parseLong((String)data.get("id")));
                popRepository.save(POP);
            }
            else if(data.get("category").equals("PTY")){    // 강수형태(세부적인 코드값은 pty model class참고)
                pty PTY = new pty();
                PTY = (pty)convertMapToObject(data,PTY);
                PTY.setId(Long.parseLong((String)data.get("id")));
                ptyRepository.save(PTY);
            }
            else if(data.get("category").equals("REH")){    // 습도 %
                reh REH = new reh();
                REH = (reh)convertMapToObject(data,REH);
                REH.setId(Long.parseLong((String)data.get("id")));
                rehRepository.save(REH);
            }
            else if(data.get("category").equals("TMN")){    // 아침최저기온 ℃
                tmn TMN = new tmn();
                TMN = (tmn)convertMapToObject(data,TMN);
                TMN.setId(Long.parseLong((String)data.get("id")));
                tmnRepository.save(TMN);
            }
            else if(data.get("category").equals("TMX")){    // 낮 최고기온 ℃
                tmx TMX = new tmx();
                TMX = (tmx)convertMapToObject(data,TMX);
                TMX.setId(Long.parseLong((String)data.get("id")));
                tmxRepository.save(TMX);
            }
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

    @DeleteMapping("/deletetmn/{id}")
    void deletetmn(@PathVariable Long id) {
        tmnRepository.deleteById(id);
    }

    @DeleteMapping("/deleteTmx/{id}")
    void deleteTmx(@PathVariable Long id) {
        tmxRepository.deleteById(id);
    }

    //convert map to object
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
                    System.out.println();
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
}
