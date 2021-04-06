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
import java.util.ArrayList;
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



    /*GetMapping("/weather")
    List<Weather> select(@RequestParam(required = false) String stnIds,
                      @RequestParam(required = false, defaultValue = "") String startDt,
                      @RequestParam(required = false, defaultValue = "") String endDt) {
        http://localhost:8080/api/weather
        return weatherRepository.findAll();
    }*/

    @PostMapping("/weather")
    void newWeather(@RequestBody  Map params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("params :: " + params);

        String json = mapper.writeValueAsString(params.get("params"));
        
        List<Map<String, String>> list = mapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});

        System.out.println("list :: " + list.toString());

        for (Map data : list){
            if (data.get("category").equals("POP")){
                pop POP = new pop();

                POP.setNy((String)data.get("ny"));
                POP.setNx((String)data.get("nx"));
                POP.setFcstValue((String)data.get("fcstValue"));
                POP.setFcstTime((String)data.get("fcstTime"));
                POP.setFcstDate((String)data.get("fcstDate"));
                POP.setBaseTime((String)data.get("baseTime"));
                POP.setBaseDate((String)data.get("baseDate"));

                popRepository.save(POP);
                System.out.println("POP :: ");
            }
            /*else if(data.get("category").equals("PTY")){
                pty PTY = (pty)getObj(data);
                ptyRepository.save(PTY);
                System.out.println("PTY :: ");
            }
            else if(data.get("category").equals("REH")){
                reh REH = (reh)getObj(data);
                rehRepository.save(REH);
                System.out.println("REH :: ");
            }
            else if(data.get("category").equals("TMN")){
                tmn TMN = (tmn)getObj(data);
                tmnRepository.save(TMN);
                System.out.println("TMN :: ");
            }
            else if(data.get("category").equals("TMX")){
                tmx TMX = (tmx)getObj(data);
                tmxRepository.save(TMX);
                System.out.println("TMX :: ");
            }
            */
        }
    }



    @DeleteMapping("/weather")
    void deleteBoard(@PathVariable Long id) {
        //weatherRepository.deleteById(id);
    }

    //Map To Object
    public Object getObj(Map map){
        Object obj = new Object();
        try {
            BeanUtils.populate(obj, map);
            System.out.println("map :: " + map.toString());
            System.out.println("obj :: " + obj.toString());
        } catch (IllegalAccessException e) { 
            e.printStackTrace(); 
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } 
        return obj; 
    }


}
