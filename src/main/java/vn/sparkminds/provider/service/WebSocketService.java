package vn.sparkminds.provider.service;

public interface WebSocketService {

    boolean start();

    boolean stop();

    void startPair(String coinPair);

}