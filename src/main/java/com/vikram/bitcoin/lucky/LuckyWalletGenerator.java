package com.vikram.bitcoin.lucky;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class LuckyWalletGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        while (true) {
            try {
                WalletModel walletModel = createWallet();
                String url = "https://api.blockchain.info/haskoin-store/btc/address/"
                        + walletModel.getPublicAddress() + "/balance";
                String jsonResponse = HttpRequestSender.sendPostRequest(url);
                BitcoinExplorerResponseModel bitcoinExplorerResponseModel = objectMapper
                        .readValue(jsonResponse, BitcoinExplorerResponseModel.class);
                if (hasNonZeroBalance(bitcoinExplorerResponseModel)) {
                    System.out.println(jsonResponse);
                    System.out.println("Found Lucky Wallet: " + walletModel);
                }
            } catch (Exception e) {
                System.out.println("Exception occured - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private static final NetworkParameters networkParameters = MainNetParams.get();

    public static WalletModel createWallet() throws MnemonicException.MnemonicLengthException {
        byte[] randomness = SecureRandom.getSeed(16);
        List<String> mnemonicCode = MnemonicCode.INSTANCE.toMnemonic(randomness);
        DeterministicSeed seed = new DeterministicSeed(mnemonicCode, null, "", System.currentTimeMillis());
        Wallet wallet = Wallet.fromSeed(networkParameters, seed);
        ECKey key = wallet.freshReceiveKey();
        Address address = LegacyAddress.fromKey(networkParameters, key);
        String privateKeyWIF = key.getPrivateKeyEncoded(networkParameters).toBase58();
        return new WalletModel(address.toString(), privateKeyWIF, mnemonicCode);
    }


    public static WalletModel restoreWallet(ArrayList<String> mnemonicCode) {
        DeterministicSeed seed = new DeterministicSeed(mnemonicCode, null, "", System.currentTimeMillis());
        Wallet wallet = Wallet.fromSeed(networkParameters, seed);
        ECKey key = wallet.freshReceiveKey();
        Address address = LegacyAddress.fromKey(networkParameters, key);
        String privateKeyWIF = key.getPrivateKeyEncoded(networkParameters).toBase58();
        return new WalletModel(address.toString(), privateKeyWIF, mnemonicCode);
    }

    public static boolean hasNonZeroBalance(BitcoinExplorerResponseModel balance) {
        return !"0".equals(balance.getConfirmed()) ||
                !"0".equals(balance.getUnconfirmed()) ||
                !"0".equals(balance.getUtxo()) ||
                !"0".equals(balance.getTxs()) ||
                !"0".equals(balance.getReceived());
    }

}