public class Campsite {
    private int campsiteId;
    private String name;
    private int gapRule;

    public Campsite(int campsiteId, String name) {
        this.campsiteId = campsiteId;
        this.name = name;
        this.gapRule = 0;
    }

    public Campsite(int campsiteId, String name, int gapRule) {
        this.campsiteId = campsiteId;
        this.name = name;
        this.gapRule = gapRule;
    }

    public int getCampsiteId() {
        return campsiteId;
    }

    public String getCampsiteName() { return name; }

    public int getGapRule() { return gapRule; }

    public void setGapRule(int gapRule) {
        this.gapRule = gapRule;
    }
}
