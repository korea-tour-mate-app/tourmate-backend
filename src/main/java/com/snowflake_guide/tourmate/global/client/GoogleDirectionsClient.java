//package com.snowflake_guide.tourmate.global.client;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(name = "googleDirectionsClient", url = "https://maps.googleapis.com/maps/api/directions")
//public interface GoogleDirectionsClient {
//
//    @GetMapping("/json")
//    String getDirections(
//            @RequestParam("origin") String origin,
//            @RequestParam("destination") String destination,
//            @RequestParam("waypoints") String waypoints,
//            @RequestParam("mode") String mode,
//            @RequestParam("key") String apiKey
//    );
//}
