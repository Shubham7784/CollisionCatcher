package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.*;

import com.asus.Collision.Catcher.Repository.SpeedRepository;
import com.asus.Collision.Catcher.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hardware")
@Slf4j
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

    @Autowired
    private FCMService fcmService;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> addHardware(@RequestBody Hardware newHardware){
        Optional<Hardware> hardwareById = hardwareService.getHardwareById(newHardware.getHardwareId());
        if(hardwareById.isPresent())
        {
            Hardware hardwareDb = hardwareById.get();
            hardwareDb.setHardwareIp(newHardware.getHardwareIp());
            hardwareService.saveHardware(hardwareDb);
        }
        hardwareService.saveHardware(newHardware);
        return ResponseEntity.ok(new ApiResponse<String>(true,"Hardware Added Successfully!!",null));
    }
    @PostMapping("/alert")
    public ResponseEntity<?> alertGenerated(@RequestBody Alert body) {
        String hardwareId = body.getHardwareId();
        try {
            List<User> listUser = userService.getAllUsers().stream().filter(x->x.getHardware().getHardwareId().equals(hardwareId)).toList();
            if(listUser.getFirst()!=null) {
                body.setUserName(listUser.getFirst().getUserName());
                alertService.saveAlert(body);
                if(listUser.getFirst().getFcmToken() != null)
                    fcmService.sendAccidentAlert(listUser.getFirst().getFcmToken());
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

    @GetMapping("/{hardwareId}/disable-motor")
    public ResponseEntity<?> disableMotor(@PathVariable String hardwareId) {
        Optional<Hardware> hardwareById = hardwareService.getHardwareById(hardwareId);
        if (hardwareById.isPresent()) {
            String url = "http://"+hardwareById.get().getHardwareIp() + "/disable-motor";
            try {
                String response = restTemplate.getForObject(url, String.class);
                return ResponseEntity.ok(new ApiResponse<String>(true, response, null));
            } catch (RestClientException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{hardwareId}/enable-motor")
    public ResponseEntity<?> enableMotor(@PathVariable String hardwareId)
    {
        Optional<Hardware> hardwareById = hardwareService.getHardwareById(hardwareId);
        if(hardwareById.isPresent()){
            String url = "http://"+hardwareById.get().getHardwareIp()+ "/enable-motor";
            try {
                String response = restTemplate.getForObject(url, String.class);
                return ResponseEntity.ok(new ApiResponse<String>(true,response,null));
            } catch (RestClientException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{hardwareId}/get-gps-data")
    public ResponseEntity<?> getGpsData(@PathVariable String hardwareId)
    {
        Optional<Hardware> hardwareById = hardwareService.getHardwareById(hardwareId);
        if(hardwareById.isPresent()) {
            String url = hardwareById.get().getHardwareIp() + "/sendGps";
            //latitude = 25.442008
            //longitude = 81.817825
            try {
                String response = restTemplate.getForObject(url, String.class);
                ObjectMapper mapper = new ObjectMapper();
                MapData mapData = mapper.readValue(response, MapData.class);
                return new ResponseEntity<>(mapData, HttpStatus.OK);
            } catch (RestClientException e) {
                return new ResponseEntity<>("Error Connecting to ESP32" + e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
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

    @GetMapping("/{id}/stream/start")
    public ResponseEntity<?> startStream(@PathVariable String id){
        boolean b = publisher.sendControl(id, true, 100);
        if(b)
            return ResponseEntity.ok(new ApiResponse<>(true,"Streaming Started",null));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/stream/stop")
    public ResponseEntity<?> stopStream(@PathVariable String id){
        boolean b = publisher.sendControl(id, false, 100);
        if(b){
            repo.deleteAll();
            return ResponseEntity.ok(new ApiResponse<>(true,"Streaming Stopped",null));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/latest")
    public ResponseEntity<Speed> latestSpeed(@PathVariable String id){
        return new ResponseEntity<>(repo.findTopByHardwareIdOrderByTimestampDesc(id),HttpStatus.OK);
    }
}
