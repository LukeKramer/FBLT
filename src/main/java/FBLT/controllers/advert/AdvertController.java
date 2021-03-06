package FBLT.controllers.advert;

import FBLT.domain.advert.Advert;
import FBLT.service.advert.ImplAdvertService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

/**
 * Created by maybra01 on 10/12/2016.
 */
@Controller
public class AdvertController {

    @Autowired
    ImplAdvertService advertService;


    //-------------------Retrieve Single Advert--------------------------------------------------------
    @RequestMapping(value = "/advert-controller/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Advert> getAdvert(@PathVariable("id") String id) {
        System.out.println("Fetching request with id: " + id);
        Advert request = advertService.readById(id);
        if (request == null) {
            System.out.println("Request with id " + id + "Not Found");
            return new ResponseEntity<Advert>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Advert>(request, HttpStatus.OK);
    }

    //-------------------Retrieve Adverts For UserID--------------------------------------------------------
    @RequestMapping(value = "/advert-controller/find-adverts-by-user-id/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Advert>> getAdvertsForUser(@PathVariable("userId") String userId) {
        System.out.println("Fetching adverts with userId: " + userId);
        List<Advert> request = advertService.findAdvertsByUserId(userId);
        if (request == null) {
            System.out.println("Request with id " + userId + "Not Found");
            return new ResponseEntity<List<Advert>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Advert>>(request, HttpStatus.OK);
    }

    //-------------------Retrieve All Adverts--------------------------------------------------------

    @RequestMapping(value = "/advert-controller/", method = RequestMethod.GET)
    public ResponseEntity<Set<Advert>> getAdverts() {
        Set<Advert> requests = advertService.readAll();
        if (requests.isEmpty()) {
            return new ResponseEntity<Set<Advert>>(HttpStatus.NO_CONTENT);// OR HttpStatus.NOT_FOUND http err 204 no content
        }
        return new ResponseEntity<Set<Advert>>(requests, HttpStatus.OK);
    }

    //-------------------Update an Advert--------------------------------------------------------
    @RequestMapping(value = "/advert-controller/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Advert> updateAdvert(@PathVariable("id") String id, @RequestBody String advert) {

        Advert currentRequest = (Advert) advertService.readById(id);

        if (currentRequest == null) {
            return new ResponseEntity<Advert>(HttpStatus.NOT_FOUND);
        }

        Gson myGson = new Gson();

        Advert updatedAdvert = new Advert.Builder()
                .copy(currentRequest)
                .price(myGson.fromJson(advert, Advert.class).getPrice())
                .product(myGson.fromJson(advert, Advert.class).getProduct())
                .user(myGson.fromJson(advert, Advert.class).getUser())
                .location(myGson.fromJson(advert, Advert.class).getLocation())
                .buyOrSell(myGson.fromJson(advert, Advert.class).isBuyOrSell())
                .build();


        advertService.update(updatedAdvert);
        return new ResponseEntity<Advert>(updatedAdvert, HttpStatus.OK);
    }

    //-------------------Insert an Advert--------------------------------------------------------
    @RequestMapping(value = "/advert-controller/", method = RequestMethod.POST)
    public ResponseEntity<Void> createAdvert(@RequestBody Advert newAdvert, UriComponentsBuilder ucBuilder) {

        advertService.create(newAdvert);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/advert-controller/{id}").buildAndExpand(newAdvert.getId()).toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //-------------------Delete an Advert--------------------------------------------------------
    @RequestMapping(value = "/advert-controller/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Advert> deleteAdvert(@PathVariable("id") String id) {
        Advert advert =advertService.readById(id);

        if (advert == null) {
            return new ResponseEntity<Advert>(HttpStatus.NOT_FOUND);
        }
        advertService.delete(advert);
        return new ResponseEntity<Advert>(HttpStatus.NO_CONTENT);
    }

}
