package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.Alert;
import com.asus.Collision.Catcher.Entity.Hardware;
import com.asus.Collision.Catcher.Entity.MapData;

import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Repository.SpeedRepository;
import com.asus.Collision.Catcher.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hardware")
public class HardwareController {

    @Autowired
    private AlertService alertService;
    @Autowired
    private HardwareService hardwareService;

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ControlPublisher publisher;

    @Autowired
    private SpeedRepository repo;

    private final String hardwareIp = "10.242.239.104";

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/alert")
    public ResponseEntity<?> alertGenerated(@RequestBody Alert body) {
        String hardwareId = body.getHardwareId();
        try {
            List<User> listUser = userService.getAllUsers().stream().filter(x -> x.getHardware().getHardwareId().equals(hardwareId)).toList();
            if(!listUser.isEmpty()) {
                body.setUserName(listUser.getFirst().getUserName());
                alertService.saveAlert(body);
                return new ResponseEntity<>("ALERT SAVED", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("NO USER EXISTS FOR THE PASSED HARDWARE ID",HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("FAILED TO SAVE ALERT, SOME ERROR OCCURED",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public String checkHardware(){
        return "Hello Hardware";
    }

    @GetMapping("/disable-motor")
    public ResponseEntity<?> disableMotor()
    {
        String url = hardwareIp + "/disable-motor";
        try
        {
            String response = restTemplate.getForObject(url,String.class);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (RestClientException e) {
            return new ResponseEntity<>("Error Connecting to ESP32"+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/enable-motor")
    public ResponseEntity<?> enableMotor()
    {
        String url = hardwareIp + "/enable-motor";
        try
        {
            String response = restTemplate.getForObject(url,String.class);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (RestClientException e) {
            return new ResponseEntity<>("Error Connecting to ESP32"+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-gps-data")
    public ResponseEntity<?> getGpsData()
    {
        String url = hardwareIp + "/sendGps";
        try
        {
            String response = restTemplate.getForObject(url,String.class);
            ObjectMapper mapper = new ObjectMapper();
            MapData mapData = mapper.readValue(response, MapData.class);
                return new ResponseEntity<>(mapData,HttpStatus.OK);
        } catch (RestClientException e) {
            return new ResponseEntity<>("Error Connecting to ESP32"+e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-speed-data")
    public ResponseEntity<?> getSpeedData()
    {
        String espIp = "http://192.168.131.104";
        String url = espIp + "/send-speed-data";
        try
        {
            String response = restTemplate.getForObject(url, String.class);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (RestClientException e) {
            return new ResponseEntity<>("Error Connecting to ESP32"+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/stream/start")
    public ResponseEntity<?> startStream(@PathVariable String id){
        publisher.sendControl(id,true,100);
        return new ResponseEntity<>("Streaming Started",HttpStatus.OK);
    }

    @PostMapping("/{id}/stream/stop")
    public ResponseEntity<?> stopStream(@PathVariable String id){
        publisher.sendControl(id,false,0);
        repo.deleteAll();
        return new ResponseEntity<>("Streaming Stopped",HttpStatus.OK);
    }

    @GetMapping("/{id}/latest")
    public ResponseEntity<?> latestSpeed(@PathVariable String id){
        return new ResponseEntity<>(repo.findTop10ByHardwareIdOrderByTimestampDesc(id),HttpStatus.OK);
    }



}
