package com.lcwd.user.user.service.service.impl;

import com.lcwd.user.user.service.eceptions.ResourceNotFoundException;
import com.lcwd.user.user.service.entities.Hotel;
import com.lcwd.user.user.service.entities.Rating;
import com.lcwd.user.user.service.entities.User;
import com.lcwd.user.user.service.external.services.HotelService;
import com.lcwd.user.user.service.repositories.UserRepository;
import com.lcwd.user.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {
        //generate unique userid
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        //implement RATING SERVIVE CALL: USING REST TEMPLATE


        return userRepository.findAll();
    }

    //get single user

    @Override
    public User getUser(String userId) {
        //get user from database with te help of user repository
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User with given id is not found on server !! :"+userId));
       //fetch rating of the above user form RATING SERVICE
        //http://localhost:8083/ratings/users/87249ccd-3c39-40a1-b861-2b0e6c3feb4e

      Rating[] ratingsOfUser =  restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(),Rating[].class);
      logger.info("{} ",ratingsOfUser);
//        ArrayList<Rating> ratingsOfUser =  restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserID(),ArrayList.class);
//       logger.info("{} ",ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();


      List<Rating> ratingList = ratings.stream().map(rating -> {
        //List<Rating> ratingList = ratingsOfUser.stream().map(rating -> {
          //api call to hotel service to get the hotel
          // http://localhost:8082/hotels/4c95e4ce-341a-41c1-9937-782688418ddb

//          ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
//          Hotel hotel = forEntity.getBody();

          Hotel hotel = hotelService.getHotel(rating.getHotelId());

          //logger.info("response status code {} ",forEntity.getStatusCode());

          //set the hotel to rating
          rating.setHotel(hotel);

          return rating;
//
          //  return new Rating();

      }).collect(Collectors.toList());


        user.setRatings(ratingList);

        return user;
    }
}
