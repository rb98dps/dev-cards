package com.devapi.model.requestentities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public class DeleteCardsRequest {
    UUID cardId;
    String cardName;
}
