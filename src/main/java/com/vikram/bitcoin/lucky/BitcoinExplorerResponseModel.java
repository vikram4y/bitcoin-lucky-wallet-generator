package com.vikram.bitcoin.lucky;

import lombok.Data;

@Data
public class BitcoinExplorerResponseModel {
    private String address;
    private String confirmed;
    private String unconfirmed;
    private String utxo;
    private String txs;
    private String received;
}