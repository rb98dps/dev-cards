package com.devapi.controllers;

import com.devapi.services.CardService;
import com.devapi.entities.Card;
import com.devapi.requestentities.CreateCardsRequest;
import com.devapi.requestentities.DeleteCardsRequest;
import com.devapi.requestentities.GetCardsRequest;
import com.devapi.requestentities.UpdateCardRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RepositoryRestController(path = "/cards")
public class CardsController {

    @Autowired
    CardService cardService;

    @PostMapping("/create-card")
    @Transactional
    public Response createCards(@RequestBody @NonNull CreateCardsRequest createCardsRequest) {
        Card card = cardService.createCard(createCardsRequest);
        return new BasicResponse<>("Subtopic Created ", HttpStatus.OK.value(), card);
    }

    @PostMapping("/delete-card")
    public Response deleteCards(@RequestBody @NonNull DeleteCardsRequest deleteCardsRequest) {
        cardService.deleteCard(deleteCardsRequest);
        //delete from progress tables as well in future
        return new BasicResponse<>("Card Deleted ", HttpStatus.OK.value());
    }

    @PostMapping("/get-cards")
    public Response getNCards(@RequestBody @NonNull GetCardsRequest getCardsRequest) {
        List<Card> cards = cardService.getNCards(getCardsRequest);
        return new BasicResponse<>("Cards ", HttpStatus.OK.value(), cards);
    }

    @PostMapping("/update")
    public Response updateCardForUser(@RequestBody @NonNull UpdateCardRequest updateCardRequest) {
        cardService.updateCardForUser(updateCardRequest);
        return new BasicResponse<>("Updated Progress", HttpStatus.OK.value());
    }

}
