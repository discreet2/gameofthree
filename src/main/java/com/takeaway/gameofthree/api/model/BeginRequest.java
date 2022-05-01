package com.takeaway.gameofthree.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeginRequest implements Serializable {
    private Integer startingNumber;
    private String actionUrl;
    private String playerName;
}
