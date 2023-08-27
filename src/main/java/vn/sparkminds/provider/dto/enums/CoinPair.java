package vn.sparkminds.provider.dto.enums;

public enum CoinPair {
    BTC_USD("btcusdt@depth", "BTCUSDT", 1),
    ETH_USD("ethusdt@depth", "ETHUSDT", 2),
    BCH_USD("bchusdt@depth", "BCHUSDT", 3),
    LTC_USD("ltcusdt@depth", "LTCUSDT", 4),
    XRP_USD("xrpusdt@depth", "XRPUSDT", 5),
    XLM_USD("xlmusdt@depth", "XLMUSDT", 6),
    BNB_USD("bnbusdt@depth", "BNBUSDT", 7),
    ADA_USD("adausdt@depth", "ADAUSDT", 8),
    SOL_USD("solusdt@depth", "SOLUSDT", 10),
    EOS_USD("eosusdt@depth", "EOSUSDT", 11),
    UNI_USD("uniusdt@depth", "UNIUSDT", 12),
    KSM_USD("ksmusdt@depth", "KSMUSDT", 13),
    AVAX_USD("avaxusdt@depth", "AVAXUSDT", 14),
    LUNA_USD("lunausdt@depth", "LUNAUSDT", 15),
    DOGE_USD("dogeusdt@depth", "DOGEUSDT", 16),
    DOT_USD("dotusdt@depth", "DOTUSDT", 17),
    LINK_USD("linkusdt@depth", "LINKUSDT", 18),
    MATIC_USD("maticusdt@depth", "MATICUSDT", 19),
    XTZ_USD("xtzusdt@depth", "XTZUSDT", 20),
    AAVE_USD("aaveusdt@depth", "AAVEUSDT", 21),
    ALGO_USD("algousdt@depth", "ALGOUSDT", 22),
    ATOM_USD("atomusdt@depth", "ATOMUSDT", 23),
    AXS_USD("axsusdt@depth", "AXSUSDT", 24),
    CRV_USD("crvusdt@depth", "CRVUSDT", 25),
    ETC_USD("etcusdt@depth", "ETCUSDT", 27),
    FTM_USD("ftmusdt@depth", "FTMUSDT", 28),
    MANA_USD("manausdt@depth", "MANAUSDT", 29),
    PAXG_USD("paxgusdt@depth", "PAXGUSDT", 30),
    SAND_USD("sandusdt@depth", "SANDUSDT", 31),
    SUSHI_USD("sushiusdt@depth", "SUSHIUSDT", 32),
    ;

    private String streamName;
    private String coinPairInBinance;
    private int id;

    CoinPair(String streamName, String coinPairInBinance, int id) {
        this.streamName = streamName;
        this.coinPairInBinance = coinPairInBinance;
        this.id = id;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getCoinPairInBinance() {
        return coinPairInBinance;
    }

    public int getId() {
        return id;
    }

    public static CoinPair getStreamType(String streamName) {
        for (CoinPair item : CoinPair.values()) {
            if (item.getStreamName().equals(streamName)) {
                return item;
            }
        }
        return null;
    }

    public static CoinPair getBinanceCoinPairByName(String name) {
        try {
            return CoinPair.valueOf(name);
        } catch (Exception e) {
        }
        return null;
    }
}
