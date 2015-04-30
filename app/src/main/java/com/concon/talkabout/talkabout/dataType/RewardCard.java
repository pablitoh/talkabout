package com.concon.talkabout.talkabout.dataType;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Pablito on 30/04/2015.
 */
public class RewardCard {
    public String cardTitle;
    public String cardDescription;
    public int cardIcon;

    public RewardCard(String title, String description,int imgId)
    {
        this.cardTitle = title;
        this.cardDescription = description;
        this.cardIcon = imgId;
    }
}
