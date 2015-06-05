package com.concon.talkabout.talkabout.dataType;

/**
 * Created by OE on 05/06/2015.
 */
public class UserRule {

    private long id;
    private String rule;
    private String country;
    private long amountAdds;

    /***
     *
     * @param country Country code
     * @param rule Text to add as rule
     */
    public UserRule(String country, String rule)
    {
        this.country = country;
        this.rule = rule;
        this.amountAdds= 0;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRule() {
        return rule;
    }

    public void setText(String text) {
        this.rule = text;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getAmountAdds() {
        return amountAdds;
    }

    public void setAmountAdds(long amountAdds) {
        this.amountAdds = amountAdds;
    }


}
