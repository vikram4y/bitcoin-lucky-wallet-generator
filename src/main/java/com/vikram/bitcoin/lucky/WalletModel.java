package com.vikram.bitcoin.lucky;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class WalletModel {
    private String publicAddress;
    private String privateKey;
    private List<String> mnemonicCode;
}