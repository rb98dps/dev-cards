package com.devapi.services;

import com.devapi.entities.Card;
import com.devapi.requestentities.CreateCardsRequest;
import com.devapi.requestentities.DeleteCardsRequest;
import com.devapi.requestentities.GetCardsRequest;
import com.devapi.requestentities.UpdateCardRequest;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CardService{
    @Transactional
    public Card createCard(CreateCardsRequest createCardsRequest);
    @Transactional
    public void deleteCard(DeleteCardsRequest deleteCardsRequest);
    @Transactional
    public void updateCardForUser(UpdateCardRequest updateCardRequest);
    @Transactional
    public List<Card> getNCards(GetCardsRequest getCardsRequest);

}
