package com.weather.api.WeatherAPI.controller;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    Weather select(   @RequestParam(required = false, defaultValue = "") String baseDate,
                      @RequestParam(required = false, defaultValue = "") String baseTime) {
        Weather weather = new Weather();

        weather.setPOP(popRepository.findByBaseDateOrBaseTime(baseDate,baseTime));
        weather.setPTY(ptyRepository.findByBaseDateOrBaseTime(baseDate,baseTime));
        weather.setREH(rehRepository.findByBaseDateOrBaseTime(baseDate,baseTime));
        weather.setTMN(tmnRepository.findByBaseDateOrBaseTime(baseDate,baseTime));
        weather.setTMX(tmxRepository.findByBaseDateOrBaseTime(baseDate,baseTime));

        return weather;
    }


    // 삽입
    @PostMapping("/insertWeather")
    void newWeather(@RequestBody  Map params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(params.get("params"));
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

    //Map To Object
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
                if(methodString.equals(methods[i].getName())){
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
